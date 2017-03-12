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
 * Last modified 2017-03-08 04:22:13
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.gcssloop.diycode.R;
import com.gcssloop.diycode.base.recyclerview.GcsAdapter;
import com.gcssloop.diycode.base.recyclerview.GcsViewHolder;
import com.gcssloop.diycode.base.app.BaseActivity;
import com.gcssloop.diycode.base.app.ViewHolder;
import com.gcssloop.diycode.base.glide.GlideImageGetter;
import com.gcssloop.diycode.base.webview.GcsWebViewClient;
import com.gcssloop.diycode.utils.DataCache;
import com.gcssloop.diycode.utils.HtmlUtil;
import com.gcssloop.diycode.utils.NetUtil;
import com.gcssloop.diycode.utils.RecyclerViewUtil;
import com.gcssloop.diycode.widget.MarkdownView;
import com.gcssloop.diycode_sdk.api.topic.bean.Topic;
import com.gcssloop.diycode_sdk.api.topic.bean.TopicContent;
import com.gcssloop.diycode_sdk.api.topic.bean.TopicReply;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicRepliesListEvent;
import com.gcssloop.diycode_sdk.api.user.bean.User;
import com.gcssloop.diycode_sdk.log.Logger;
import com.gcssloop.diycode_sdk.utils.TimeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class TopicContentActivity extends BaseActivity implements View.OnClickListener {
    public static String TOPIC = "topic";
    public static String TOPIC_ID = "topic_id";
    private Topic topic;
    private DataCache mDataCache;
    private GcsAdapter<TopicReply> mAdapter;
    MarkdownView mMarkdownView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_topic_content;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        mDataCache = new DataCache(this);
        initRecyclerView(holder);
        loadData(holder);
    }

    // 初始化 topic 内容面板的数据
    private void loadData(ViewHolder holder) {
        topic = (Topic) getIntent().getSerializableExtra(TOPIC);
        if (null != topic) {
            User user = topic.getUser();
            holder.setText(R.id.username, user.getLogin() + "(" + user.getName() + ")");
            holder.setText(R.id.time, TimeUtil.computePastTime(topic.getUpdated_at()));
            holder.setText(R.id.title, topic.getTitle());
            holder.setText(R.id.reply_count, "共收到 " + topic.getReplies_count() + "条回复");
            holder.loadImage(this, user.getAvatar_url(), R.id.avatar);
            holder.setOnClickListener(this, R.id.avatar, R.id.username);
            mMarkdownView = holder.get(R.id.content);

            GcsWebViewClient client = new GcsWebViewClient(this);
            client.setOpenUrlInBrowser(true);
            mMarkdownView.setWebViewClient(client);

            if (shouldReloadTopic(topic)) {
                mDiycode.getTopic(topic.getId());
                mDiycode.getTopicRepliesList(topic.getId(), null, topic.getReplies_count());
                return;
            }

            // 读取缓存
            TopicContent topicContent = mDataCache.getTopicContent(topic.getId());
            if (null != topicContent) {
                Logger.i("数据不变 - 来自缓存");
                mMarkdownView.setMarkDownText(topicContent.getBody());
            }

            List<TopicReply> replies = mDataCache.getTopicRepliesList(topic.getId());
            if (null != replies) {
                Logger.i("回复不变 - 来自缓存");
                mAdapter.addDatas(replies);
            }
        } else {
            toastShort("未传递数据");
        }
    }

    // 是否需重新加载 topic 详情
    private boolean shouldReloadTopic(@NonNull Topic topic) {
        if (!NetUtil.isNetConnection(this)) {
            return false;   // 无网络，无法加载
        }
        if (null == mDataCache.getTopicContent(topic.getId())) {
            return true;    // 无缓存，需要加载
        }
        TopicContent content = mDataCache.getTopicContent(topic.getId());
        if (!content.getUpdated_at().equals(topic.getUpdated_at())) {
            return true;    // 更新时间不同，需要加载
        }
        return false;
    }

    private void initRecyclerView(ViewHolder holder) {
        mAdapter = new GcsAdapter<TopicReply>(this, R.layout.item_topic_reply) {
            @Override
            public void convert(int position, GcsViewHolder holder, TopicReply bean) {
                User user = bean.getUser();
                holder.setText(R.id.username, user.getLogin());
                holder.setText(R.id.time, TimeUtil.computePastTime(bean.getUpdated_at()));
                holder.loadImage(mContext, user.getAvatar_url(), R.id.avatar);
                TextView content = holder.get(R.id.content);
                // TODO 评论区代码问题
                content.setText(Html.fromHtml(HtmlUtil.removeP(bean.getBody_html()), new GlideImageGetter(mContext, content), null));

            }
        };

        RecyclerView recyclerView = holder.get(R.id.reply_list);
        RecyclerViewUtil.init(this, recyclerView, mAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTopicDetail(GetTopicEvent event) {
        if (event.isOk()) {
            Logger.i("topic 请求成功 - 来自网络");
            mMarkdownView.setMarkDownText(event.getBean().getBody());
            mDataCache.saveTopicContent(event.getBean());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTopicReplysList(GetTopicRepliesListEvent event) {
        if (event.isOk()) {
            Logger.i("topic reply 回复 - 来自网络");
            mAdapter.addDatas(event.getBean());
            mDataCache.saveTopicRepliesList(topic.getId(), event.getBean());
        } else {
            toastShort("获取回复失败");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avatar:
            case R.id.username:
                if (null != topic) {
                    openActivity(UserActivity.class, UserActivity.USER, topic.getUser());
                }
                break;
        }
    }
}
