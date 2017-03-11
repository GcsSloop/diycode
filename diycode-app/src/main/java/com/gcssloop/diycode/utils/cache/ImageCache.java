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
 * Last modified 2017-03-11 02:02:44
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.utils.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;

import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class ImageCache {
    private static final long MAX_SIZE = 1000 * 1000 * 100; // 100 mb
    DiskLruCache mCache;

    public ImageCache(Context context) {
        try {
            mCache = DiskLruCache.open(getDiskCacheDir(context, "web-image"), getAppVersion(context), 1, MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void put(String key, Bitmap bitmap) {
        String realKey = hashKey(key);
        DiskLruCache.Editor editor = null;
        OutputStream outputStream = null;
        try {
            editor = mCache.edit(hashKey(realKey));
            if (null != editor) {
                outputStream = editor.newOutputStream(0);
                outputStream.write(convertBitmap2Bytes(bitmap));
                outputStream.flush();
                editor.commit();
            }
            mCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void put(String key, GifDrawable gif) {
        String realKey = hashKey(key);
        DiskLruCache.Editor editor = null;
        OutputStream outputStream = null;
        try {
            editor = mCache.edit(hashKey(realKey));
            if (null != editor) {
                outputStream = editor.newOutputStream(0);
                outputStream.write(gif.getData());
                outputStream.flush();
                editor.commit();
            }
            mCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public InputStream getStream(String key) {
        String realKey = hashKey(key);
        try {
            return  mCache.get(realKey).getInputStream(0);
            /*
            DiskLruCache.Snapshot snapshot = mCache.get(realKey);
            return snapshot.getInputStream(0);
            */
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private byte[] convertBitmap2Bytes(Bitmap bitmap) {
        // 将图片转换为 bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 获取 app 版本号
     */
    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 获取磁盘缓存文件夹 优先获取外置磁盘
     *
     * @param context    上下文
     * @param uniqueName 自定义名字
     */
    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 哈希编码
     */
    public String hashKey(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
