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
 * Last modified 2017-02-19 13:40:10
 *
 */

package com.gcssloop.diycode_test.test_api;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gcssloop.diycode_sdk.api.topic.bean.TopicReply;
import com.gcssloop.diycode_sdk.api.topic.event.GetTopicRepliesListEvent;
import com.gcssloop.diycode_sdk.utils.TimeUtil;
import com.gcssloop.diycode_test.R;
import com.gcssloop.diycode_test.adapter.CommonAdapter;
import com.gcssloop.diycode_test.adapter.ViewHolder;
import com.gcssloop.diycode_test.base.BaseActivity;
import com.gcssloop.diycode_test.utils.ConvertUtil;
import com.mukesh.MarkdownView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicReplyTestActivity extends BaseActivity {

    @BindView(R.id.list_topic_reply)
    ListView list_topic_reply;

    @BindView(R.id.edit_id)
    EditText edit_id;

    @OnClick(R.id.btn_get_reply)
    public void getReply(View view) {
        mDiycode.getTopicRepliesList(ConvertUtil.StringToInteger(edit_id.getText().toString(), 604), null, null);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTopicReplies(GetTopicRepliesListEvent event) {

        if (event.isOk()) {
            Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
            final List<TopicReply> replies = event.getBean();
            list_topic_reply.setAdapter(new CommonAdapter<TopicReply>(mContext, replies, R.layout.item_topic_reply) {

                /**
                 * 需要处理的部分，在这里给View设置值
                 *
                 * @param position
                 * @param holder   ViewHolder
                 * @param bean     数据集
                 */
                @Override
                public void convert(int position, ViewHolder holder, TopicReply bean) {

                    ImageView img_avatar = holder.getView(R.id.img_avatar);
                    Glide.with(mContext).load(bean.getUser().getAvatar_url()).into(img_avatar);

                    holder.setText(R.id.text_username, bean.getUser().getLogin());
                    holder.setText(R.id.text_reply_floor, (position + 1) + "楼");
                    holder.setText(R.id.text_time, TimeUtil.computePastTime(bean.getUpdated_at()));

                    MarkdownView markdownView = holder.getView(R.id.markdown_reply_content);
                   // markdownView.loadData(bean.getBody_html(),"text/html;charset=UTF-8", null);
                    markdownView.setMarkDownText(bean.getBody_html());

                    // holder.setText(R.id.text_reply_content, Html.fromHtml(bean.getBody_html()));
                }
            });
        } else {
            Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_reply_test);
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
