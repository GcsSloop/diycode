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

import com.gcssloop.diycode_sdk.api.login.api.LoginAPI;
import com.gcssloop.diycode_sdk.api.login.api.LoginImplement;
import com.gcssloop.diycode_sdk.api.login.bean.Token;
import com.gcssloop.diycode_sdk.api.login.event.LoginEvent;
import com.gcssloop.diycode_sdk.api.login.event.RefreshTokenEvent;
import com.gcssloop.diycode_sdk.api.test.Event.HelloEvent;
import com.gcssloop.diycode_sdk.api.test.api.TestAPI;
import com.gcssloop.diycode_sdk.api.test.api.TestImplement;
import com.gcssloop.diycode_sdk.api.topic.api.TopicAPI;
import com.gcssloop.diycode_sdk.api.topic.api.TopicImplement;
import com.gcssloop.diycode_sdk.api.topic.event.BanTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.CollectionTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.CreateTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.CreateTopicReplyEvent;
import com.gcssloop.diycode_sdk.api.topic.event.DeleteTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.DeleteTopicReplyEvent;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicRepliesEvent;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicReplyEvent;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicsListEvent;
import com.gcssloop.diycode_sdk.api.topic.event.UnCollectionTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.UnWatchTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.UpdateTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.UpdateTopicReplyEvent;
import com.gcssloop.diycode_sdk.api.topic.event.WatchTopicEvent;
import com.gcssloop.diycode_sdk.utils.DebugUtil;
import com.gcssloop.gcs_log.Config;
import com.gcssloop.gcs_log.Logger;

/**
 * diycode 实现类，没有回调接口，使用 EventBus 来接收数据
 */
public class Diycode implements LoginAPI, TestAPI, TopicAPI {

    private static LoginImplement sLoginImplement;
    private static TestImplement sTestImplement;
    private static TopicImplement sTopicImplement;

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

    public static Diycode init(@NonNull Context context, @NonNull final String client_id, @NonNull final String client_secret) {
        initLogger(context);
        Logger.i("初始化 diycode");

        initImplement(context, client_id, client_secret);

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

    private static void initImplement(Context context, String client_id, String client_secret) {
        Logger.i("初始化 implement");
        try {
            sLoginImplement = new LoginImplement(context, client_id, client_secret);
            sTestImplement = new TestImplement(context);
            sTopicImplement = new TopicImplement(context);
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
        return sLoginImplement.login(user_name, password);
    }

    /**
     * 用户登出
     */
    @Override
    public void logout() {
        sLoginImplement.logout();
    }

    //--- token ------------------------------------------------------------------------------------

    /**
     * 刷新 token
     *
     * @see RefreshTokenEvent
     */
    @Override
    public String refreshToken() {
        return sLoginImplement.refreshToken();
    }

    /**
     * 获取当前缓存的 token
     *
     * @return 当前缓存的 token
     */
    @Override
    public Token getCacheToken() {
        return sLoginImplement.getCacheToken();
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
        return sLoginImplement.updateDevices();
    }

    /**
     * 删除 Device 信息，请注意在用户登出或删除应用的时候调用，以便能确保清理掉。
     */
    @Deprecated
    @Override
    public String deleteDevices() {
        return sLoginImplement.deleteDevices();
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
        return sTestImplement.hello(limit);
    }


    //--- topic ------------------------------------------------------------------------------------

    /**
     * 获取 topic 列表
     *
     * @param type    类型，默认 last_actived，可选["last_actived", "recent", "no_reply", "popular", "excellent"]
     * @param node_id 如果你需要只看某个节点的，请传此参数, 如果不传 则返回全部
     * @param offset  偏移数值，默认值 0
     * @param limit   数量极限，默认值 20，值范围 1..150
     * @see GetTopicsListEvent
     */
    @Override
    public String getTopicsList(String type, @Nullable Integer node_id, @Nullable Integer offset, @Nullable Integer limit) {
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
        return sTopicImplement.getTopicReplies(id, offset, limit);
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


}
