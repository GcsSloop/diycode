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
 * Last modified 2017-02-12 17:44:53
 *
 */

package com.gcssloop.diycode_sdk.api.event;

import com.gcssloop.diycode_sdk.api.bean.Hello;

public class HelloEvent {
    private boolean ok = false;     // 是否登录成功
    private Hello hello;            // Hello bean
    private Integer state = -1;     // 状态码

    public HelloEvent(Integer state) {
        this.ok = false;
        this.state = state;
    }

    public HelloEvent(Integer state, Hello hello) {
        this.ok = true;
        this.state = state;
        this.hello = hello;
    }

    public boolean isOk() {
        return ok;
    }

    public Hello getHello() {
        return hello;
    }

    public Integer getState() {
        return state;
    }
}
