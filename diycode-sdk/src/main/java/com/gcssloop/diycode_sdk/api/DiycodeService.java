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
 * Last modified 2017-02-09 23:07:17
 *
 */

package com.gcssloop.diycode_sdk.api;

import com.gcssloop.diycode_sdk.api.bean.Hello;
import com.gcssloop.diycode_sdk.api.bean.State;
import com.gcssloop.diycode_sdk.api.bean.Token;
import com.gcssloop.diycode_sdk.api.bean.Topic;
import com.gcssloop.diycode_sdk.api.bean.TopicContent;
import com.gcssloop.diycode_sdk.api.bean.TopicReply;
import com.gcssloop.diycode_sdk.api.utils.Constant;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DiycodeService {

    //--- Token ------------------------------------------------------------------------------------

    /**
     * 获取 Token (一般在登录时调用)
     *
     * @param client_id     客户端 id
     * @param client_secret 客户端私钥
     * @param grant_type    授权方式 - 密码
     * @param username      用户名
     * @param password      密码
     * @return Token 实体类
     */
    @POST(Constant.OAUTH_URL)
    @FormUrlEncoded
    Call<Token> getToken(
            @Field("client_id") String client_id, @Field("client_secret") String client_secret,
            @Field("grant_type") String grant_type, @Field("username") String username,
            @Field("password") String password);

    /**
     * 刷新 token
     *
     * @param client_id     客户端 id
     * @param client_secret 客户端私钥
     * @param grant_type    授权方式 - Refresh Token
     * @param refresh_token token 信息
     * @return Token 实体类
     */
    @POST(Constant.OAUTH_URL)
    @FormUrlEncoded
    Call<Token> refreshToken(@Field("client_id") String client_id, @Field("client_secret") String client_secret,
                             @Field("grant_type") String grant_type, @Field("refresh_token") String refresh_token);


    //--- devices -------------------------------------------------------------------------------

    /**
     * 记录用户 Device 信息，用于 Push 通知。
     * 请在每次用户打开 App 的时候调用此 API 以便更新 Token 的 last_actived_at 让服务端知道这个设备还活着。
     * Push 将会忽略那些超过两周的未更新的设备。
     * @param platform 平台 ["ios", "android"]
     * @param token 令牌 token
     * @return 是否成功
     */
    @POST("devices.json")
    @FormUrlEncoded
    Call<State> registerDevices(@Field("platform") String platform, @Field("token") String token);

    /**
     * 删除 Device 信息，请注意在用户登出或删除应用的时候调用，以便能确保清理掉
     * @param platform
     * @param platform 平台 ["ios", "android"]
     * @param token 令牌 token
     * @return 是否成功
     */
    @DELETE("devices.json")
    @FormUrlEncoded
    Call<State> unRegisterDevices(@Field("platform") String platform, @Field("token") String token);


    //--- 测试接口 -------------------------------------------------------------------------------

    /**
     * 测试 token 是否正常
     *
     * @param limit 极限值
     * @return Hello 实体类
     */
    @GET("hello.json")
    Call<Hello> hello(@Query("limit") Integer limit);


    //--- topic ------------------------------------------------------------------------------------

    /**
     * 获取 topic 列表
     *
     * @param type    类型，默认 last_actived，可选["last_actived", "recent", "no_reply", "popular", "excellent"]
     * @param node_id 如果你需要只看某个节点的，请传此参数, 如果不传 则返回全部
     * @param offset  偏移数值，默认值 0
     * @param limit   数量极限，默认值 20，值范围 1..150
     * @return topic 列表
     */
    @GET("topics.json")
    Call<List<Topic>> getTopics(@Query("type") String type, @Query("node_id") Integer node_id,
                                @Query("offset") Integer offset, @Query("limit") Integer limit);


    /**
     * 创建一个新的 topic
     *
     * @param title   话题标题
     * @param body    话题内容, Markdown 格式
     * @param node_id 节点编号
     * @return 新话题的内容详情
     */
    @POST("topics.json")
    Call<TopicContent> newTopic(@Field("title") String title, @Field("body") String body,
                                @Field("node_id") Integer node_id);

    /**
     * 获取 topic 内容
     *
     * @param id topic 的 id
     * @return 内容详情
     */
    @GET("topics/{id}.json")
    Call<TopicContent> getTopic(@Path("id") int id);


    /**
     * 更新(修改) topic
     *
     * @param id      要修改的话题 id
     * @param title   话题标题
     * @param body    话题内容, Markdown 格式
     * @param node_id 节点编号
     * @return 更新后的话题内容详情
     */
    @POST("topics/{id}.json")
    Call<TopicContent> updateTopic(@Path("id") int id, @Field("title") String title,
                                   @Field("body") String body, @Field("node_id") Integer node_id);

    /**
     * 删除一个话题
     *
     * @param id 要删除的话题 id
     * @return
     */
    @DELETE("topics/{id}.json")
    Call<State> deleteTopic(@Path("id") int id);

    /**
     * 屏蔽话题，移到 NoPoint 节点 (管理员限定)
     *
     * @param id 要屏蔽的话题 id
     * @return
     */
    @POST("topics/{id}/ban.json")
    Call<State> banTopic(@Path("id") int id);

    /**
     * 取消收藏话题
     *
     * @param id 被收藏的话题 id
     * @return 状态信息
     */
    @POST("topics/{id}/unfavorite.json")
    Call<State> unCollectionTopic(@Path("id") int id);

    /**
     * 收藏话题
     *
     * @param id 被收藏的话题 id
     * @return 状态信息
     */
    @POST("topics/{id}/favorite.json")
    Call<State> collectionTopic(@Path("id") int id);

    /**
     * 关注话题
     *
     * @param id 话题 id
     * @return 状态
     */
    @POST("topics/{id}/follow.json")
    Call<State> watchTopic(@Path("id") int id);

    /**
     * 取消关注话题
     *
     * @param id 话题 id
     * @return 状态
     */
    @POST("topics/{id}/unfollow.json")
    Call<State> unWatchTopic(@Path("id") int id);

    //--- topic  reply -----------------------------------------------------------------------------

    /**
     * 获取 topic 回复列表
     *
     * @param id     topic 的 id
     * @param offset 偏移数值 默认 0
     * @param limit  数量极限，默认值 20，值范围 1...150
     * @return 回复列表
     */
    @GET("topics/{id}/replies.json")
    Call<List<TopicReply>> getTopicReplies(@Path("id") int id, @Query("offset") Integer offset,
                                           @Query("limit") Integer limit);

    /**
     * 创建 topic 回帖(回复，评论)
     *
     * @param id   话题列表
     * @param body 回帖内容, Markdown 格式
     * @return
     */
    @POST("topics/{id}/replies.json")
    Call<List<TopicReply>> newTopicReplies(@Path("id") int id, @Query("body") String body);


}
