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
 * Last modified 2017-04-09 20:47:16
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.gcssloop.diycode.R;
import com.gcssloop.diycode.base.app.BaseActivity;
import com.gcssloop.diycode.base.app.ViewHolder;
import com.gcssloop.diycode.fragment.UserCollectionTopicFragment;
import com.gcssloop.diycode.fragment.UserCreateTopicFragment;
import com.gcssloop.diycode.utils.DataCache;
import com.gcssloop.diycode_sdk.api.Diycode;
import com.gcssloop.diycode_sdk.api.user.bean.UserDetail;

import java.io.IOException;

public class MyTopicActivity extends BaseActivity {
    private DataCache mDataCache;

    // 数据类型
    enum InfoType {
        MY_TOPIC, MY_COLLECT
    }

    private InfoType current_type = InfoType.MY_TOPIC;

    public static void newInstance(Context context, InfoType type) {
        Intent intent = new Intent(context, MyTopicActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void initDatas() {
        mDiycode = Diycode.getSingleInstance();
        mDataCache = new DataCache(this);
        Intent intent = getIntent();
        InfoType type = (InfoType) intent.getSerializableExtra("type");
        if (type != null) {
            current_type = type;
        } else {
            current_type = InfoType.MY_TOPIC;
        }
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        if (!mDiycode.isLogin()) {
            toastShort("用户未登录");
            return;
        }

        // 获取用户名
        if (mDataCache.getMe() == null) {
            try {
                UserDetail me = mDiycode.getMeNow();
                mDataCache.saveMe(me);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String username = mDataCache.getMe().getLogin();

        // 初始化 Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (current_type == InfoType.MY_COLLECT){
            setTitle("我的收藏");
            UserCollectionTopicFragment fragment1 = UserCollectionTopicFragment.newInstance(username);
            transaction.add(R.id.fragment, fragment1);
        } else if (current_type == InfoType.MY_TOPIC){
            setTitle("我的话题");
            UserCreateTopicFragment fragment2 = UserCreateTopicFragment.newInstance(username);
            transaction.add(R.id.fragment, fragment2);
        }
        transaction.commit();
    }
}
