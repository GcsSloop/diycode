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

package com.gcssloop.diycode_sdk.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gcssloop.diycode_sdk.api.login.bean.Token;

/**
 * 缓存工具类，用于缓存各类数据
 */
public class CacheUtil {

    ACache cache;

    public CacheUtil(Context context) {
        cache = ACache.get(context);
    }

    //--- token ------------------------------------------------------------------------------------

    public void saveToken(@NonNull Token token){
        cache.put("token", token);
    }

    public Token getToken(){
        return (Token) cache.getAsObject("token");
    }

    public void clearToken(){
        cache.remove("token");
    }
}
