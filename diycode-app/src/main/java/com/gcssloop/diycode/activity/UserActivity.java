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

package com.gcssloop.diycode.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcssloop.diycode.R;
import com.gcssloop.diycode.base.app.BaseActivity;
import com.gcssloop.diycode.base.app.ViewHolder;
import com.gcssloop.diycode.utils.ImageUtils;
import com.gcssloop.diycode.utils.RecyclerViewUtil;
import com.gcssloop.diycode_sdk.api.topic.bean.Topic;
import com.gcssloop.diycode_sdk.api.user.bean.User;
import com.gcssloop.diycode_sdk.api.user.bean.UserDetail;
import com.gcssloop.diycode_sdk.api.user.event.GetUserCreateTopicListEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserEvent;
import com.gcssloop.diycode_sdk.log.Logger;
import com.gcssloop.diycode.utils.TimeUtil;
import com.gcssloop.recyclerview.adapter.base.RecyclerViewHolder;
import com.gcssloop.recyclerview.adapter.singletype.SingleTypeAdapter;
import com.gcssloop.view.utils.DensityUtils;
import com.github.florent37.expectanim.ExpectAnim;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static com.gcssloop.diycode.R.id.recycler_view;
import static com.github.florent37.expectanim.core.Expectations.alpha;
import static com.github.florent37.expectanim.core.Expectations.height;
import static com.github.florent37.expectanim.core.Expectations.leftOfParent;
import static com.github.florent37.expectanim.core.Expectations.sameCenterVerticalAs;
import static com.github.florent37.expectanim.core.Expectations.scale;
import static com.github.florent37.expectanim.core.Expectations.toRightOf;
import static com.github.florent37.expectanim.core.Expectations.topOfParent;

/**
 * 展示用户信息
 */
public class UserActivity extends BaseActivity implements View.OnClickListener {
    public static String TYPE_DEFAULT = "default";
    public static String TYPE_USER_CREATE = "user_create";
    public static String TYPE_USER_REPLY = "user_reply";
    public static String USER = "user";
    private ExpectAnim expectAnimMove;
    private SingleTypeAdapter<Topic> mAdapter;

    public static void newInstance(@NonNull Context context, @NonNull User user) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(USER, user);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        initUserInfo(holder);
        initRecyclerView(holder);
        initScrollAnimation(holder);
    }

    private void initUserInfo(ViewHolder holder) {
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra(USER);
        if (null != user) {
            setTitle(user.getLogin());
            holder.setText(user.getName(), R.id.username);
            holder.loadImage(this, user.getAvatar_url(), R.id.avatar);
            mDiycode.getUser(user.getLogin());
        } else {
        }
    }

    private void initRecyclerView(ViewHolder holder) {
        mAdapter = new SingleTypeAdapter<Topic>(this, R.layout.item_topic) {
            /**
             * 在此处处理数据
             *
             * @param position 位置
             * @param holder   view holder
             * @param bean     数据
             */
            @Override public void convert(int position, RecyclerViewHolder holder, final Topic bean) {
                final User user = bean.getUser();
                holder.setText(R.id.username, user.getLogin());
                holder.setText(R.id.node_name, bean.getNode_name());
                holder.setText(R.id.time, TimeUtil.computePastTime(bean.getUpdated_at()));
                holder.setText(R.id.title, bean.getTitle());
                ImageView avatar = holder.get(R.id.avatar);
                ImageUtils.loadImage(mContext, user.getAvatar_url(), avatar);
                holder.get(R.id.item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TopicContentActivity.newInstance(mContext, bean);
                    }
                });
                holder.get(R.id.node_name).setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        TopicActivity.newInstance(mContext, bean.getNode_id(), bean.getNode_name());
                    }
                });
            }
        };

        RecyclerView recyclerView = holder.get(recycler_view);
        RecyclerViewUtil.init(this, recyclerView, mAdapter);
    }

    private void initScrollAnimation(ViewHolder holder) {
        NestedScrollView scrollView = holder.get(R.id.scroll_view);
        ImageView avatar = holder.get(R.id.avatar);
        TextView username = holder.get(R.id.username);
        View backbground = holder.get(R.id.background);

        this.expectAnimMove = new ExpectAnim()
                .expect(avatar)
                .toBe(
                        topOfParent().withMarginDp(13),
                        leftOfParent().withMarginDp(13),
                        scale(0.5f, 0.5f)
                )
                .expect(username)
                .toBe(
                        toRightOf(avatar).withMarginDp(16),
                        sameCenterVerticalAs(avatar),
                        alpha(0.5f)
                )
                .expect(backbground)
                .toBe(
                        height(DensityUtils.dip2px(this, 60)).withGravity(Gravity.LEFT, Gravity.TOP)
                )
                .toAnimation();

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int
                    oldScrollX, int oldScrollY) {
                final float percent = (scrollY * 1f) / v.getMaxScrollAmount();
                expectAnimMove.setPercent(percent);
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUser(GetUserEvent event) {
        if (event.isOk()) {
            UserDetail user = event.getBean();
            mDiycode.getUserCreateTopicList(user.getLogin(), null, 0, user.getTopics_count());
            Logger.e("返回 user 成功");
        } else {
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTopicList(GetUserCreateTopicListEvent event) {
        if (event.isOk()) {
            List<Topic> topics = event.getBean();
            mAdapter.addDatas(topics);
        } else {
        }
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

    @Override
    public void onClick(View v) {
    }
}
