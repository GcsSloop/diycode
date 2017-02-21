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
 * Last modified 2017-02-21 20:31:51
 *
 */

package com.gcssloop.diycode_test.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 转化工具
 */
public class ConvertUtil {

    /**
     * String 转化为 Integer
     *
     * @param str 字符串
     * @return 转化成功返回一个 Integer，转化失败则返回 null
     */
    public static Integer StringToInteger(@Nullable String str) {
        if (null == str || str.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * String 转化为 Integet
     *
     * @param str           字符串
     * @param default_value 默认数值
     * @return 转化成功返回一个 Integer，转化失败则返回默认数值
     */
    public static Integer StringToInteger(@Nullable String str, @NonNull Integer default_value) {
        if (null == str || str.isEmpty()) {
            return default_value;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return default_value;
        }
    }
}
