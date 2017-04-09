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
 * Last modified 2017-03-22 04:30:23
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.activity;

import android.view.View;

import com.gcssloop.diycode.R;
import com.gcssloop.diycode.base.app.BaseActivity;
import com.gcssloop.diycode.base.app.ViewHolder;
import com.gcssloop.diycode.utils.AppUtil;
import com.gcssloop.diycode.utils.Config;
import com.gcssloop.diycode.utils.DataCleanManager;
import com.gcssloop.diycode.utils.FileUtil;
import com.gcssloop.diycode.utils.IntentUtil;

import java.io.File;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private Config mConfig;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        setTitle("设置");
        mConfig = Config.getSingleInstance();
        showCacheSize(holder);

        String versionName = AppUtil.getVersionName(this);
        holder.setText(R.id.app_version, versionName);

        if (mDiycode.isLogin()) {
            holder.get(R.id.user).setVisibility(View.VISIBLE);
        } else {
            holder.get(R.id.user).setVisibility(View.GONE);
        }

        holder.setOnClickListener(this, R.id.clear_cache, R.id.logout, R.id.about, R.id.contribute);
    }

    // 显示缓存大小
    private void showCacheSize(ViewHolder holder) {
        try {
            File cacheDir = new File(FileUtil.getExternalCacheDir(this));
            String cacheSize = DataCleanManager.getCacheSize(cacheDir);
            if (!cacheSize.isEmpty()) {
                holder.setText(R.id.cache_size, cacheSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                if (!mDiycode.isLogin())
                    return;
                mDiycode.logout();
                toastShort("退出成功");
                break;
            case R.id.clear_cache:
                DataCleanManager.deleteFolderFile(FileUtil.getExternalCacheDir(this), false);
                showCacheSize(getViewHolder());
                toastShort("清除缓存成功");
                break;
            case R.id.about:
                openActivity(AboutActivity.class);
                break;
            case R.id.contribute:
                IntentUtil.openAlipay(this);
                break;
        }
    }
}
