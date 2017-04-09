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
 * Last modified 2017-04-09 21:22:01
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.fragment.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcssloop.diycode.R;
import com.gcssloop.diycode.activity.TopicContentActivity;
import com.gcssloop.diycode.activity.UserActivity;
import com.gcssloop.diycode.utils.HtmlUtil;
import com.gcssloop.diycode.utils.ImageUtils;
import com.gcssloop.diycode_sdk.api.notifications.bean.Notification;
import com.gcssloop.diycode_sdk.api.user.bean.User;
import com.gcssloop.recyclerview.adapter.base.RecyclerViewHolder;
import com.gcssloop.recyclerview.adapter.multitype.BaseViewProvider;

public class NotificationsProvider extends BaseViewProvider<Notification> {
    private static String TYPE_NodeChanged = "NodeChanged";             // 节点变更
    private static String TYPE_TopicReply = "TopicReply";               // Topic 回复
    private static String TYPE_NewsReply = "Hacknews";                  // News  回复
    private static String TYPE_Mention = "Mention";                     // 有人提及
    private static String MENTION_TYPE_TopicReply = "Reply";            // - Topic 回复中提及
    private static String MENTION_TYPE_NewReply = "HacknewsReply";      // - News  回复中提及
    private static String MENTION_TYPE_ProjectReply = "ProjectReply";   // - 项目   回复中提及

    public NotificationsProvider(@NonNull Context context) {
        super(context, R.layout.item_notification);
    }

    @Override public void onBindView(RecyclerViewHolder holder, Notification bean) {

        final User actor = bean.getActor();
        String suffix = "";
        String desc = "";
        int topic_id = -1;
        if (bean.getType().equals(TYPE_TopicReply)) {
            suffix = "回复了话题：";
            desc = bean.getReply().getTopic_title();
            topic_id = bean.getReply().getTopic_id();
        } else if (bean.getType().equals(TYPE_Mention) && bean.getMention_type().equals
                (MENTION_TYPE_TopicReply)) {
            suffix = "提到了你:";
            desc = bean.getMention().getBody_html();
            topic_id = bean.getMention().getTopic_id();
        }
        String type = actor.getLogin() + " " + suffix;

        ImageView imageView = holder.get(R.id.avatar);
        ImageUtils.loadImage(mContext, actor.getAvatar_url(), imageView);
        holder.setText(R.id.notification_type, type);

        Spanned result_desc = Html.fromHtml(HtmlUtil.removeP(desc));
        TextView text_desc = holder.get(R.id.desc);
        text_desc.setText(result_desc);

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserActivity.newInstance(mContext, actor);
            }
        }, R.id.avatar, R.id.notification_type);

        final int final_topic_id = topic_id;
        holder.get(R.id.item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopicContentActivity.newInstance(mContext, final_topic_id);
            }
        });
    }


}
