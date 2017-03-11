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
 * Last modified 2017-03-11 22:25:17
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.utils.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.gcssloop.diycode.utils.FileUtil;
import com.gcssloop.diycode_sdk.log.Logger;
import com.gcssloop.diycode_sdk.utils.ACache;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * 数据缓存工具
 */
public class Base64ImageCache {

    ACache cache;

    public Base64ImageCache(Context context) {
        cache = ACache.get(new File(FileUtil.getExternalCacheDir(context.getApplicationContext(), "base64img-cache")));
    }

    public void saveImg(String url, String base64img) {
        cache.put(url, base64img, ACache.TIME_WEEK * 5);     // 数据缓存时间为 5 周，一个月左右
    }

    public void saveImg(String url, Bitmap resource) {
        Logger.e("缓存 图片");
        try {
            // 将图片转换为 bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (url.endsWith(".jpg") || url.endsWith(".JPG")
                    || url.endsWith(".jpeg") || url.endsWith(".JPEG")) {
                resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            } else {
                resource.compress(Bitmap.CompressFormat.PNG, 100, baos);
            }
            byte[] bytes = baos.toByteArray();

            // 转换 图像 类型
            String baseType = imgEx2BaseType(url);
            String base64Img = baseType + Base64.encodeToString(bytes, Base64.NO_WRAP);
            saveImg(url, base64Img);

            // 清除资源
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveImg(String url, GifDrawable resource) {
        Logger.e("保存 gif");
        byte[] bytes = resource.getData();
        String baseType = imgEx2BaseType(url);
        String base64Img = baseType + Base64.encodeToString(bytes, Base64.NO_WRAP);
        saveImg(url, base64Img);
    }


    public String getImg(String url) {
        return cache.getAsString(url);
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
