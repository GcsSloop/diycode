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
 * Last modified 2017-03-13 21:51:55
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gcssloop.diycode.R;
import com.gcssloop.diycode.activity.UserActivity;
import com.gcssloop.diycode.base.app.BaseFragment;
import com.gcssloop.diycode.base.app.ViewHolder;
import com.gcssloop.diycode.base.recyclerview.GcsAdapter;
import com.gcssloop.diycode.base.recyclerview.GcsViewHolder;
import com.gcssloop.diycode.utils.DataCache;
import com.gcssloop.diycode.utils.HtmlUtil;
import com.gcssloop.diycode.utils.NetUtil;
import com.gcssloop.diycode.utils.RecyclerViewUtil;
import com.gcssloop.diycode_sdk.api.Diycode;
import com.gcssloop.diycode_sdk.api.news.bean.New;
import com.gcssloop.diycode_sdk.api.news.event.GetNewsListEvent;
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
public class NewsListFragment extends BaseFragment {
    private static String CACHE_KEY = "News_List_";
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
    GcsAdapter<New> mAdapter;
    RecyclerView recyclerView;

    private TextView mFooter;

    public static NewsListFragment newInstance() {
        Bundle args = new Bundle();
        NewsListFragment fragment = new NewsListFragment();
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
        mAdapter = new GcsAdapter<New>(context, R.layout.item_news) {

            @Override
            public void convert(int position, GcsViewHolder holder, final New bean) {
                final User user = bean.getUser();
                holder.setText(R.id.username, user.getLogin());
                holder.setText(R.id.node_name, bean.getNode_name());
                holder.setText(R.id.time, TimeUtil.computePastTime(bean.getUpdated_at()));
                holder.setText(R.id.title, bean.getTitle());
                holder.loadImage(mContext, user.getAvatar_url(), R.id.avatar);

                holder.setText(R.id.host_name, HtmlUtil.getHost(bean.getAddress()));

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
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(bean.getAddress()));
                        mContext.startActivity(intent);
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
                View childView = v.getChildAt(0);
                if (scrollY == (childView.getHeight() - v.getHeight()) && mState == STATE_NORMAL) {
                    loadMore();
                }
            }
        });

        if (NetUtil.isNetConnection(getContext())) {
            loadMore();
        } else {
            List<New> news = mDataCache.getData(CACHE_KEY);
            if (null != news) {
                mAdapter.addDatas(news);
            }
            mFooter.setText(NORMAL);
        }
    }

    /**
     * 加载更多
     */
    public void loadMore() {
        mDiycode.getNewsList(null, pageIndex * pageCount, pageCount);
        pageIndex++;
        mFooter.setText(LOADING);
        mState = STATE_LOADING;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewsList(GetNewsListEvent event) {
        if (event.isOk()) {
            Logger.e("获取 topic list 成功 - showlist");
            List<New> news = event.getBean();
            if (news.size() < pageCount) {
                mState = STATE_NO_MORE;
                mFooter.setText(NORMAL);
            } else {
                mState = STATE_NORMAL;
                mFooter.setText(NORMAL);
            }
            mAdapter.addDatas(news);
            mDataCache.saveListData(CACHE_KEY, mAdapter.getDatas());
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
