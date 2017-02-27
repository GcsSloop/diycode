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
 * Last modified 2017-02-27 03:34:01
 *
 */

package com.gcssloop.diycode_sdk.api.diycode.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gcssloop.diycode_sdk.api.diycode.event.HelloEvent;
import com.gcssloop.diycode_sdk.api.diycode.event.LoginEvent;

import java.io.File;

/**
 * API 列表，所有用户可以使用的方法接口
 */
public interface DiycodeAPI {

    //--- 登录相关内容 ----------------------------------------------------------------------------

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


    //--- 测试接口 -------------------------------------------------------------------------------

    /**
     * 简单的 API 测试接口，需要登录验证，便于快速测试 OAuth 以及其他 API 的基本格式是否正确。
     * 使用 HelloEvent 接收结果。
     *
     * @param limit 数量极限，值范围[0..100]
     * @see HelloEvent
     */
    String hello(@Nullable Integer limit);


    //--- photo ------------------------------------------------------------------------------------

    /**
     * 上传图片,请使用 Multipart 的方式提交图片文件
     *
     * @param img_file 图片文件
     */
    String uploadPhoto(@NonNull File img_file);
}
