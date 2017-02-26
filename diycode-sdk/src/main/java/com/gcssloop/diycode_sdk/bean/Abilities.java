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
 * Last modified 2017-02-13 01:08:26
 *
 */

package com.gcssloop.diycode_sdk.bean;

/**
 * 权限控制
 */
public class Abilities {
    private boolean update;

    private boolean destroy;

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean getUpdate() {
        return this.update;
    }

    public void setDestroy(boolean destroy) {
        this.destroy = destroy;
    }

    public boolean getDestroy() {
        return this.destroy;
    }

}