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
 * Last modified 2017-03-09 00:34:31
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode_sdk.api.sites.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gcssloop.diycode_sdk.api.base.callback.BaseCallback;
import com.gcssloop.diycode_sdk.api.base.impl.BaseImpl;
import com.gcssloop.diycode_sdk.api.sites.event.GetSitesEvent;
import com.gcssloop.diycode_sdk.utils.UUIDGenerator;

public class SitesImpl extends BaseImpl<SitesService> implements SitesAPI {
    public SitesImpl(@NonNull Context context) {
        super(context);
    }

    /**
     * 获取 酷站 列表
     *
     * @see GetSitesEvent
     */
    @Override
    public String getSites() {
        String uuid = UUIDGenerator.getUUID();
        mService.getSites().enqueue(new BaseCallback<>(new GetSitesEvent(uuid)));
        return uuid;
    }
}
