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
 * Last modified 2017-03-27 18:42:04
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 计算时间工具
 */
public class TimeUtil {
    private static final String TAG = "TimeUtil";

    public static String computePastTime(String time) {
        // Log.v(TAG, "computePastTime: " + time);
        String result = "刚刚";
        //2017-02-13T01:20:13.035+08:00
        time = time.replace("T", " ");
        time = time.substring(0, 22);
        // Log.v(TAG, "computePastTime time: " + time);
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.SIMPLIFIED_CHINESE);
        try {
            Date t = simpleDateFormat.parse(time);
            Date now = new Date(System.currentTimeMillis());
            long diff = (now.getTime() - t.getTime()) / 1000;
            if (diff < 60) {
                result = "刚刚";
            } else if ((diff /= 60) < 60) {
                result = diff + "分钟前";
            } else if ((diff /= 60) < 24) {
                result = diff + "小时前";
            } else if ((diff /= 24) < 30) {
                result = diff + "天前";
            } else if ((diff /= 30) < 12) {
                result = diff + "月前";
            } else {
                diff /= 12;
                result = diff + "年前";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Log.v(TAG, "computePastTime result: " + result);
        return result;
    }

    public static String formatTime(String time) {
        // Log.v(TAG, "formatTime: " + time);
        //2017-02-13T01:20:13.035+08:00
        time = time.replace("T", " ");
        time = time.substring(0, 16);
        // Log.v(TAG, "formatTime result: " + time);
        return time;
    }
}
