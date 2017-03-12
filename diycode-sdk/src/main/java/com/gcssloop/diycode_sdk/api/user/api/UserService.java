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

import com.gcssloop.diycode_sdk.api.base.bean.State;
import com.gcssloop.diycode_sdk.api.topic.bean.Topic;
import com.gcssloop.diycode_sdk.api.user.bean.User;
import com.gcssloop.diycode_sdk.api.user.bean.UserDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface UserService {

    //--- user info --------------------------------------------------------------------------------

    /**
     * 获取用户列表
     *
     * @param limit 数量极限，默认值 20，值范围 1..150
     * @return 用户列表
     */
    @GET("users.json")
    Call<List<User>> getUsersList(@Query("limit") Integer limit);

    /**
     * 获取用户详细资料
     *
     * @param login_name 登录用户名(非昵称)
     * @return 用户详情
     */
    @GET("users/{login}.json")
    Call<UserDetail> getUser(@Path("login") String login_name);

    /**
     * 获取当前登录者的详细资料
     *
     * @return 用户详情
     */
    @GET("users/me.json")
    Call<UserDetail> getMe();

    //--- user block -------------------------------------------------------------------------------

    /**
     * 屏蔽用户
     *
     * @param login_name 登录用户名(非昵称)
     * @return 状态
     */
    @Deprecated
    @POST("users/{login}/block.json")
    Call<State> blockUser(@Path("login") String login_name);

    /**
     * 取消屏蔽用户
     *
     * @param login_name 登录用户名(非昵称)
     * @return 状态
     */
    @Deprecated
    @POST("users/{login}/unblock.json")
    Call<State> unBlockUser(@Path("login") String login_name);

    /**
     * 获取用户屏蔽的用户列表(只能获取自己的)
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @return 被屏蔽的用户列表
     */
    @GET("users/{login}/blocked.json")
    Call<List<User>> getUserBlockedList(@Path("login") String login_name,
                                    @Query("offset") Integer offset, @Query("limit") Integer limit);


    //--- user follow ------------------------------------------------------------------------------

    /**
     * 关注用户
     *
     * @param login_name 登录用户名(非昵称)
     * @return 状态
     */
    @Deprecated
    @POST("users/{login}/follow.json")
    Call<State> followUser(@Path("login") String login_name);

    /**
     * 取消关注用户
     *
     * @param login_name 登录用户名(非昵称)
     * @return 状态
     */
    @Deprecated
    @POST("users/{login}/unfollow.json")
    Call<State> unFollowUser(@Path("login") String login_name);

    /**
     * 用户正在关注的人列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @return 用户关注的人列表
     */
    @GET("users/{login}/following.json")
    Call<List<User>> getUserFollowingList(@Path("login") String login_name,
                                      @Query("offset") Integer offset, @Query("limit") Integer limit);

    /**
     * 关注该用户的人列白哦
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @return 关注该用户的人列表
     */
    @GET("users/{login}/followers.json")
    Call<List<User>> getUserFollowerList(@Path("login") String login_name,
                                     @Query("offset") Integer offset, @Query("limit") Integer limit);


    //--- user list --------------------------------------------------------------------------------

    /**
     * 用户收藏的话题列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @return 话题列表
     */
    @GET("users/{login}/favorites.json")
    Call<List<Topic>> getUserCollectionTopicList(@Path("login") String login_name,
                                             @Query("offset") Integer offset, @Query("limit") Integer limit);


    /**
     * 获取用户创建的话题列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param order      类型 默认 recent，可选["recent", "likes", "replies"]
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @return 话题列表
     */
    @GET("users/{login}/topics.json")
    Call<List<Topic>> getUserCreateTopicList(@Path("login") String login_name, @Query("order") String order,
                                         @Query("offset") Integer offset, @Query("limit") Integer limit);


    /**
     * 用户回复过的话题列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param order      类型 默认 recent，可选["recent"]
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @return 话题列表
     */
    @GET("users/{login}/replies.json")
    Call<List<Topic>> getUserReplyTopicList(@Path("login") String login_name, @Query("order") String order,
                                        @Query("offset") Integer offset, @Query("limit") Integer limit);
}
