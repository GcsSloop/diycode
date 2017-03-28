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
import android.util.LruCache;

import com.gcssloop.diycode_sdk.utils.ACache;

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

    private static String Key_Browser = "isUseInsideBrowser_";

    public void setUesInsideBrowser(@NonNull Boolean bool) {
        mLruCache.put(Key_Browser, bool);
        mDiskCache.put(Key_Browser, bool);
    }

    public Boolean isUseInsideBrowser() {
        Boolean bool = (Boolean) mLruCache.get(Key_Browser);
        if (bool == null)
            bool = (Boolean) mDiskCache.getAsObject(Key_Browser);
        if (bool == null)
            bool = true;
        setUesInsideBrowser(bool);
        return bool;
    }
}
