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
 * Last modified 2017-03-09 23:37:46
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode_sdk.api.base.bean;

public class OAuth {
    // 认证类型
    public static final String GRANT_TYPE_LOGIN = "password";             // 密码
    public static final String GRANT_TYPE_REFRESH = "refresh_token";      // 刷新令牌

    public static final String KEY_TOKEN = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    public static String client_id = "";                            // 应用ID
    public static String client_secret = "";                        // 私钥

    // debug 使用，正常情况下慎用
    private static boolean debug_remove_auto_token = false;          // 移除自定义的token

    /**
     * 为所有请求移除 token，debug 时使用
     */
    public static void removeAutoToken() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (StackTraceElement traceElement : stackTraceElements) {
            if (traceElement.toString().contains("com.gcssloop.diycode_test.test_api")) {
                debug_remove_auto_token = true;
                break;
            }
        }
    }

    public static void resetAutoToken() {
        debug_remove_auto_token = false;
    }

    public static boolean getRemoveAutoTokenState() {
        return debug_remove_auto_token;
    }
}
