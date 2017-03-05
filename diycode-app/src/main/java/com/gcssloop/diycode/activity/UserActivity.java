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
 * Last modified 2017-03-06 00:42:28
 *
 */

package com.gcssloop.diycode.activity;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gcssloop.diycode.R;
import com.gcssloop.diycode.base.BaseActivity;
import com.gcssloop.diycode.base.ViewHolder;
import com.gcssloop.diycode.base.adapter.GcsAdapter;
import com.gcssloop.diycode.base.adapter.GcsViewHolder;
import com.gcssloop.diycode_sdk.api.topic.bean.Topic;
import com.gcssloop.diycode_sdk.api.user.bean.User;
import com.gcssloop.diycode_sdk.api.user.bean.UserDetail;
import com.gcssloop.diycode_sdk.api.user.event.GetUserCreateTopicListEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserEvent;
import com.gcssloop.diycode_sdk.utils.TimeUtil;
import com.gcssloop.gcs_log.Logger;
import com.github.florent37.expectanim.ExpectAnim;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static com.gcssloop.diycode.R.id.topic_list;
import static com.gcssloop.diycode.R.id.username;
import static com.github.florent37.expectanim.core.Expectations.alpha;
import static com.github.florent37.expectanim.core.Expectations.height;
import static com.github.florent37.expectanim.core.Expectations.leftOfParent;
import static com.github.florent37.expectanim.core.Expectations.rightOfParent;
import static com.github.florent37.expectanim.core.Expectations.sameCenterVerticalAs;
import static com.github.florent37.expectanim.core.Expectations.scale;
import static com.github.florent37.expectanim.core.Expectations.toRightOf;
import static com.github.florent37.expectanim.core.Expectations.topOfParent;

public class UserActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_user;
    }


    private ExpectAnim expectAnimMove;
    private GcsAdapter<Topic> mAdapter;

    @Override
    protected void initViews(ViewHolder holder, View root) {
        setTitle("User");
        Intent intent = getIntent();
        String name = intent.getStringExtra("username");
        if (null != name){
            holder.setText(username, name);
            mDiycode.getUser(name);
            mDiycode.getUserCreateTopicList(name,null,null,null);
        }

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.e("follow");
            }
        }, R.id.follow);

        mAdapter = new GcsAdapter<Topic>(this, R.layout.item_topic) {
            @Override
            public void convert(int position, GcsViewHolder holder, Topic topic) {
                final User user = topic.getUser();
                holder.setText(R.id.text_username, user.getLogin());
                holder.setText(R.id.text_node, topic.getNode_name());
                holder.setText(R.id.text_time, TimeUtil.computePastTime(topic.getUpdated_at()));
                holder.setText(R.id.text_title, topic.getTitle());

                ImageView avatar = holder.get(R.id.img_avatar);
                Glide.with(UserActivity.this).load(user.getAvatar_url()).into(avatar);
            }
        };

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        RecyclerView recyclerView = holder.get(topic_list);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);


        NestedScrollView scrollView = holder.get(R.id.sv_user_main);
        ImageView avatar = holder.get(R.id.avatar);
        TextView username = holder.get(R.id.username);
        AppCompatButton follow = holder.get(R.id.follow);
        View backbground = holder.get(R.id.background);

        this.expectAnimMove = new ExpectAnim()
                .expect(avatar)
                .toBe(
                        topOfParent().withMarginDp(20),
                        leftOfParent().withMarginDp(20),
                        scale(0.5f, 0.5f)
                )

                .expect(username)
                .toBe(
                        toRightOf(avatar).withMarginDp(16),
                        sameCenterVerticalAs(avatar),

                        alpha(0.5f)
                )

                .expect(follow)
                .toBe(
                        rightOfParent().withMarginDp(20),
                        sameCenterVerticalAs(avatar)
                )

                .expect(backbground)
                .toBe(
                        height(90*3).withGravity(Gravity.LEFT, Gravity.TOP)
                )

                .toAnimation();

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                final float percent = (scrollY * 1f) / v.getMaxScrollAmount();
                expectAnimMove.setPercent(percent);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUser(GetUserEvent event) {
        if (event.isOk()) {
            UserDetail user = event.getBean();
            mViewHolder.setText(R.id.username, user.getLogin()+"("+user.getName()+")");
            ImageView avatar = mViewHolder.get(R.id.avatar);
            Glide.with(this).load(user.getAvatar_url()).into(avatar);
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
}
