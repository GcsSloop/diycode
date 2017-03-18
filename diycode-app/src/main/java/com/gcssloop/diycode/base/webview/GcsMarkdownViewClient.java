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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.gcssloop.diycode_sdk.log.Logger;

import java.io.FileInputStream;

import static com.gcssloop.diycode.utils.UrlUtil.getMimeType;
import static com.gcssloop.diycode.utils.UrlUtil.isGifSuffix;
import static com.gcssloop.diycode.utils.UrlUtil.isImageSuffix;

/**
 * 自定义 web client， 做一些不可描述的事情
 */
public class GcsMarkdownViewClient extends WebViewClient {
    public static final String URL = "url";
    private Class<? extends Activity> mWebActivity = null;
    private boolean mIsOpenUrlInBrowser = false;
    private Context mContext;
    private DiskImageCache mCache;


    public GcsMarkdownViewClient(@NonNull Context context) {
        mContext = context;
        mCache = new DiskImageCache(context);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        addImageClickListener(view);
        super.onPageFinished(view, url);
    }

    /**
     * 注入js函数监听
     * <p>
     * 原理是：循环扫描所有 img 节点，将链接提取出来，并且注入监听函数
     *
     * @param webView
     */
    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    private <Web extends WebView> void addImageClickListener(Web webView) {
        // 遍历所有签过名的图片，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByClassName(\"gcs-img-sign\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{" +
                "  window.listener.collectImage(objs[i].src); " +
                "  objs[i].onclick=function()  " +
                "  {  " +
                "    window.listener.onImageClicked(this.src);  " +
                "  }  " +
                "}" +
                "})()");
    }


    //--- html 链接打开方式 -----------------------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return handleLink(request.getUrl().toString());
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return handleLink(url);
    }

    /**
     * 处理超链接
     *
     * @param url 超链接
     */
    private boolean handleLink(String url) {
        Logger.e("handleLink" + url);
        if (null != mWebActivity) {
            Intent intent = new Intent(mContext, mWebActivity);
            intent.putExtra(URL, url);
            mContext.startActivity(intent);
            return true;
        }
        if (mIsOpenUrlInBrowser) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            mContext.startActivity(intent);
            return true;
        }
        return false;
    }

    //--- 监听加载了哪些资源 -----------------------------------------------------------------------

    @Override
    public void onLoadResource(WebView view, final String url) {
        Logger.e("加载资源：" + url);
        // 是图片就缓存
        if (isImageSuffix(url)) {
            Glide.with(mContext).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    mCache.saveBitmap(url, resource);
                }
            });
        } else if (isGifSuffix(url)) {
            Glide.with(mContext).load(url).asGif().into(new SimpleTarget<GifDrawable>() {
                @Override
                public void onResourceReady(GifDrawable resource, GlideAnimation<? super GifDrawable> glideAnimation) {
                    mCache.saveBytes(url, resource.getData());
                }
            });
        }
    }

    //--- 请求资源 -------------------------------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        WebResourceResponse response = getWebResourceResponse(url);
        if (response != null) return response;
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        WebResourceResponse response = getWebResourceResponse(url);
        if (response != null) return response;
        return super.shouldInterceptRequest(view, url);
    }

    /**
     * 获取本地资源
     */
    @Nullable
    private WebResourceResponse getWebResourceResponse(String url) {
        try {
            // 如果是图片且本地有缓存
            if (mCache.hasCache(url)) {
                FileInputStream inputStream = mCache.getStream(url);
                if (null != inputStream) {
                    return new WebResourceResponse(getMimeType(url), "base64", inputStream);
                }
            }
            /*
            if (isImageSuffix(url) || isGifSuffix(url)) {
                FileInputStream inputStream = mCache.getStream(url);
                if (null != inputStream) {
                    return new WebResourceResponse(getMimeType(url), "base64", inputStream);
                }
            }
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //--- 设置 -----------------------------------------------------------------------------------

    /**
     * 设置链接在哪个 Activity 打开
     *
     * @param webActivity 包含 WebView 的 Activity
     */
    public void setWebActivity(Class<? extends Activity> webActivity) {
        this.mWebActivity = webActivity;
    }

    /**
     * 判断链接是否是使用 浏览器 打开
     */
    public boolean isOpenUrlInBrowser() {
        return mIsOpenUrlInBrowser;
    }

    /**
     * 设置链接打开方式
     *
     * @param openUrlInBrowser true 使用浏览器打开，false 在当前打开
     */
    public void setOpenUrlInBrowser(boolean openUrlInBrowser) {
        mIsOpenUrlInBrowser = openUrlInBrowser;
    }
}
