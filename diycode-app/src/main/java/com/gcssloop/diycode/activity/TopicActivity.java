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
 * Last modified 2017-04-09 19:39:57
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
import com.gcssloop.diycode.fragment.NodeTopicListFragment;

/**
 * 查看不同分类的 Topic
 */
public class TopicActivity extends BaseActivity {
    private static String Key_Node_ID = "Key_Node_ID";
    private static String Key_Node_Name = "Key_Node_Name";

    public static void newInstance(Context context, int nodeId, String nodeName) {
        Intent intent = new Intent(context, TopicActivity.class);
        intent.putExtra(Key_Node_ID, nodeId);
        intent.putExtra(Key_Node_Name, nodeName);
        context.startActivity(intent);
    }

    @Override protected int getLayoutId() {
        return R.layout.activity_fragment;
    }

    @Override protected void initViews(ViewHolder holder, View root) {
        Intent intent = getIntent();
        int NodeId = intent.getIntExtra(Key_Node_ID, 0);
        String NodeName = intent.getStringExtra(Key_Node_Name);
        setTitle(NodeName);

        NodeTopicListFragment fragment = NodeTopicListFragment.newInstance(NodeId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment, fragment);
        transaction.commit();
    }
}
