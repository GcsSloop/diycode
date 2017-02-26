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
 * Last modified 2017-02-16 20:58:27
 *
 */

package com.gcssloop.diycode_test.test_api;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gcssloop.diycode_sdk.api.topic.bean.TopicContent;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicContentEvent;
import com.gcssloop.diycode_sdk.utils.TimeUtil;
import com.gcssloop.diycode_test.R;
import com.gcssloop.diycode_test.base.BaseActivity;
import com.gcssloop.diycode_test.utils.ConvertUtil;
import com.mukesh.MarkdownView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicContentTestActivity extends BaseActivity {

    @BindView(R.id.edit_id)
    EditText edit_id;

    @BindView(R.id.img_avatar)
    ImageView img_avatar;

    @BindView(R.id.text_username)
    TextView text_username;

    @BindView(R.id.text_time)
    TextView text_time;

    @BindView(R.id.text_title)
    TextView text_title;

    @BindView(R.id.markdown_body)
    MarkdownView markdown_body;

    @BindView(R.id.text_reply_count)
    TextView text_reply_count;

    @OnClick(R.id.btn_get_content)
    public void getContent(View view) {
        mDiycode.getTopicContent(ConvertUtil.StringToInteger(edit_id.getText().toString(), 604));
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTopicContent(GetTopicContentEvent event) {
        if (event.isOk()) {
            toast("获取成功");
            TopicContent topicContent = event.getBean();
            Glide.with(mContext).load(topicContent.getUser().getAvatar_url()).into(img_avatar);
            text_username.setText(topicContent.getUser().getLogin());
            text_time.setText(TimeUtil.computePastTime(topicContent.getUpdated_at()));
            text_title.setText(topicContent.getTitle());
            text_reply_count.setText("共收到" + topicContent.getReplies_count() + "条回复");
            markdown_body.setMarkDownText(topicContent.getBody());
            // web_body.loadDataWithBaseURL(null, topicContent.getBody_html(), "text/html", "utf-8", null);
        } else {
            toast("获取失败");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_content_test);
        ButterKnife.bind(this);
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
}
