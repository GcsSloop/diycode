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
 * Last modified 2017-03-28 04:48:02
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.LruCache;

import com.gcssloop.diycode_sdk.utils.ACache;

import java.io.Serializable;

/**
 * 用户设置
 */
public class Config {
    private static int M = 1024 * 1024;
    private volatile static Config mConfig;
    private static LruCache<String, Object> mLruCache = new LruCache<>(1 * M);
    private static ACache mDiskCache;

    private Config(Context context) {
        mDiskCache = ACache.get(context, "config");
    }

    public static Config init(Context context) {
        if (null == mConfig) {
            synchronized (Config.class) {
                if (null == mConfig) {
                    mConfig = new Config(context);
                }
            }
        }
        return mConfig;
    }

    public static Config getSingleInstance() {
        return mConfig;
    }

    //--- 基础 -----------------------------------------------------------------------------------

    public <T extends Serializable> void saveData(@NonNull String key, @NonNull T value) {
        mLruCache.put(key, value);
        mDiskCache.put(key, value);
    }

    public <T extends Serializable> T getData(@NonNull String key, @Nullable T defaultValue) {
        T result = (T) mLruCache.get(key);
        if (result != null) {
            return result;
        }
        result = (T) mDiskCache.getAsObject(key);
        if (result != null) {
            mLruCache.put(key, result);
            return result;
        }
        return defaultValue;
    }

    //--- 浏览器 ---------------------------------------------------------------------------------

    private static String Key_Browser = "UseInsideBrowser_";

    public void setUesInsideBrowser(@NonNull Boolean bool) {
        saveData(Key_Browser, bool);
    }

    public Boolean isUseInsideBrowser() {
        return getData(Key_Browser, Boolean.TRUE);
    }


    //--- 首页状态 -------------------------------------------------------------------------------

    private String Key_MainViewPager_Position = "Key_MainViewPager_Position";

    public void saveMainViewPagerPosition(Integer position) {
        mLruCache.put(Key_MainViewPager_Position, position);
    }

    public Integer getMainViewPagerPosition() {
        return getData(Key_MainViewPager_Position, 0);
    }

    //--- Topic状态 ------------------------------------------------------------------------------

    private String Key_TopicList_LastPosition = "Key_TopicList_LastPosition";
    private String Key_TopicList_LastOffset = "Key_TopicList_LastOffset";

    public void saveTopicListState(Integer lastPosition, Integer lastOffset) {
        saveData(Key_TopicList_LastPosition, lastPosition);
        saveData(Key_TopicList_LastOffset, lastOffset);
    }

    public Integer getTopicListLastPosition() {
        return getData(Key_TopicList_LastPosition, 0);
    }

    public Integer getTopicListLastOffset() {
        return getData(Key_TopicList_LastOffset, 0);
    }

    private String Key_TopicList_PageIndex = "Key_TopicList_PageIndex";

    public void saveTopicListPageIndex(Integer pageIndex) {
        saveData(Key_TopicList_PageIndex, pageIndex);
    }

    public Integer getTopicListPageIndex() {
        return getData(Key_TopicList_PageIndex, 0);
    }

    //--- News状态 ------------------------------------------------------------------------------

    private String Key_NewsList_LastScroll = "Key_NewsList_LastScroll";

    public void saveNewsListScroll(Integer lastScrollY) {
        saveData(Key_NewsList_LastScroll, lastScrollY);
    }

    public Integer getNewsLastScroll() {
        return getData(Key_NewsList_LastScroll, 0);
    }

    private String Key_NewsList_LastPosition = "Key_NewsList_LastPosition";

    public void saveNewsListPosition(Integer lastPosition) {
        saveData(Key_NewsList_LastPosition, lastPosition);
    }

    public Integer getNewsListLastPosition() {
        return getData(Key_NewsList_LastPosition, 0);
    }


    private String Key_NewsList_PageIndex = "Key_NewsList_PageIndex";

    public void saveNewsListPageIndex(Integer pageIndex) {
        saveData(Key_NewsList_PageIndex, pageIndex);
    }

    public Integer getNewsListPageIndex() {
        return getData(Key_NewsList_PageIndex, 0);
    }
}
