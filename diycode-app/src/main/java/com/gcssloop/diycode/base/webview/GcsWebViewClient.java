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
 * Last modified 2017-03-11 22:24:28
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.base.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gcssloop.diycode_sdk.log.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 自定义 web client， 做一些不可描述的事情
 */
public class GcsWebViewClient extends WebViewClient {
    public static final String URL = "url";
    private Class<? extends Activity> mWebActivity = null;
    private boolean mIsOpenUrlInBrowser = false;
    private Context mContext;

    public GcsWebViewClient(@NonNull Context context) {
        mContext = context;
    }

    //--- html 链接打开方式 -----------------------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (mIsOpenUrlInBrowser) {
            Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
            mContext.startActivity(intent);
            return true;
        }
        if (null != mWebActivity) {
            Intent intent = new Intent(mContext, mWebActivity);
            intent.putExtra(URL, request.getUrl().toString());
            mContext.startActivity(intent);
            return true;
        }
        return false;
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (mIsOpenUrlInBrowser) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            mContext.startActivity(intent);
            return true;
        }
        if (null != mWebActivity) {
            Intent intent = new Intent(mContext, mWebActivity);
            intent.putExtra(URL, url);
            mContext.startActivity(intent);
            return true;
        }
        return false;
    }

    //--- 监听加载了哪些资源 -----------------------------------------------------------------------

    @Override
    public void onLoadResource(WebView view, String url) {
        // TODO cache Image?
    }

    //--- 请求资源 -------------------------------------------------------------------------------

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        Logger.e("拦截请求");
        try {
            // 如果是图片且本地有缓存

            if (isImageSuffixCheck(url) || isGifSuffixCheck(url)) {
                //     InputStream inputStream2 = mImageCache.getStream(url);
                File file = new File("/sdcard/a.jpeg");
                InputStream inputStream = new FileInputStream(file);
                WebResourceResponse response = new WebResourceResponse("image/jpeg", "base64", inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.shouldInterceptRequest(view, url);
    }


    //--- 设置 -----------------------------------------------------------------------------------

    public void setWebActivity(Class<? extends Activity> webActivity) {
        this.mWebActivity = webActivity;
    }

    public boolean isOpenUrlInBrowser() {
        return mIsOpenUrlInBrowser;
    }

    public void setOpenUrlInBrowser(boolean openUrlInBrowser) {
        mIsOpenUrlInBrowser = openUrlInBrowser;
    }

    //--- 工具 -----------------------------------------------------------------------------------

    private boolean isUrlPrefix(String text) {
        return text.startsWith("http://") || text.startsWith("https://");
    }

    /**
     * 判断后缀是不是图片类型的
     *
     * @param url url
     */
    private boolean isImageSuffixCheck(String url) {
        return url.endsWith(".png")
                || url.endsWith(".PNG")
                || url.endsWith(".jpg")
                || url.endsWith(".JPG")
                || url.endsWith(".jpeg")
                || url.endsWith(".JPEG");
    }

    /**
     * 判断后缀是不是 GIF
     *
     * @param url url
     */
    private boolean isGifSuffixCheck(String url) {
        return url.endsWith(".gif")
                || url.endsWith(".GIF");
    }

    /**
     * 获取后缀名
     */
    public static String getSuffix(String url) {
        if ((url != null) && (url.length() > 0)) {
            int dot = url.lastIndexOf('.');
            if ((dot > -1) && (dot < (url.length() - 1))) {
                return url.substring(dot + 1);
            }
        }
        return url;
    }

    private String imgEx2BaseType(String text) {
        if (text.endsWith(".png") || text.endsWith(".PNG")) {
            return "data:image/png;base64,";
        } else if (text.endsWith(".jpg") || text.endsWith(".jpeg") || text.endsWith(".JPG") || text.endsWith(".JPEG")) {
            return "data:image/jpg;base64,";
        } else if (text.endsWith(".gif") || text.endsWith(".GIF")) {
            return "data:image/gif;base64,";
        } else {
            return "";
        }
    }
}
