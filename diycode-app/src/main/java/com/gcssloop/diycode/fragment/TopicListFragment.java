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
 * Last modified 2017-04-08 23:15:33
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gcssloop.diycode.fragment.base.SimpleRefreshRecyclerFragment;
import com.gcssloop.diycode.fragment.provider.TopicProvider;
import com.gcssloop.diycode_sdk.api.topic.bean.Topic;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicsListEvent;
import com.gcssloop.diycode_sdk.log.Logger;
import com.gcssloop.recyclerview.adapter.multitype.HeaderFooterAdapter;

import java.util.List;

/**
 * 首页 topic 列表
 */
public class TopicListFragment extends SimpleRefreshRecyclerFragment<Topic, GetTopicsListEvent> {

    private boolean isFirstLaunch = true;

    public static TopicListFragment newInstance() {
        Bundle args = new Bundle();
        TopicListFragment fragment = new TopicListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void initData(HeaderFooterAdapter adapter) {
        // 优先从缓存中获取数据，如果是第一次加载则恢复滚动位置，如果没有缓存则从网络加载
        List<Object> topics = mDataCache.getTopicsListObj();
        if (null != topics && topics.size() > 0) {
            Logger.e("topics : " + topics.size());
            pageIndex = mConfig.getTopicListPageIndex();
            adapter.addDatas(topics);
            if (isFirstLaunch) {
                int lastPosition = mConfig.getTopicListLastPosition();
                mRecyclerView.getLayoutManager().scrollToPosition(lastPosition);
                isFirstAddFooter = false;
                isFirstLaunch = false;
            }
        } else {
            loadMore();
        }
    }

    @Override protected void setAdapterRegister(Context context, RecyclerView recyclerView,
                                                HeaderFooterAdapter adapter) {
        adapter.register(Topic.class, new TopicProvider(getContext()));
    }

    @NonNull @Override protected String request(int offset, int limit) {
        return mDiycode.getTopicsList(null, null, offset, limit);
    }

    @Override protected void onRefresh(GetTopicsListEvent event, HeaderFooterAdapter adapter) {
        super.onRefresh(event, adapter);
        mDataCache.saveTopicsListObj(adapter.getDatas());
    }

    @Override protected void onLoadMore(GetTopicsListEvent event, HeaderFooterAdapter adapter) {
        // TODO 排除重复数据
        super.onLoadMore(event, adapter);
        mDataCache.saveTopicsListObj(adapter.getDatas());
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        // 存储 PageIndex
        mConfig.saveTopicListPageIndex(pageIndex);
        // 存储 RecyclerView 滚动位置
        View view = mRecyclerView.getLayoutManager().getChildAt(0);
        int lastPosition = mRecyclerView.getLayoutManager().getPosition(view);
        mConfig.saveTopicListState(lastPosition, 0);
    }
}