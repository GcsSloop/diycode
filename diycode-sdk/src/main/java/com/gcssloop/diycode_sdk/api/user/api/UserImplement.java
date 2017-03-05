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
 * Last modified 2017-03-06 01:34:39
 *
 */

package com.gcssloop.diycode_sdk.api.user.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gcssloop.diycode_sdk.api.base.bean.State;
import com.gcssloop.diycode_sdk.api.base.callback.BaseCallback;
import com.gcssloop.diycode_sdk.api.base.implement.BaseImplement;
import com.gcssloop.diycode_sdk.api.topic.bean.Topic;
import com.gcssloop.diycode_sdk.api.user.event.GetMeEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserCreateTopicListEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserEvent;
import com.gcssloop.diycode_sdk.utils.UUIDGenerator;

import java.util.List;

import retrofit2.Call;

public class UserImplement extends BaseImplement<UserService> implements UserAPI {
    public UserImplement(@NonNull Context context) {
        super(context);
    }

    @Override
    public String getUsersList(Integer limit) {
        return null;
    }

    @Override
    public String getUser(String login_name) {
        String uuid = UUIDGenerator.getUUID();
        mService.getUser(login_name).enqueue(new BaseCallback<>(new GetUserEvent(uuid)));
        return uuid;
    }

    @Override
    public String getMe() {
        String uuid = UUIDGenerator.getUUID();
        mService.getMe().enqueue(new BaseCallback<>(new GetMeEvent(uuid)));
        return uuid;
    }

    @Override
    public Call<State> blockUser(String login_name) {
        return null;
    }

    @Override
    public String unBlockUser(String login_name) {
        return null;
    }

    @Override
    public String getUserBlockedList(String login_name, Integer offset, Integer limit) {
        return null;
    }

    @Override
    public String followUser(String login_name) {
        return null;
    }

    @Override
    public String unFollowUser(String login_name) {
        return null;
    }

    @Override
    public String getUserFollowingList(String login_name, Integer offset, Integer limit) {
        return null;
    }

    @Override
    public String getUserFollowerList(String login_name, Integer offset, Integer limit) {
        return null;
    }

    @Override
    public Call<List<Topic>> getUserCollectionTopicList(String login_name, Integer offset, Integer limit) {
        return null;
    }

    @Override
    public String getUserCreateTopicList(String login_name, String order, Integer offset, Integer limit) {
        String uuid = UUIDGenerator.getUUID();
        mService.getUserCreateTopicList(login_name, order, offset, limit)
                .enqueue(new BaseCallback<>(new GetUserCreateTopicListEvent(uuid)));
        return uuid;
    }

    @Override
    public String getUserReplyTopicList(String login_name, String order, Integer offset, Integer limit) {
        return null;
    }
}
