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
 * Last modified 2017-02-11 02:24:31
 *
 */

package com.gcssloop.diycode_sdk.api.event;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gcssloop.diycode_sdk.api.bean.Token;

/**
 * 登录
 * HTTP Status
 * -1 - 可能是网络未连接
 * 200, 201 - 请求成功，或执行成功
 * 400 - 参数不符合 API 的要求、或者数据格式验证没有通过，请配合 Response Body 里面的 error 信息确定问题。
 * 401 - 用户认证失败，或缺少认证信息，比如 access_token 过期，或没传，可以尝试用 refresh_token 方式获得新的 access_token。
 * 403 - 当前用户对资源没有操作权限
 * 404 - 资源不存在。
 * 500 - 服务器异常
 */
public class LoginEvent {
    private boolean ok = false;     // 是否登录成功
    private Token token;            // 令牌
    private Integer state = -1;     // 状态码
    private String state_msg = "";  // 状态信息

    /**
     * @param state 状态码
     */
    public LoginEvent(@NonNull Integer state) {
        this.ok = false;
        this.token = null;
        this.state = state;
    }

    /**
     * @param state 状态码
     * @param token 令牌 token
     */
    public LoginEvent(@NonNull Integer state, @Nullable Token token) {
        this.ok = true;
        this.token = token;
        this.state = state;
    }

    /**
     * 判断是否成功
     *
     * @return 是否成功
     */
    public boolean isOk() {
        return ok;
    }

    /**
     * 获取 token
     *
     * @return token
     */
    public Token getToken() {
        return token;
    }

    /**
     * 获取请求状态
     *
     * @return 请求状态
     */
    public Integer getState() {
        return state;
    }

    /**
     * 获得当前状态对应对说明
     *
     * @return 说明
     */
    public String getStateMsg() {
        switch (state) {
            case -1:
                return "可能是网络未连接";
            case 200:
            case 201:
                return "请求成功，或执行成功。";
            case 400:
                return "参数不符合 API 的要求、或者数据格式验证没有通过";
            case 401:
                return "用户认证失败，或缺少认证信息，比如 access_token 过期，或没传，可以尝试用 refresh_token 方式获得新的 access_token";
            case 403:
                return "当前用户对资源没有操作权限";
            case 404:
                return "资源不存在";
            case 500:
                return "服务器异常";
            default:
                return "未知异常(" + state + ")";
        }
    }
}
