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
 * Last modified 2017-04-07 18:47:53
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gcssloop.diycode.adapter.TopicAdapter;
import com.gcssloop.diycode.base.app.ViewHolder;
import com.gcssloop.diycode_sdk.api.topic.bean.Topic;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicsListEvent;
import com.gcssloop.diycode_sdk.log.Logger;

import java.util.List;

public class GcsTopicListRecyclerFragment extends RefreshRecyclerFragment<Topic, GetTopicsListEvent> {

    private TopicAdapter mAdapter;      // 适配器

    public static GcsTopicListRecyclerFragment newInstance() {
        Bundle args = new Bundle();
        GcsTopicListRecyclerFragment fragment = new GcsTopicListRecyclerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        super.initViews(holder, root);
        List<Topic> topics = mDataCache.getTopicsList();
        if (null != topics && topics.size() > 0) {
            // 缓存模式，取出上一次的pageIndex
            mAdapter.addDatas(topics);
            mFooter.setText(FOOTER_NORMAL);
            // 恢复状态
            pageIndex = mConfig.getTopicListPageIndex();
            if (isFirstLaunch) {
                final int lastScroll = mConfig.getTopicLastScroll();
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
    protected void setRecyclerView(Context context, RecyclerView recyclerView) {
        mAdapter = new TopicAdapter(context, mDataCache);
        recyclerView.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @NonNull
    @Override
    protected String request(int offset, int limit) {
        Logger.e("发出请求");
        return mDiycode.getTopicsList(null, null, offset, limit);
    }

    @Override
    protected void onRefresh(GetTopicsListEvent event) {
        super.onRefresh(event);
        mAdapter.clearDatas();
        mAdapter.addDatas(event.getBean());
        mDataCache.saveTopicsList(mAdapter.getDatas());
        toast("刷新数据成功");
    }

    @Override
    protected void onLoadMore(GetTopicsListEvent event) {
        super.onLoadMore(event);

        long time = System.currentTimeMillis();
        // 没有数据直接返回
        if (event.getBean().size() == 0) {
            return;
        }

        /*
         * 数据查重，清除重复数据
         * 由于是按照时间线排序的，所以如果数据有重复肯定尾部和头部，
         * 所以只判断尾部和头部信息是否重复。
         * 从旧数据的最后取值，如果新数据中包含这条数据，说明有重复的，移除重复内容，继续判断
         * 如果没有，则说明一定没有，直接 break 掉。
         * 最坏的情况是，新的条数据全部重复，需要对比 210 次
         * 最好的情况是，没有一条重复，需要对比 20 次
         * 个人觉得还有更为优化的方案，不过写出来逻辑要更加难以理解，后续优化再说。
         */
        List<Topic> oldDate = mAdapter.getDatas();  // 旧数据
        List<Topic> newData = event.getBean();      // 新数据
        for (int i = 1; i <= newData.size(); i++) {
            Topic topic = oldDate.get(oldDate.size() - i);
            if (newData.contains(topic)) {
                newData.remove(topic);
            } else {
                break;
            }
        }
        // 如果过滤掉重复数据后内容大于0，则添加数据，否则尝试触发一次 loadMore
        if (newData.size() > 0) {
            mAdapter.addDatas(newData);
            mDataCache.saveTopicsList(mAdapter.getDatas());
            toast("加载数据成功");
        } else {
            loadMore();
        }

        Logger.e("耗时：" + (System.currentTimeMillis() - time) + "ms");

    }

    @Override
    public void onDestroyView() {
        // 保存状态
        int lastScrollY = mScrollView.getScrollY();
        mConfig.saveTopicListScroll(lastScrollY);
        mConfig.saveTopicListPageIndex(pageIndex);
        super.onDestroyView();
    }
}
