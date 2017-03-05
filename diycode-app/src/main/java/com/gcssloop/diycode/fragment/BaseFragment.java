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
 * Last modified 2017-03-05 06:40:35
 *
 */

package com.gcssloop.diycode.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class BaseFragment extends Fragment {

    protected FragmentViewHolder mViewHolder;

    @LayoutRes
    abstract int getLayoutId();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = new FragmentViewHolder(inflater, container, getLayoutId());
        initViews();
        return mViewHolder.getRootView();
    }

    abstract void initViews();

    public FragmentViewHolder getViewHolder() {
        return mViewHolder;
    }

    public static class FragmentViewHolder {

        private SparseArray<View> mViews;
        private View mRootView;

        public FragmentViewHolder(LayoutInflater inflater, ViewGroup parent, int layoutId) {
            this.mViews = new SparseArray<View>();
            mRootView = inflater.inflate(layoutId, parent, false);
        }

        /**
         * 通过View的id来获取子View
         *
         * @param resId view的id
         * @param <T>   泛型
         * @return 子View
         */
        public <T extends View> T get(int resId) {
            View view = mViews.get(resId);
            //如果该View没有缓存过，则查找View并缓存
            if (view == null) {
                view = mRootView.findViewById(resId);
                mViews.put(resId, view);
            }
            return (T) view;
        }

        /**
         * 获取布局View
         *
         * @return 布局View
         */
        public View getRootView() {
            return mRootView;
        }

        /**
         * 设置文本
         *
         * @param res_id view 的 id
         * @param text   文本内容
         * @return 是否成功
         */
        public boolean setText(@NonNull int res_id, CharSequence text) {
            try {
                TextView textView = get(res_id);
                textView.setText(text);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
