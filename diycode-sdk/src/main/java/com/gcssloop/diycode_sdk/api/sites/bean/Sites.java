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

package com.gcssloop.diycode_sdk.api.sites.bean;

import java.io.Serializable;
import java.util.List;

public class Sites implements Serializable {
    /**
     * sites : [{"name":"botlist","url":"http://botlist.co","avatar_url":"https://favicon.b0.upaiyun.com/ip2/botlist.co.ico"},{"name":"Producthunt","url":"http://www.producthunt.com","avatar_url":"https://favicon.b0.upaiyun.com/ip2/www.producthunt.com.ico"},{"name":"Hacker News","url":"http://news.ycombinator.com","avatar_url":"https://favicon.b0.upaiyun.com/ip2/news.ycombinator.com.ico"},{"name":"小众软件","url":"http://www.appinn.com","avatar_url":"https://favicon.b0.upaiyun.com/ip2/www.appinn.com.ico"},{"name":"NEXT","url":"http://next.36kr.com","avatar_url":"https://favicon.b0.upaiyun.com/ip2/next.36kr.com.ico"},{"name":"Startup News","url":"http://news.dbanotes.net","avatar_url":"https://favicon.b0.upaiyun.com/ip2/news.dbanotes.net.ico"},{"name":"mindstore","url":"http://mindstore.io","avatar_url":"https://diycode.b0.upaiyun.com/site/2016/4b347afdafb20540e072eae3424dc40d.ico"},{"name":"少数派","url":"http://sspai.com","avatar_url":"https://favicon.b0.upaiyun.com/ip2/sspai.com.ico"},{"name":"leetcode","url":"http://leetcode.com","avatar_url":"https://favicon.b0.upaiyun.com/ip2/leetcode.com.ico"},{"name":"Designernews","url":"http://www.designernews.co","avatar_url":"https://favicon.b0.upaiyun.com/ip2/www.designernews.co.ico"},{"name":"前辈之路","url":"http://www.52cs.org","avatar_url":"https://favicon.b0.upaiyun.com/ip2/www.52cs.org.ico"},{"name":"代码时间","url":"http://codetimecn.com","avatar_url":"https://favicon.b0.upaiyun.com/ip2/codetimecn.com.ico"},{"name":"牛客网","url":"http://www.nowcoder.com","avatar_url":"https://favicon.b0.upaiyun.com/ip2/www.nowcoder.com.ico"},{"name":"pmcaff","url":"http://www.pmcaff.com","avatar_url":"https://favicon.b0.upaiyun.com/ip2/www.pmcaff.com.ico"},{"name":"利器IO","url":"http://liqi.io","avatar_url":"https://diycode.b0.upaiyun.com/site/2016/445f3b1330c26e275eb886e4d777af84.png"},{"name":"湾区日报","url":"http://wanqu.co","avatar_url":"https://diycode.b0.upaiyun.com/site/2016/bfb10ca7c264f60ab57d4001714e3574.png"},{"name":"硅谷密探","url":"http://www.svinsight.com","avatar_url":"https://diycode.b0.upaiyun.com/site/2016/9a96c32771192cc09b2beac1fa8ceded.ico"},{"name":"机器之心","url":"http://www.jiqizhixin.com","avatar_url":"https://favicon.b0.upaiyun.com/ip2/www.jiqizhixin.com.ico"},{"name":"UsePanda","url":"http://usepanda.com","avatar_url":"https://favicon.b0.upaiyun.com/ip2/usepanda.com.ico"}]
     * name : FUN & COOL
     * id : 18
     */

    private String name;
    private int id;
    private List<Site> sites;

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

    public List<Site> getSites() {
        return sites;
    }

    public void setSites(List<Site> sites) {
        this.sites = sites;
    }

    public static class Site implements Serializable {
        /**
         * name : botlist
         * url : http://botlist.co
         * avatar_url : https://favicon.b0.upaiyun.com/ip2/botlist.co.ico
         */

        private String name;
        private String url;
        private String avatar_url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }
    }
}
