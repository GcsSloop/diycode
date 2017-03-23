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
 * Last modified 2017-03-23 00:21:12
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gcssloop.diycode.R;
import com.gcssloop.diycode.adapter.TopicAdapter;
import com.gcssloop.diycode.base.app.BaseActivity;
import com.gcssloop.diycode.base.app.ViewHolder;
import com.gcssloop.diycode.base.recyclerview.GcsAdapter;
import com.gcssloop.diycode.utils.DataCache;
import com.gcssloop.diycode.utils.RecyclerViewUtil;
import com.gcssloop.diycode_sdk.api.Diycode;
import com.gcssloop.diycode_sdk.api.topic.bean.Topic;
import com.gcssloop.diycode_sdk.api.user.bean.UserDetail;
import com.gcssloop.diycode_sdk.api.user.event.GetUserCollectionTopicListEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserCreateTopicListEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;

public class MyTopicActivity extends BaseActivity {
    private static final String key = "MyTopicActivity";
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
    private GcsAdapter<Topic> mAdapter;
    private SwipeRefreshLayout mRefreshLayout;

    // 数据类型
    enum InfoType {
        MY_TOPIC, MY_COLLECT
    }

    private InfoType current_type = InfoType.MY_TOPIC;

    public static void newInstance(Context context, InfoType type) {
        Intent intent = new Intent(context, MyTopicActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_topic;
    }

    @Override
    protected void initDatas() {
        Intent intent = getIntent();
        InfoType type = (InfoType) intent.getSerializableExtra("type");
        if (type != null) {
            current_type = type;
        } else {
            current_type = InfoType.MY_TOPIC;
        }
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
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
            return;
        }
        if (mDataCache.getMe() == null) {
            try {
                UserDetail me = mDiycode.getMeNow();
                mDataCache.saveMe(me);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        loadMore();
        mFooter.setText(FOOTER_LOADING);
    }

    private void initRefreshLayout(ViewHolder holder) {
        mRefreshLayout = holder.get(R.id.refresh_layout);
        mRefreshLayout.setProgressViewOffset(false, -20, 80);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.diy_red));
        mRefreshLayout.setEnabled(false);
    }

    private void initRecyclerView(final Context context, ViewHolder holder) {
        mAdapter = new TopicAdapter(context, mDataCache);
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
        if (!mDiycode.isLogin())
            return;
        pageIndex = 0;
        String uuid = "uuid";
        String username = mDataCache.getMe().getLogin();
        if (username.isEmpty()) {
            return;
        }
        switch (current_type) {
            case MY_TOPIC:
                uuid = mDiycode.getUserCreateTopicList(username, null, pageIndex * pageCount, pageCount);
                break;
            case MY_COLLECT:
                uuid = mDiycode.getUserCollectionTopicList(username, pageIndex * pageCount, pageCount);
                break;
            default:
                toastShort("类型错误");
                return;
        }
        mPostTypes.put(uuid, POST_REFRESH);
        pageIndex++;
        mState = STATE_REFRESH;
    }

    private void onRefresh(List<Topic> topics) {
        mState = STATE_NORMAL;
        mRefreshLayout.setRefreshing(false);
        mAdapter.clearDatas();
        mAdapter.addDatas(topics);
        toastShort("数据刷新成功");
        if (topics.size() == 0) {
            mFooter.setText(FOOTER_NO_DATA);
        }
    }

    // 加载更多
    private void loadMore() {
        if (!mDiycode.isLogin())
            return;
        String uuid = "uuid";
        String username = mDataCache.getMe().getLogin();
        if (username.isEmpty()) {
            return;
        }
        switch (current_type) {
            case MY_TOPIC:
                uuid = mDiycode.getUserCreateTopicList(username, null, pageIndex * pageCount, pageCount);
                break;
            case MY_COLLECT:
                uuid = mDiycode.getUserCollectionTopicList(username, pageIndex * pageCount, pageCount);
                break;
            default:
                toastShort("类型错误");
                return;
        }
        mPostTypes.put(uuid, POST_LOAD_MORE);
        pageIndex++;
        mState = STATE_LOADING;
        mFooter.setText(FOOTER_LOADING);
    }

    private void onLoadMore(List<Topic> topics) {
        if (topics.size() == 0) {
            mState = STATE_NORMAL;
            mFooter.setText(FOOTER_NO_DATA);
            return;
        } else if (topics.size() < pageCount) {
            mState = STATE_NO_MORE;
            mFooter.setText(FOOTER_NORMAL);
        } else {
            mState = STATE_NORMAL;
            mFooter.setText(FOOTER_NORMAL);
        }
        mAdapter.addDatas(topics);
        mRefreshLayout.setEnabled(true);
    }

    // 数据加载出现异常
    private void onError(String postType) {
        mState = STATE_NORMAL;  // 状态重置为正常，以便可以重试，否则进入异常状态后无法再变为正常状态
        if (postType.equals(POST_LOAD_MORE)) {
            mFooter.setText(FOOTER_ERROR);
            toastShort("加载更多失败");
        } else if (postType.equals(POST_REFRESH)) {
            mRefreshLayout.setRefreshing(false);
            toastShort("刷新数据失败");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserCreateTopicList(GetUserCreateTopicListEvent event) {
        String postType = mPostTypes.get(event.getUUID());
        if (event.isOk()) {
            if (postType.equals(POST_LOAD_MORE)) {
                onLoadMore(event.getBean());
            } else if (postType.equals(POST_REFRESH)) {
                onRefresh(event.getBean());
            }
        } else {
            onError(postType);
        }
        mPostTypes.remove(event.getUUID());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserCollectTopicList(GetUserCollectionTopicListEvent event) {
        String postType = mPostTypes.get(event.getUUID());
        if (event.isOk()) {
            if (postType.equals(POST_LOAD_MORE)) {
                onLoadMore(event.getBean());
            } else if (postType.equals(POST_REFRESH)) {
                onRefresh(event.getBean());
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
}
