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
 * Last modified 2017-03-08 01:01:18
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode_sdk.api.project.bean;

import java.io.Serializable;

public class Project implements Serializable {
    /**
     * id : 18470
     * name : TodayMind
     * description : 提供了直接在通知中心管理提醒事项的神奇能力的 iOS 开源通知插件
     * readme : # ✏️ TodayMind [中文说明](https://github.com/cyanzhong/TodayMind/blob/master/README_CN.md)
     Make Reminder Great Again!

     ![image](https://raw.githubusercontent.com/cyanzhong/TodayMind/master/Resource/demo.gif)

     # 🤔 What is TodayMind
     `TodayMind` is a tiny project that provides amazing ability to manage `Reminders` directly in Today Widget

     # 🤘 Features
     - Text inputting on widget
     - Full-featured widget with ultimate swipe gestures
     - EventKit usages

     # 📱 Screenshots
     ![image](https://raw.githubusercontent.com/cyanzhong/TodayMind/master/Resource/1.jpg)
     ![image](https://raw.githubusercontent.com/cyanzhong/TodayMind/master/Resource/2.jpg)
     ![image](https://raw.githubusercontent.com/cyanzhong/TodayMind/master/Resource/3.jpg)

     # 🙏 Thanks
     - [SnapKit](https://github.com/SnapKit/SnapKit): An elegant Swift autolayout DSL for iOS & OS X
     - [Jonny](http://weibo.com/u/2813718033): An outstanding iOS developer, he explorered a new way to make widget

     # ⚠️ Tips
     - Some tricks inside the project are **NOT** App Store friendly, so please weigh the pros and cons yourself
     - Besides, please don't upload the project to App Store as a copycat of `TodayMind`, it's not cool

     # ❤️ Donation
     - You can donate to this project by installing it from [App Store](https://itunes.apple.com/app/id1207158665) which is a paid app
     - A better way would be `alipay`, through account: zhongying822@126.com or scanning the following qrcode. Even 1 CNY helps

     ![image](https://raw.githubusercontent.com/cyanzhong/TodayMind/master/Resource/alipay.jpg)

     * github : https://github.com/cyanzhong/TodayMind
     * website : https://itunes.apple.com/app/id1207158665
     * download : https://api.github.com/repos/cyanzhong/TodayMind/zipball
     * star : 460
     * fork : 71
     * watch : 15
     * project_cover_url : https://diycode.b0.upaiyun.com/developer_user/avatar/2872_1488334627.jpg
     * label_str : Reminders,Widget,EventKit
     * category : {"name":"iOS","id":2}
     * sub_category : {"name":"其它(other)","id":44}
     * last_updated_at : 2017-03-01T11:17:51.000+08:00
     */

    private int id;
    private String name;
    private String description;
    private String readme;
    private String github;
    private String website;
    private String download;
    private int star;
    private int fork;
    private int watch;
    private String project_cover_url;
    private String label_str;
    private Category category;
    private SubCategory sub_category;
    private String last_updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReadme() {
        return readme;
    }

    public void setReadme(String readme) {
        this.readme = readme;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getFork() {
        return fork;
    }

    public void setFork(int fork) {
        this.fork = fork;
    }

    public int getWatch() {
        return watch;
    }

    public void setWatch(int watch) {
        this.watch = watch;
    }

    public String getProject_cover_url() {
        return project_cover_url;
    }

    public void setProject_cover_url(String project_cover_url) {
        this.project_cover_url = project_cover_url;
    }

    public String getLabel_str() {
        return label_str;
    }

    public void setLabel_str(String label_str) {
        this.label_str = label_str;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public SubCategory getSub_category() {
        return sub_category;
    }

    public void setSub_category(SubCategory sub_category) {
        this.sub_category = sub_category;
    }

    public String getLast_updated_at() {
        return last_updated_at;
    }

    public void setLast_updated_at(String last_updated_at) {
        this.last_updated_at = last_updated_at;
    }

    public static class Category {
        /**
         * name : iOS
         * id : 2
         */

        private String name;
        private int id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class SubCategory {
        /**
         * name : 其它(other)
         * id : 44
         */

        private String name;
        private int id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
