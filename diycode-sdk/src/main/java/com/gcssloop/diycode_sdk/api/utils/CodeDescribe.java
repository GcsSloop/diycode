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
 * Last modified 2017-02-12 17:49:02
 *
 */

package com.gcssloop.diycode_sdk.api.utils;

/**
 * 获取返回码的描述信息
 * HTTP Status
 * -1 - 可能是网络未连接
 * 200, 201 - 请求成功，或执行成功
 * 400 - 参数不符合 API 的要求、或者数据格式验证没有通过，请配合 Response Body 里面的 error 信息确定问题。
 * 401 - 用户认证失败，或缺少认证信息，比如 access_token 过期，或没传，可以尝试用 refresh_token 方式获得新的 access_token。
 * 403 - 当前用户对资源没有操作权限
 * 404 - 资源不存在。
 * 500 - 服务器异常
 */
public class CodeDescribe {
    /**
     * 获取返回码详情
     *
     * @param code 返回码
     * @return 描述信息
     */
    public static String getDescribe(Integer code) {
        switch (code) {
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
                return "未知异常(" + code + ")";
        }
    }
}
