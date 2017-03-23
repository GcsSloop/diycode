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

package com.gcssloop.diycode_sdk.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gcssloop.diycode_sdk.api.base.bean.OAuth;
import com.gcssloop.diycode_sdk.api.likes.api.LikesAPI;
import com.gcssloop.diycode_sdk.api.likes.api.LikesImpl;
import com.gcssloop.diycode_sdk.api.likes.event.LikeEvent;
import com.gcssloop.diycode_sdk.api.likes.event.UnLikeEvent;
import com.gcssloop.diycode_sdk.api.login.api.LoginAPI;
import com.gcssloop.diycode_sdk.api.login.api.LoginImpl;
import com.gcssloop.diycode_sdk.api.login.bean.Token;
import com.gcssloop.diycode_sdk.api.login.event.LoginEvent;
import com.gcssloop.diycode_sdk.api.login.event.RefreshTokenEvent;
import com.gcssloop.diycode_sdk.api.news.api.NewsAPI;
import com.gcssloop.diycode_sdk.api.news.api.NewsImpl;
import com.gcssloop.diycode_sdk.api.news.event.CreateNewsEvent;
import com.gcssloop.diycode_sdk.api.news.event.CreateNewsReplyEvent;
import com.gcssloop.diycode_sdk.api.news.event.DeleteNewsReplyEvent;
import com.gcssloop.diycode_sdk.api.news.event.GetNewsListEvent;
import com.gcssloop.diycode_sdk.api.news.event.GetNewsNodesListEvent;
import com.gcssloop.diycode_sdk.api.news.event.GetNewsRepliesListEvent;
import com.gcssloop.diycode_sdk.api.news.event.GetNewsReplyEvent;
import com.gcssloop.diycode_sdk.api.news.event.UpdateNewsReplyEvent;
import com.gcssloop.diycode_sdk.api.notifications.api.NotificationsAPI;
import com.gcssloop.diycode_sdk.api.notifications.api.NotificationsImpl;
import com.gcssloop.diycode_sdk.api.notifications.event.DeleteAllNotificationEvent;
import com.gcssloop.diycode_sdk.api.notifications.event.DeleteNotificationEvent;
import com.gcssloop.diycode_sdk.api.notifications.event.GetNotificationUnReadCountEvent;
import com.gcssloop.diycode_sdk.api.notifications.event.GetNotificationsListEvent;
import com.gcssloop.diycode_sdk.api.notifications.event.MarkNotificationAsReadEvent;
import com.gcssloop.diycode_sdk.api.photo.api.PhotoAPI;
import com.gcssloop.diycode_sdk.api.photo.api.PhotoImpl;
import com.gcssloop.diycode_sdk.api.photo.event.UploadPhotoEvent;
import com.gcssloop.diycode_sdk.api.project.api.ProjectAPI;
import com.gcssloop.diycode_sdk.api.project.api.ProjectImpl;
import com.gcssloop.diycode_sdk.api.sites.api.SitesAPI;
import com.gcssloop.diycode_sdk.api.sites.api.SitesImpl;
import com.gcssloop.diycode_sdk.api.sites.event.GetSitesEvent;
import com.gcssloop.diycode_sdk.api.test.Event.HelloEvent;
import com.gcssloop.diycode_sdk.api.test.api.TestAPI;
import com.gcssloop.diycode_sdk.api.test.api.TestImpl;
import com.gcssloop.diycode_sdk.api.topic.api.TopicAPI;
import com.gcssloop.diycode_sdk.api.topic.api.TopicImpl;
import com.gcssloop.diycode_sdk.api.topic.event.BanTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.CollectionTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.CreateTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.CreateTopicReplyEvent;
import com.gcssloop.diycode_sdk.api.topic.event.DeleteTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.DeleteTopicReplyEvent;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicRepliesListEvent;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicReplyEvent;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicsListEvent;
import com.gcssloop.diycode_sdk.api.topic.event.UnCollectionTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.UnWatchTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.UpdateTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.UpdateTopicReplyEvent;
import com.gcssloop.diycode_sdk.api.topic.event.WatchTopicEvent;
import com.gcssloop.diycode_sdk.api.user.api.UserAPI;
import com.gcssloop.diycode_sdk.api.user.api.UserImpl;
import com.gcssloop.diycode_sdk.api.user.bean.UserDetail;
import com.gcssloop.diycode_sdk.api.user.event.BlockUserEvent;
import com.gcssloop.diycode_sdk.api.user.event.FollowUserEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetMeEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserBlockedListEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserCreateTopicListEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserFollowerListEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserFollowingListEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserReplyTopicListEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUsersListEvent;
import com.gcssloop.diycode_sdk.api.user.event.UnBlockUserEvent;
import com.gcssloop.diycode_sdk.api.user.event.UnFollowUserEvent;
import com.gcssloop.diycode_sdk.utils.DebugUtil;
import com.gcssloop.diycode_sdk.log.Config;
import com.gcssloop.diycode_sdk.log.Logger;

import java.io.File;
import java.io.IOException;

/**
 * diycode 实现类，没有回调接口，使用 EventBus 来接收数据
 */
public class Diycode implements LoginAPI, LikesAPI, TestAPI, TopicAPI, NewsAPI, SitesAPI, UserAPI,
        PhotoAPI, NotificationsAPI, ProjectAPI {

    private static LoginImpl sLoginImpl;
    private static TestImpl sTestImpl;
    private static LikesImpl sLikesImpl;
    private static TopicImpl sTopicImplement;
    private static NewsImpl sNewsImpl;
    private static SitesImpl sSitesImpl;
    private static UserImpl sUserImpl;
    private static PhotoImpl sPhotoImpl;
    private static ProjectImpl sProjectImpl;
    private static NotificationsImpl sNotificationsImpl;

    //--- 单例 -----------------------------------------------------------------------------------

    private volatile static Diycode mDiycode;

    private Diycode() {
    }


    public static Diycode getSingleInstance() {
        if (null == mDiycode) {
            synchronized (Diycode.class) {
                if (null == mDiycode) {
                    mDiycode = new Diycode();
                }
            }
        }
        return mDiycode;
    }

    //--- 初始化 ---------------------------------------------------------------------------------

    public static Diycode init(@NonNull Context context, @NonNull final String client_id,
                               @NonNull final String client_secret) {
        initLogger(context);
        Logger.i("初始化 diycode");

        OAuth.client_id = client_id;
        OAuth.client_secret = client_secret;

        initImplement(context);

        return getSingleInstance();
    }

    private static void initLogger(@NonNull Context context) {
        // 在 debug 模式输出日志， release 模式自动移除
        if (DebugUtil.isInDebug(context)) {
            Logger.init("Diycode").setLevel(Config.LEVEL_FULL);
        } else {
            Logger.init("Diycode").setLevel(Config.LEVEL_NONE);
        }
    }

    private static void initImplement(Context context) {
        Logger.i("初始化 implement");
        try {
            sLoginImpl = new LoginImpl(context);
            sTestImpl = new TestImpl(context);
            sLikesImpl = new LikesImpl(context);
            sTopicImplement = new TopicImpl(context);
            sNewsImpl = new NewsImpl(context);
            sSitesImpl = new SitesImpl(context);
            sUserImpl = new UserImpl(context);
            sPhotoImpl = new PhotoImpl(context);
            sProjectImpl = new ProjectImpl(context);
            sNotificationsImpl = new NotificationsImpl(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.i("初始化 implement 结束");
    }


    //--- login ------------------------------------------------------------------------------------

    /**
     * 登录时调用
     * 返回一个 token，用于获取各类私有信息使用，该 token 用 LoginEvent 接收。
     *
     * @param user_name 用户名
     * @param password  密码
     * @see LoginEvent
     */
    @Override
    public String login(@NonNull String user_name, @NonNull String password) {
        return sLoginImpl.login(user_name, password);
    }

    /**
     * 用户登出
     */
    @Override
    public void logout() {
        sLoginImpl.logout();
    }

    /**
     * 是否登录
     *
     * @return 是否登录
     */
    @Override
    public boolean isLogin() {
        return sLoginImpl.isLogin();
    }

    //--- token ------------------------------------------------------------------------------------

    /**
     * 刷新 token
     *
     * @see RefreshTokenEvent
     */
    @Override
    public String refreshToken() {
        return sLoginImpl.refreshToken();
    }

    /**
     * 获取当前缓存的 token
     *
     * @return 当前缓存的 token
     */
    @Override
    public Token getCacheToken() {
        return sLoginImpl.getCacheToken();
    }

    //--- devices ----------------------------------------------------------------------------------

    /**
     * 更新设备信息
     * 记录用户 Device 信息，用于 Push 通知。
     * 请在每次用户打开 App 的时候调用此 API 以便更新 Token 的 last_actived_at 让服务端知道这个设备还活着。
     * Push 将会忽略那些超过两周的未更新的设备。
     */
    @Deprecated
    @Override
    public String updateDevices() {
        return sLoginImpl.updateDevices();
    }

    /**
     * 删除 Device 信息，请注意在用户登出或删除应用的时候调用，以便能确保清理掉。
     */
    @Deprecated
    @Override
    public String deleteDevices() {
        return sLoginImpl.deleteDevices();
    }


    //--- test -------------------------------------------------------------------------------------

    /**
     * 简单的 API 测试接口，需要登录验证，便于快速测试 OAuth 以及其他 API 的基本格式是否正确。
     * 使用 HelloEvent 接收结果。
     *
     * @param limit 数量极限，值范围[0..100]
     * @see HelloEvent
     */
    @Override
    public String hello(@Nullable Integer limit) {
        return sTestImpl.hello(limit);
    }


    //--- like -------------------------------------------------------------------------------------

    /**
     * 赞
     *
     * @param obj_type 值范围["topic", "reply", "news"]
     * @param obj_id   唯一id
     * @see LikeEvent
     */
    @Override
    public String like(@NonNull String obj_type, @NonNull Integer obj_id) {
        return sLikesImpl.like(obj_type, obj_id);
    }

    /**
     * 取消之前的赞
     *
     * @param obj_type 值范围["topic", "reply", "news"]
     * @param obj_id   唯一id
     * @see UnLikeEvent
     */
    @Override
    public String unLike(@NonNull String obj_type, @NonNull Integer obj_id) {
        return sLikesImpl.unLike(obj_type, obj_id);
    }


    //--- topic ------------------------------------------------------------------------------------

    /**
     * 获取 topic 列表
     *
     * @param type    类型，默认 last_actived，
     *                可选["last_actived", "recent", "no_reply", "popular", "excellent"]
     * @param node_id 如果你需要只看某个节点的，请传此参数, 如果不传 则返回全部
     * @param offset  偏移数值，默认值 0
     * @param limit   数量极限，默认值 20，值范围 1..150
     * @see GetTopicsListEvent
     */
    @Override
    public String getTopicsList(@Nullable String type, @Nullable Integer node_id,
                                @Nullable Integer offset, @Nullable Integer limit) {
        return sTopicImplement.getTopicsList(type, node_id, offset, limit);
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
    public String createTopic(@NonNull String title, @NonNull String body,
                              @NonNull Integer node_id) {
        return sTopicImplement.createTopic(title, body, node_id);
    }

    /**
     * 获取 topic 内容
     *
     * @param id topic 的 id
     * @see GetTopicEvent
     */
    @Override
    public String getTopic(@NonNull int id) {
        return sTopicImplement.getTopic(id);
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
    public String updateTopic(@NonNull int id, @NonNull String title, @NonNull String body,
                              @NonNull Integer node_id) {
        return sTopicImplement.updateTopic(id, title, body, node_id);
    }

    /**
     * 删除一个话题
     *
     * @param id 要删除的话题 id
     * @see DeleteTopicEvent
     */
    @Override
    public String deleteTopic(@NonNull int id) {
        return sTopicImplement.deleteTopic(id);
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
        return sTopicImplement.collectionTopic(id);
    }

    /**
     * 取消收藏话题
     *
     * @param id 被收藏的话题 id
     * @see UnCollectionTopicEvent
     */
    @Override
    public String unCollectionTopic(@NonNull int id) {
        return sTopicImplement.unCollectionTopic(id);
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
        return sTopicImplement.watchTopic(id);
    }

    /**
     * 取消关注话题
     *
     * @param id 话题 id
     * @see UnWatchTopicEvent
     */
    @Override
    public String unWatchTopic(@NonNull int id) {
        return sTopicImplement.unWatchTopic(id);
    }

    //--- topic reply ------------------------------------------------------------------------------

    /**
     * 获取 topic 回复列表
     *
     * @param id     topic 的 id
     * @param offset 偏移数值 默认 0
     * @param limit  数量极限，默认值 20，值范围 1...150
     * @see GetTopicRepliesListEvent
     */
    @Override
    public String getTopicRepliesList(@NonNull int id, @Nullable Integer offset,
                                      @Nullable Integer limit) {
        return sTopicImplement.getTopicRepliesList(id, offset, limit);
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
        return sTopicImplement.createTopicReply(id, body);
    }

    /**
     * 获取回帖的详细内容（一般用于编辑回帖的时候）
     *
     * @param id id
     * @see GetTopicReplyEvent
     */
    @Override
    public String getTopicReply(@NonNull int id) {
        return sTopicImplement.getTopicReply(id);
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
        return sTopicImplement.updateTopicReply(id, body);
    }

    /**
     * 删除回帖
     *
     * @param id id
     * @see DeleteTopicReplyEvent
     */
    @Override
    public String deleteTopicReply(@NonNull int id) {
        return sTopicImplement.deleteTopicReply(id);
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
        return sTopicImplement.banTopic(id);
    }


    //--- news -------------------------------------------------------------------------------------

    /**
     * 获取 news 列表
     *
     * @param node_id 如果你需要只看某个节点的，请传此参数, 如果不传 则返回全部
     * @param offset  偏移数值，默认值 0
     * @param limit   数量极限，默认值 20，值范围 1..150
     * @see GetNewsListEvent
     */
    @Override
    public String getNewsList(@Nullable Integer node_id, @Nullable Integer offset,
                              @Nullable Integer limit) {
        return sNewsImpl.getNewsList(node_id, offset, limit);
    }

    /**
     * 创建一个 new (分享)
     *
     * @param title   标题
     * @param address 地址(网址链接)
     * @param node_id 节点 id
     * @see CreateNewsEvent
     */
    @Override
    public String createNews(@NonNull Integer title, @NonNull Integer address,
                             @NonNull Integer node_id) {
        return sNewsImpl.createNews(title, address, node_id);
    }

    //--- news reply ----------------------------------------------------------0--------------------

    /**
     * 获取 news 回帖列表
     *
     * @param id     id
     * @param offset 偏移数值 默认 0
     * @param limit  数量极限，默认值 20，值范围 1...150
     * @see GetNewsRepliesListEvent
     */
    @Override
    public String getNewsRepliesList(@NonNull int id, @Nullable Integer offset,
                                     @Nullable Integer limit) {
        return sNewsImpl.getNewsRepliesList(id, offset, limit);
    }

    /**
     * 创建 news 回帖 (暂不可用, 没有api)
     *
     * @param id   id
     * @param body 回帖内容， markdown格式
     * @see CreateNewsReplyEvent
     */
    @Override
    public String createNewsReply(@NonNull int id, @NonNull Integer body) {
        return sNewsImpl.createNewsReply(id, body);
    }

    /**
     * 获取 news 回帖详情
     *
     * @param id id
     * @see GetNewsReplyEvent
     */
    @Override
    public String getNewsReply(@NonNull int id) {
        return sNewsImpl.getNewsReply(id);
    }

    /**
     * 更新 news 回帖
     *
     * @param id   id
     * @param body 回帖内容
     * @see UpdateNewsReplyEvent
     */
    @Override
    public String updateNewsReply(@NonNull int id, @NonNull String body) {
        return sNewsImpl.updateNewsReply(id, body);
    }

    /**
     * 删除 news 回帖
     *
     * @param id id
     * @see DeleteNewsReplyEvent
     */
    @Override
    public String deleteNewsReply(@NonNull int id) {
        return sNewsImpl.deleteNewsReply(id);
    }

    //--- news node --------------------------------------------------------------------------------

    /**
     * 获取 news 分类列表
     *
     * @see GetNewsNodesListEvent
     */
    @Override
    public String getNewsNodesList() {
        return sNewsImpl.getNewsNodesList();
    }


    //--- sites ------------------------------------------------------------------------------------

    /**
     * 获取 酷站 列表
     *
     * @see GetSitesEvent
     */
    @Override
    public String getSites() {
        return sSitesImpl.getSites();
    }


    //--- user info --------------------------------------------------------------------------------

    /**
     * 获取用户列表
     *
     * @param limit 数量极限，默认值 20，值范围 1..150
     * @see GetUsersListEvent
     */
    @Override
    public String getUsersList(@Nullable Integer limit) {
        return sUserImpl.getUsersList(limit);
    }

    /**
     * 获取用户详细资料
     *
     * @param login_name 登录用户名(非昵称)
     * @see GetUserEvent
     */
    @Override
    public String getUser(@NonNull String login_name) {
        return sUserImpl.getUser(login_name);
    }

    /**
     * 获取当前登录者的详细资料
     *
     * @see GetMeEvent
     */
    @Override
    public String getMe() {
        return sUserImpl.getMe();
    }

    /**
     * 立即获取获取当前登录者的详细资料
     *
     * @return 用户资料
     */
    @Override
    public UserDetail getMeNow() throws IOException {
        return sUserImpl.getMeNow();
    }

    //--- user block -------------------------------------------------------------------------------

    /**
     * 屏蔽用户
     *
     * @param login_name 登录用户名(非昵称)
     * @see BlockUserEvent
     */
    @Override
    public String blockUser(@NonNull String login_name) {
        return sUserImpl.blockUser(login_name);
    }

    /**
     * 取消屏蔽用户
     *
     * @param login_name 登录用户名(非昵称)
     * @see UnBlockUserEvent
     */
    @Override
    public String unBlockUser(@NonNull String login_name) {
        return sUserImpl.unBlockUser(login_name);
    }

    /**
     * 获取用户屏蔽的用户列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see GetUserBlockedListEvent
     */
    @Override
    public String getUserBlockedList(@NonNull String login_name, @Nullable Integer offset,
                                     @Nullable Integer limit) {
        return sUserImpl.getUserBlockedList(login_name, offset, limit);
    }

    //--- user follow ------------------------------------------------------------------------------

    /**
     * 关注用户
     *
     * @param login_name 登录用户名(非昵称)
     * @see FollowUserEvent
     */
    @Override
    public String followUser(@NonNull String login_name) {
        return sUserImpl.followUser(login_name);
    }

    /**
     * 取消关注用户
     *
     * @param login_name 登录用户名(非昵称)
     * @see UnFollowUserEvent
     */
    @Override
    public String unFollowUser(@NonNull String login_name) {
        return sUserImpl.unFollowUser(login_name);
    }

    /**
     * 用户正在关注的人列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see GetUserFollowingListEvent
     */
    @Override
    public String getUserFollowingList(@NonNull String login_name, @Nullable Integer offset,
                                       @Nullable Integer limit) {
        return sUserImpl.getUserFollowingList(login_name, offset, limit);
    }

    /**
     * 关注该用户的人列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see GetUserFollowerListEvent
     */
    @Override
    public String getUserFollowerList(@NonNull String login_name, @Nullable Integer offset,
                                      @Nullable Integer limit) {
        return sUserImpl.getUserFollowerList(login_name, offset, limit);
    }

    //--- user list --------------------------------------------------------------------------------

    /**
     * 用户收藏的话题列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see GetUserCreateTopicListEvent
     */
    @Override
    public String getUserCollectionTopicList(@NonNull String login_name, @Nullable Integer offset,
                                             @Nullable Integer limit) {
        return sUserImpl.getUserCollectionTopicList(login_name, offset, limit);
    }

    /**
     * 获取用户创建的话题列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param order      类型 默认 recent，可选["recent", "likes", "replies"]
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see GetUserCreateTopicListEvent
     */
    @Override
    public String getUserCreateTopicList(@NonNull String login_name, @Nullable String order,
                                         @Nullable Integer offset, @Nullable Integer limit) {
        return sUserImpl.getUserCreateTopicList(login_name, order, offset, limit);
    }

    /**
     * 用户回复过的话题列表
     *
     * @param login_name 登录用户名(非昵称)
     * @param order      类型 默认 recent，可选["recent"]
     * @param offset     偏移数值，默认值 0
     * @param limit      数量极限，默认值 20，值范围 1..150
     * @see GetUserReplyTopicListEvent
     */
    @Override
    public String getUserReplyTopicList(@NonNull String login_name, @Nullable String order,
                                        @Nullable Integer offset, @Nullable Integer limit) {
        return sUserImpl.getUserReplyTopicList(login_name, order, offset, limit);
    }


    //--- photo ------------------------------------------------------------------------------------

    /**
     * 上传图片,请使用 Multipart 的方式提交图片文件
     *
     * @param img_file 图片文件
     * @see UploadPhotoEvent
     */
    @Override
    public String uploadPhoto(@NonNull File img_file) {
        return sPhotoImpl.uploadPhoto(img_file);
    }


    //--- notification -----------------------------------------------------------------------------

    /**
     * 获取通知列表
     *
     * @param offset 偏移数值，默认值 0
     * @param limit  数量极限，默认值 20，值范围 1..150
     * @see GetNotificationsListEvent
     */
    @Override
    public String getNotificationsList(@NonNull Integer offset, @NonNull Integer limit) {
        return sNotificationsImpl.getNotificationsList(offset, limit);
    }

    /**
     * 获得未读通知的数量
     *
     * @see GetNotificationUnReadCountEvent
     */
    @Override
    public String getNotificationUnReadCount() {
        return sNotificationsImpl.getNotificationUnReadCount();
    }

    /**
     * 将某些通知标记为已读
     *
     * @param ids id集合
     * @see MarkNotificationAsReadEvent
     */
    @Override
    public String markNotificationAsRead(int[] ids) {
        return sNotificationsImpl.markNotificationAsRead(ids);
    }

    /**
     * 删除用户的某条通知
     *
     * @param id id
     * @see DeleteNotificationEvent
     */
    @Override
    public String deleteNotionfition(@NonNull Integer id) {
        return sNotificationsImpl.deleteNotionfition(id);
    }

    /**
     * 删除当前用户的所有通知
     *
     * @see DeleteAllNotificationEvent
     */
    @Override
    public String deleteAllNotification() {
        return sNotificationsImpl.deleteAllNotification();
    }


    //--- project  ---------------------------------------------------------------------------------

    /**
     * 获取 project 列表
     *
     * @param node_id 如果你需要只看某个节点的，请传此参数, 如果不传 则返回全部
     * @param offset  偏移数值，默认值 0
     * @param limit   数量极限，默认值 20，值范围 1..150
     * @see
     */
    @Override
    public String getProjectsList(Integer node_id, Integer offset, Integer limit) {
        return sProjectImpl.getProjectsList(node_id, offset, limit);
    }

    //--- project reply ----------------------------------------------------------------------------

    /**
     * 获取 project 回复列表
     *
     * @param id     project 的 id
     * @param offset 偏移数值 默认 0
     * @param limit  数量极限，默认值 20，值范围 1...150
     * @see
     */
    @Override
    public String getProjectRepliesList(int id, Integer offset, Integer limit) {
        return sProjectImpl.getProjectsList(id, offset, limit);
    }

    /**
     * 创建 project 回帖(回复，评论)
     *
     * @param id   话题列表
     * @param body 回帖内容, Markdown 格式
     */
    @Override
    public String createProjectReply(int id, String body) {
        return sProjectImpl.createProjectReply(id, body);
    }

    /**
     * 获取回帖的详细内容（一般用于编辑回帖的时候）
     *
     * @param id id
     * @see
     */
    @Override
    public String getProjectReply(int id) {
        return sProjectImpl.getProjectReply(id);
    }

    /**
     * 更新回帖
     *
     * @param id   id
     * @param body 回帖详情
     * @see
     */
    @Override
    public String updateProjectReply(int id, String body) {
        return sProjectImpl.updateProjectReply(id, body);
    }

    /**
     * 删除回帖
     *
     * @param id id
     * @see
     */
    @Override
    public String deleteProjectReply(int id) {
        return sProjectImpl.deleteProjectReply(id);
    }
}
