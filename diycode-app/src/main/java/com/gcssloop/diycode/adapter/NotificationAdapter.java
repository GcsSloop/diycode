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
 * Last modified 2017-03-26 14:37:00
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.gcssloop.diycode.R;
import com.gcssloop.diycode.activity.UserActivity;
import com.gcssloop.diycode.base.recyclerview.GcsAdapter;
import com.gcssloop.diycode.base.recyclerview.GcsViewHolder;
import com.gcssloop.diycode_sdk.api.notifications.bean.Notification;
import com.gcssloop.diycode_sdk.api.user.bean.User;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends GcsAdapter<Notification> {
    private static String TYPE_Mention = "Mention";
    private static String MENTION_TYPE_TopicReply = "Reply";
    private static String MENTION_TYPE_NewReply = "HacknewsReply";
    private static String MENTION_TYPE_ProjectReply = "ProjectReply";
    private static String TYPE_NewsReply = "Hacknews";
    private static String TYPE_TopicReply = "TopicReply";

    private Context mContext;

    public NotificationAdapter(@NonNull Context context) {
        super(context, R.layout.item_notification);
        mContext = context;
    }

    @Override
    public void convert(int position, GcsViewHolder holder, Notification bean) {
        final User actor = bean.getActor();

        String type = actor.getLogin() + " " + "";

        holder.setText(actor.getLogin(), R.id.notification_type);
        holder.loadImage(mContext, actor.getAvatar_url(), R.id.avatar);

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserActivity.newInstance(mContext, actor);
            }
        }, R.id.avatar);
    }

    /**
     * 清洗数据，主要清洗对象
     * 1. HackNew 的回复 type = Hacknews
     * 2. HackNew 的提及 type = Mention, mention_type = HacknewsReply
     * 3. Project 的提及 type = Mention, mention_type = ProjectReply
     * <p>
     * 保留数据
     * 1. Topic 的回复 type = TopicReply
     * 2. Topic 的提及 type = Mention, mention_type = Reply
     */
    @Override
    public void addDatas(List<Notification> datas) {
        List<Notification> clearDatas = new ArrayList<>();
        for (Notification data : datas) {
            if (data.getType().equals(TYPE_TopicReply) ||
                    (data.getType().equals(TYPE_Mention) && data.getMention_type().equals(MENTION_TYPE_TopicReply))) {
                clearDatas.add(data);
            }
        }
        super.addDatas(clearDatas);
    }
}
