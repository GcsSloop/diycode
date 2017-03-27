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
 * Last modified 2017-03-26 05:35:12
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
import com.gcssloop.diycode.activity.UserActivity;
import com.gcssloop.diycode.base.glide.GlideImageGetter;
import com.gcssloop.diycode.base.recyclerview.GcsAdapter;
import com.gcssloop.diycode.base.recyclerview.GcsViewHolder;
import com.gcssloop.diycode.utils.HtmlUtil;
import com.gcssloop.diycode_sdk.api.topic.bean.TopicReply;
import com.gcssloop.diycode_sdk.api.user.bean.User;
import com.gcssloop.diycode.utils.TimeUtil;

public class TopicReplyAdapter extends GcsAdapter<TopicReply> {
    private Context mContext;

    public TopicReplyAdapter(@NonNull Context context) {
        super(context, R.layout.item_topic_reply);
        mContext = context;
    }

    @Override
    public void convert(int position, GcsViewHolder holder, TopicReply bean) {
        final User user = bean.getUser();
        holder.setText(R.id.username, user.getLogin());
        holder.setText(R.id.time, TimeUtil.computePastTime(bean.getUpdated_at()));
        holder.loadImage(mContext, user.getAvatar_url(), R.id.avatar);
        TextView content = holder.get(R.id.content);
        // TODO 评论区代码问题
        content.setText(Html.fromHtml(HtmlUtil.removeP(bean.getBody_html()), new GlideImageGetter(mContext, content), null));

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserActivity.class);
                intent.putExtra(UserActivity.USER, user);
                mContext.startActivity(intent);
            }
        }, R.id.avatar, R.id.username);
    }
}
