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
 * Last modified 2017-03-28 01:44:34
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.multitype;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * ItemView 的管理者
 */
public abstract class BaseViewProvider<T> {
    private RecyclerViewHolder mViewHolder;
    private LayoutInflater mInflater;
    private int mLayoutId;

    public BaseViewProvider(@NonNull Context context, @NonNull @LayoutRes int layout_id) {
        mInflater = LayoutInflater.from(context);
        mLayoutId = layout_id;
    }

    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(mLayoutId, parent, false);
        mViewHolder = new RecyclerViewHolder(view);
        return mViewHolder;
    }

    /**
     * 在绑定数据时调用，需要用户自己处理
     *
     * @param holder ViewHolder
     * @param bean   数据
     */
    public abstract void onBindView(RecyclerViewHolder holder, T bean);
}
