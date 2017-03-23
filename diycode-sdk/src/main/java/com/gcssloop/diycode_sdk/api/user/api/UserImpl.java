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
 * Last modified 2017-03-08 01:01:18
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode_sdk.api.user.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gcssloop.diycode_sdk.api.base.callback.BaseCallback;
import com.gcssloop.diycode_sdk.api.base.impl.BaseImpl;
import com.gcssloop.diycode_sdk.api.user.bean.UserDetail;
import com.gcssloop.diycode_sdk.api.user.event.BlockUserEvent;
import com.gcssloop.diycode_sdk.api.user.event.FollowUserEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetMeEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserBlockedListEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserCollectionTopicListEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserCreateTopicListEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserFollowerListEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserFollowingListEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserReplyTopicListEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUsersListEvent;
import com.gcssloop.diycode_sdk.api.user.event.UnBlockUserEvent;
import com.gcssloop.diycode_sdk.api.user.event.UnFollowUserEvent;
import com.gcssloop.diycode_sdk.utils.UUIDGenerator;

import java.io.IOException;

public class UserImpl extends BaseImpl<UserService> implements UserAPI {
    public UserImpl(@NonNull Context context) {
        super(context);
    }

    //--- user info --------------------------------------------------------------------------------

    /**
     * 获取用户列表
     *
     * @param limit 数量极限，默认值 20，值范围 1..150
     * @see GetUsersListEvent
     */
    @Override
    public String getUsersList(@Nullable Integer limit) {
        String uuid = UUIDGenerator.getUUID();
        mService.getUsersList(limit).enqueue(new BaseCallback<>(new GetUsersListEvent(uuid)));
        return uuid;
    }

    /**
     * 获取用户详细资料
     *
     * @param login_name 登录用户名(非昵称)
     * @see GetUserEvent
     */
    @Override
    public String getUser(@NonNull String login_name) {
        String uuid = UUIDGenerator.getUUID();
        mService.getUser(login_name).enqueue(new BaseCallback<>(new GetUserEvent(uuid)));
        return uuid;
    }

    /**
     * 获取当前登录者的详细资料
     *
     * @see GetMeEvent
     */
    @Override
    public String getMe() {
        String uuid = UUIDGenerator.getUUID();
        mService.getMe().enqueue(new BaseCallback<>(new GetMeEvent(uuid)));
        return uuid;
    }

    /**
     * 立即获取获取当前登录者的详细资料
     *
     * @return 用户资料
     */
    @Override
    public UserDetail getMeNow() throws IOException {
        return mService.getMe().execute().body();
    }

    //--- user block -------------------------------------------------------------------------------

    /**
     * 屏蔽用户
     *
     * @param login_name 登录用户名(非昵称)
     * @see BlockUserEvent
     */
    @Override
    public String blockUser(@NonNull String login_name) {
        String uuid = UUIDGenerator.getUUID();
        mService.blockUser(login_name).enqueue(new BaseCallback<>(new BlockUserEvent(uuid)));
        return uuid;
    }

    /**
     * 取消屏蔽用户
     *
     * @param login_name 登录用户名(非昵称)
     * @see UnBlockUserEvent
     */
    @Override
    public String unBlockUser(@NonNull String login_name) {
        String uuid = UUIDGenerator.getUUID();
        mService.unBlockUser(login_name).enqueue(new BaseCallback<>(new UnBlockUserEvent(uuid)));
        return uuid;
    }

    /**
     * 获取用户屏蔽的用户列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see GetUserBlockedListEvent
     */
    @Override
    public String getUserBlockedList(@NonNull String login_name, @Nullable Integer offset,
                                     @Nullable Integer limit) {
        String uuid = UUIDGenerator.getUUID();
        mService.getUserBlockedList(login_name, offset, limit).enqueue(
                new BaseCallback<>(new GetUserBlockedListEvent(uuid)));
        return uuid;
    }

    //--- user follow ------------------------------------------------------------------------------

    /**
     * 关注用户
     *
     * @param login_name 登录用户名(非昵称)
     * @see FollowUserEvent
     */
    @Override
    public String followUser(@NonNull String login_name) {
        String uuid = UUIDGenerator.getUUID();
        mService.followUser(login_name).enqueue(new BaseCallback<>(new FollowUserEvent(uuid)));
        return uuid;
    }

    /**
     * 取消关注用户
     *
     * @param login_name 登录用户名(非昵称)
     * @see UnFollowUserEvent
     */
    @Override
    public String unFollowUser(@NonNull String login_name) {
        String uuid = UUIDGenerator.getUUID();
        mService.unFollowUser(login_name).enqueue(new BaseCallback<>(new UnFollowUserEvent(uuid)));
        return uuid;
    }

    /**
     * 用户正在关注的人列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see GetUserFollowingListEvent
     */
    @Override
    public String getUserFollowingList(@NonNull String login_name, @Nullable Integer offset,
                                       @Nullable Integer limit) {
        String uuid = UUIDGenerator.getUUID();
        mService.getUserFollowingList(login_name, offset, limit).enqueue(
                new BaseCallback<>(new GetUserFollowingListEvent(uuid)));
        return uuid;
    }

    /**
     * 关注该用户的人列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see GetUserFollowerListEvent
     */
    @Override
    public String getUserFollowerList(@NonNull String login_name, @Nullable Integer offset,
                                      @Nullable Integer limit) {
        String uuid = UUIDGenerator.getUUID();
        mService.getUserFollowerList(login_name, offset, limit).enqueue(
                new BaseCallback<>(new GetUserFollowerListEvent(uuid)));
        return uuid;
    }

    //--- user list --------------------------------------------------------------------------------

    /**
     * 用户收藏的话题列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see GetUserCreateTopicListEvent
     */
    @Override
    public String getUserCollectionTopicList(@NonNull String login_name, @Nullable Integer offset,
                                             @Nullable Integer limit) {
        String uuid = UUIDGenerator.getUUID();
        mService.getUserCollectionTopicList(login_name, offset, limit).enqueue(
                new BaseCallback<>(new GetUserCollectionTopicListEvent(uuid)));
        return uuid;
    }

    /**
     * 获取用户创建的话题列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param order      类型 默认 recent，可选["recent", "likes", "replies"]
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see GetUserCreateTopicListEvent
     */
    @Override
    public String getUserCreateTopicList(@NonNull String login_name, @Nullable String order,
                                         @Nullable Integer offset, @Nullable Integer limit) {
        String uuid = UUIDGenerator.getUUID();
        mService.getUserCreateTopicList(login_name, order, offset, limit).enqueue(
                new BaseCallback<>(new GetUserCreateTopicListEvent(uuid)));
        return uuid;
    }

    /**
     * 用户回复过的话题列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param order      类型 默认 recent，可选["recent"]
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see GetUserReplyTopicListEvent
     */
    @Override
    public String getUserReplyTopicList(@NonNull String login_name, @Nullable String order,
                                        @Nullable Integer offset, @Nullable Integer limit) {
        String uuid = UUIDGenerator.getUUID();
        mService.getUserReplyTopicList(login_name, order, offset, limit).enqueue(
                new BaseCallback<>(new GetUserReplyTopicListEvent(uuid)));
        return uuid;
    }
}
