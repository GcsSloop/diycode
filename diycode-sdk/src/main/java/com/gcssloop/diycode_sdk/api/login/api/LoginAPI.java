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

package com.gcssloop.diycode_sdk.api.login.api;

import android.support.annotation.NonNull;

import com.gcssloop.diycode_sdk.api.login.bean.Token;
import com.gcssloop.diycode_sdk.api.login.event.LoginEvent;
import com.gcssloop.diycode_sdk.api.login.event.RefreshTokenEvent;

public interface LoginAPI {

    //--- login ------------------------------------------------------------------------------------

    /**
     * 登录时调用
     * 返回一个 token，用于获取各类私有信息使用，该 token 用 LoginEvent 接收。
     *
     * @param user_name 用户名
     * @param password  密码
     * @see LoginEvent
     */
    String login(@NonNull String user_name, @NonNull String password);

    /**
     * 用户登出
     */
    void logout();

    /**
     * 是否登录
     * @return 是否登录
     */
    boolean isLogin();

    //--- token ------------------------------------------------------------------------------------

    /**
     * 刷新 token
     *
     * @see RefreshTokenEvent
     */
    String refreshToken();

    /**
     * 获取当前缓存的 token
     *
     * @return 当前缓存的 token
     */
    Token getCacheToken();


    //--- devices ----------------------------------------------------------------------------------

    /**
     * 更新设备信息
     * 记录用户 Device 信息，用于 Push 通知。
     * 请在每次用户打开 App 的时候调用此 API 以便更新 Token 的 last_actived_at 让服务端知道这个设备还活着。
     * Push 将会忽略那些超过两周的未更新的设备。
     */
    String updateDevices();

    /**
     * 删除 Device 信息，请注意在用户登出或删除应用的时候调用，以便能确保清理掉。
     */
    String deleteDevices();


}
