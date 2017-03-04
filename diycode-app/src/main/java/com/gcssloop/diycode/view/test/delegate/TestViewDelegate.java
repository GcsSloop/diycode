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
 * Last modified 2017-03-05 03:09:15
 *
 */

package com.gcssloop.diycode.view.test.delegate;

import android.widget.TextView;

import com.gcssloop.diycode.R;
import com.gcssloop.diycode.view.base.BaseViewDelegate;
import com.gcssloop.diycode.view.test.define.ITestView;

public class TestViewDelegate extends BaseViewDelegate implements ITestView {

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_test;
    }


    @Override
    public void setText(String text) {
        TextView textView = get(R.id.text_test);
        textView.setText(text);
    }
}
