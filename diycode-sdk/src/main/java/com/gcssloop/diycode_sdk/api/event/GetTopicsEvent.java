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
 * Last modified 2017-02-13 01:55:05
 *
 */

package com.gcssloop.diycode_sdk.api.event;

import com.gcssloop.diycode_sdk.api.bean.Topic;

import java.util.List;

public class GetTopicsEvent {
    private boolean ok = false;     // 是否登录成功
    private Integer state = -1;     // 状态码
    private List<Topic> topics;     // 话题列表

    public GetTopicsEvent(Integer state) {
        this.ok = false;
        this.state = state;
    }

    public GetTopicsEvent(Integer state, List<Topic> topics) {
        this.ok = true;
        this.state = state;
        this.topics = topics;
    }

    public boolean isOk() {
        return ok;
    }

    public Integer getState() {
        return state;
    }

    public List<Topic> getTopics() {
        return topics;
    }
}
