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
 * Last modified 2017-03-26 05:13:03
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.activity;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gcssloop.diycode.R;
import com.gcssloop.diycode.adapter.NotificationAdapter;
import com.gcssloop.diycode.base.app.BaseActivity;
import com.gcssloop.diycode.base.app.ViewHolder;
import com.gcssloop.diycode.utils.DataCache;
import com.gcssloop.diycode.utils.RecyclerViewUtil;
import com.gcssloop.diycode_sdk.api.Diycode;
import com.gcssloop.diycode_sdk.api.notifications.bean.Notification;
import com.gcssloop.diycode_sdk.api.notifications.event.GetNotificationsListEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class NotificationActivity extends BaseActivity {

    // 底部状态显示
    private static final String FOOTER_LOADING = "loading...";
    private static final String FOOTER_NORMAL = "-- end --";
    private static final String FOOTER_ERROR = "-- 获取失败 --";
    private static final String FOOTER_NOT_LOGIN = "-- 未登录 --";
    private static final String FOOTER_NO_DATA = "-- 没有数据 --";
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
    private DataCache mDataCache;                   // 缓存(本地)

    // View
    private NotificationAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recycler_refresh;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        setTitle("通知");
        mDiycode = Diycode.getSingleInstance();
        mDataCache = new DataCache(this);
        mFooter = holder.get(R.id.footer);

        initRefreshLayout(holder);
        initRecyclerView(this, holder);
        initListener(holder);

        if (!mDiycode.isLogin()) {
            toastShort("用户未登录");
            mRefreshLayout.setEnabled(false);
            mFooter.setText(FOOTER_NOT_LOGIN);
            openActivity(LoginActivity.class);
            finish();
        }
        loadMore();
    }

    private void initRefreshLayout(ViewHolder holder) {
        mRefreshLayout = holder.get(R.id.refresh_layout);
        mRefreshLayout.setProgressViewOffset(false, -20, 80);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.diy_red));
        mRefreshLayout.setEnabled(false);
    }

    private void initRecyclerView(final Context context, ViewHolder holder) {
        mAdapter = new NotificationAdapter(context);
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
        String uuid = mDiycode.getNotificationsList(pageIndex * pageCount, pageCount);
        pageIndex++;
        mPostTypes.put(uuid, POST_REFRESH);
        mState = STATE_REFRESH;
    }

    // 加载更多
    private void loadMore() {
        String uuid = mDiycode.getNotificationsList(pageIndex * pageCount, pageCount);
        pageIndex++;
        mPostTypes.put(uuid, POST_LOAD_MORE);
        mState = STATE_LOADING;
        mFooter.setText(FOOTER_LOADING);
    }

    private void onRefresh(List<Notification> beans) {
        mState = STATE_NORMAL;
        mRefreshLayout.setRefreshing(false);
        mAdapter.clearDatas();
        mAdapter.addDatas(beans);
        toastShort("数据刷新成功");
        if (beans.size() == 0) {
            mFooter.setText(FOOTER_NO_DATA);
        }
    }

    private void onLoadMore(List<Notification> beans) {
        if (beans.size() == 0) {
            mState = STATE_NORMAL;
            mFooter.setText(FOOTER_NO_DATA);
            return;
        } else if (beans.size() < pageCount) {
            mState = STATE_NO_MORE;
            mFooter.setText(FOOTER_NORMAL);
        } else {
            mState = STATE_NORMAL;
            mFooter.setText(FOOTER_NORMAL);
        }
        mAdapter.addDatas(beans);
        mRefreshLayout.setEnabled(true);
    }

    // 数据加载出现异常
    private void onError(String postType, String errorType) {
        mState = STATE_NORMAL;  // 状态重置为正常，以便可以重试，否则进入异常状态后无法再变为正常状态
        if (postType.equals(POST_LOAD_MORE)) {
            mFooter.setText(FOOTER_ERROR);
            toastShort("加载更多失败: " + errorType);
        } else if (postType.equals(POST_REFRESH)) {
            mRefreshLayout.setRefreshing(false);
            toastShort("刷新数据失败: " + errorType);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotificationList(GetNotificationsListEvent event) {
        String postType = mPostTypes.get(event.getUUID());
        if (event.isOk()) {
            if (postType.equals(POST_LOAD_MORE)) {
                onLoadMore(event.getBean());
            } else if (postType.equals(POST_REFRESH)) {
                onRefresh(event.getBean());
            }
        } else {
            onError(postType, event.getCodeDescribe());
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
}
