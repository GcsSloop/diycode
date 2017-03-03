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
 * Last modified 2017-03-04 05:14:50
 *
 */

package com.gcssloop.diycode.view.topic.delegate;

import com.gcssloop.diycode.view.base.BaseViewDelegate;
import com.gcssloop.diycode.view.topic.define.ITopicListView;
import com.gcssloop.diycode_sdk.api.topic.bean.Topic;

import java.util.List;

public class TopicListViewDelegate extends BaseViewDelegate implements ITopicListView {

    @Override
    public int getRootLayoutId() {
        return 0;
    }

    /**
     * 显示列表
     *
     * @param topics topics
     */
    @Override
    public void showList(List<Topic> topics) {

    }

    /**
     * 刷新列表
     *
     * @param topics topics
     */
    @Override
    public void refreshList(List<Topic> topics) {

    }

    /**
     * 加载更多内容
     *
     * @param topics topics
     */
    @Override
    public void loadMore(List<Topic> topics) {

    }
}
