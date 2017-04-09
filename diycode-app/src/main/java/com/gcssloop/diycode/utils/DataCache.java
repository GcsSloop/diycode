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
 * Last modified 2017-03-12 02:50:16
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.LruCache;

import com.gcssloop.diycode_sdk.api.news.bean.New;
import com.gcssloop.diycode_sdk.api.sites.bean.Sites;
import com.gcssloop.diycode_sdk.api.topic.bean.TopicContent;
import com.gcssloop.diycode_sdk.api.topic.bean.TopicReply;
import com.gcssloop.diycode_sdk.api.user.bean.UserDetail;
import com.gcssloop.diycode_sdk.utils.ACache;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据缓存工具
 */
public class DataCache {
    private static int M = 1024 * 1024;
    ACache mDiskCache;
    LruCache<String, Object> mLruCache;

    public DataCache(Context context) {
        mDiskCache = ACache.get(new File(FileUtil.getExternalCacheDir(context.getApplicationContext(), "diy-data")));
        mLruCache = new LruCache<>(5 * M);
    }

    public <T extends Serializable> void saveListData(String key, List<T> data) {
        ArrayList<T> datas = (ArrayList<T>) data;
        mLruCache.put(key, datas);
        mDiskCache.put(key, datas, ACache.TIME_WEEK);     // 数据缓存时间为 1 周
    }

    public <T extends Serializable> void saveData(@NonNull String key, @NonNull T data) {
        mLruCache.put(key, data);
        mDiskCache.put(key, data, ACache.TIME_WEEK);     // 数据缓存时间为 1 周
    }

    public <T extends Serializable> T getData(@NonNull String key) {
        T result = (T) mLruCache.get(key);
        if (result == null) {
            result = (T) mDiskCache.getAsObject(key);
            if (result != null) {
                mLruCache.put(key, result);
            }
        }
        return result;
    }

    public void removeDate(String key) {
        mDiskCache.remove(key);
    }

    public void saveTopicContent(TopicContent content) {
        saveData("topic_content_" + content.getId(), content);
        String preview = HtmlUtil.Html2Text(content.getBody_html());
        if (preview.length() > 100) {
            preview = preview.substring(0, 100);
        }
        saveData("topic_content_preview" + content.getId(), preview);
    }

    public TopicContent getTopicContent(int id) {
        return getData("topic_content_" + id);
    }

    public String getTopicPreview(int id) {
        String key = "topic_content_preview" + id;
        return getData(key);
    }

    public void saveTopicRepliesList(int topic_id, List<TopicReply> replyList) {
        ArrayList<TopicReply> replies = new ArrayList<>(replyList);
        saveData("topic_reply_" + topic_id, replies);
    }

    public List<TopicReply> getTopicRepliesList(int topic_id) {
        return getData("topic_reply_" + topic_id);
    }

    public void saveTopicsListObj(List<Object> topicList) {
        ArrayList<Object> topics = new ArrayList<>(topicList);
        saveData("topic_list_obj_", topics);
    }

    public List<Object> getTopicsListObj() {
        return getData("topic_list_obj_");
    }

    public void saveNewsList(List<New> newList) {
        ArrayList<New> news = new ArrayList<>(newList);
        saveData("news_list_", news);
    }

    public List<New> getNewsList() {
        return getData("news_list_");
    }

    public void saveNewsListObj(List<Object> newList) {
        ArrayList<Object> news = new ArrayList<>(newList);
        saveData("news_list_obj_", news);
    }

    public List<Object> getNewsListObj() {
        return getData("news_list_obj_");
    }

    public void saveMe(UserDetail user) {
        saveData("Gcs_Me_", user);
    }

    public UserDetail getMe() {
        return getData("Gcs_Me_");
    }

    public void removeMe() {
        removeDate("Gcs_Me_");
    }

    public void saveSites(List<Sites> sitesList) {
        saveListData("sites_", sitesList);
    }

    public List<Sites> getSites() {
        return getData("sites_");
    }

    public <T extends Serializable> void saveSitesItems(List<T> sitesList) {
        saveListData("sites_item_", sitesList);
    }

    public <T extends Serializable> ArrayList<T> getSitesItems() {
        return getData("sites_item_");
    }
}
