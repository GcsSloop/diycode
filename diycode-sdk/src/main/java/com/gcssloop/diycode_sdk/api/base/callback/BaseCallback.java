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

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseCallback<T> implements Callback<T> {
    protected BaseEvent<T> event;                   // 事件

    public <Event extends BaseEvent<T>> BaseCallback(@NonNull Event event) {
        this.event = event;
    }

    /**
     * Invoked for a received HTTP response.
     * <p>
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     * Call {@link Response#isSuccessful()} to determine if the response indicates success.
     *
     * @param call     回调
     * @param response 请求
     */
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful())
            EventBus.getDefault().post(event.setEvent(response.code(), response.body()));
        else {
            EventBus.getDefault().post(event.setEvent(response.code(), null));
        }
    }

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     *
     * @param call 回调
     * @param t    抛出的异常
     */
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        EventBus.getDefault().post(event.setEvent(-1, null));
    }
}
