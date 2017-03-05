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

import com.gcssloop.diycode.R;
import com.gcssloop.diycode.adapter.TopicListAdapter;
import com.gcssloop.diycode.base.BaseActivity;
import com.gcssloop.diycode.base.ViewHolder;
import com.gcssloop.diycode.base.adapter.GcsViewHolder;
import com.gcssloop.diycode_sdk.api.topic.bean.Topic;
import com.gcssloop.diycode_sdk.api.user.bean.User;
import com.gcssloop.diycode_sdk.api.user.bean.UserDetail;
import com.gcssloop.diycode_sdk.api.user.event.GetUserCreateTopicListEvent;
import com.gcssloop.diycode_sdk.api.user.event.GetUserEvent;
import com.gcssloop.gcs_log.Logger;
import com.gcssloop.view.utils.DensityUtils;
import com.github.florent37.expectanim.ExpectAnim;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static com.gcssloop.diycode.R.id.topic_list;
import static com.github.florent37.expectanim.core.Expectations.alpha;
import static com.github.florent37.expectanim.core.Expectations.height;
import static com.github.florent37.expectanim.core.Expectations.leftOfParent;
import static com.github.florent37.expectanim.core.Expectations.rightOfParent;
import static com.github.florent37.expectanim.core.Expectations.sameCenterVerticalAs;
import static com.github.florent37.expectanim.core.Expectations.scale;
import static com.github.florent37.expectanim.core.Expectations.toRightOf;
import static com.github.florent37.expectanim.core.Expectations.topOfParent;

public class UserActivity extends BaseActivity implements View.OnClickListener {
    public static String USER = "user";
    private ExpectAnim expectAnimMove;
    private TopicListAdapter mAdapter;

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
            toastShort("获取用户数据成功");
            holder.setText(user.getLogin() + "(" + user.getName() + ")", R.id.username);
            holder.loadImage(this, user.getAvatar_url(), R.id.avatar);
            mDiycode.getUser(user.getLogin());
            holder.setOnClickListener(this, R.id.follow);
        } else {
            toastShort("未获取到用户数据");
        }
    }

    private void initRecyclerView(ViewHolder holder) {
        mAdapter = new TopicListAdapter(this) {
            @Override
            public void setListener(int position, GcsViewHolder holder, Topic topic) {
                // 设置监听器
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
    }

    private void initScrollAnimation(ViewHolder holder) {
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
                        height(DensityUtils.dip2px(this, 90)).withGravity(Gravity.LEFT, Gravity.TOP)
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
        switch (v.getId()) {
            case R.id.follow:
                Logger.e("follow");
                break;
        }
    }
}
