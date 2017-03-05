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
 * Last modified 2017-03-04 04:30:49
 *
 */

package com.gcssloop.diycode.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gcssloop.diycode.R;
import com.gcssloop.diycode.activity.UserActivity;
import com.gcssloop.diycode.base.BaseFragment;
import com.gcssloop.diycode.base.ViewHolder;
import com.gcssloop.diycode.base.adapter.GcsAdapter;
import com.gcssloop.diycode.base.adapter.GcsViewHolder;
import com.gcssloop.diycode_sdk.api.Diycode;
import com.gcssloop.diycode_sdk.api.topic.bean.Topic;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicsListEvent;
import com.gcssloop.diycode_sdk.api.user.bean.User;
import com.gcssloop.diycode_sdk.utils.TimeUtil;
import com.gcssloop.gcs_log.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * topic 相关的 fragment， 主要用于显示 topic 列表
 */
public class TopicListFragment extends BaseFragment {

    GcsAdapter<Topic> mAdapter;

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
        initRecyclerView(getContext(), holder);
    }

    private void initRecyclerView(final Context context, ViewHolder holder) {
        RecyclerView recyclerView = holder.get(R.id.rv_topic_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new GcsAdapter<Topic>(context, R.layout.item_topic) {
            @Override
            public void convert(int position, GcsViewHolder holder, Topic topic) {
                final User user = topic.getUser();
                holder.setText(R.id.text_username, user.getLogin());
                holder.setText(R.id.text_node, topic.getNode_name());
                holder.setText(R.id.text_time, TimeUtil.computePastTime(topic.getUpdated_at()));
                holder.setText(R.id.text_title, topic.getTitle());

                ImageView avatar = holder.get(R.id.img_avatar);
                Glide.with(context).load(user.getAvatar_url()).into(avatar);

                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Logger.e("username:"+user.getName());
                        Intent intent = new Intent(context, UserActivity.class);
                        intent.putExtra("username", user.getLogin());
                        context.startActivity(intent);

                    }
                }, R.id.img_avatar);
            }
        };
        recyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Diycode diycode = Diycode.getSingleInstance();
        diycode.getTopicsList(null, null, null, null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTopicList(GetTopicsListEvent event) {
        if (event.isOk()) {
            Logger.e("获取 topic list 成功 - showlist");
            mAdapter.addDatas(event.getBean());
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
