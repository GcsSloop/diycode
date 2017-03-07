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

package com.gcssloop.diycode_test.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.gcssloop.diycode_sdk.api.Diycode;
import com.gcssloop.diycode_test.utils.CrashHandler;


public class BaseActivity extends AppCompatActivity {
    protected Context mContext;

    public Diycode mDiycode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle();

        CrashHandler.getInstance().init(this.getApplicationContext());

        // 获取 Diycode
        mDiycode = Diycode.getSingleInstance();

        mContext = this;
    }

    /**
     * 设置 title
     * 如果 intent 传递了 title，就设置为这个 title
     * 如果没有传递，则设置为当前类名
     */
    private void setTitle() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        if (null != title) {
            this.setTitle(title);
        } else {
            this.setTitle(this.getClass().getSimpleName());
        }
    }

    /**
     * 监听重载 ActionBar 左上角按钮事件。
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
