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

package com.gcssloop.diycode_sdk.api.base.impl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gcssloop.diycode_sdk.api.base.bean.OAuth;
import com.gcssloop.diycode_sdk.api.login.bean.Token;
import com.gcssloop.diycode_sdk.log.Logger;
import com.gcssloop.diycode_sdk.utils.CacheUtil;
import com.gcssloop.diycode_sdk.utils.Constant;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 实现类，具体实现在此处
 *
 * @param <Service>
 */
public class BaseImpl<Service> {
    protected CacheUtil mCacheUtil;
    private static Retrofit mRetrofit;
    protected Service mService;

    public BaseImpl(@NonNull Context context) {
        mCacheUtil = new CacheUtil(context.getApplicationContext());
        initRetrofit();
        this.mService = mRetrofit.create(getServiceClass());
    }

    private Class<Service> getServiceClass() {
        return (Class<Service>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private void initRetrofit() {
        if (null != mRetrofit)
            return;

        // 设置 Log 拦截器，可以用于以后处理一些异常情况
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 为所有请求自动添加 token
        Interceptor mTokenInterceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                // 如果当前没有缓存 token 或者请求已经附带 token 了，就不再添加
                if (null == mCacheUtil.getToken() || alreadyHasAuthorizationHeader(originalRequest)) {
                    return chain.proceed(originalRequest);
                }
                String token = OAuth.TOKEN_PREFIX + mCacheUtil.getToken().getAccess_token();
                // 为请求附加 token
                Request authorised = originalRequest.newBuilder()
                        .header(OAuth.KEY_TOKEN, token)
                        .build();
                return chain.proceed(authorised);
            }
        };

        // 自动刷新 token
        Authenticator mAuthenticator = new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) {
                Logger.i("自动刷新 token 开始");
                TokenService tokenService = mRetrofit.create(TokenService.class);
                String accessToken = "";
                try {
                    if (null != mCacheUtil.getToken()) {
                        Call<Token> call = tokenService.refreshToken(OAuth.client_id,
                                OAuth.client_secret, OAuth.GRANT_TYPE_REFRESH,
                                mCacheUtil.getToken().getRefresh_token());
                        retrofit2.Response<Token> tokenResponse = call.execute();
                        Token token = tokenResponse.body();
                        if (null != token) {
                            mCacheUtil.saveToken(token);
                            accessToken = token.getAccess_token();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Logger.i("自动刷新 token 结束：" + accessToken);
                return response.request().newBuilder()
                        .addHeader(OAuth.KEY_TOKEN, OAuth.TOKEN_PREFIX + accessToken)
                        .build();
            }
        };

        // 配置 client
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)                // 设置拦截器
                .retryOnConnectionFailure(true)             // 是否重试
                .connectTimeout(5, TimeUnit.SECONDS)        // 连接超时事件
                .readTimeout(5, TimeUnit.SECONDS)           // 读取超时时间
                .addNetworkInterceptor(mTokenInterceptor)   // 自动附加 token
                .authenticator(mAuthenticator)              // 认证失败自动刷新token
                .build();

        // 配置 Retrofit
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)                         // 设置 base url
                .client(client)                                     // 设置 client
                .addConverterFactory(GsonConverterFactory.create()) // 设置 Json 转换工具
                .build();
    }

    private boolean alreadyHasAuthorizationHeader(Request originalRequest) {
        // 用于 debug 时临时移除 token
        if (OAuth.getRemoveAutoTokenState()) {
            return true;
        }
        String token = originalRequest.header(OAuth.KEY_TOKEN);
        // 如果本身是请求 token 的 URL，直接返回 true
        // 如果不是，则判断 header 中是否已经添加过 Authorization 这个字段，以及是否为空
        return !(null == token || token.isEmpty() || originalRequest.url().toString().contains(Constant.OAUTH_URL));
    }

    interface TokenService {
        /**
         * 刷新 token
         */
        @POST(Constant.OAUTH_URL)
        @FormUrlEncoded
        Call<Token> refreshToken(@Field("client_id") String client_id, @Field("client_secret") String client_secret,
                                 @Field("grant_type") String grant_type, @Field("refresh_token") String refresh_token);
    }
}



