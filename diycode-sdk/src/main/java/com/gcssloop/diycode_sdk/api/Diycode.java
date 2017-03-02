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
 * Last modified 2017-02-27 04:43:54
 *
 */

package com.gcssloop.diycode_sdk.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gcssloop.diycode_sdk.api.base.bean.State;
import com.gcssloop.diycode_sdk.api.base.callback.BaseCallback;
import com.gcssloop.diycode_sdk.api.base.callback.TokenCallback;
import com.gcssloop.diycode_sdk.api.diycode.api.DiycodeAPI;
import com.gcssloop.diycode_sdk.api.diycode.api.DiycodeService;
import com.gcssloop.diycode_sdk.api.diycode.bean.Hello;
import com.gcssloop.diycode_sdk.api.diycode.bean.Token;
import com.gcssloop.diycode_sdk.api.diycode.event.HelloEvent;
import com.gcssloop.diycode_sdk.api.diycode.event.LoginEvent;
import com.gcssloop.diycode_sdk.api.diycode.event.RefreshTokenEvent;
import com.gcssloop.diycode_sdk.api.likes.api.LikesAPI;
import com.gcssloop.diycode_sdk.api.news.api.NewsAPI;
import com.gcssloop.diycode_sdk.api.notifications.api.NotificationsAPI;
import com.gcssloop.diycode_sdk.api.sites.api.SitesAPI;
import com.gcssloop.diycode_sdk.api.topic.api.TopicAPI;
import com.gcssloop.diycode_sdk.api.topic.api.TopicService;
import com.gcssloop.diycode_sdk.api.topic.bean.Topic;
import com.gcssloop.diycode_sdk.api.topic.event.BanTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.CollectionTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.CreateTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.CreateTopicReplyEvent;
import com.gcssloop.diycode_sdk.api.topic.event.DeleteTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.DeleteTopicReplyEvent;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicRepliesEvent;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicReplyEvent;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicsEvent;
import com.gcssloop.diycode_sdk.api.topic.event.UnCollectionTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.UnWatchTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.UpdateTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.UpdateTopicReplyEvent;
import com.gcssloop.diycode_sdk.api.topic.event.WatchTopicEvent;
import com.gcssloop.diycode_sdk.api.user.api.UserAPI;
import com.gcssloop.diycode_sdk.utils.CacheUtil;
import com.gcssloop.diycode_sdk.utils.Constant;
import com.gcssloop.diycode_sdk.utils.DebugUtil;
import com.gcssloop.diycode_sdk.utils.UUIDGenerator;
import com.gcssloop.gcs_log.Config;
import com.gcssloop.gcs_log.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
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
public class Diycode implements DiycodeAPI, LikesAPI, NewsAPI, TopicAPI, UserAPI, SitesAPI, NotificationsAPI {

    //--- 初始化和生命周期 -------------------------------------------------------------------------

    private volatile static Diycode mDiycode;

    private Diycode() {
    }

    public static Diycode getInstance() {
        if (null == mDiycode) {
            synchronized (Diycode.class) {
                if (null == mDiycode) {
                    mDiycode = new Diycode();
                }
            }
        }
        return mDiycode;
    }

    private static DiycodeService mDiycodeService;
    private static TopicService mTopicService;


    private static Retrofit mRetrofit;
    private static CacheUtil mCacheUtil; // 缓存工具

    public static Diycode init(@NonNull Context context, @NonNull final String client_id, @NonNull final String client_secret) {
        initLogger(context);
        Logger.i("初始化 diycode");

        CLIENT_ID = client_id;
        CLIENT_SECRET = client_secret;

        initCache(context);     // 初始化缓存
        initRetrofit();         // 初始化 retrofit
        initService();          // 初始化 service

        return getInstance();
    }


    private static void initService() {
        // 使用 Retrofit2 将定义的网络接口转化为实际的请求方式
        mDiycodeService = mRetrofit.create(DiycodeService.class);
        mTopicService = mRetrofit.create(TopicService.class);
    }

    private static void initCache(@NonNull Context context) {
        // 缓存工具
        mCacheUtil = new CacheUtil(context.getApplicationContext());
    }

    private static void initRetrofit() {
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
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)                         // 设置 base url
                .client(client)                                     // 设置 client
                .addConverterFactory(GsonConverterFactory.create()) // 设置 Json 转换工具
                .build();
    }

    /**
     * 判断请求中是否已经有 token 了
     *
     * @param originalRequest request
     * @return 是否有token
     */
    private static boolean alreadyHasAuthorizationHeader(Request originalRequest) {
        String token = originalRequest.header(Constant.KEY_TOKEN);
        // 如果本身是请求 token 的 URL，直接返回 true
        // 如果不是，则判断 header 中是否已经添加过 Authorization 这个字段，以及是否为空
        return !(null == token || token.isEmpty() || originalRequest.url().toString().contains(Constant.OAUTH_URL));
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


    //--- OAuth 认证相关 -------------------------------------------------------------------------

    private static String CLIENT_ID = "";                       // 应用 ID
    private static String CLIENT_SECRET = "";                   // 私钥

    /**
     * 刷新 token
     */
    public String refreshToken() {
        final String uuid = UUIDGenerator.getUUID();
        // 如果本地没有缓存的 token，则直接返回一个 401 异常
        if (null == getToken()) {
            EventBus.getDefault().post(new RefreshTokenEvent(uuid, 401, null));
            return null;
        }

        // 如果本地有缓存的 token，尝试刷新 token 信息，并缓存新的 Token
        String GRANT_TYPE_REFRESH = "refresh_token";
        Call<Token> call = mDiycodeService.refreshToken(CLIENT_ID, CLIENT_SECRET,
                GRANT_TYPE_REFRESH, mCacheUtil.getToken().getRefresh_token());
        call.enqueue(new TokenCallback(mCacheUtil, new RefreshTokenEvent(uuid)));
        return uuid;
    }

    /**
     * 获取当前缓存的 token
     * 在用户登录后，token 会缓存下来，用于请求数据。
     * 在用户登出后，token 会被删除。
     * (在正常使用情况下，该接口不应该被上层应用调用，可用于调试)
     *
     * @return 当前的 token
     */
    public static Token getToken() {
        return mCacheUtil.getToken();
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
        String GRANT_TYPE_GET = "password";
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
        call.enqueue(new BaseCallback<>(new HelloEvent(uuid)));
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


    //--- topic ------------------------------------------------------------------------------------

    /**
     * 获取 topic 列表
     *
     * @param type    类型，默认 last_actived，可选["last_actived", "recent", "no_reply", "popular", "excellent"]
     * @param node_id 如果你需要只看某个节点的，请传此参数, 如果不传 则返回全部
     * @param offset  偏移数值，默认值 0
     * @param limit   数量极限，默认值 20，值范围 1..150
     * @see GetTopicsEvent
     */
    @Override
    public String getTopics(@Nullable String type, @Nullable Integer node_id, @Nullable Integer offset, @Nullable Integer limit) {
        Call<List<Topic>> call = mTopicService.getTopics(type, node_id, offset, limit);
        return null;
    }

    /**
     * 创建一个新的 topic
     *
     * @param title   话题标题
     * @param body    话题内容, Markdown 格式
     * @param node_id 节点编号
     * @see CreateTopicEvent
     */
    @Override
    public String createTopic(@NonNull String title, @NonNull String body, @NonNull Integer node_id) {
        return null;
    }

    /**
     * 获取 topic 内容
     *
     * @param id topic 的 id
     * @see GetTopicEvent
     */
    @Override
    public String getTopic(@NonNull int id) {
        return null;
    }

    /**
     * 更新(修改) topic
     *
     * @param id      要修改的话题 id
     * @param title   话题标题
     * @param body    话题内容, Markdown 格式
     * @param node_id 节点编号
     * @see UpdateTopicEvent
     */
    @Override
    public String updateTopic(@NonNull int id, @NonNull String title, @NonNull String body, @NonNull Integer node_id) {
        return null;
    }

    /**
     * 删除一个话题
     *
     * @param id 要删除的话题 id
     * @see DeleteTopicEvent
     */
    @Override
    public String deleteTopic(@NonNull int id) {
        return null;
    }

    //--- topic collection -------------------------------------------------------------------------

    /**
     * 收藏话题
     *
     * @param id 被收藏的话题 id
     * @see CollectionTopicEvent
     */
    @Override
    public String collectionTopic(@NonNull int id) {
        return null;
    }

    /**
     * 取消收藏话题
     *
     * @param id 被收藏的话题 id
     * @see UnCollectionTopicEvent
     */
    @Override
    public String unCollectionTopic(@NonNull int id) {
        return null;
    }

    //--- topic watch ------------------------------------------------------------------------------

    /**
     * 关注话题
     *
     * @param id 话题 id
     * @see WatchTopicEvent
     */
    @Override
    public String watchTopic(@NonNull int id) {
        return null;
    }

    /**
     * 取消关注话题
     *
     * @param id 话题 id
     * @see UnWatchTopicEvent
     */
    @Override
    public String unWatchTopic(@NonNull int id) {
        return null;
    }

    //--- topic reply ------------------------------------------------------------------------------

    /**
     * 获取 topic 回复列表
     *
     * @param id     topic 的 id
     * @param offset 偏移数值 默认 0
     * @param limit  数量极限，默认值 20，值范围 1...150
     * @see GetTopicRepliesEvent
     */
    @Override
    public String getTopicReplies(@NonNull int id, @Nullable Integer offset, @Nullable Integer limit) {
        return null;
    }

    /**
     * 创建 topic 回帖(回复，评论)
     *
     * @param id   话题列表
     * @param body 回帖内容, Markdown 格式
     * @see CreateTopicReplyEvent
     */
    @Override
    public String createTopicReply(@NonNull int id, @NonNull String body) {
        return null;
    }

    /**
     * 获取回帖的详细内容（一般用于编辑回帖的时候）
     *
     * @param id id
     * @see GetTopicReplyEvent
     */
    @Override
    public String getTopicReply(@NonNull int id) {
        return null;
    }

    /**
     * 更新回帖
     *
     * @param id   id
     * @param body 回帖详情
     * @see UpdateTopicReplyEvent
     */
    @Override
    public String updateTopicReply(@NonNull int id, @NonNull String body) {
        return null;
    }

    /**
     * 删除回帖
     *
     * @param id id
     * @see DeleteTopicReplyEvent
     */
    @Override
    public String deleteTopicReply(@NonNull int id) {
        return null;
    }

    //--- topic ban --------------------------------------------------------------------------------

    /**
     * 屏蔽话题，移到 NoPoint 节点 (管理员限定)
     *
     * @param id 要屏蔽的话题 id
     * @see BanTopicEvent
     */
    @Override
    public String banTopic(@NonNull int id) {
        return null;
    }


    //--- news -------------------------------------------------------------------------------------

    /**
     * 获取 news 列表
     *
     * @param node_id 如果你需要只看某个节点的，请传此参数, 如果不传 则返回全部
     * @param offset  偏移数值，默认值 0
     * @param limit   数量极限，默认值 20，值范围 1..150
     */
    @Override
    public String getNews(@Nullable Integer node_id, @Nullable Integer offset, @Nullable Integer limit) {
        return null;
    }

    /**
     * 创建一个 new (分享)
     *
     * @param title   标题
     * @param address 地址(网址链接)
     * @param node_id 节点 id
     */
    @Override
    public String createNews(@NonNull Integer title, @NonNull Integer address, @NonNull Integer node_id) {
        return null;
    }

    /**
     * 获取 news 回帖列表
     *
     * @param id     id
     * @param offset 偏移数值 默认 0
     * @param limit  数量极限，默认值 20，值范围 1...150
     */
    @Override
    public String getNewsReplies(@NonNull int id, @Nullable Integer offset, @Nullable Integer limit) {
        return null;
    }

    /**
     * 创建 news 回帖 (暂不可用)
     *
     * @param id   id
     * @param body 回帖内容， markdown格式
     */
    @Override
    public String createNewsReply(@NonNull int id, @NonNull Integer body) {
        return null;
    }

    /**
     * 获取 news 回帖详情
     *
     * @param id id
     */
    @Override
    public String getNewsReply(@NonNull int id) {
        return null;
    }

    /**
     * 更新 news 回帖
     *
     * @param id   id
     * @param body 回帖内容
     */
    @Override
    public String updateNewsReply(@NonNull int id, @NonNull String body) {
        return null;
    }

    /**
     * 删除 news 回帖
     *
     * @param id id
     */
    @Override
    public String deleteNewsReply(@NonNull int id) {
        return null;
    }

    /**
     * 获取 news 分类列表
     */
    @Override
    public String getNewsNodes() {
        return null;
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
     * 获得未读通知的数量
     */
    @Override
    public String getNotificationUnReadCount() {
        return null;
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
    public String markNotificationAsRead(int[] ids) {
        return null;
    }


    //--- user info --------------------------------------------------------------------------------

    /**
     * 获取用户列表
     *
     * @param limit 数量极限，默认值 20，值范围 1..150
     * @return 用户列表
     */
    @Override
    public String getUsersList(Integer limit) {
        return null;
    }

    /**
     * 获取用户详细资料
     *
     * @param login_name 登录用户名(非昵称)
     * @return 用户详情
     */
    @Override
    public String getUser(String login_name) {
        return null;
    }

    /**
     * 获取当前登录者的详细资料
     *
     * @return 用户详情
     */
    @Override
    public String getMe() {
        return null;
    }

    //--- user block -------------------------------------------------------------------------------

    /**
     * 屏蔽用户
     *
     * @param login_name 登录用户名(非昵称)
     * @return 状态
     */
    @Override
    public Call<State> blockUser(String login_name) {
        return null;
    }

    /**
     * 取消屏蔽用户
     *
     * @param login_name 登录用户名(非昵称)
     * @return 状态
     */
    @Override
    public String unBlockUser(String login_name) {
        return null;
    }

    /**
     * 获取用户屏蔽的用户列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @return 被屏蔽的用户列表
     */
    @Override
    public String getUserBlockedList(String login_name, Integer offset, Integer limit) {
        return null;
    }

    //--- user follow ------------------------------------------------------------------------------

    /**
     * 关注用户
     *
     * @param login_name 登录用户名(非昵称)
     * @return 状态
     */
    @Override
    public String followUser(String login_name) {
        return null;
    }

    /**
     * 取消关注用户
     *
     * @param login_name 登录用户名(非昵称)
     * @return 状态
     */
    @Override
    public String unFollowUser(String login_name) {
        return null;
    }

    /**
     * 用户正在关注的人列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @return 用户关注的人列表
     */
    @Override
    public String getUserFollowingList(String login_name, Integer offset, Integer limit) {
        return null;
    }

    /**
     * 关注该用户的人列白哦
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @return 关注该用户的人列表
     */
    @Override
    public String getUserFollowerList(String login_name, Integer offset, Integer limit) {
        return null;
    }

    //--- user list --------------------------------------------------------------------------------

    /**
     * 用户收藏的话题列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @return 话题列表
     */
    @Override
    public Call<List<Topic>> getUserCollectionTopicList(String login_name, Integer offset, Integer limit) {
        return null;
    }

    /**
     * 获取用户创建的话题列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param order      类型 默认 recent，可选["recent", "likes", "replies"]
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @return 话题列表
     */
    @Override
    public String getUserCreateTopicList(String login_name, String order, Integer offset, Integer limit) {
        return null;
    }

    /**
     * 用户回复过的话题列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param order      类型 默认 recent，可选["recent"]
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @return 话题列表
     */
    @Override
    public String getUserReplyTopicList(String login_name, String order, Integer offset, Integer limit) {
        return null;
    }

}
