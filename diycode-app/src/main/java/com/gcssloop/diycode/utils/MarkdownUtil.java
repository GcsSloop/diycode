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
 * Last modified 2017-03-11 05:37:15
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.utils;

import android.content.Context;

import com.gcssloop.diycode_sdk.log.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownUtil {
    private static final String IMAGE_PATTERN = "!\\[(.*)\\]\\((.*)\\)";
    Base64ImageCache mCache;

    public MarkdownUtil(Context context) {
        mCache = new Base64ImageCache(context);
    }

    // 替 html 中的图片
    public String imgToBase64(String mdText) {
        Logger.e("替换图片 - 开始");
        try {
            Pattern ptn = Pattern.compile(IMAGE_PATTERN);
            Matcher matcher = ptn.matcher(mdText);
            while (matcher.find()) {
                String imgPath = matcher.group(2);
                Logger.e("imgToBase64-imgPath", imgPath);
                String base64Image = mCache.getImg(imgPath);
                // 如果缓存中没有这个图片 直接跳过
                if (null == base64Image || base64Image.isEmpty())
                    continue;
                // 如果获取到了，就替换掉
                   Logger.e("cache-img", base64Image);
                mdText = mdText.replace(imgPath, base64Image);
                //    Logger.e("替换", mdText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mdText;
    }
}
