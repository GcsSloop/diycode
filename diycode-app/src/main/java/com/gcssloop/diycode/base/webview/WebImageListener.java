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
 * Last modified 2017-03-18 02:02:46
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.base.webview;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.gcssloop.diycode.base.app.BaseImageActivity;
import com.gcssloop.diycode.utils.UrlUtil;
import com.gcssloop.diycode_sdk.log.Logger;

import java.util.ArrayList;

public class WebImageListener {
    private Context mContext;
    private Class<? extends BaseImageActivity> mImageActivity;
    private ArrayList<String> mImages = new ArrayList<>();

    public WebImageListener(Context context, Class<? extends BaseImageActivity> imageActivity) {
        mContext = context;
        mImageActivity = imageActivity;
    }

    public ArrayList<String> getImages() {
        return mImages;
    }

    /**
     * 收集图片(当发现图片时会调用该方法)
     *
     * @param url 图片链接
     */
    @JavascriptInterface
    public void collectImage(final String url) {
        Logger.e("collect:" + url);
        if (UrlUtil.isGifSuffix(url)){
            return;
        }
        if (!mImages.contains(url))
            mImages.add(url);
    }

    /**
     * 图片被点击(图片被点击时调用该方法)
     *
     * @param url 图片链接
     */
    @JavascriptInterface
    public void onImageClicked(String url) {
        Logger.e("clicked:" + url);
        if (mImageActivity != null) {
            Intent intent = new Intent(mContext, mImageActivity);
            intent.putExtra(BaseImageActivity.CURRENT_IMAGE_URL, url);
            if (!UrlUtil.isGifSuffix(url)){
                intent.putExtra(BaseImageActivity.ALL_IMAGE_URLS, mImages);
            }
            mContext.startActivity(intent);
        }
    }

}
