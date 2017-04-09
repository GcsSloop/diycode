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
 * Last modified 2017-04-08 23:04:45
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.fragment.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gcssloop.diycode.R;
import com.gcssloop.diycode.activity.TopicActivity;
import com.gcssloop.diycode.activity.TopicContentActivity;
import com.gcssloop.diycode.activity.UserActivity;
import com.gcssloop.diycode.utils.TimeUtil;
import com.gcssloop.diycode_sdk.api.topic.bean.Topic;
import com.gcssloop.diycode_sdk.api.user.bean.User;
import com.gcssloop.recyclerview.adapter.base.RecyclerViewHolder;
import com.gcssloop.recyclerview.adapter.multitype.BaseViewProvider;

public class TopicProvider extends BaseViewProvider<Topic> {

    public TopicProvider(@NonNull Context context) {
        super(context, R.layout.item_topic);
    }

    /**
     * 在绑定数据时调用，需要用户自己处理
     *
     * @param holder ViewHolder
     * @param bean   数据
     */
    @Override
    public void onBindView(RecyclerViewHolder holder, final Topic bean) {
        final User user = bean.getUser();
        holder.setText(R.id.username, user.getLogin());
        holder.setText(R.id.node_name, bean.getNode_name());
        holder.setText(R.id.time, TimeUtil.computePastTime(bean.getUpdated_at()));
        holder.setText(R.id.title, bean.getTitle());

        // 加载头像
        ImageView imageView = holder.get(R.id.avatar);
        String url = user.getAvatar_url();
        String url2 = url;
        if (url.contains("diycode"))    // 添加判断，防止替换掉其他网站掉图片
            url2 = url.replace("large_avatar", "avatar");
        Glide.with(mContext).load(url2).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageView);

        String state = "评论 " + bean.getReplies_count();
        holder.setText(R.id.state, state);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.avatar:
                    case R.id.username:
                        UserActivity.newInstance(mContext, user);
                        break;
                    case R.id.item:
                        TopicContentActivity.newInstance(mContext, bean);
                        break;
                    case R.id.node_name:
                        TopicActivity.newInstance(mContext, bean.getNode_id(), bean.getNode_name());
                        break;
                }
            }
        };

        holder.setOnClickListener(listener, R.id.avatar, R.id.username, R.id.item, R.id.node_name);
    }
}
