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

import com.gcssloop.diycode_sdk.api.base.bean.State;
import com.gcssloop.diycode_sdk.api.diycode.bean.Hello;
import com.gcssloop.diycode_sdk.api.diycode.bean.Token;
import com.gcssloop.diycode_sdk.utils.Constant;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DiycodeService {

    //--- Token ------------------------------------------------------------------------------------

    /**
     * 获取 Token (一般在登录时调用)
     *
     * @param client_id     客户端 id
     * @param client_secret 客户端私钥
     * @param grant_type    授权方式 - 密码
     * @param username      用户名
     * @param password      密码
     * @return Token 实体类
     */
    @POST(Constant.OAUTH_URL)
    @FormUrlEncoded
    Call<Token> getToken(
            @Field("client_id") String client_id, @Field("client_secret") String client_secret,
            @Field("grant_type") String grant_type, @Field("username") String username,
            @Field("password") String password);

    /**
     * 刷新 token
     *
     * @param client_id     客户端 id
     * @param client_secret 客户端私钥
     * @param grant_type    授权方式 - Refresh Token
     * @param refresh_token token 信息
     * @return Token 实体类
     */
    @POST(Constant.OAUTH_URL)
    @FormUrlEncoded
    Call<Token> refreshToken(@Field("client_id") String client_id, @Field("client_secret") String client_secret,
                             @Field("grant_type") String grant_type, @Field("refresh_token") String refresh_token);


    //--- devices -------------------------------------------------------------------------------

    /**
     * 记录用户 Device 信息，用于 Push 通知。
     * 请在每次用户打开 App 的时候调用此 API 以便更新 Token 的 last_actived_at 让服务端知道这个设备还活着。
     * Push 将会忽略那些超过两周的未更新的设备。
     *
     * @param platform 平台 ["ios", "android"]
     * @param token    令牌 token
     * @return 是否成功
     */
    @POST("devices.json")
    @FormUrlEncoded
    Call<State> registerDevices(@Field("platform") String platform, @Field("token") String token);

    /**
     * 删除 Device 信息，请注意在用户登出或删除应用的时候调用，以便能确保清理掉
     *
     * @param platform
     * @param platform 平台 ["ios", "android"]
     * @param token    令牌 token
     * @return 是否成功
     */
    @DELETE("devices.json")
    @FormUrlEncoded
    Call<State> unRegisterDevices(@Field("platform") String platform, @Field("token") String token);


    //--- 测试接口 -------------------------------------------------------------------------------

    /**
     * 测试 token 是否正常
     *
     * @param limit 极限值
     * @return Hello 实体类
     */
    @GET("hello.json")
    Call<Hello> hello(@Query("limit") Integer limit);

}
