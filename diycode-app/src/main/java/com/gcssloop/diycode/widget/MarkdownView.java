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
 * Last modified 2017-03-08 04:23:44
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MarkdownView extends WebView {
    private static final String TAG = MarkdownView.class.getSimpleName();
    // 带有点击的图片 => 为了防止被转换，提前转化为 html
    // [text ![text](image_url) text](link) => <a href="link" ><img src="image_url" /></a>
    private static final String IMAGE_LINK_PATTERN = "\\[(.*)!\\[(.*)\\]\\((.*)\\)(.*)\\]\\((.*)\\)";
    private static final String IMAGE_LINK_REPLACE = "<a href=\"$5\" >$1<img src=\"$3\" />$4</a>";
    // 纯图片 => 添加点击跳转，方便后期拦截
    // ![text](image_url) => <img class="gcs-img-sign" src="image_url" />
    private static final String IMAGE_PATTERN = "!\\[(.*)\\]\\((.*)\\)";
    private static final String IMAGE_REPLACE = "<img class=\"gcs-img-sign\" src=\"$2\" />";

    private String mPreviewText;

    public MarkdownView(Context context) {
        this(context, null);
    }

    public MarkdownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    public MarkdownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) {
            return;
        }
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        initialize();
    }

    private void initialize() {
        loadUrl("file:///android_asset/html/preview.html");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getSettings().setAllowUniversalAccessFromFileURLs(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        setWebChromeClient(new WebChromeClient() {
            @SuppressLint("JavascriptInterface")
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                        loadUrl(mPreviewText);
                    } else {
                        evaluateJavascript(mPreviewText, null);
                    }
                }
            }
        });
    }

    public void loadMarkdownFromFile(File markdownFile) {
        String mdText = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(markdownFile);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String readText;
            StringBuilder stringBuilder = new StringBuilder();
            while ((readText = bufferedReader.readLine()) != null) {
                stringBuilder.append(readText);
                stringBuilder.append("\n");
            }
            fileInputStream.close();
            mdText = stringBuilder.toString();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException:" + e);
        } catch (IOException e) {
            Log.e(TAG, "IOException:" + e);
        }
        setMarkDownText(mdText);
    }

    public void loadMarkdownFromAssets(String assetsFilePath) {
        try {
            StringBuilder buf = new StringBuilder();
            InputStream json = getContext().getAssets().open(assetsFilePath);
            BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                buf.append(str).append("\n");
            }
            in.close();
            setMarkDownText(buf.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMarkDownText(String markdownText) {
        String injectMdText = injectImageLink(markdownText);
        String escMdText = escapeForText(injectMdText);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mPreviewText = String.format("javascript:preview('%s')", escMdText);
        } else {
            mPreviewText = String.format("preview('%s')", escMdText);
        }
        initialize();
    }

    /**
     * 注入图片链接
     */
    private String injectImageLink(String mdText) {
        // TODO 修复代码区md格式图片被替换问题
        mdText = mdText.replaceAll(IMAGE_LINK_PATTERN, IMAGE_LINK_REPLACE);
        mdText = mdText.replaceAll(IMAGE_PATTERN, IMAGE_REPLACE);
        return mdText;
    }

    private String escapeForText(String mdText) {
        String escText = mdText.replace("\n", "\\\\n");
        escText = escText.replace("'", "\\\'");
        escText = escText.replace("\r", "");
        return escText;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode()) {
            canvas.drawColor(Color.WHITE);
            canvas.translate(canvas.getWidth() / 2, 30);
            Paint paint = new Paint();
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(30);
            paint.setColor(Color.GRAY);
            canvas.drawText("MarkdownView", -30, 0, paint);
        }
    }
}
