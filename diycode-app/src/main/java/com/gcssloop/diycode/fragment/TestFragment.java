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
 * Last modified 2017-03-05 03:03:02
 *
 */

package com.gcssloop.diycode.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gcssloop.diycode.view.test.TestViewDelegate;

public class TestFragment extends android.support.v4.app.Fragment {
    private static final String TYPE = "type";

    public static TestFragment newInstance(@NonNull String type) {
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TestViewDelegate testViewDelegate = new TestViewDelegate();
        testViewDelegate.create(inflater, container, savedInstanceState);
        String type = getArguments().getString(TYPE);
        testViewDelegate.setText(type);
        return testViewDelegate.getRootView();
    }
}
