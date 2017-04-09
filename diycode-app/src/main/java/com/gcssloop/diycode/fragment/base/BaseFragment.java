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
 * Last modified 2017-04-09 21:16:47
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.fragment.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gcssloop.diycode.base.app.ViewHolder;
import com.gcssloop.diycode.utils.Config;
import com.gcssloop.diycode.utils.DataCache;
import com.gcssloop.diycode_sdk.api.Diycode;

/**
 * 提供基础内容和生命周期控制
 */
public abstract class BaseFragment extends Fragment {
    private ViewHolder mViewHolder;     // View 管理
    // 数据
    protected Config mConfig;         // 配置(状态信息)
    protected Diycode mDiycode;       // 在线(服务器)
    protected DataCache mDataCache;   // 缓存(本地)

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConfig = Config.getSingleInstance();
        mDiycode = Diycode.getSingleInstance();
        mDataCache = new DataCache(getContext());
    }

    @LayoutRes
    protected abstract int getLayoutId();

    public ViewHolder getViewHolder() {
        return mViewHolder;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = new ViewHolder(inflater, container, getLayoutId());
        return mViewHolder.getRootView();
    }

    protected abstract void initViews(ViewHolder holder, View root);

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews(mViewHolder, mViewHolder.getRootView());
    }

    protected void toast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}
