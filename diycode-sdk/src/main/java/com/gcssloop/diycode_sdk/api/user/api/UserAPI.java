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

import java.util.List;

import retrofit2.Call;

public interface UserAPI {

    //--- user info --------------------------------------------------------------------------------

    /**
     * 获取用户列表
     *
     * @param limit 数量极限，默认值 20，值范围 1..150
     * @see GetUs
     */
    String getUsersList(Integer limit);

    /**
     * 获取用户详细资料
     *
     * @param login_name 登录用户名(非昵称)
     * @see
     */
    String getUser(String login_name);

    /**
     * 获取当前登录者的详细资料
     *
     * @see
     */
    String getMe();

    //--- user block -------------------------------------------------------------------------------

    /**
     * 屏蔽用户
     *
     * @param login_name 登录用户名(非昵称)
     * @see
     */
    @Deprecated
    Call<State> blockUser(String login_name);

    /**
     * 取消屏蔽用户
     *
     * @param login_name 登录用户名(非昵称)
     * @see
     */
    @Deprecated
    String unBlockUser(String login_name);

    /**
     * 获取用户屏蔽的用户列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see
     */
    String getUserBlockedList(String login_name,
                                        Integer offset, Integer limit);

    //--- user follow ------------------------------------------------------------------------------

    /**
     * 关注用户
     *
     * @param login_name 登录用户名(非昵称)
     * @see
     */
    @Deprecated
    String followUser(String login_name);

    /**
     * 取消关注用户
     *
     * @param login_name 登录用户名(非昵称)
     * @see
     */
    @Deprecated
    String unFollowUser(String login_name);

    /**
     * 用户正在关注的人列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see
     */
    String getUserFollowingList(String login_name,
                                          Integer offset, Integer limit);

    /**
     * 关注该用户的人列白哦
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see
     */
    String getUserFollowerList(String login_name,
                                         Integer offset, Integer limit);


    //--- user list --------------------------------------------------------------------------------

    /**
     * 用户收藏的话题列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see
     */
    Call<List<Topic>> getUserCollectionTopicList(String login_name,
                                                 Integer offset, Integer limit);


    /**
     * 获取用户创建的话题列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param order      类型 默认 recent，可选["recent", "likes", "replies"]
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see
     */
    String getUserCreateTopicList(String login_name, String order,
                                             Integer offset, Integer limit);


    /**
     * 用户回复过的话题列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param order      类型 默认 recent，可选["recent"]
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see
     */
    String getUserReplyTopicList(String login_name, String order,
                                            Integer offset, Integer limit);
}
