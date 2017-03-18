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
 * Last modified 2017-03-12 00:56:52
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.base.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;

public class DiskImageCache {
    private static final String CACHE_SUFFIX = ".cache";

    private static final int MB = 1024 * 1024;
    private static final int CACHE_SIZE = 50;   // 缓存占用空间大小
    private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;    // 为 SD 卡保留多少空间

    private File cacheDir;

    public DiskImageCache(Context context) {
        cacheDir = getDiskCacheDir(context, "web-image");
        // 整理缓存
        organizeCache(cacheDir);
    }

    /**
     * 从缓存中获取图片
     **/
    public Bitmap getBitmap(final String key) {
        final String path = getCachePath(key);
        File file = new File(path);
        if (file.exists()) {
            Bitmap bmp = BitmapFactory.decodeFile(path);
            if (bmp == null) {
                file.delete();
            } else {
                updateFileTime(path);
                return bmp;
            }
        }
        return null;
    }

    /**
     * 将图片存入文件缓存
     **/
    public void saveBitmap(String key, Bitmap bm) {
        if (bm == null) {
            return;
        }
        //判断sdcard上的空间
        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            return;     //SD空间不足
        }
        File file = new File(getCachePath(key));
        try {
            file.createNewFile();
            OutputStream outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
            Log.w("ImageFileCache", "FileNotFoundException");
        } catch (IOException e) {
            Log.w("ImageFileCache", "IOException");
        }
    }

    /**
     * 保存 bytes 数据
     *
     * @param key   url
     * @param bytes bytes 数据
     */
    public void saveBytes(String key, byte[] bytes) {
        if (bytes == null) {
            return;
        }
        //判断sdcard上的空间
        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            return;     //SD空间不足
        }
        File file = new File(getCachePath(key));
        try {
            file.createNewFile();
            OutputStream outStream = new FileOutputStream(file);
            outStream.write(bytes);
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
            Log.w("ImageFileCache", "FileNotFoundException");
        } catch (IOException e) {
            Log.w("ImageFileCache", "IOException");
        }
    }

    /**
     * 获取一个本地缓存的输入流
     *
     * @param key url
     * @return FileInputStream
     */
    public FileInputStream getStream(String key) {
        File file = new File(getCachePath(key));
        if (!file.exists())
            return null;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            return inputStream;
        } catch (FileNotFoundException e) {
            Log.e("getStream", "FileNotFoundException");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取本地缓存路径
     *
     * @param key url
     * @return 路径
     */
    public String getDiskPath(String key) {
        File file = new File(getCachePath(key));
        if (!file.exists())
            return null;
        return file.getAbsolutePath();
    }

    /**
     * 是否有缓存
     *
     * @param key url
     * @return 是否有缓存
     */
    public boolean hasCache(String key) {
        File file = new File(getCachePath(key));
        return file.exists();
    }

    /**
     * 计算存储目录下的文件大小，
     * 当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定
     * 那么删除40%最近没有被使用的文件
     */
    private boolean organizeCache(@NonNull File cacheDir) {
        File[] files = cacheDir.listFiles();
        if (files == null) {
            return true;
        }
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return false;
        }

        int dirSize = 0;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(CACHE_SUFFIX)) {
                dirSize += files[i].length();
            }
        }

        if (dirSize > CACHE_SIZE * MB || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            int removeFactor = (int) ((0.4 * files.length) + 1);
            Arrays.sort(files, new FileLastModifSort());
            for (int i = 0; i < removeFactor; i++) {
                if (files[i].getName().contains(CACHE_SUFFIX)) {
                    files[i].delete();
                }
            }
        }

        if (freeSpaceOnSd() <= CACHE_SIZE) {
            return false;
        }

        return true;
    }

    /**
     * 修改文件的最后修改时间
     **/
    public void updateFileTime(String path) {
        File file = new File(path);
        long newModifiedTime = System.currentTimeMillis();
        file.setLastModified(newModifiedTime);
    }

    /**
     * 计算sdcard上的剩余空间
     **/
    private int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
        return (int) sdFreeMB;
    }

    /**
     * 根据文件的最后修改时间进行排序
     */
    private class FileLastModifSort implements Comparator<File> {
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }


    /**
     * 获取缓存文件的绝对路径
     */
    private String getCachePath(String key) {
        return cacheDir.getAbsolutePath() + File.separator + convertKey(key);
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
        File cacheDir = new File(cachePath + File.separator + uniqueName);
        if (!cacheDir.exists())
            cacheDir.mkdir();
        return cacheDir;
    }

    /**
     * 哈希编码
     */
    public String convertKey(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey + CACHE_SUFFIX;
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
