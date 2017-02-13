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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gcssloop.diycode_sdk.api.base.BaseEvent;
import com.gcssloop.diycode_sdk.api.bean.Hello;

public class HelloEvent extends BaseEvent<Hello> {

    public HelloEvent(@NonNull Integer code, @Nullable Hello hello) {
        super(code, hello);
    }

}
