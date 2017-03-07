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

package com.gcssloop.diycode_sdk.api.user.bean;

import java.io.Serializable;

/**
 * 用户详细信息
 */
public class UserDetail implements Serializable {

    private int id;                     // ID
    private String login;               // 用户名
    private String name;                // 昵称
    private String avatar_url;          // 头像链接
    private String location;            // 城市
    private String company;             // 公司
    private String twitter;             // twitter
    private String website;             // 网站地址
    private String bio;                 // 个人介绍
    private String tagline;             // 签名
    private String github;              // github
    private String created_at;          // 创建时间
    private String email;               // email
    private int topics_count;           // 话题数量
    private int replies_count;          // 回复数量
    private int following_count;        // 正在 follow 的人数
    private int followers_count;        // follow 他的人数
    private int favorites_count;        // 收藏的数量
    private String level;               // 等级
    private String level_name;          // 等级名称

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return this.login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getAvatar_url() {
        return this.avatar_url;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return this.location;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return this.company;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getTwitter() {
        return this.twitter;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWebsite() {
        return this.website;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBio() {
        return this.bio;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getTagline() {
        return this.tagline;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getGithub() {
        return this.github;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setTopics_count(int topics_count) {
        this.topics_count = topics_count;
    }

    public int getTopics_count() {
        return this.topics_count;
    }

    public void setReplies_count(int replies_count) {
        this.replies_count = replies_count;
    }

    public int getReplies_count() {
        return this.replies_count;
    }

    public void setFollowing_count(int following_count) {
        this.following_count = following_count;
    }

    public int getFollowing_count() {
        return this.following_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public int getFollowers_count() {
        return this.followers_count;
    }

    public void setFavorites_count(int favorites_count) {
        this.favorites_count = favorites_count;
    }

    public int getFavorites_count() {
        return this.favorites_count;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }

    public String getLevel_name() {
        return this.level_name;
    }
}
