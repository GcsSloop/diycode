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
 * Last modified 2017-03-15 20:02:59
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.base.app;

import android.content.Intent;

import com.gcssloop.diycode_sdk.log.Logger;

import java.util.ArrayList;

public abstract class BaseImageActivity extends BaseActivity {
    public static final String ALL_IMAGE_URLS = "all_images";
    public static final String CURRENT_IMAGE_URL = "current_image";

    protected static final String MODE_NORMAL = "normal";
    protected static final String MODE_ERROR = "error";
    protected String mCurrentMode = MODE_NORMAL;

    protected ArrayList<String> images = new ArrayList<>();     // 所有图片
    protected String current_image_url = null;                  // 当前图片
    protected int current_image_position = 0;                  // 当前图片位置

    @Override
    protected void initDatas() {
        super.initDatas();

        // 初始化图片 url 和 图片集合，保证两个数据都存在
        Intent intent = getIntent();

        // 没有传递当前图片，设置模式为错误
        String imageUrl = intent.getStringExtra(CURRENT_IMAGE_URL);
        if (imageUrl == null || imageUrl.isEmpty()) {
            toastShort("没有传递图片链接");
            mCurrentMode = MODE_ERROR;
            return;
        } else {
            current_image_url = imageUrl;
            mCurrentMode = MODE_NORMAL;
        }

        // 如果集合为空，不处理，返回
        ArrayList<String> temp = intent.getStringArrayListExtra(ALL_IMAGE_URLS);
        if (temp == null || temp.size() <= 0) {
            return;
        }
        Logger.e("result:" + temp.toString());

        // 如果图片集合大于1，而且包括当前图片，记录集合，计算位置
        if ((temp.size() >= 1) && temp.contains(current_image_url)) {
            images = new ArrayList<>(temp);
            Logger.e("result2:" + images.toString());
            current_image_position = images.indexOf(current_image_url);
            return;
        }

        // 记录当前图片，计算位置
        images.clear();
        images.add(current_image_url);
        current_image_position = images.indexOf(current_image_url);
    }
}
