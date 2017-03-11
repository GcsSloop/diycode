/*
 * Copyright 2017 GcsSloop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified 2017-03-08 01:01:18
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gcssloop.diycode.R;
import com.gcssloop.diycode.activity.TopicContentActivity;
import com.gcssloop.diycode.activity.UserActivity;
import com.gcssloop.diycode.adapter.TopicListAdapter;
import com.gcssloop.diycode.base.app.BaseFragment;
import com.gcssloop.diycode.base.app.ViewHolder;
import com.gcssloop.diycode.base.adapter.GcsViewHolder;
import com.gcssloop.diycode.utils.DataCache;
import com.gcssloop.diycode.utils.NetUtil;
import com.gcssloop.diycode_sdk.api.Diycode;
import com.gcssloop.diycode_sdk.api.topic.bean.Topic;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicsListEvent;
import com.gcssloop.diycode_sdk.api.user.bean.User;
import com.gcssloop.diycode_sdk.log.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * topic 相关的 fragment， 主要用于显示 topic 列表
 */
public class TopicListFragment extends BaseFragment {
    DataCache mDataCache;
    TopicListAdapter mAdapter;

    public static TopicListFragment newInstance() {
        Bundle args = new Bundle();
        TopicListFragment fragment = new TopicListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_topic_list;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        mDataCache = new DataCache(getContext());
        initRecyclerView(getContext(), holder);
    }

    private void initRecyclerView(final Context context, ViewHolder holder) {
        mAdapter = new TopicListAdapter(context) {
            @Override
            public void setListener(int position, GcsViewHolder holder, final Topic topic) {
                final User user = topic.getUser();
                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, UserActivity.class);
                        intent.putExtra(UserActivity.USER, user);
                        context.startActivity(intent);

                    }
                }, R.id.avatar, R.id.username);

                holder.get(R.id.title).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, TopicContentActivity.class);
                        intent.putExtra(TopicContentActivity.TOPIC, topic);
                        context.startActivity(intent);
                    }
                });
            }
        };

        RecyclerView recyclerView = holder.get(R.id.topic_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (NetUtil.isNetConnection(getContext())) {
            Diycode diycode = Diycode.getSingleInstance();
            diycode.getTopicsList(null, null, null, null);
        } else {
            List<Topic> topics = mDataCache.getTopicsList();
            mAdapter.addDatas(topics);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTopicList(GetTopicsListEvent event) {
        if (event.isOk()) {
            Logger.e("获取 topic list 成功 - showlist");
            mAdapter.addDatas(event.getBean());
            mDataCache.saveTopicsList(event.getBean());
        } else {
            Logger.e("获取 topic list 失败 - showlist");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
