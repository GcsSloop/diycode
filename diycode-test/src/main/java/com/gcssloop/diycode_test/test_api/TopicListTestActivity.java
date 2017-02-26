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
 * Last modified 2017-02-14 20:54:39
 *
 */

package com.gcssloop.diycode_test.test_api;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gcssloop.diycode_sdk.bean.Topic;
import com.gcssloop.diycode_sdk.event.GetTopicsEvent;
import com.gcssloop.diycode_sdk.utils.TimeUtil;
import com.gcssloop.diycode_test.R;
import com.gcssloop.diycode_test.adapter.CommonAdapter;
import com.gcssloop.diycode_test.adapter.ViewHolder;
import com.gcssloop.diycode_test.base.BaseActivity;
import com.gcssloop.diycode_test.utils.ConvertUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicListTestActivity extends BaseActivity {
    String[] types = {"last_actived", "recent", "no_reply", "popular", "excellent"};

    @BindView(R.id.topic_list)
    ListView topic_list;

    @BindView(R.id.text_type)
    TextView text_type;

    @BindView(R.id.text_node_id)
    TextView text_node_id;

    @BindView(R.id.text_offset)
    TextView text_offset;

    @BindView(R.id.text_limit)
    TextView text_limit;

    @OnClick(R.id.get_topic_list)
    public void GetTopicList(View view) {

        String type = null;
        Integer type_id = ConvertUtil.StringToInteger(text_type.getText().toString());
        if (null != type_id && type_id >= 0 && type_id < 5) {
            type = types[type_id];
        }

        Integer node_id = ConvertUtil.StringToInteger(text_node_id.getText().toString());
        Integer offset = ConvertUtil.StringToInteger(text_offset.getText().toString());
        Integer limit = ConvertUtil.StringToInteger(text_limit.getText().toString());

        mDiycode.getTopics(type, node_id, offset, limit);
    }


    private CommonAdapter<Topic> mAdapter;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTopicList(GetTopicsEvent event) {
        if (event.isOk()) {
            List<Topic> topics = event.getBean();
            if (null == mAdapter){
                mAdapter = new CommonAdapter<Topic>(this, topics, R.layout.item_topic) {
                    @Override
                    public void convert(int position, ViewHolder holder, Topic bean) {
                        ImageView img_avatar = holder.getView(R.id.img_avatar);
                        Glide.with(mContext).load(bean.getUser().getAvatar_url()).into(img_avatar);

                        TextView text_username = holder.getView(R.id.text_username);
                        text_username.setText(bean.getUser().getLogin());

                        TextView text_time = holder.getView(R.id.text_time);
                        String time = TimeUtil.computePastTime(bean.getUpdated_at());
                        text_time.setText(time);

                        TextView text_title = holder.getView(R.id.text_title);
                        text_title.setText(bean.getTitle());
                    }

                };
                topic_list.setAdapter(mAdapter);
            } else {
                mAdapter.addDatas(topics);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list_test);
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
