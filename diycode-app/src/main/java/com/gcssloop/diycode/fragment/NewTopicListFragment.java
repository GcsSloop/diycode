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

import com.gcssloop.diycode.R;
import com.gcssloop.diycode.base.app.ViewHolder;
import com.gcssloop.diycode.base.recyclerview.SpeedyLinearLayoutManager;
import com.gcssloop.diycode.fragment.provider.TopicProvider;
import com.gcssloop.diycode_sdk.api.topic.bean.Topic;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicsListEvent;
import com.gcssloop.diycode_sdk.log.Logger;
import com.gcssloop.recyclerview.adapter.multitype.HeaderFooterAdapter;

public class NewTopicListFragment extends RefreshRecyclerFragment<Topic, GetTopicsListEvent> {

    public static NewTopicListFragment newInstance() {
        Bundle args = new Bundle();
        NewTopicListFragment fragment = new NewTopicListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        Logger.e("initViews");
        super.initViews(holder, root);
        loadMore();
    }

    @Override
    protected void setRecyclerView(Context context, RecyclerView recyclerView,
                                   HeaderFooterAdapter adapter) {
        Logger.e("setRecyclerView - start");
        recyclerView.setLayoutManager(new SpeedyLinearLayoutManager(getContext()));
        TopicProvider topicProvider = new TopicProvider(getContext(), R.layout.item_topic);
        adapter.register(Topic.class, topicProvider);
        Logger.e("setRecyclerView - end");
    }

    @NonNull
    @Override
    protected String request(int offset, int limit) {
        Logger.e("request - start");
        return mDiycode.getTopicsList(null, null, offset, limit);
    }

    @Override
    protected void onRefresh(GetTopicsListEvent event, HeaderFooterAdapter adapter) {
        adapter.clearDatas();
        adapter.addDatas(event.getBean());
        toast("刷新成功");
    }

    @Override
    protected void onLoadMore(GetTopicsListEvent event, HeaderFooterAdapter adapter) {
        adapter.addDatas(event.getBean());
        toast("加载更多成功");
    }

    @Override
    protected void onError(GetTopicsListEvent event, String postType) {
        if (postType.equals(POST_LOAD_MORE)) {
            toast("加载更多失败");
        } else if (postType.equals(POST_REFRESH)) {
            toast("刷新数据失败");
        }
    }
}