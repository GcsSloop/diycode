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

package com.gcssloop.diycode.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gcssloop.diycode.R;
import com.gcssloop.diycode.base.adapter.GcsAdapter;
import com.gcssloop.diycode.base.adapter.GcsViewHolder;
import com.gcssloop.diycode_sdk.api.topic.bean.Topic;
import com.gcssloop.diycode_sdk.api.user.bean.User;
import com.gcssloop.diycode_sdk.utils.TimeUtil;

public class TopicListAdapter extends GcsAdapter<Topic> {
    public TopicListAdapter(@NonNull Context context) {
        super(context, R.layout.item_topic);
    }

    @Override
    public void convert(int position, GcsViewHolder holder, Topic topic) {
        final User user = topic.getUser();
        holder.setText(R.id.username, user.getLogin());
        holder.setText(R.id.node_name, topic.getNode_name());
        holder.setText(R.id.time, TimeUtil.computePastTime(topic.getUpdated_at()));
        holder.setText(R.id.title, topic.getTitle());
        holder.loadImage(mContext, user.getAvatar_url(), R.id.avatar);

        setListener(position, holder, topic);
    }

    public void setListener(int position, GcsViewHolder holder, Topic topic) {

    }
}
