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
 * Last modified 2017-04-09 21:20:23
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.gcssloop.diycode.fragment.base.SimpleRefreshRecyclerFragment;
import com.gcssloop.diycode_sdk.api.notifications.bean.Notification;
import com.gcssloop.diycode_sdk.api.notifications.event.GetNotificationsListEvent;
import com.gcssloop.recyclerview.adapter.multitype.HeaderFooterAdapter;

public class NotificationsFragment extends SimpleRefreshRecyclerFragment<Notification,
        GetNotificationsListEvent> {
    @Override public void initData(HeaderFooterAdapter adapter) {

    }

    @Override
    protected void setRecyclerViewAdapter(Context context, RecyclerView recyclerView,
                                          HeaderFooterAdapter adapter) {

    }

    @NonNull @Override protected String request(int offset, int limit) {
        return mDiycode.getNotificationsList(offset, limit);
    }
}
