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
 * Last modified 2017-04-08 16:14:18
 *
 * GitHub: https://github.com/GcsSloop
 * WeiBo: http://weibo.com/GcsSloop
 * WebSite: http://www.gcssloop.com
 */

package com.gcssloop.recyclerview.adapter.multitype;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gcssloop.recyclerview.adapter.base.RecyclerViewHolder;

/**
 * ItemView 的管理者
 */
public abstract class BaseViewProvider<T> {
    private LayoutInflater mInflater;
    private int mLayoutId;

    public BaseViewProvider(@NonNull Context context, @NonNull @LayoutRes int layout_id) {
        mInflater = LayoutInflater.from(context);
        mLayoutId = layout_id;
    }

    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(mLayoutId, parent, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);
        return viewHolder;
    }

    /**
     * 在绑定数据时调用，需要用户自己处理
     *
     * @param holder ViewHolder
     * @param bean   数据
     */
    public abstract void onBindView(RecyclerViewHolder holder, T bean);
}
