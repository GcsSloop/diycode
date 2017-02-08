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
 * Last modified 2017-02-03 06:33:23
 *
 */

package com.gcssloop.diycode_sdk.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;

public interface DiycodeAPI {

    //--- 登录相关内容 ----------------------------------------------------------------------------

    /**
     * 登录时调用
     * 返回一个 token，用于获取各类私有信息使用，该 token 用 LoginEvent 接收。
     *
     * @param user_name 用户名
     * @param password  密码
     */
    void login(@NonNull String user_name, @NonNull String password);

    /**
     * 更新设备信息
     * 记录用户 Device 信息，用于 Push 通知。
     * 请在每次用户打开 App 的时候调用此 API 以便更新 Token 的 last_actived_at 让服务端知道这个设备还活着。
     * Push 将会忽略那些超过两周的未更新的设备。
     */
    void updateDevices();

    /**
     * 删除 Device 信息，请注意在用户登出或删除应用的时候调用，以便能确保清理掉。
     */
    void delateDevices();


    //--- 测试接口 -------------------------------------------------------------------------------

    /**
     * 简单的 API 测试接口，需要登录验证，便于快速测试 OAuth 以及其他 API 的基本格式是否正确。
     * 使用 HelloEvent 接收结果。
     *
     * @param limit 数量极限，值范围[0..100]
     */
    void hello(@Nullable Integer limit);


    //--- like -------------------------------------------------------------------------------------

    /**
     * 赞
     *
     * @param obj_type 值范围["topic", "reply", "news"]
     * @param obj_id   唯一id
     */
    void like(@NonNull String obj_type, @NonNull Integer obj_id);


    /**
     * 取消之前的赞
     *
     * @param obj_type 值范围["topic", "reply", "news"]
     * @param obj_id   唯一id
     */
    void unLike(@NonNull String obj_type, @NonNull Integer obj_id);


    //--- news -------------------------------------------------------------------------------------

    /**
     * 获取 News 列表
     *
     * @param node_id 如果你需要只看某个节点的，请传此参数, 如果不传 则返回全部
     * @param offset  偏移数值，默认值 0
     * @param limit   数量极限，默认值 20，值范围 1..150
     */
    void getNews(@Nullable Integer node_id, @Nullable Integer offset, @Nullable Integer limit);

    /**
     * 创建 News
     *
     * @param title   News 标题
     * @param address News 链接
     * @param node_id News 节点编号
     */
    void createNews(@NonNull String title, @NonNull String address, @NonNull Integer node_id);

    /**
     * 获取 News 评论列表
     *
     * @param obj_id 编号
     * @param offset 偏移数值，默认值 0
     * @param limit  数量极限，默认值 20，值范围 1..150
     */
    void getNewsReply(@NonNull Integer obj_id, @Nullable Integer offset, @Nullable Integer limit);

    /**
     * 获取 News 评论详情
     *
     * @param obj_id 编号
     */
    void getNewsReplyContent(@NonNull Integer obj_id);

    /**
     * 更新 News 评论
     *
     * @param obj_id 编号
     * @param body   详情
     */
    void updateNewsReply(@NonNull Integer obj_id, @NonNull String body);

    /**
     * 删除 News 评论
     *
     * @param obj_id 编号
     */
    void deleteNewsReply(@NonNull Integer obj_id);


    //--- topic ------------------------------------------------------------------------------------

    /**
     * 获取 Topics 列表
     *
     * @param node_id 如果你需要只看某个节点的，请传此参数, 如果不传 则返回全部
     * @param offset  偏移数值，默认值 0
     * @param limit   数量极限，默认值 20，值范围 1..150
     */
    void getTopics(@Nullable Integer node_id, @Nullable Integer offset, @Nullable Integer limit);


    /**
     * 创建 Topic
     *
     * @param title   Topic 标题
     * @param body    Topic 内容
     * @param node_id Topic 节点编号
     */
    void createTopic(@NonNull String title, @NonNull String body, @NonNull Integer node_id);

    /**
     * 获取 topic 内容
     */
    void getTopicContent(@NonNull Integer obj_id);

    /**
     * 更新 topic
     *
     * @param title   标题
     * @param body    话题内容 Markdown 格式
     * @param node_id 节点编号
     */
    void updateTopic(@NonNull String title, @NonNull String body, @NonNull Integer node_id);

    /**
     * 删除 topic 仅支持删除自己创建的 topic
     *
     * @param id 编号
     */
    void deleteTopic(@NonNull Integer id);

    /**
     * 屏蔽话题，移到 NoPoint 节点 (Admin only)
     *
     * @param id 编号
     */
    void banTopic(@NonNull Integer id);

    /**
     * 关注话题
     *
     * @param id 编号
     */
    void followTopic(@NonNull Integer id);

    /**
     * 取消关注话题
     *
     * @param id 编号
     */
    void unFollowRopic(@NonNull Integer id);

    /**
     * 收藏一个话题
     *
     * @param id 编号
     */
    void collectionTopic(@NonNull Integer id);

    /**
     * 取消收藏一个话题
     *
     * @param id 编号
     */
    void unCollectionTopic(@NonNull Integer id);

    /**
     * 获取话题评论列表
     *
     * @param id     编号
     * @param offset 偏移数值，默认值 0
     * @param limit  数量极限，默认值 20，值范围 1..150
     */
    void getTopicReplies(@NonNull Integer id, @NonNull Integer offset, @NonNull Integer limit);

    /**
     * 创建话题
     *
     * @param id   编号
     * @param body 内容 Markdown 格式
     */
    void createTopicReplies(@NonNull Integer id, @NonNull String body);

    //--- reply ------------------------------------------------------------------------------------

    /**
     * 获取回帖的详细内容（一般用于编辑回帖的时候）
     *
     * @param id 编号
     */
    void getReply(@NonNull Integer id);

    /**
     * 更新回帖
     *
     * @param id   编号
     * @param body 帖子详情
     */
    void postReply(@NonNull Integer id, @NonNull String body);

    /**
     * 删除回帖
     *
     * @param id 编号
     */
    void deleteReply(@NonNull Integer id);


    //--- photo ------------------------------------------------------------------------------------

    /**
     * 上传图片,请使用 Multipart 的方式提交图片文件
     *
     * @param img_file 图片文件
     */
    void uploadPhoto(@NonNull File img_file);


    //--- sites ------------------------------------------------------------------------------------

    /**
     * 获取 sites 列表
     */
    void getSites();


    //--- user -------------------------------------------------------------------------------------

    /**
     * 获得用户列表
     *
     * @param limit 数量极限，默认值 20，值范围 1..150
     */
    void getUsers(@NonNull Integer limit);

    /**
     * 获取用户信息
     *
     * @param username 用户名
     */
    void getUserInfo(@NonNull String username);

    /**
     * 屏蔽某个用户
     *
     * @param username 被屏蔽的用户名
     */
    void blockUser(@NonNull String username);

    /**
     * 取消屏蔽某个用户
     *
     * @param username 被屏蔽的用户名
     */
    void unBlockUser(@NonNull String username);

    /**
     * 获得被屏蔽的用户列表
     *
     * @param username 自己用户名
     */
    void getBlocked(@NonNull String username);

    /**
     * 关注某个用户
     *
     * @param username 关注的用户名
     */
    void followUser(@NonNull String username);

    /**
     * 取消关注某个用户
     *
     * @param username 取消关注的用户名
     */
    void unFollowUser(@NonNull String username);

    /**
     * 获取关注者列表
     *
     * @param offset 偏移数值，默认值 0
     * @param limit  数量极限，默认值 20，值范围 1..150
     */
    void getFollowers(@NonNull String username, @NonNull Integer offset, @NonNull Integer limit);

    /**
     * 获取正在关注的列表
     *
     * @param offset 偏移数值，默认值 0
     * @param limit  数量极限，默认值 20，值范围 1..150
     */
    void getFollowing(@NonNull String username, @NonNull Integer offset, @NonNull Integer limit);

    /**
     * 获取用户创建的回帖列表
     *
     * @param username 用户名
     * @param order    顺序，默认值 recent，取值范围 	["recent"]
     * @param offset   偏移数值，默认值 0
     * @param limit    数量极限，默值 20，值范围 1..150
     */
    void getUserReplies(@NonNull String username, @Nullable String order, @Nullable Integer offset, @Nullable String limit);

    /**
     * 获取用户相关的话题列表
     *
     * @param username 用户名
     * @param order    顺序，默认值 recent，取值范围 	["recent", "likes", "replies"]
     * @param offset   偏移数值，默认值 0
     * @param limit    数量极限，默认值 20，值范围 1..150
     */
    void getUserTopics(@NonNull String username, @Nullable String order, @Nullable Integer offset, @Nullable String limit);

    /**
     * 获取用户收藏的话题列表
     *
     * @param username 用户名
     * @param offset   偏移数值，默认值 0
     * @param limit    数量极限，默认值 20，值范围 1..150
     */
    void getCollection(@NonNull String username, @Nullable Integer offset, @Nullable String limit);

    /**
     * 获取当前登录者资料
     */
    void getMe();

    //--- notification -----------------------------------------------------------------------------

    /**
     * 获取通知列表
     *
     * @param offset 偏移数值，默认值 0
     * @param limit  数量极限，默认值 20，值范围 1..150
     */
    void getNotifications(@NonNull Integer offset, @NonNull Integer limit);

    /**
     * 删除用户的某条通知
     *
     * @param id
     */
    void deleteNotionfition(@NonNull Integer id);

    /**
     * 删除当前用户的所有通知
     */
    void deleteAllNotification();

    /**
     * 将某些通知标记为已读
     *
     * @param ids id集合
     */
    void markNotificationAsRead(ArrayList<Integer> ids);

    /**
     * 获得未读通知的数量
     */
    void getUnreadNotificationCount();


    //--- nodes ------------------------------------------------------------------------------------
    //--- project ----------------------------------------------------------------------------------
}
