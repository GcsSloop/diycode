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
 * Last modified 2017-02-28 03:00:21
 *
 */

package com.gcssloop.diycode_sdk.api.user.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface UserAPI {

    /**
     * 获得用户列表
     *
     * @param limit 数量极限，默认值 20，值范围 1..150
     */
    String getUsers(@NonNull Integer limit);

    /**
     * 获取用户信息
     *
     * @param username 用户名
     */
    String getUserInfo(@NonNull String username);

    /**
     * 屏蔽某个用户
     *
     * @param username 被屏蔽的用户名
     */
    String blockUser(@NonNull String username);

    /**
     * 取消屏蔽某个用户
     *
     * @param username 被屏蔽的用户名
     */
    String unBlockUser(@NonNull String username);

    /**
     * 获得被屏蔽的用户列表
     *
     * @param username 自己用户名
     */
    String getBlocked(@NonNull String username);

    /**
     * 关注某个用户
     *
     * @param username 关注的用户名
     */
    String followUser(@NonNull String username);

    /**
     * 取消关注某个用户
     *
     * @param username 取消关注的用户名
     */
    String unFollowUser(@NonNull String username);

    /**
     * 获取关注者列表
     *
     * @param offset 偏移数值，默认值 0
     * @param limit  数量极限，默认值 20，值范围 1..150
     */
    String getFollowers(@NonNull String username, @NonNull Integer offset, @NonNull Integer limit);

    /**
     * 获取正在关注的列表
     *
     * @param offset 偏移数值，默认值 0
     * @param limit  数量极限，默认值 20，值范围 1..150
     */
    String getFollowing(@NonNull String username, @NonNull Integer offset, @NonNull Integer limit);

    /**
     * 获取用户创建的回帖列表
     *
     * @param username 用户名
     * @param order    顺序，默认值 recent，取值范围 	["recent"]
     * @param offset   偏移数值，默认值 0
     * @param limit    数量极限，默值 20，值范围 1..150
     */
    String getUserReplies(@NonNull String username, @Nullable String order, @Nullable Integer offset, @Nullable String limit);

    /**
     * 获取用户相关的话题列表
     *
     * @param username 用户名
     * @param order    顺序，默认值 recent，取值范围 	["recent", "likes", "replies"]
     * @param offset   偏移数值，默认值 0
     * @param limit    数量极限，默认值 20，值范围 1..150
     */
    String getUserTopics(@NonNull String username, @Nullable String order, @Nullable Integer offset, @Nullable String limit);

    /**
     * 获取用户收藏的话题列表
     *
     * @param username 用户名
     * @param offset   偏移数值，默认值 0
     * @param limit    数量极限，默认值 20，值范围 1..150
     */
    String getCollection(@NonNull String username, @Nullable Integer offset, @Nullable String limit);

    /**
     * 获取当前登录者资料
     */
    String getMe();
}
