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
 * Last modified 2017-02-26 03:37:14
 *
 */

package com.gcssloop.diycode.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.gcssloop.diycode_sdk.api.Diycode;

public abstract class BaseActivity extends AppCompatActivity {

    protected Diycode mDiycode;
    protected ViewHolder mViewHolder;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDiycode = Diycode.getSingleInstance();
        mViewHolder = new ViewHolder(getLayoutInflater(), null, getLayoutId());
        setContentView(mViewHolder.getRootView());
        initViews(mViewHolder, mViewHolder.getRootView());
    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initViews(ViewHolder holder, View root);

    public ViewHolder getViewHolder() {
        return mViewHolder;
    }

    /**
     * 发出一个短Toast
     *
     * @param text 内容
     */
    public void toastShort(String text) {
        toast(text, Toast.LENGTH_SHORT);
    }

    /**
     * 发出一个长toast提醒
     *
     * @param text 内容
     */
    public void toastLong(String text) {
        toast(text, Toast.LENGTH_LONG);
    }


    private void toast(final String text, final int duration) {
        if (!TextUtils.isEmpty(text)) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mToast == null) {
                        mToast = Toast.makeText(getApplicationContext(), text, duration);
                    } else {
                        mToast.setText(text);
                        mToast.setDuration(duration);
                    }
                    mToast.show();
                }
            });
        }
    }
}
