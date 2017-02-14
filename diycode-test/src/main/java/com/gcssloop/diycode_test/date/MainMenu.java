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
 * Last modified 2017-02-11 00:44:14
 *
 */

package com.gcssloop.diycode_test.date;

import com.gcssloop.diycode_test.test_api.LoginTestActivity;
import com.gcssloop.diycode_test.test_api.TokenTestActivity;
import com.gcssloop.diycode_test.test_api.TopicListTestActivity;

import java.util.ArrayList;
import java.util.List;

public class MainMenu {
    public static List<MenuEntity> menu = new ArrayList<MenuEntity>();

    static{
        menu.add(new MenuEntity("登录测试(获取 Token )", LoginTestActivity.class)) ;
        menu.add(new MenuEntity("获取数据测试(Token)", TokenTestActivity.class)) ;
        menu.add(new MenuEntity("获取 topic 列表测试", TopicListTestActivity.class)) ;
    }

    public static class MenuEntity{
        public String info;
        public Class<?> goClass;
        public MenuEntity(String info, Class<?> goClass) {
            this.info=info;
            this.goClass=goClass;
        }
    }
}
