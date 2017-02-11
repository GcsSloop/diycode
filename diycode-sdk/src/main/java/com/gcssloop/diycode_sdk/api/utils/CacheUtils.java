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
 * Last modified 2017-02-10 15:31:04
 *
 */

package com.gcssloop.diycode_sdk.api.utils;

import android.content.Context;

import com.gcssloop.diycode_sdk.api.bean.Token;

/**
 * 缓存工具类，用于缓存各类数据
 */
public class CacheUtils {

    ACache cache;

    public CacheUtils(Context context) {
        cache = ACache.get(context);
    }


    //--- 用户相关 -------------------------------------------------------------------------------

    public void saveLoginInfo(String user_name, String password){
        cache.put("username", user_name);
        cache.put("password", password);
    }

    public String getUserName(){
        return cache.getAsString("username");
    }

    public String getPassword(){
        return cache.getAsString("password");
    }

    public void clearLoginInfo(){
        cache.remove("username");
        cache.remove("password");
    };


    //--- token ------------------------------------------------------------------------------------

    public void saveToken(Token token){
        cache.put("token", token);
    }

    public Token getToke(){
        return (Token) cache.getAsObject("token");
    }

    public void clearToken(){
        cache.remove("token");
    }


}
