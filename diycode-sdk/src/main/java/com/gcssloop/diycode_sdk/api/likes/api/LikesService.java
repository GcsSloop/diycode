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

package com.gcssloop.diycode_sdk.api.likes.api;

import com.gcssloop.diycode_sdk.api.base.bean.State;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

interface LikesService {


    /**
     * 赞
     *
     * @param obj_type ["topic", "reply", "news"]
     * @param obj_id   id
     * @return 是否成功
     */
    @POST("likes.json")
    @FormUrlEncoded
    Call<State> like(@Field("obj_type") String obj_type, @Field("obj_id") Integer obj_id);

    /**
     * 取消赞
     *
     * @param obj_type ["topic", "reply", "news"]
     * @param obj_id   id
     * @return 是否成功
     */
    @DELETE("likes.json")
    Call<State> unLike(@Field("obj_type") String obj_type, @Field("obj_id") Integer obj_id);
}
