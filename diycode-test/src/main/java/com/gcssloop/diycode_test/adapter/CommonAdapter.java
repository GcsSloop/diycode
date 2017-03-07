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

package com.gcssloop.diycode_test.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<T> mDatas = new ArrayList<>();
    private int mLayoutId;

    /**
     * @param context  上下文
     * @param datas    数据集
     * @param layoutId 布局ID
     */
    public CommonAdapter(@NonNull Context context, List<T> datas, @NonNull int layoutId) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mLayoutId = layoutId;
        if(datas!=null){
            this.mDatas = datas;
        }
    }

    public void addDatas(List<T> datas){
        this.mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void clearDatas(){
        this.mDatas.clear();
        notifyDataSetChanged();
    }

    public T getDataById(int position){
       return mDatas.get(position);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //实例化一个ViewHolder
        ViewHolder holder = ViewHolder.getInstance(mContext, convertView, parent, mLayoutId, position);
        //需要自定义的部分
        convert(position, holder, getItem(position));

        return holder.getConvertView();
    }

    /**
     * 需要处理的部分，在这里给View设置值
     *
     * @param holder ViewHolder
     * @param bean   数据集
     */
    public abstract void convert(int position, ViewHolder holder, T bean);
}