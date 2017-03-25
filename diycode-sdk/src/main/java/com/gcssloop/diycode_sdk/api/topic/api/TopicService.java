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

package com.gcssloop.diycode_sdk.api.topic.api;

import com.gcssloop.diycode_sdk.api.base.bean.State;
import com.gcssloop.diycode_sdk.api.topic.bean.Topic;
import com.gcssloop.diycode_sdk.api.topic.bean.TopicContent;
import com.gcssloop.diycode_sdk.api.topic.bean.TopicReply;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface TopicService {

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
    Call<List<Topic>> getTopicsList(@Query("type") String type, @Query("node_id") Integer node_id,
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
    @FormUrlEncoded
    Call<TopicContent> createTopic(@Field("title") String title, @Field("body") String body,
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
    @FormUrlEncoded
    Call<TopicContent> updateTopic(@Path("id") int id, @Field("title") String title,
                                   @Field("body") String body, @Field("node_id") Integer node_id);

    /**
     * 删除一个话题
     *
     * @param id 要删除的话题 id
     * @return 状态
     */
    @DELETE("topics/{id}.json")
    Call<State> deleteTopic(@Path("id") int id);


    //--- topic collection -------------------------------------------------------------------------

    /**
     * 收藏话题
     *
     * @param id 被收藏的话题 id
     * @return 状态信息
     */
    @POST("topics/{id}/favorite.json")
    @FormUrlEncoded
    Call<State> collectionTopic(@Path("id") int id);

    /**
     * 取消收藏话题
     *
     * @param id 被收藏的话题 id
     * @return 状态信息
     */
    @POST("topics/{id}/unfavorite.json")
    @FormUrlEncoded
    Call<State> unCollectionTopic(@Path("id") int id);


    //--- topic watch ------------------------------------------------------------------------------

    /**
     * 关注话题
     *
     * @param id 话题 id
     * @return 状态
     */
    @POST("topics/{id}/follow.json")
    @FormUrlEncoded
    Call<State> watchTopic(@Path("id") int id);

    /**
     * 取消关注话题
     *
     * @param id 话题 id
     * @return 状态
     */
    @POST("topics/{id}/unfollow.json")
    @FormUrlEncoded
    Call<State> unWatchTopic(@Path("id") int id);


    //--- topic reply ------------------------------------------------------------------------------

    /**
     * 获取 topic 回复列表
     *
     * @param id     topic 的 id
     * @param offset 偏移数值 默认 0
     * @param limit  数量极限，默认值 20，值范围 1...150
     * @return 回复列表
     */
    @GET("topics/{id}/replies.json")
    Call<List<TopicReply>> getTopicRepliesList(@Path("id") int id, @Query("offset") Integer offset,
                                               @Query("limit") Integer limit);

    /**
     * 创建 topic 回帖(回复，评论)
     *
     * @param id   话题列表
     * @param body 回帖内容, Markdown 格式
     * @return 回复详情
     */
    @POST("topics/{id}/replies.json")
    @FormUrlEncoded
    Call<TopicReply> createTopicReply(@Path("id") int id, @Field("body") String body);

    /**
     * 获取回帖的详细内容（一般用于编辑回帖的时候）
     *
     * @param id id
     * @return 回帖内容
     */
    @GET("replies/{id}.json")
    Call<TopicReply> getTopicReply(@Path("id") int id);


    /**
     * 更新回帖
     *
     * @param id   id
     * @param body 回帖详情
     * @return 回帖内容
     */
    @POST("replies/{id}.json")
    @FormUrlEncoded
    Call<TopicReply> updateTopicReply(@Path("id") int id, @Field("body") String body);

    /**
     * 删除回帖
     *
     * @param id id
     * @return 状态
     */
    @DELETE("replies/{id}.json")
    Call<State> deleteTopicReply(@Path("id") int id);

    //--- topic ban --------------------------------------------------------------------------------

    /**
     * 屏蔽话题，移到 NoPoint 节点 (管理员限定)
     *
     * @param id 要屏蔽的话题 id
     * @return 状态
     */
    @POST("topics/{id}/ban.json")
    @FormUrlEncoded
    Call<State> banTopic(@Path("id") int id);
}
