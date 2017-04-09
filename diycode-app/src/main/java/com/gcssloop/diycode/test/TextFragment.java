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
 * Last modified 2017-04-09 14:15:03
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.gcssloop.diycode.R;
import com.gcssloop.diycode.fragment.base.BaseFragment;
import com.gcssloop.diycode.base.app.ViewHolder;

public class TextFragment extends BaseFragment {
    private static final String TYPE = "type";

    public static TextFragment newInstance(@NonNull String type) {
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        TextFragment fragment = new TextFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_text;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        String text = getArguments().getString(TYPE);
        holder.setText("Gcs:"+text, R.id.text);
    }
}
