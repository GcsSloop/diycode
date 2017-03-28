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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.gcssloop.diycode.R;
import com.gcssloop.diycode.adapter.TopicReplyAdapter;
import com.gcssloop.diycode.base.app.BaseActivity;
import com.gcssloop.diycode.base.app.ViewHolder;
import com.gcssloop.diycode.base.webview.GcsMarkdownViewClient;
import com.gcssloop.diycode.base.webview.WebImageListener;
import com.gcssloop.diycode.utils.DataCache;
import com.gcssloop.diycode.utils.NetUtil;
import com.gcssloop.diycode.utils.RecyclerViewUtil;
import com.gcssloop.diycode.widget.MarkdownView;
import com.gcssloop.diycode_sdk.api.topic.bean.Topic;
import com.gcssloop.diycode_sdk.api.topic.bean.TopicContent;
import com.gcssloop.diycode_sdk.api.topic.bean.TopicReply;
import com.gcssloop.diycode_sdk.api.topic.event.CreateTopicReplyEvent;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicEvent;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicRepliesListEvent;
import com.gcssloop.diycode_sdk.api.user.bean.User;
import com.gcssloop.diycode_sdk.log.Logger;
import com.gcssloop.diycode.utils.TimeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class TopicContentActivity extends BaseActivity implements View.OnClickListener {
    public static String TOPIC = "topic";
    public static String TOPIC_ID = "topic_id";
    public static String ERROR = "error";
    public static String TYPE = "type";
    private int topic_id = -1;
    private Topic topic = null;
    private DataCache mDataCache;
    private TopicReplyAdapter mAdapter;
    private MarkdownView mMarkdownView;
    private GcsMarkdownViewClient mWebViewClient;

    private EditText myReply;

    public static void newInstance(@NonNull Context context, @NonNull Topic topic) {
        Intent intent = new Intent(context, TopicContentActivity.class);
        intent.putExtra(TOPIC, topic);
        context.startActivity(intent);
    }

    public static void newInstance(@NonNull Context context, @NonNull int topic_id) {
        Intent intent = new Intent(context, TopicContentActivity.class);
        intent.putExtra(TOPIC_ID, topic_id);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_topic_content;
    }

    // 初始化数据
    @Override
    protected void initDatas() {
        Intent intent = getIntent();
        topic_id = intent.getIntExtra(TOPIC_ID, -1);
        topic = (Topic) intent.getSerializableExtra(TOPIC);
        if (topic != null && topic_id <= 0) {
            topic_id = topic.getId();
        }
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        setTitle("话题");
        mDataCache = new DataCache(this);
        initRecyclerView(holder);
        initMarkdownView(holder);
        loadData(holder);
    }

    private void initRecyclerView(ViewHolder holder) {
        mAdapter = new TopicReplyAdapter(this);
        RecyclerView recyclerView = holder.get(R.id.reply_list);
        RecyclerViewUtil.init(this, recyclerView, mAdapter);
    }

    private void initMarkdownView(ViewHolder holder) {
        FrameLayout layout = holder.get(R.id.webview_container);
        mMarkdownView = new MarkdownView(this.getApplicationContext());
        mMarkdownView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.addView(mMarkdownView);

        WebImageListener listener = new WebImageListener(this, ImageActivity.class);
        mMarkdownView.addJavascriptInterface(listener, "listener");
        mWebViewClient = new GcsMarkdownViewClient(this);
        mMarkdownView.setWebViewClient(mWebViewClient);
    }

    // 在 start 和 restart 调用
    private void initReply(ViewHolder holder) {
        if (!mDiycode.isLogin()) {
            holder.get(R.id.need_login).setVisibility(View.VISIBLE);
            holder.get(R.id.can_reply).setVisibility(View.GONE);
            holder.setOnClickListener(this, R.id.login);
        } else {
            holder.get(R.id.need_login).setVisibility(View.GONE);
            holder.get(R.id.can_reply).setVisibility(View.VISIBLE);
            holder.setOnClickListener(this, R.id.send_reply);
            myReply = holder.get(R.id.my_reply);
        }
    }

    // 初始化 topic 内容面板的数据
    @SuppressLint({"AddJavascriptInterface", "JavascriptInterface"})
    private void loadData(ViewHolder holder) {
        if (null != topic) {
            showPreview(topic);

            if (shouldReloadTopic(topic)) {
                mDiycode.getTopic(topic.getId());
                mDiycode.getTopicRepliesList(topic.getId(), null, topic.getReplies_count());
            } else {
                loadCache();
            }

        } else if (topic_id > 0) {
            // 若是缓存有内容则读取缓存
            TopicContent temp = mDataCache.getTopicContent(topic_id);
            if (temp != null) {
                showPreview(temp);
            }
            mDiycode.getTopic(topic_id);
            mDiycode.getTopicRepliesList(topic_id, null, 100);
        } else {
            toastShort("参数传递错误");
        }
    }

    private void loadCache() {
        TopicContent topicContent = mDataCache.getTopicContent(topic.getId());
        if (null != topicContent) {
            Logger.i("数据不变 - 来自缓存");
            mMarkdownView.setMarkDownText(topicContent.getBody());
        } else {
            mDiycode.getTopic(topic.getId());
        }

        List<TopicReply> replies = mDataCache.getTopicRepliesList(topic.getId());
        if (null != replies) {
            Logger.i("回复不变 - 来自缓存");
            mAdapter.addDatas(replies);
        } else {
            mDiycode.getTopicRepliesList(topic.getId(), null, topic.getReplies_count());
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

    // 显示基础数据
    private void showPreview(Topic topic) {
        ViewHolder holder = getViewHolder();
        User user = topic.getUser();
        holder.setText(R.id.username, user.getLogin() + "(" + user.getName() + ")");
        holder.setText(R.id.time, TimeUtil.computePastTime(topic.getUpdated_at()));
        holder.setText(R.id.title, topic.getTitle());
        holder.setText(R.id.reply_count, "共收到 " + topic.getReplies_count() + "条回复");
        holder.loadImage(this, user.getAvatar_url(), R.id.avatar);
        holder.setOnClickListener(this, R.id.avatar, R.id.username);
    }

    // 显示基础数据
    private void showPreview(TopicContent topic) {
        ViewHolder holder = getViewHolder();
        User user = topic.getUser();
        holder.setText(R.id.username, user.getLogin() + "(" + user.getName() + ")");
        holder.setText(R.id.time, TimeUtil.computePastTime(topic.getUpdated_at()));
        holder.setText(R.id.title, topic.getTitle());
        holder.setText(R.id.reply_count, "共收到 " + topic.getReplies_count() + "条回复");
        holder.loadImage(this, user.getAvatar_url(), R.id.avatar);
        holder.setOnClickListener(this, R.id.avatar, R.id.username);
    }

    // 显示全部数据
    private void showAll(TopicContent topic) {
        showPreview(topic);
        mMarkdownView.setMarkDownText(topic.getBody());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTopicDetail(GetTopicEvent event) {
        if (event.isOk()) {
            showAll(event.getBean());
            mDataCache.saveTopicContent(event.getBean());
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTopicRepliesList(GetTopicRepliesListEvent event) {
        if (event.isOk()) {
            Logger.i("topic reply 回复 - 来自网络");
            mAdapter.clearDatas();
            mAdapter.addDatas(event.getBean());
            mDataCache.saveTopicRepliesList(topic_id, event.getBean());
        } else {
            // toastShort("获取回复失败");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreateReply(CreateTopicReplyEvent event) {
        if (event.isOk()) {
            Logger.i("回复成功");
            myReply.setText("");
            mDiycode.getTopicRepliesList(topic.getId(), null, topic.getReplies_count() + 1);
        } else {
            toastShort("回复失败");
        }
    }

    // 防止 WebView 引起的内存泄漏
    public void clearWebViewResource() {
        if (mMarkdownView != null) {
            mMarkdownView.clearHistory();
            ((ViewGroup) mMarkdownView.getParent()).removeView(mMarkdownView);
            mMarkdownView.setTag(null);
            mMarkdownView.loadUrl("about:blank");
            mMarkdownView.stopLoading();
            mMarkdownView.setWebViewClient(null);
            mMarkdownView.setWebChromeClient(null);
            mMarkdownView.removeAllViews();
            mMarkdownView.destroy();
            mMarkdownView = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        initReply(getViewHolder());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initReply(getViewHolder());
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearWebViewResource();
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
            case R.id.login:
                openActivity(LoginActivity.class);
                break;
            case R.id.send_reply:
                String reply = myReply.getText().toString();
                if (reply.isEmpty()) {
                    toastShort("评论内容不能为空。");
                    return;
                }
                mDiycode.createTopicReply(topic.getId(), reply);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topic_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                String mUrl = "https://www.diycode.cc/topics/" + topic.getId();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, topic.getTitle());
                shareIntent.putExtra(Intent.EXTRA_TEXT, mUrl);
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
