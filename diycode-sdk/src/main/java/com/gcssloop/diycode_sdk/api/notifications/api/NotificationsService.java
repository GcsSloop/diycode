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

package com.gcssloop.diycode_sdk.api.notifications.api;

import com.gcssloop.diycode_sdk.api.base.bean.State;
import com.gcssloop.diycode_sdk.api.notifications.bean.Count;
import com.gcssloop.diycode_sdk.api.notifications.bean.Notification;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface NotificationsService {

    /**
     * 获取通知列表
     *
     * @param offset 偏移数值，默认值 0
     * @param limit  数量极限，默认值 20，值范围 1..150
     * @return 通知列表
     */
    @GET("notifications.json")
    Call<List<Notification>> getNotificationsList(@Query("offset") Integer offset,
                                                  @Query("limit") Integer limit);

    /**
     * 获得未读通知数量
     *
     * @return 未读通知数量
     */
    @GET("notifications/unread_count.json")
    Call<Count> getNotificationUnReadCount();

    /**
     * 将当前用户的一些通知设成已读状态
     *
     * @param ids id 集合
     * @return 状态
     */
    @Deprecated
    @POST("notifications/read.json")
    Call<State> markNotificationAsRead(@Field("ids") int[] ids);


    /**
     * 删除当前用户的某个通知
     *
     * @param id 通知 id
     * @return 状态
     */
    @DELETE("notifications/{id}.json")
    Call<State> deleteNotification(@Path("id") int id);

    /**
     * 删除当前用户的所有通知
     *
     * @return 状态
     */
    @DELETE("notifications/all.json")
    Call<State> deleteAllNotification();
}
