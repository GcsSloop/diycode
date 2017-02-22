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
import com.gcssloop.diycode_sdk.api.bean.Token;
import com.gcssloop.diycode_sdk.api.bean.Topic;
import com.gcssloop.diycode_sdk.api.bean.TopicContent;
import com.gcssloop.diycode_sdk.api.bean.TopicReply;
import com.gcssloop.diycode_sdk.api.utils.Constant;

import java.util.List;

import retrofit2.Call;
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

    //--- 测试接口 -------------------------------------------------------------------------------

    /**
     * 测试 token 是否正常
     *
     * @param limit 极限值
     * @return Hello 实体类
     */
    @GET("hello.json")
    Call<Hello> hello(@Query("limit") Integer limit);


    //--- topic -------------------------------------------------------------------------------

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
     * 获取 topic 内容
     *
     * @param id topic 的 id
     * @return 内容详情
     */
    @GET("topics/{id}.json")
    Call<TopicContent> getTopic(@Path("id") int id);


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
}
