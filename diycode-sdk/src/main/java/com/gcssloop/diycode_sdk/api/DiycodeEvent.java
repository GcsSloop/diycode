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
 * Last modified 2017-02-03 06:44:43
 *
 */

package com.gcssloop.diycode_sdk.api;

/**
 * 各种 Event，基于 EventBus 3.0
 */
public class DiycodeEvent {

    /**
     * 登录
     */
    public static class LoginEvent {
        public boolean ok;      // 是否登录成功
    }


    /**
     * Topic 列表
     */
    public static class TopicListEvent {
        public boolean ok;      // 是否获取成功

        public int page_index;  // 第几页(分页加载)
        // TODO 数据集合
    }


    /**
     * Topic 内容
     */
    public static class TopicContentEvent {
        public boolean ok;      // 是否获取成功

        public int id;          // 节点id
    }
}
