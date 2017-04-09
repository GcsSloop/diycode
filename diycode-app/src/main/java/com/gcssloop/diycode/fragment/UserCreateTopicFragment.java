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
 * Last modified 2017-04-09 20:34:58
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
import com.gcssloop.diycode_sdk.api.user.event.GetUserCreateTopicListEvent;
import com.gcssloop.recyclerview.adapter.multitype.HeaderFooterAdapter;

/**
 * 用户创建的 topic 列表
 */
public class UserCreateTopicFragment extends SimpleRefreshRecyclerFragment<Topic,
        GetUserCreateTopicListEvent> {
    private static String Key_User_Login_Name = "Key_User_Login_Name";
    private String loginName;

    public static UserCreateTopicFragment newInstance(String user_login_name) {
        Bundle args = new Bundle();
        args.putString(Key_User_Login_Name, user_login_name);
        UserCreateTopicFragment fragment = new UserCreateTopicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void initData(HeaderFooterAdapter adapter) {
        Bundle args = getArguments();
        loginName = args.getString(Key_User_Login_Name);
        loadMore();
    }

    @Override
    protected void setAdapterRegister(Context context, RecyclerView recyclerView,
                                      HeaderFooterAdapter adapter) {
        adapter.register(Topic.class, new TopicProvider(context));
    }

    @NonNull @Override protected String request(int offset, int limit) {
        return mDiycode.getUserCreateTopicList(loginName, null, offset, limit);
    }
}
