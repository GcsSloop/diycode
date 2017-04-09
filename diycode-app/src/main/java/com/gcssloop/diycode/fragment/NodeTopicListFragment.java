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
 * Last modified 2017-04-09 19:30:45
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

import com.gcssloop.diycode.fragment.base.SimpleRefreshRecyclerFragment;
import com.gcssloop.diycode.fragment.provider.TopicProvider;
import com.gcssloop.diycode_sdk.api.topic.bean.Topic;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicsListEvent;
import com.gcssloop.recyclerview.adapter.multitype.HeaderFooterAdapter;

/**
 * 分类 topic 列表
 */
public class NodeTopicListFragment extends SimpleRefreshRecyclerFragment<Topic, GetTopicsListEvent> {
    private static String Key_Node_ID = "Key_Node_ID";
    private int mNodeId = 0;

    public static NodeTopicListFragment newInstance(int nodeId) {
        Bundle args = new Bundle();
        args.putInt(Key_Node_ID, nodeId);
        NodeTopicListFragment fragment = new NodeTopicListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void initData(HeaderFooterAdapter adapter) {
        mNodeId = getArguments().getInt(Key_Node_ID, 0);
        loadMore();
    }

    @Override
    protected void setAdapterRegister(Context context, RecyclerView recyclerView,
                                      HeaderFooterAdapter adapter) {
        adapter.register(Topic.class, new TopicProvider(getContext()));
    }

    @NonNull @Override protected String request(int offset, int limit) {
        return mDiycode.getTopicsList(null, mNodeId, offset, limit);
    }
}
