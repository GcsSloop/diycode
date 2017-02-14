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

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gcssloop.diycode_sdk.api.bean.Topic;
import com.gcssloop.diycode_sdk.api.event.GetTopicsEvent;
import com.gcssloop.diycode_sdk.api.utils.TimeUtil;
import com.gcssloop.diycode_test.R;
import com.gcssloop.diycode_test.adapter.CommonAdapter;
import com.gcssloop.diycode_test.adapter.ViewHolder;
import com.gcssloop.diycode_test.base.BaseActivity;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicListTestActivity extends BaseActivity {

    @BindView(R.id.topic_list)
    ListView topic_list;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @OnClick(R.id.get_topic_list)
    public void GetTopicList(View view) {
        mDiycode.getTopics(null, null, null, null);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTopicList(GetTopicsEvent event) {
        if (event.isOk()) {
            List<Topic> topics = event.getBean();
            topic_list.setAdapter(new CommonAdapter<Topic>(this, topics, R.layout.item_topic) {
                /**
                 * 需要处理的部分，在这里给View设置值
                 *
                 * @param position
                 * @param holder   ViewHolder
                 * @param bean     数据集
                 */
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
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list_test);
        ButterKnife.bind(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        EventBus.getDefault().register(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        EventBus.getDefault().unregister(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("TopicListTest Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
}
