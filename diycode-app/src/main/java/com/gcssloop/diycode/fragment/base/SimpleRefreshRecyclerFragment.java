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
 * Last modified 2017-04-09 21:16:47
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.fragment.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.gcssloop.recyclerview.layoutmanager.SpeedyLinearLayoutManager;
import com.gcssloop.diycode_sdk.api.base.event.BaseEvent;
import com.gcssloop.recyclerview.adapter.multitype.HeaderFooterAdapter;

import java.util.List;

public abstract class SimpleRefreshRecyclerFragment<T, Event extends BaseEvent<List<T>>> extends
        RefreshRecyclerFragment<T, Event> {

    @NonNull @Override protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return new SpeedyLinearLayoutManager(getContext());
    }

    @Override protected void onRefresh(Event event, HeaderFooterAdapter adapter) {
        adapter.clearDatas();
        adapter.addDatas(event.getBean());
        toast("刷新成功");
    }

    @Override protected void onLoadMore(Event event, HeaderFooterAdapter adapter) {
        adapter.addDatas(event.getBean());
    }

    @Override protected void onError(Event event, String postType) {
        if (postType.equals(POST_LOAD_MORE)) {
            toast("加载更多失败");
        } else if (postType.equals(POST_REFRESH)) {
            toast("刷新数据失败");
        }
    }
}
