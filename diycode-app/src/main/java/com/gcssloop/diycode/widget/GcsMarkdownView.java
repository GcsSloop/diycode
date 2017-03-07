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

package com.gcssloop.diycode.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.ArrayList;

public class GcsMarkdownView extends WebView {
    private static final String TAG = GcsMarkdownView.class.getSimpleName();
    private static final String IMAGE_PATTERN = "!\\[(.*)\\]\\((.*)\\)";

    private ArrayList<String> images_url;

    public GcsMarkdownView(Context context) {
        this(context, null);
    }

    public GcsMarkdownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GcsMarkdownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    private void init() {
        images_url = new ArrayList<>();

        // 状态设置
        setClickable(false);
        setFocusable(false);
        setHorizontalScrollBarEnabled(false);

        WebSettings settings = getSettings();
        // 默认字号
        settings.setDefaultFontSize(14);
        // 设置是否允许缩放
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        // 允许执行 JavaScript
        settings.setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            addJavascriptInterface(new OnWebViewImageListener() {
                @Override @JavascriptInterface
                public void showImagePreview(String bigImageUrl) {
                    if (!TextUtils.isEmpty(bigImageUrl)) {
                    //    Intent intent = new Intent(getContext(), ImageActivity.class);
                    //    intent.putExtra(ImageActivity.URL, bigImageUrl);
                    //    getContext().startActivity(intent);
                    }
                }
            }, "mWebViewImageListener");
        }
    }


    public interface OnWebViewImageListener {

        /**
         * 点击webview上的图片，传入该缩略图的大图Url
         */
        void showImagePreview(String bigImageUrl);

    }
}
