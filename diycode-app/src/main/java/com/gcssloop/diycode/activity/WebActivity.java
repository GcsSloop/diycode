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
 * Last modified 2017-03-16 22:13:31
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.gcssloop.diycode.R;
import com.gcssloop.diycode.base.app.BaseActivity;
import com.gcssloop.diycode.base.app.ViewHolder;
import com.gcssloop.diycode_sdk.log.Logger;

public class WebActivity extends BaseActivity {
    public static final String URL = "url";
    Toolbar toolbar;

    private WebView mWebView;
    String mUrl;
    ProgressBar progressBar;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    /**
     * 初始化 View， 调用位置在 initDatas 之后
     *
     * @param holder
     * @param root
     */
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initViews(ViewHolder holder, View root) {
        toolbar = holder.get(R.id.toolbar);
        progressBar = holder.get(R.id.progress_bar);

        if (toolbar != null)
            setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        mUrl = intent.getStringExtra(URL);

        if (mUrl == null || mUrl.isEmpty()) {
            return;
        }

        FrameLayout layout = holder.get(R.id.webview_container);
        mWebView = new WebView(this.getApplicationContext());
        mWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(mWebView);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(webChromeClient);

        mWebView.loadUrl(mUrl);
    }

    // 防止内存泄漏
    // in android 5.1(sdk:21) we should invoke this to avoid memory leak
    // see (https://coolpers.github.io/webview/memory/leak/2015/07/16/
    // android-5.1-webview-memory-leak.html)
    public void clearWebViewResource() {
        if (mWebView != null) {
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.setTag(null);
            mWebView.loadUrl("about:blank");
            mWebView.stopLoading();
            mWebView.setWebViewClient(null);
            mWebView.setWebChromeClient(null);
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearWebViewResource();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_web, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_browser:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mUrl));
                startActivity(intent);
                break;
            case R.id.action_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, toolbar.getTitle());
                shareIntent.putExtra(Intent.EXTRA_TEXT, mUrl);
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int progress) {
            if (progress < 100) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(progress);
            } else if (progress == 100) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            toolbar.setTitle(title);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    class WebImageListenerImpl {

        @JavascriptInterface
        public void onImageClicked(String url) {

        }

        @JavascriptInterface
        public void clicked(String url) {
            Logger.e("clicked：" + url);
        }
    }
}
