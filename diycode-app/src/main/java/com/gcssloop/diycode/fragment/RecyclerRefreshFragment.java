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
 * Last modified 2017-04-07 17:39:46
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gcssloop.diycode.R;
import com.gcssloop.diycode.base.app.BaseFragment;
import com.gcssloop.diycode.base.app.ViewHolder;
import com.gcssloop.diycode.utils.Config;
import com.gcssloop.diycode.utils.DataCache;
import com.gcssloop.diycode_sdk.api.Diycode;
import com.gcssloop.diycode_sdk.api.base.event.BaseEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 具有下拉刷新和上拉加载功能的 Fregment
 */
public abstract class RecyclerRefreshFragment<T, Event extends BaseEvent<List<T>>> extends
        BaseFragment {
    // 底部状态显示
    protected static final String FOOTER_LOADING = "loading...";
    protected static final String FOOTER_NORMAL = "-- end --";
    protected static final String FOOTER_ERROR = "-- 获取失败 --";
    protected TextView mFooter;

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
    protected int pageIndex = 0;                      // 当面页码
    protected int pageCount = 20;                     // 每页个数

    // 数据
    protected Diycode mDiycode;                     // 在线(服务器)
    protected DataCache mDataCache;                 // 缓存(本地)

    // View
    protected SwipeRefreshLayout mRefreshLayout;
    protected NestedScrollView mScrollView;

    // 状态
    protected Config mConfig;
    protected boolean isFirstLaunch = true;         // 是否是第一次加载s
    private boolean refreshEnable = true;           // 是否允许刷新
    private boolean loadMoreEnable = true;          // 是否允许加载


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recycler_refresh;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConfig = Config.getSingleInstance();
        mDiycode = Diycode.getSingleInstance();
        mDataCache = new DataCache(getContext());
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        mFooter = holder.get(R.id.footer);
        mScrollView = holder.get(R.id.scroll_view);
        // refreshLayout
        mRefreshLayout = holder.get(R.id.refresh_layout);
        mRefreshLayout.setProgressViewOffset(false, -20, 80);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.diy_red));
        mRefreshLayout.setEnabled(true);
        // RecyclerView
        RecyclerView recyclerView = holder.get(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        setRecyclerView(getContext(), recyclerView);

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
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int
                    oldScrollX, int oldScrollY) {
                View childView = v.getChildAt(0);
                if (scrollY == (childView.getHeight() - v.getHeight()) && mState == STATE_NORMAL)
                { //滑动到底部 && 正常模式
                    loadMore();
                }
            }
        });
    }

    protected void refresh() {
        if (!refreshEnable) return;
        pageIndex = 0;
        String uuid = request(pageIndex * pageCount, pageCount);
        mPostTypes.put(uuid, POST_REFRESH);
        pageIndex++;
        mState = STATE_REFRESH;
    }

    protected void loadMore() {
        if (!loadMoreEnable) return;
        String uuid = request(pageIndex * pageCount, pageCount);
        mPostTypes.put(uuid, POST_LOAD_MORE);
        pageIndex++;
        mState = STATE_LOADING;
        mFooter.setText(FOOTER_LOADING);
    }


    //--- 用户处理部分 ----------------------------------------------------------------------------

    /**
     * 设置 RecyclerView，为其添加 Adapter 和 LayoutManager
     *
     * @param recyclerView RecyclerView
     */
    protected abstract void setRecyclerView(Context context, RecyclerView recyclerView);

    /**
     * 请求数据。
     *
     * @param offset 偏移量
     * @param limit  数量
     * @return uuid
     */
    @NonNull
    protected abstract String request(int offset, int limit);


    /**
     * 出现错误
     *
     * @param postType  请求类型
     * @param errorCode 错误代码
     * @param errorDesc 错误描述
     */
    protected void onError(String postType, int errorCode, String errorDesc) {
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
    public void onResultEvent(Event event) {
        String postType = mPostTypes.get(event.getUUID());
        if (event.isOk()) {
            if (postType.equals(POST_LOAD_MORE)) {
                onLoadMore(event);
            } else if (postType.equals(POST_REFRESH)) {
                onRefresh(event);
            }
        } else {
            onError(event);
        }
        mPostTypes.remove(event.getUUID());
    }


    protected void onRefresh(Event event) {
        mState = STATE_NORMAL;
        mRefreshLayout.setRefreshing(false);
    }

    protected void onLoadMore(Event event) {
        if (event.getBean().size() < pageCount) {
            mState = STATE_NO_MORE;
            mFooter.setText(FOOTER_NORMAL);
        } else {
            mState = STATE_NORMAL;
            mFooter.setText(FOOTER_NORMAL);
        }
    }

    protected void onError(Event event) {
        mState = STATE_NORMAL;  // 状态重置为正常，以便可以重试，否则进入异常状态后无法再变为正常状态
        String postType = mPostTypes.get(event.getUUID());
        if (postType.equals(POST_LOAD_MORE)) {
            mFooter.setText(FOOTER_ERROR);
            toast("加载更多失败");
        } else if (postType.equals(POST_REFRESH)) {
            mRefreshLayout.setRefreshing(false);
            toast("刷新数据失败");
        }
    }

    //--- 设置 -----------------------------------------------------------------------------------

    public void setRefreshEnable(boolean refreshEnable) {
        this.refreshEnable = refreshEnable;
    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        this.loadMoreEnable = loadMoreEnable;
    }

    /**
     * 快速返回顶部
     */
    public void quickToTop() {
        if (mScrollView != null) {
            mScrollView.smoothScrollTo(0, 0);
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
