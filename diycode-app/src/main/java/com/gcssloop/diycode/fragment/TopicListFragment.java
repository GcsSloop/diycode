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
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.gcssloop.diycode.R;
import com.gcssloop.diycode.activity.TopicContentActivity;
import com.gcssloop.diycode.activity.UserActivity;
import com.gcssloop.diycode.base.app.BaseFragment;
import com.gcssloop.diycode.base.app.ViewHolder;
import com.gcssloop.diycode.base.recyclerview.GcsAdapter;
import com.gcssloop.diycode.base.recyclerview.GcsViewHolder;
import com.gcssloop.diycode.utils.DataCache;
import com.gcssloop.diycode.utils.NetUtil;
import com.gcssloop.diycode.utils.RecyclerViewUtil;
import com.gcssloop.diycode_sdk.api.Diycode;
import com.gcssloop.diycode_sdk.api.topic.bean.Topic;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicsListEvent;
import com.gcssloop.diycode_sdk.api.user.bean.User;
import com.gcssloop.diycode_sdk.log.Logger;
import com.gcssloop.diycode_sdk.utils.TimeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * topic 相关的 fragment， 主要用于显示 topic 列表
 */
public class TopicListFragment extends BaseFragment {
    private static final String LOADING = "loading...";
    private static final String NORMAL = "-- end --";
    private static final String ERROR = "-- 获取失败 --";

    private static final int STATE_NORMAL = 0;
    private static final int STATE_NO_MORE = 1;
    private static final int STATE_LOADING = 2;
    private int mState = STATE_NORMAL;

    private int pageIndex = 0;
    private int pageCount = 20;

    private Diycode mDiycode;

    DataCache mDataCache;
    GcsAdapter<Topic> mAdapter;
    RecyclerView recyclerView;

    private TextView mFooter;

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
        mDiycode = Diycode.getSingleInstance();
        mFooter = holder.get(R.id.footer);
        mFooter.setText(LOADING);
        initRecyclerView(getContext(), holder);
    }

    private void initRecyclerView(final Context context, ViewHolder holder) {
        mDataCache = new DataCache(getContext());
        mAdapter = new GcsAdapter<Topic>(context, R.layout.item_topic) {
            @Override
            public void convert(int position, GcsViewHolder holder, final Topic topic) {
                final User user = topic.getUser();
                holder.setText(R.id.username, user.getLogin());
                holder.setText(R.id.node_name, topic.getNode_name());
                holder.setText(R.id.time, TimeUtil.computePastTime(topic.getUpdated_at()));
                holder.setText(R.id.title, topic.getTitle());
                holder.loadImage(mContext, user.getAvatar_url(), R.id.avatar);

                TextView preview = holder.get(R.id.preview);
                String text = mDataCache.getTopicPreview(topic.getId());
                if (null != text) {
                    preview.setVisibility(View.VISIBLE);
                    preview.setText(Html.fromHtml(text));
                } else {
                    preview.setVisibility(View.GONE);
                }

                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, UserActivity.class);
                        intent.putExtra(UserActivity.USER, user);
                        context.startActivity(intent);

                    }
                }, R.id.avatar, R.id.username);

                holder.get(R.id.item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, TopicContentActivity.class);
                        intent.putExtra(TopicContentActivity.TOPIC, topic);
                        context.startActivity(intent);
                    }
                });
            }
        };

        recyclerView = holder.get(R.id.topic_list);
        RecyclerViewUtil.init(context, recyclerView, mAdapter);

        NestedScrollView scrollView = holder.get(R.id.scroll_view);

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(v.getScrollY() == 0){
                    // 到顶部了
                    //TODO
                }
                View childView = v.getChildAt(0);
                if (scrollY == (childView.getHeight() - v.getHeight())) {
                    //滑动到底部
                    Logger.e("滑动到底部");
                    if (mState == STATE_NORMAL) {    // 正常模式
                        mDiycode.getTopicsList(null, null, pageIndex * pageCount, pageCount);
                        pageIndex++;
                        mFooter.setText(LOADING);
                        mState = STATE_LOADING;
                    } else if (mState == STATE_LOADING) {
                        return;
                    } else if (mState == STATE_NO_MORE) {
                        return;
                    }
                }
            }
        });

        if (NetUtil.isNetConnection(getContext())) {
            mDiycode.getTopicsList(null, null, pageIndex * pageCount, pageCount);
        } else {
            List<Topic> topics = mDataCache.getTopicsList();
            if (null != topics) {
                mAdapter.addDatas(topics);
            }
            mFooter.setText(NORMAL);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTopicList(GetTopicsListEvent event) {
        if (event.isOk()) {
            Logger.e("获取 topic list 成功 - showlist");
            List<Topic> topics = event.getBean();
            if (topics.size() < pageCount) {
                mState = STATE_NO_MORE;
                mFooter.setText(NORMAL);
            } else {
                mState = STATE_NORMAL;
                mFooter.setText(NORMAL);
            }
            mAdapter.addDatas(topics);
            mDataCache.saveTopicsList(mAdapter.getDatas());
        } else {
            mFooter.setText(ERROR);
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
