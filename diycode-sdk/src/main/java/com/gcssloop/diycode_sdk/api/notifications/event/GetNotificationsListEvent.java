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
 * Last modified 2017-03-09 02:30:16
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode_sdk.api.notifications.event;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gcssloop.diycode_sdk.api.base.event.BaseEvent;
import com.gcssloop.diycode_sdk.api.notifications.bean.Notification;

import java.util.List;

public class GetNotificationsListEvent extends BaseEvent<List<Notification>> {

    /**
     * @param uuid 唯一识别码
     */
    public GetNotificationsListEvent(@Nullable String uuid) {
        super(uuid);
    }

    /**
     * @param uuid          唯一识别码
     * @param code          网络返回码
     * @param notifications 实体数据
     */
    public GetNotificationsListEvent(@Nullable String uuid, @NonNull Integer code, @Nullable List<Notification> notifications) {
        super(uuid, code, notifications);
    }
}
