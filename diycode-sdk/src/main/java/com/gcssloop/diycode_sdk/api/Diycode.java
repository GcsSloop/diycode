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
 * Last modified 2017-02-09 23:14:48
 *
 */

package com.gcssloop.diycode_sdk.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gcssloop.diycode_sdk.api.base.BaseCallback;
import com.gcssloop.diycode_sdk.api.bean.Hello;
import com.gcssloop.diycode_sdk.api.bean.Token;
import com.gcssloop.diycode_sdk.api.bean.Topic;
import com.gcssloop.diycode_sdk.api.bean.TopicContent;
import com.gcssloop.diycode_sdk.api.bean.TopicReply;
import com.gcssloop.diycode_sdk.api.callback.TokenCallback;
import com.gcssloop.diycode_sdk.api.event.GetTopicContentEvent;
import com.gcssloop.diycode_sdk.api.event.GetTopicRepliesEvent;
import com.gcssloop.diycode_sdk.api.event.GetTopicsEvent;
import com.gcssloop.diycode_sdk.api.event.HelloEvent;
import com.gcssloop.diycode_sdk.api.event.LoginEvent;
import com.gcssloop.diycode_sdk.api.event.NewTopicEvent;
import com.gcssloop.diycode_sdk.api.event.RefreshTokenEvent;
import com.gcssloop.diycode_sdk.api.utils.CacheUtil;
import com.gcssloop.diycode_sdk.api.utils.Constant;
import com.gcssloop.diycode_sdk.api.utils.DebugUtil;
import com.gcssloop.diycode_sdk.api.utils.UUIDGenerator;
import com.gcssloop.gcs_log.Config;
import com.gcssloop.gcs_log.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * diycode 实现类，没有回调接口，使用 EventBus 来接收数据
 */
public class Diycode implements DiycodeAPI {
    public static final String TAG = "GcsDiy";

    //--- 初始化和生命周期 -------------------------------------------------------------------------

    private static final Diycode mDiycode = new Diycode();
    private static DiycodeService mDiycodeService;
    private static CacheUtil mCacheUtil;

    private static String client_id;       // 应用 id
    private static String client_secret;   // 应用秘钥

    private Diycode() {
    }

    public static Diycode getInstance() {
        return mDiycode;
    }

    public Diycode init(@NonNull Context context, @NonNull final String client_id, @NonNull final String client_secret) {
        // 初始化 log 工具
        initLogger(context);
        Logger.i("初始化 diycode");

        this.client_id = client_id;
        this.client_secret = client_secret;

        // 设置 Log 拦截器，可以用于以后处理一些异常情况
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 为所有请求自动添加 token
        Interceptor mTokenInterceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                // 如果当前没有缓存 token 或者请求已经附带 token 了，就不再添加
                if (null == getToken() || alreadyHasAuthorizationHeader(originalRequest)) {
                    return chain.proceed(originalRequest);
                }
                String token = Constant.VALUE_TOKEN_PREFIX + getToken().getAccess_token();
                Logger.i("AutoToken:" + token); // 查看 token 是否异常
                // 为请求附加 token
                Request authorised = originalRequest.newBuilder()
                        .header(Constant.KEY_TOKEN, token)
                        .build();
                return chain.proceed(authorised);
            }
        };

        // 配置 client
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)                // 设置拦截器
                .retryOnConnectionFailure(true)             // 是否重试
                .connectTimeout(5, TimeUnit.SECONDS)        // 连接超时事件
                .addNetworkInterceptor(mTokenInterceptor)   // 自动附加 token (附加和不附加请求结果可能会有差别)
                .build();

        // 配置 Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)                         // 设置 base url
                .client(client)                                     // 设置 client
                .addConverterFactory(GsonConverterFactory.create()) // 设置 Json 转换工具
                .build();

        // 使用 Retrofit2 将定义的网络接口转化为实际的请求方式
        mDiycodeService = retrofit.create(DiycodeService.class);

        // 缓存工具
        mCacheUtil = new CacheUtil(context);

        // 存储
        CLIENT_ID = client_id;
        CLIENT_SECRET = client_secret;

        return this;
    }

    /**
     * 初始化 log 工具，在 debug 模式输出日志， release 模式自动移除
     *
     * @param context 上下文
     */
    private static void initLogger(@NonNull Context context) {
        if (DebugUtil.isInDebug(context)) {
            Logger.init("Diycode").setLevel(Config.LEVEL_FULL);
        } else {
            Logger.init("Diycode").setLevel(Config.LEVEL_NONE);
        }
    }

    /**
     * 判断请求中是否已经有 token 了
     *
     * @param originalRequest request
     * @return 是否有token
     */
    private boolean alreadyHasAuthorizationHeader(Request originalRequest) {
        String token = originalRequest.header(Constant.KEY_TOKEN);
        // 如果本身是请求 token 的 URL，直接返回 true
        // 如果不是，则判断 header 中是否已经添加过 Authorization 这个字段，以及是否为空
        return !(null == token || token.isEmpty() || originalRequest.url().toString().contains(Constant.OAUTH_URL));
    }

    //--- OAuth 认证相关 -------------------------------------------------------------------------

    private static String CLIENT_ID = "";                       // 应用 ID
    private static String CLIENT_SECRET = "";                   // 私钥
    private static String GRANT_TYPE_GET = "password";          // 认证类型(密码)
    private static String GRANT_TYPE_REFRESH = "refresh_token"; // 认证类型(Token)

    /**
     * 获取当前缓存的 token
     * 在用户登录后，token 会缓存下来，用于请求数据。
     * 在用户登出后，token 会被删除。
     * (在正常使用情况下，该接口不应该被上层应用调用，可用于调试)
     *
     * @return 当前的 token
     */
    public Token getToken() {
        return mCacheUtil.getToke();
    }

    /**
     * 刷新 token
     */
    public String refreshToken() {
        final String uuid = UUIDGenerator.getUUID();
        // 如果本地没有缓存的 token，则直接返回一个 401 异常
        if (null == mCacheUtil.getToke()) {
            EventBus.getDefault().post(new RefreshTokenEvent(uuid, 401, null));
            return null;
        }

        // 如果本地有缓存的 token，尝试刷新 token 信息，并缓存新的 Token
        Call<Token> call = mDiycodeService.refreshToken(client_id, client_secret,
                GRANT_TYPE_REFRESH, mCacheUtil.getToke().getRefresh_token());
        call.enqueue(new TokenCallback(mCacheUtil, new RefreshTokenEvent(uuid)));
        return uuid;
    }


    //--- 登录相关内容 ----------------------------------------------------------------------------

    /**
     * 登录时调用
     * 返回一个 token，用于获取各类私有信息使用
     *
     * @param user_name 用户名
     * @param password  密码
     * @see LoginEvent
     */
    @Override
    public String login(@NonNull final String user_name, @NonNull final String password) {
        final String uuid = UUIDGenerator.getUUID();
        Call<Token> call = mDiycodeService.getToken(CLIENT_ID, CLIENT_SECRET, GRANT_TYPE_GET,
                user_name, password);
        call.enqueue(new TokenCallback(mCacheUtil, new LoginEvent(uuid)));
        return uuid;
    }

    /**
     * 用户登出
     */
    @Override
    public void logout() {
        // 清除token
        mCacheUtil.clearToken();
    }

    /**
     * 更新设备信息
     * 记录用户 Device 信息，用于 Push 通知。
     * 请在每次用户打开 App 的时候调用此 API 以便更新 Token 的 last_actived_at 让服务端知道这个设备还活着。
     * Push 将会忽略那些超过两周的未更新的设备。
     */
    @Override
    public String updateDevices() {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 删除 Device 信息，请注意在用户登出或删除应用的时候调用，以便能确保清理掉。
     */
    @Override
    public String deleteDevices() {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }


    //--- 测试接口 -------------------------------------------------------------------------------

    /**
     * 简单的 API 测试接口，需要登录验证，便于快速测试 OAuth 以及其他 API 的基本格式是否正确。
     * 使用 HelloEvent 接收结果。
     *
     * @param limit 数量极限，值范围[0..100]
     * @see HelloEvent
     */
    @Override
    public String hello(@Nullable Integer limit) {
        final String uuid = UUIDGenerator.getUUID();
        Call<Hello> call = mDiycodeService.hello(limit);
        call.enqueue(new BaseCallback<Hello>(new HelloEvent(uuid)));
        return uuid;
    }


    //--- topic ------------------------------------------------------------------------------------

    /**
     * 获取 Topics 列表
     *
     * @param type    类型，默认 last_actived，可选["last_actived", "recent", "no_reply", "popular", "excellent"]
     * @param node_id 如果你需要只看某个节点的，请传此参数, 如果不传 则返回全部
     * @param offset  偏移数值，默认值 0
     * @param limit   数量极限，默认值 20，值范围 1..150
     * @see GetTopicsEvent
     */
    @Override
    public String getTopics(@Nullable String type, @Nullable Integer node_id, @Nullable Integer offset, @Nullable Integer limit) {
        final String uuid = UUIDGenerator.getUUID();
        Call<List<Topic>> call = mDiycodeService.getTopics(type, node_id, offset, limit);
        call.enqueue(new BaseCallback<List<Topic>>(new GetTopicsEvent(uuid)));
        return uuid;
    }

    /**
     * 获取 topic 内容
     *
     * @param id topic 的 id
     * @see GetTopicContentEvent
     */
    @Override
    public String getTopicContent(@NonNull Integer id) {
        final String uuid = UUIDGenerator.getUUID();
        Call<TopicContent> call = mDiycodeService.getTopic(id);
        call.enqueue(new BaseCallback<TopicContent>(new GetTopicContentEvent(uuid)));
        return uuid;
    }

    /**
     * 创建一个新的 Topic
     *
     * @param title   Topic 标题
     * @param body    Topic 内容
     * @param node_id Topic 节点编号
     */
    @Override
    public String newTopic(@NonNull String title, @NonNull String body, @NonNull Integer node_id) {
        final String uuid = UUIDGenerator.getUUID();
        Call<TopicContent> call = mDiycodeService.newTopic(title, body, node_id);
        call.enqueue(new BaseCallback<TopicContent>(new NewTopicEvent(uuid)));
        return uuid;
    }

    /**
     * 更新 topic
     *
     * @param title   标题
     * @param body    话题内容 Markdown 格式
     * @param node_id 节点编号
     */
    @Override
    public String updateTopic(@NonNull String title, @NonNull String body, @NonNull Integer node_id) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 删除 topic 仅支持删除自己创建的 topic
     *
     * @param id 编号
     */
    @Override
    public String deleteTopic(@NonNull Integer id) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 屏蔽话题，移到 NoPoint 节点 (Admin only)
     *
     * @param id 编号
     */
    @Override
    public String banTopic(@NonNull Integer id) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 关注话题
     *
     * @param id 编号
     */
    @Override
    public String followTopic(@NonNull Integer id) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 取消关注话题
     *
     * @param id 编号
     */
    @Override
    public String unFollowRopic(@NonNull Integer id) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 收藏一个话题
     *
     * @param id 编号
     */
    @Override
    public String collectionTopic(@NonNull Integer id) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 取消收藏一个话题
     *
     * @param id 编号
     */
    @Override
    public String unCollectionTopic(@NonNull Integer id) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 获取话题评论列表
     *
     * @param id     编号
     * @param offset 偏移数值，默认值 0
     * @param limit  数量极限，默认值 20，值范围 1..150
     */
    @Override
    public String getTopicReplies(@NonNull Integer id, @Nullable Integer offset, @Nullable Integer limit) {
        final String uuid = UUIDGenerator.getUUID();
        Call<List<TopicReply>> call = mDiycodeService.getTopicReplies(id, offset, limit);
        call.enqueue(new BaseCallback<List<TopicReply>>(new GetTopicRepliesEvent(uuid)));
        return uuid;
    }

    /**
     * 创建话题
     *
     * @param id   编号
     * @param body 内容 Markdown 格式
     */
    @Override
    public String createTopicReplies(@NonNull Integer id, @NonNull String body) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }


    //--- reply ------------------------------------------------------------------------------------

    /**
     * 获取回帖的详细内容（一般用于编辑回帖的时候）
     *
     * @param id 编号
     */
    @Override
    public String getReply(@NonNull Integer id) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 更新回帖
     *
     * @param id   编号
     * @param body 帖子详情
     */
    @Override
    public String postReply(@NonNull Integer id, @NonNull String body) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 删除回帖
     *
     * @param id 编号
     */
    @Override
    public String deleteReply(@NonNull Integer id) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }


    //--- like -------------------------------------------------------------------------------------

    /**
     * 赞
     *
     * @param obj_type 值范围["topic", "reply", "news"]
     * @param obj_id   唯一id
     */
    @Override
    public String like(@NonNull String obj_type, @NonNull Integer obj_id) {
        final String uuid = UUIDGenerator.getUUID();
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
        return uuid;
    }


    //--- news -------------------------------------------------------------------------------------

    /**
     * 获取 News 列表
     *
     * @param node_id 如果你需要只看某个节点的，请传此参数, 如果不传 则返回全部
     * @param offset  偏移数值，默认值 0
     * @param limit   数量极限，默认值 20，值范围 1..150
     */
    @Override
    public String getNews(@Nullable Integer node_id, @Nullable Integer offset, @Nullable Integer limit) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 创建 News
     *
     * @param title   News 标题
     * @param address News 链接
     * @param node_id News 节点编号
     */
    @Override
    public String createNews(@NonNull String title, @NonNull String address, @NonNull Integer node_id) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 获取 News 评论列表
     *
     * @param obj_id 编号
     * @param offset 偏移数值，默认值 0
     * @param limit  数量极限，默认值 20，值范围 1..150
     */
    @Override
    public String getNewsReply(@NonNull Integer obj_id, @Nullable Integer offset, @Nullable Integer limit) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 获取 News 评论详情
     *
     * @param obj_id 编号
     */
    @Override
    public String getNewsReplyContent(@NonNull Integer obj_id) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 更新 News 评论
     *
     * @param obj_id 编号
     * @param body   详情
     */
    @Override
    public String updateNewsReply(@NonNull Integer obj_id, @NonNull String body) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 删除 News 评论
     *
     * @param obj_id 编号
     */
    @Override
    public String deleteNewsReply(@NonNull Integer obj_id) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }


    //--- photo ------------------------------------------------------------------------------------

    /**
     * 上传图片,请使用 Multipart 的方式提交图片文件
     *
     * @param img_file 图片文件
     */
    @Override
    public String uploadPhoto(@NonNull File img_file) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }


    //--- sites ------------------------------------------------------------------------------------

    /**
     * 获取 sites 列表
     */
    @Override
    public String getSites() {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }


    //--- user -------------------------------------------------------------------------------------

    /**
     * 获得用户列表
     *
     * @param limit 数量极限，默认值 20，值范围 1..150
     */
    @Override
    public String getUsers(@NonNull Integer limit) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 获取用户信息
     *
     * @param username 用户名
     */
    @Override
    public String getUserInfo(@NonNull String username) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 屏蔽某个用户
     *
     * @param username 被屏蔽的用户名
     */
    @Override
    public String blockUser(@NonNull String username) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 取消屏蔽某个用户
     *
     * @param username 被屏蔽的用户名
     */
    @Override
    public String unBlockUser(@NonNull String username) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 获得被屏蔽的用户列表
     *
     * @param username 自己用户名
     */
    @Override
    public String getBlocked(@NonNull String username) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 关注某个用户
     *
     * @param username 关注的用户名
     */
    @Override
    public String followUser(@NonNull String username) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 取消关注某个用户
     *
     * @param username 取消关注的用户名
     */
    @Override
    public String unFollowUser(@NonNull String username) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 获取关注者列表
     *
     * @param username
     * @param offset   偏移数值，默认值 0
     * @param limit    数量极限，默认值 20，值范围 1..150
     */
    @Override
    public String getFollowers(@NonNull String username, @NonNull Integer offset, @NonNull Integer limit) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 获取正在关注的列表
     *
     * @param username
     * @param offset   偏移数值，默认值 0
     * @param limit    数量极限，默认值 20，值范围 1..150
     */
    @Override
    public String getFollowing(@NonNull String username, @NonNull Integer offset, @NonNull Integer limit) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 获取用户创建的回帖列表
     *
     * @param username 用户名
     * @param order    顺序，默认值 recent，取值范围 	["recent"]
     * @param offset   偏移数值，默认值 0
     * @param limit    数量极限，默值 20，值范围 1..150
     */
    @Override
    public String getUserReplies(@NonNull String username, @Nullable String order, @Nullable Integer offset, @Nullable String limit) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 获取用户相关的话题列表
     *
     * @param username 用户名
     * @param order    顺序，默认值 recent，取值范围 	["recent", "likes", "replies"]
     * @param offset   偏移数值，默认值 0
     * @param limit    数量极限，默认值 20，值范围 1..150
     */
    @Override
    public String getUserTopics(@NonNull String username, @Nullable String order, @Nullable Integer offset, @Nullable String limit) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 获取用户收藏的话题列表
     *
     * @param username 用户名
     * @param offset   偏移数值，默认值 0
     * @param limit    数量极限，默认值 20，值范围 1..150
     */
    @Override
    public String getCollection(@NonNull String username, @Nullable Integer offset, @Nullable String limit) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 获取当前登录者资料
     */
    @Override
    public String getMe() {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }


    //--- notification -----------------------------------------------------------------------------

    /**
     * 获取通知列表
     *
     * @param offset 偏移数值，默认值 0
     * @param limit  数量极限，默认值 20，值范围 1..150
     */
    @Override
    public String getNotifications(@NonNull Integer offset, @NonNull Integer limit) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 删除用户的某条通知
     *
     * @param id
     */
    @Override
    public String deleteNotionfition(@NonNull Integer id) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 删除当前用户的所有通知
     */
    @Override
    public String deleteAllNotification() {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 将某些通知标记为已读
     *
     * @param ids id集合
     */
    @Override
    public String markNotificationAsRead(ArrayList<Integer> ids) {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }

    /**
     * 获得未读通知的数量
     */
    @Override
    public String getUnreadNotificationCount() {
        final String uuid = UUIDGenerator.getUUID();
        return uuid;
    }


    //--- nodes ------------------------------------------------------------------------------------


    //--- project ----------------------------------------------------------------------------------
}
