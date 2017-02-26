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
 * Last modified 2017-02-27 04:54:45
 *
 */

package com.gcssloop.diycode_sdk.api.topic.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gcssloop.diycode_sdk.api.topic.event.GetTopicContentEvent;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicsEvent;

public interface TopicAPI {
    /**
     * 获取 Topics 列表
     *
     * @param type    类型，默认 last_actived，可选["last_actived", "recent", "no_reply", "popular", "excellent"]
     * @param node_id 如果你需要只看某个节点的，请传此参数, 如果不传 则返回全部
     * @param offset  偏移数值，默认值 0
     * @param limit   数量极限，默认值 20，值范围 1..150
     * @see GetTopicsEvent
     */
    String getTopics(@Nullable String type, @Nullable Integer node_id, @Nullable Integer offset, @Nullable Integer limit);

    /**
     * 获取 topic 内容
     *
     * @param id topic 的 id
     * @see GetTopicContentEvent
     */
    String getTopicContent(@NonNull Integer id);

    /**
     * 创建 Topic
     *
     * @param title   Topic 标题
     * @param body    Topic 内容
     * @param node_id Topic 节点编号
     */
    String newTopic(@NonNull String title, @NonNull String body, @NonNull Integer node_id);


    /**
     * 更新 topic
     *
     * @param title   标题
     * @param body    话题内容 Markdown 格式
     * @param node_id 节点编号
     */
    String updateTopic(@NonNull String title, @NonNull String body, @NonNull Integer node_id);

    /**
     * 删除 topic 仅支持删除自己创建的 topic
     *
     * @param id 编号
     */
    String deleteTopic(@NonNull Integer id);

    /**
     * 屏蔽话题，移到 NoPoint 节点 (Admin only)
     *
     * @param id 编号
     */
    String banTopic(@NonNull Integer id);

    /**
     * 关注话题
     *
     * @param id 编号
     */
    String followTopic(@NonNull Integer id);

    /**
     * 取消关注话题
     *
     * @param id 编号
     */
    String unFollowRopic(@NonNull Integer id);

    /**
     * 收藏一个话题
     *
     * @param id 编号
     */
    String collectionTopic(@NonNull Integer id);

    /**
     * 取消收藏一个话题
     *
     * @param id 编号
     */
    String unCollectionTopic(@NonNull Integer id);

    /**
     * 获取话题评论列表
     *
     * @param id     编号
     * @param offset 偏移数值，默认值 0
     * @param limit  数量极限，默认值 20，值范围 1..150
     */
    String getTopicReplies(@NonNull Integer id, @Nullable Integer offset, @Nullable Integer limit);

    /**
     * 创建话题
     *
     * @param id   编号
     * @param body 内容 Markdown 格式
     */
    String createTopicReplies(@NonNull Integer id, @NonNull String body);

}
