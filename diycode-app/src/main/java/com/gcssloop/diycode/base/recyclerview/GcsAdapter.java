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
 * Last modified 2017-03-13 00:19:17
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.base.recyclerview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gcssloop.diycode_sdk.log.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class GcsAdapter<T> extends RecyclerView.Adapter<GcsViewHolder> {

    protected Context mContext;
    private LayoutInflater mInflater;
    private List<T> mDatas = new ArrayList<>();
    private int mLayoutId;

    public GcsAdapter(@NonNull Context context, @LayoutRes int layoutId) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mLayoutId = layoutId;
    }

    @Override
    public GcsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = null;
        try {
            rootView = mInflater.inflate(mLayoutId, parent, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new GcsViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(GcsViewHolder holder, int position) {
        convert(position, holder, mDatas.get(position));
    }

    /**
     * 在此处处理数据
     *
     * @param position 位置
     * @param holder   view holder
     * @param bean     数据
     */
    public abstract void convert(int position, GcsViewHolder holder, T bean);

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void addDatas(List<T> datas) {
        // 消除重复
        Logger.e("清除重复数据");
        for (T data : datas){
            if (!mDatas.contains(data)){
                mDatas.add(data);
            } else {
                Logger.i("过滤掉一条重复数据");
            }
        }
        // this.mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void clearDatas() {
        this.mDatas.clear();
        notifyDataSetChanged();
    }
}
