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
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gcssloop.diycode.R;
import com.gcssloop.diycode.activity.UserActivity;
import com.gcssloop.diycode.base.app.BaseFragment;
import com.gcssloop.diycode.base.app.ViewHolder;
import com.gcssloop.diycode.base.recyclerview.GcsAdapter;
import com.gcssloop.diycode.base.recyclerview.GcsViewHolder;
import com.gcssloop.diycode.utils.Config;
import com.gcssloop.diycode.utils.DataCache;
import com.gcssloop.diycode.utils.RecyclerViewUtil;
import com.gcssloop.diycode.utils.TimeUtil;
import com.gcssloop.diycode.utils.UrlUtil;
import com.gcssloop.diycode_sdk.api.Diycode;
import com.gcssloop.diycode_sdk.api.news.bean.New;
import com.gcssloop.diycode_sdk.api.news.event.GetNewsListEvent;
import com.gcssloop.diycode_sdk.api.user.bean.User;
import com.gcssloop.diycode_sdk.log.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * topic 相关的 fragment， 主要用于显示 topic 列表
 */
public class NewsListFragment extends BaseFragment {
    // 底部状态显示
    private static final String FOOTER_LOADING = "loading...";
    private static final String FOOTER_NORMAL = "-- end --";
    private static final String FOOTER_ERROR = "-- 获取失败 --";
    private TextView mFooter;

    // 请求状态 - 下拉刷新 还是 加载更多
    private static final String POST_LOAD_MORE = "load_more";
    private static final String POST_REFRESH = "refresh";
    private ArrayMap<String, String> mPostTypes = new ArrayMap<>();    // 请求类型

    // 当前状态
    private static final int STATE_NORMAL = 0;      // 正常
    private static final int STATE_NO_MORE = 1;     // 正在
    private static final int STATE_LOADING = 2;     // 加载
    private static final int STATE_REFRESH = 3;     // 刷新
    private int mState = STATE_NORMAL;

    // 分页加载
    private int pageIndex = 0;                      // 当面页码
    private int pageCount = 20;                     // 每页个数

    // 数据
    private Diycode mDiycode;                       // 在线(服务器)
    private Config mConfig;
    private DataCache mDataCache;                   // 缓存(本地)

    // View
    private GcsAdapter<New> mAdapter;
    private SwipeRefreshLayout mRefreshLayout;

    private NestedScrollView mScrollView;
    private boolean isFirstLunch = true;
    boolean isFirstLaunch = true;

    public static NewsListFragment newInstance() {
        Bundle args = new Bundle();
        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDiycode = Diycode.getSingleInstance();
        mConfig = Config.getSingleInstance();
        mDataCache = new DataCache(getContext());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recycler_refresh;
    }

    private void initData() {
        mRefreshLayout.setEnabled(true);
        // 第一次加载，默认从缓存加载
        List<New> news = mDataCache.getNewsList();
        if (null != news && news.size() > 0) {
            pageIndex = mConfig.getNewsListPageIndex();
            mAdapter.addDatas(news);
            mFooter.setText(FOOTER_NORMAL);
            if (isFirstLaunch) {
                final int lastScroll = mConfig.getNewsLastScroll();
                mScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mScrollView.scrollTo(0, lastScroll);
                    }
                });
                isFirstLaunch = false;
            }
        } else {
            loadMore();
            mFooter.setText(FOOTER_LOADING);
        }
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        mFooter = holder.get(R.id.footer);
        mScrollView = holder.get(R.id.scroll_view);
        initRefreshLayout(holder);
        initRecyclerView(getContext(), holder);
        initListener(holder);
        initData();
    }

    private void initRefreshLayout(ViewHolder holder) {
        mRefreshLayout = holder.get(R.id.refresh_layout);
        mRefreshLayout.setProgressViewOffset(false, -20, 80);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.diy_red));
        mRefreshLayout.setEnabled(false);
    }

    private void initRecyclerView(final Context context, ViewHolder holder) {
        mAdapter = new GcsAdapter<New>(context, R.layout.item_news) {

            @Override
            public void convert(int position, GcsViewHolder holder, final New bean) {
                final User user = bean.getUser();
                holder.setText(R.id.username, user.getLogin());
                holder.setText(R.id.node_name, bean.getNode_name());
                holder.setText(R.id.time, TimeUtil.computePastTime(bean.getUpdated_at()));
                holder.setText(R.id.title, bean.getTitle());
                holder.loadImage(mContext, user.getAvatar_url(), R.id.avatar);
                holder.setText(R.id.host_name, UrlUtil.getHost(bean.getAddress()));

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

        RecyclerView recyclerView = holder.get(R.id.recycler_view);
        RecyclerViewUtil.init(context, recyclerView, mAdapter);
    }

    private void initListener(ViewHolder holder) {
        // 监听 RefreshLayout 下拉刷新
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        // 监听 scrollView 加载更多
        NestedScrollView scrollView = holder.get(R.id.scroll_view);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                View childView = v.getChildAt(0);
                if (scrollY == (childView.getHeight() - v.getHeight()) && mState == STATE_NORMAL) { //滑动到底部 && 正常模式
                    loadMore();
                }
            }
        });
    }

    // 刷新
    private void refresh() {
        pageIndex = 0;
        String uuid = mDiycode.getNewsList(null, pageIndex * pageCount, pageCount);
        mPostTypes.put(uuid, POST_REFRESH);
        pageIndex++;
        mState = STATE_REFRESH;
    }

    private void onRefresh(GetNewsListEvent event) {
        mState = STATE_NORMAL;
        mRefreshLayout.setRefreshing(false);
        mAdapter.clearDatas();
        mAdapter.addDatas(event.getBean());
        mDataCache.saveNewsList(mAdapter.getDatas());
        toast("数据刷新成功");
    }

    // 加载更多
    private void loadMore() {
        String uuid = mDiycode.getNewsList(null, pageIndex * pageCount, pageCount);
        mPostTypes.put(uuid, POST_LOAD_MORE);
        pageIndex++;
        mState = STATE_LOADING;
        mFooter.setText(FOOTER_LOADING);
    }

    private void onLoadMore(GetNewsListEvent event) {
        List<New> news = event.getBean();
        if (news.size() < pageCount) {
            mState = STATE_NO_MORE;
            mFooter.setText(FOOTER_NORMAL);
        } else {
            mState = STATE_NORMAL;
            mFooter.setText(FOOTER_NORMAL);
        }
        mAdapter.addDatas(news);
        mDataCache.saveNewsList(mAdapter.getDatas());
        mRefreshLayout.setEnabled(true);
    }

    // 数据加载出现异常
    private void onError(String postType) {
        mState = STATE_NORMAL;  // 状态重置为正常，以便可以重试，否则进入异常状态后无法再变为正常状态
        if (postType.equals(POST_LOAD_MORE)) {
            mFooter.setText(FOOTER_ERROR);
            toast("加载更多失败");
        } else if (postType.equals(POST_REFRESH)) {
            mRefreshLayout.setRefreshing(false);
            toast("刷新数据失败");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewsList(GetNewsListEvent event) {
        String postType = mPostTypes.get(event.getUUID());
        if (event.isOk()) {
            if (postType.equals(POST_LOAD_MORE)) {
                onLoadMore(event);
            } else if (postType.equals(POST_REFRESH)) {
                onRefresh(event);
            }
        } else {
            onError(postType);
        }
        mPostTypes.remove(event.getUUID());
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

    @Override
    public void onDestroyView() {
        // 保存
        int lastScrollY = mScrollView.getScrollY();
        mConfig.saveNewsListScroll(lastScrollY);
        mConfig.saveNewsListPageIndex(pageIndex);
        super.onDestroyView();
    }

    public void quickToTop() {
        Logger.e("快速返回");
        if (mScrollView != null) {
            mScrollView.smoothScrollTo(0, 0);
        }
    }
}
