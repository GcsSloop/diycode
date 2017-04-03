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
 * Last modified 2017-03-24 02:19:11
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.gcssloop.diycode.R;
import com.gcssloop.diycode.activity.TopicContentActivity;
import com.gcssloop.diycode.activity.UserActivity;
import com.gcssloop.diycode.base.recyclerview.GcsAdapter;
import com.gcssloop.diycode.base.recyclerview.GcsViewHolder;
import com.gcssloop.diycode.utils.DataCache;
import com.gcssloop.diycode.utils.TimeUtil;
import com.gcssloop.diycode_sdk.api.topic.bean.Topic;
import com.gcssloop.diycode_sdk.api.user.bean.User;

public class TopicAdapter extends GcsAdapter<Topic> {
    private Context mContext;
    private DataCache mDataCache;

    public TopicAdapter(@NonNull Context context, DataCache dataCache) {
        super(context, R.layout.item_topic);
        mContext = context;
        mDataCache = dataCache;
    }

    @Override
    public void convert(int position, GcsViewHolder holder, final Topic topic) {
        final User user = topic.getUser();
        holder.setText(R.id.username, user.getLogin());
        holder.setText(R.id.node_name, topic.getNode_name());
        holder.setText(R.id.time, TimeUtil.computePastTime(topic.getUpdated_at()));
        holder.setText(R.id.title, topic.getTitle());
        holder.loadImage(mContext, user.getAvatar_url(), R.id.avatar);

        String state = "评论 "+topic.getReplies_count();
        holder.setText(R.id.state, state);

        TextView preview = holder.get(R.id.preview);
        String text = mDataCache.getTopicPreview(topic.getId());
        if (null != text) {
            preview.setVisibility(View.VISIBLE);
            preview.setText(Html.fromHtml(text));
        } else {
            preview.setVisibility(View.GONE);
        }

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserActivity.class);
                intent.putExtra(UserActivity.USER, user);
                mContext.startActivity(intent);
            }
        }, R.id.avatar, R.id.username);

        holder.get(R.id.item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopicContentActivity.newInstance(mContext, topic);
            }
        });
    }
}
