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

package com.gcssloop.diycode_sdk.api.base.callback;

import android.support.annotation.NonNull;

import com.gcssloop.diycode_sdk.api.base.event.BaseEvent;
import com.gcssloop.diycode_sdk.api.login.bean.Token;
import com.gcssloop.diycode_sdk.utils.CacheUtil;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 为 token 设置的 callback，相比于 BaseCallback，多了一个缓存。
 */
public class TokenCallback extends BaseCallback<Token> {
    private CacheUtil cacheUtil;

    public <Event extends BaseEvent<Token>> TokenCallback(@NonNull CacheUtil cacheUtil, @NonNull Event event) {
        super(event);
        this.cacheUtil = cacheUtil;
    }

    @Override
    public void onResponse(Call<Token> call, Response<Token> response) {
        if (response.isSuccessful()) {
            Token token = response.body();
            cacheUtil.saveToken(token);     // 请求成功后token缓存起来
            EventBus.getDefault().post(event.setEvent(response.code(), token));
        } else {
            EventBus.getDefault().post(event.setEvent(response.code(), null));
        }
    }
}
