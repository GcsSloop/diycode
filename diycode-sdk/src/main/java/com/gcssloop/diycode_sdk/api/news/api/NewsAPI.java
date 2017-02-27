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
 * Last modified 2017-02-27 14:58:32
 *
 */

package com.gcssloop.diycode_sdk.api.news.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface NewsAPI {

    /**
     * 获取 News 列表
     *
     * @param node_id 如果你需要只看某个节点的，请传此参数, 如果不传 则返回全部
     * @param offset  偏移数值，默认值 0
     * @param limit   数量极限，默认值 20，值范围 1..150
     */
    String getNews(@Nullable Integer node_id, @Nullable Integer offset, @Nullable Integer limit);

    /**
     * 创建 News
     *
     * @param title   News 标题
     * @param address News 链接
     * @param node_id News 节点编号
     */
    String createNews(@NonNull String title, @NonNull String address, @NonNull Integer node_id);

    /**
     * 获取 News 评论列表
     *
     * @param obj_id 编号
     * @param offset 偏移数值，默认值 0
     * @param limit  数量极限，默认值 20，值范围 1..150
     */
    String getNewsReply(@NonNull Integer obj_id, @Nullable Integer offset, @Nullable Integer limit);

    /**
     * 获取 News 评论详情
     *
     * @param obj_id 编号
     */
    String getNewsReplyContent(@NonNull Integer obj_id);

    /**
     * 更新 News 评论
     *
     * @param obj_id 编号
     * @param body   详情
     */
    String updateNewsReply(@NonNull Integer obj_id, @NonNull String body);

    /**
     * 删除 News 评论
     *
     * @param obj_id 编号
     */
    String deleteNewsReply(@NonNull Integer obj_id);
}
