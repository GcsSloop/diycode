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
 * Last modified 2017-04-08 16:14:18
 *
 * GitHub: https://github.com/GcsSloop
 * WeiBo: http://weibo.com/GcsSloop
 * WebSite: http://www.gcssloop.com
 */

package com.gcssloop.recyclerview.adapter.multitype;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * 类型池
 */
public interface TypePool {

    public void register(@NonNull Class<?> clazz, @NonNull BaseViewProvider provider);

    public int indexOf(@NonNull final Class<?> clazz);

    public List<BaseViewProvider> getProviders();

    public BaseViewProvider getProviderByIndex(int index);

    public <T extends BaseViewProvider> T getProviderByClass(@NonNull final Class<?> clazz);
}
