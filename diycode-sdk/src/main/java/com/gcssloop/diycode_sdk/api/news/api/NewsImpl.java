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
 * Last modified 2017-03-08 23:50:55
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode_sdk.api.news.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gcssloop.diycode_sdk.api.base.callback.BaseCallback;
import com.gcssloop.diycode_sdk.api.base.impl.BaseImpl;
import com.gcssloop.diycode_sdk.api.news.event.CreateNewsEvent;
import com.gcssloop.diycode_sdk.api.news.event.CreateNewsReplyEvent;
import com.gcssloop.diycode_sdk.api.news.event.DeleteNewsReplyEvent;
import com.gcssloop.diycode_sdk.api.news.event.GetNewsListEvent;
import com.gcssloop.diycode_sdk.api.news.event.GetNewsNodesListEvent;
import com.gcssloop.diycode_sdk.api.news.event.GetNewsRepliesListEvent;
import com.gcssloop.diycode_sdk.api.news.event.GetNewsReplyEvent;
import com.gcssloop.diycode_sdk.api.news.event.UpdateNewsReplyEvent;
import com.gcssloop.diycode_sdk.utils.UUIDGenerator;

public class NewsImpl extends BaseImpl<NewsService> implements NewsAPI {

    public NewsImpl(@NonNull Context context) {
        super(context);
    }

    /**
     * 获取 news 列表
     *
     * @param node_id 如果你需要只看某个节点的，请传此参数, 如果不传 则返回全部
     * @param offset  偏移数值，默认值 0
     * @param limit   数量极限，默认值 20，值范围 1..150
     * @see GetNewsListEvent
     */
    @Override
    public String getNewsList(@Nullable Integer node_id, @Nullable Integer offset, @Nullable Integer limit) {
        String uuid = UUIDGenerator.getUUID();
        mService.getNewsList(node_id, offset, limit).enqueue(new BaseCallback<>(new GetNewsListEvent(uuid)));
        return uuid;
    }

    /**
     * 创建一个 new (分享)
     *
     * @param title   标题
     * @param address 地址(网址链接)
     * @param node_id 节点 id
     * @see CreateNewsEvent
     */
    @Override
    public String createNews(@NonNull Integer title, @NonNull Integer address, @NonNull Integer node_id) {
        String uuid = UUIDGenerator.getUUID();
        mService.createNews(title, address, node_id).enqueue(new BaseCallback<>(new CreateNewsEvent(uuid)));
        return uuid;
    }

    /**
     * 获取 news 回帖列表
     *
     * @param id     id
     * @param offset 偏移数值 默认 0
     * @param limit  数量极限，默认值 20，值范围 1...150
     * @see GetNewsRepliesListEvent
     */
    @Override
    public String getNewsRepliesList(@NonNull int id, @Nullable Integer offset, @Nullable Integer limit) {
        String uuid = UUIDGenerator.getUUID();
        mService.getNewsRepliesList(id, offset, limit).enqueue(new BaseCallback<>(new GetNewsRepliesListEvent(uuid)));
        return uuid;
    }

    /**
     * 创建 news 回帖 (暂不可用, 没有api)
     *
     * @param id   id
     * @param body 回帖内容， markdown格式
     * @see CreateNewsReplyEvent
     */
    @Override
    public String createNewsReply(@NonNull int id, @NonNull Integer body) {
        String uuid = UUIDGenerator.getUUID();
        mService.createNewsReply(id, body).enqueue(new BaseCallback<>(new CreateNewsReplyEvent(uuid)));
        return uuid;
    }

    /**
     * 获取 news 回帖详情
     *
     * @param id id
     * @see GetNewsReplyEvent
     */
    @Override
    public String getNewsReply(@NonNull int id) {
        String uuid = UUIDGenerator.getUUID();
        mService.getNewsReply(id).enqueue(new BaseCallback<>(new GetNewsReplyEvent(uuid)));
        return uuid;
    }

    /**
     * 更新 news 回帖
     *
     * @param id   id
     * @param body 回帖内容
     * @see UpdateNewsReplyEvent
     */
    @Override
    public String updateNewsReply(@NonNull int id, @NonNull String body) {
        String uuid = UUIDGenerator.getUUID();
        mService.updateNewsReply(id, body).enqueue(new BaseCallback<>(new UpdateNewsReplyEvent(uuid)));
        return uuid;
    }

    /**
     * 删除 news 回帖
     *
     * @param id id
     * @see DeleteNewsReplyEvent
     */
    @Override
    public String deleteNewsReply(@NonNull int id) {
        String uuid = UUIDGenerator.getUUID();
        mService.deleteNewsReply(id).enqueue(new BaseCallback<>(new DeleteNewsReplyEvent(uuid)));
        return uuid;
    }

    /**
     * 获取 news 分类列表
     *
     * @see GetNewsNodesListEvent
     */
    @Override
    public String getNewsNodesList() {
        String uuid = UUIDGenerator.getUUID();
        mService.getNewsNodesList().enqueue(new BaseCallback<>(new GetNewsNodesListEvent(uuid)));
        return uuid;
    }
}
