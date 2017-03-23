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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gcssloop.diycode_sdk.api.user.bean.UserDetail;
import com.gcssloop.diycode_sdk.api.user.event.BlockUserEvent;
import com.gcssloop.diycode_sdk.api.user.event.FollowUserEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetMeEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserBlockedListEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserCreateTopicListEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserFollowerListEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserFollowingListEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserReplyTopicListEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUsersListEvent;
import com.gcssloop.diycode_sdk.api.user.event.UnBlockUserEvent;
import com.gcssloop.diycode_sdk.api.user.event.UnFollowUserEvent;

import java.io.IOException;

public interface UserAPI {

    //--- user info --------------------------------------------------------------------------------

    /**
     * 获取用户列表
     *
     * @param limit 数量极限，默认值 20，值范围 1..150
     * @see GetUsersListEvent
     */
    String getUsersList(@Nullable Integer limit);

    /**
     * 获取用户详细资料
     *
     * @param login_name 登录用户名(非昵称)
     * @see GetUserEvent
     */
    String getUser(@NonNull String login_name);

    /**
     * 获取当前登录者的详细资料
     *
     * @see GetMeEvent
     */
    String getMe();

    /**
     * 立即获取获取当前登录者的详细资料
     * @return 用户资料
     */
    UserDetail getMeNow() throws IOException;

    //--- user block -------------------------------------------------------------------------------

    /**
     * 屏蔽用户
     *
     * @param login_name 登录用户名(非昵称)
     * @see BlockUserEvent
     */
    @Deprecated
    String blockUser(@NonNull String login_name);

    /**
     * 取消屏蔽用户
     *
     * @param login_name 登录用户名(非昵称)
     * @see UnBlockUserEvent
     */
    @Deprecated
    String unBlockUser(@NonNull String login_name);

    /**
     * 获取用户屏蔽的用户列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see GetUserBlockedListEvent
     */
    String getUserBlockedList(@NonNull String login_name,
                              @Nullable Integer offset, @Nullable Integer limit);

    //--- user follow ------------------------------------------------------------------------------

    /**
     * 关注用户
     *
     * @param login_name 登录用户名(非昵称)
     * @see FollowUserEvent
     */
    @Deprecated
    String followUser(@NonNull String login_name);

    /**
     * 取消关注用户
     *
     * @param login_name 登录用户名(非昵称)
     * @see UnFollowUserEvent
     */
    @Deprecated
    String unFollowUser(@NonNull String login_name);

    /**
     * 用户正在关注的人列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see GetUserFollowingListEvent
     */
    String getUserFollowingList(@NonNull String login_name,
                                @Nullable Integer offset, @Nullable Integer limit);

    /**
     * 关注该用户的人列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see GetUserFollowerListEvent
     */
    String getUserFollowerList(@NonNull String login_name,
                               @Nullable Integer offset, @Nullable Integer limit);


    //--- user list --------------------------------------------------------------------------------

    /**
     * 用户收藏的话题列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see GetUserCreateTopicListEvent
     */
    String getUserCollectionTopicList(@NonNull String login_name,
                                                 @Nullable Integer offset, @Nullable Integer limit);


    /**
     * 获取用户创建的话题列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param order      类型 默认 recent，可选["recent", "likes", "replies"]
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see GetUserCreateTopicListEvent
     */
    String getUserCreateTopicList(@NonNull String login_name, @Nullable String order,
                                  @Nullable Integer offset, @Nullable Integer limit);


    /**
     * 用户回复过的话题列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param order      类型 默认 recent，可选["recent"]
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see GetUserReplyTopicListEvent
     */
    String getUserReplyTopicList(@NonNull String login_name, @Nullable String order,
                                 @Nullable Integer offset, @Nullable Integer limit);
}
