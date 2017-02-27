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
 * Last modified 2017-02-28 02:56:45
 *
 */

package com.gcssloop.diycode_sdk.api.reply.api;

import android.support.annotation.NonNull;

public interface ReplyAPI {

    /**
     * 获取回帖的详细内容（一般用于编辑回帖的时候）
     *
     * @param id 编号
     */
    String getReply(@NonNull Integer id);

    /**
     * 更新回帖
     *
     * @param id   编号
     * @param body 帖子详情
     */
    String postReply(@NonNull Integer id, @NonNull String body);

    /**
     * 删除回帖
     *
     * @param id 编号
     */
    String deleteReply(@NonNull Integer id);

}
