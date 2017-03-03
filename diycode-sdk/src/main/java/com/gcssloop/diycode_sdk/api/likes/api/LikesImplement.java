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
 * Last modified 2017-03-03 03:33:58
 *
 */

package com.gcssloop.diycode_sdk.api.likes.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gcssloop.diycode_sdk.api.base.bean.State;
import com.gcssloop.diycode_sdk.api.base.callback.BaseCallback;
import com.gcssloop.diycode_sdk.api.base.implement.BaseImplement;
import com.gcssloop.diycode_sdk.api.likes.event.LikeEvent;
import com.gcssloop.diycode_sdk.api.likes.event.UnLikeEvent;
import com.gcssloop.diycode_sdk.utils.UUIDGenerator;

public class LikesImplement extends BaseImplement<LikesService> implements LikesAPI {

    public LikesImplement(Context context) {
        super(context);
    }

    /**
     * 赞
     *
     * @param obj_type 值范围["topic", "reply", "news"]
     * @param obj_id   唯一id
     */
    @Override
    public String like(@NonNull String obj_type, @NonNull Integer obj_id) {
        final String uuid = UUIDGenerator.getUUID();
        mService.like(obj_type, obj_id).enqueue(new BaseCallback<>(new LikeEvent(uuid)));
        return uuid;
    }

    /**
     * 取消之前的赞
     *
     * @param obj_type 值范围["topic", "reply", "news"]
     * @param obj_id   唯一id
     */
    @Override
    public String unLike(@NonNull String obj_type, @NonNull Integer obj_id) {
        final String uuid = UUIDGenerator.getUUID();
        mService.unLike(obj_type, obj_id).enqueue(new BaseCallback<State>(new UnLikeEvent(uuid)));
        return uuid;
    }
}
