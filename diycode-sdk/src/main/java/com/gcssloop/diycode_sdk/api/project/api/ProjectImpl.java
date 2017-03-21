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
 * Last modified 2017-03-09 02:57:32
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode_sdk.api.project.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gcssloop.diycode_sdk.api.base.bean.State;
import com.gcssloop.diycode_sdk.api.base.callback.BaseCallback;
import com.gcssloop.diycode_sdk.api.base.impl.BaseImpl;
import com.gcssloop.diycode_sdk.api.project.event.CreateProjectReplyEvent;
import com.gcssloop.diycode_sdk.api.project.event.DeleteProjectReplyEvent;
import com.gcssloop.diycode_sdk.api.project.event.GetProjectRepliesListEvent;
import com.gcssloop.diycode_sdk.api.project.event.GetProjectReplyEvent;
import com.gcssloop.diycode_sdk.api.project.event.GetProjectsListEvent;
import com.gcssloop.diycode_sdk.api.project.event.UpdateProjectReplyEvent;
import com.gcssloop.diycode_sdk.utils.UUIDGenerator;

public class ProjectImpl extends BaseImpl<ProjectService> implements ProjectAPI {
    public ProjectImpl(@NonNull Context context) {
        super(context);
    }

    //--- project  ---------------------------------------------------------------------------------

    /**
     * 获取 project 列表
     *
     * @param node_id 如果你需要只看某个节点的，请传此参数, 如果不传 则返回全部
     * @param offset  偏移数值，默认值 0
     * @param limit   数量极限，默认值 20，值范围 1..150
     * @see GetProjectsListEvent
     */
    @Override
    public String getProjectsList(Integer node_id, Integer offset, Integer limit) {
        String uuid = UUIDGenerator.getUUID();
        mService.getProjectsList(node_id, offset, limit)
                .enqueue(new BaseCallback<>(new GetProjectsListEvent(uuid)));
        return uuid;
    }

    //--- project reply ----------------------------------------------------------------------------

    /**
     * 获取 project 回复列表
     *
     * @param id     project 的 id
     * @param offset 偏移数值 默认 0
     * @param limit  数量极限，默认值 20，值范围 1...150
     * @see GetProjectRepliesListEvent
     */
    @Override
    public String getProjectRepliesList(int id, Integer offset, Integer limit) {
        String uuid = UUIDGenerator.getUUID();
        mService.getProjectRepliesList(id, offset, limit)
                .enqueue(new BaseCallback<>(new GetProjectRepliesListEvent(uuid)));
        return uuid;
    }

    /**
     * 创建 project 回帖(回复，评论)
     *
     * @param id   话题列表
     * @param body 回帖内容, Markdown 格式
     * @see CreateProjectReplyEvent
     */
    @Override
    public String createProjectReply(int id, String body) {
        String uuid = UUIDGenerator.getUUID();
        mService.createProjectReply(id, body)
                .enqueue(new BaseCallback<>(new CreateProjectReplyEvent(uuid)));
        return uuid;
    }

    /**
     * 获取回帖的详细内容（一般用于编辑回帖的时候）
     *
     * @param id id
     * @see GetProjectReplyEvent
     */
    @Override
    public String getProjectReply(int id) {
        String uuid = UUIDGenerator.getUUID();
        mService.getProjectReply(id).enqueue(new BaseCallback<>(new GetProjectReplyEvent(uuid)));
        return uuid;
    }

    /**
     * 更新回帖
     *
     * @param id   id
     * @param body 回帖详情
     * @see UpdateProjectReplyEvent
     */
    @Override
    public String updateProjectReply(int id, String body) {
        String uuid = UUIDGenerator.getUUID();
        mService.updateProjectReply(id, body)
                .enqueue(new BaseCallback<>(new UpdateProjectReplyEvent(uuid)));
        return uuid;
    }

    /**
     * 删除回帖
     *
     * @param id id
     * @see DeleteProjectReplyEvent
     */
    @Override
    public String deleteProjectReply(int id) {
        String uuid = UUIDGenerator.getUUID();
        mService.deleteProjectReply(id)
                .enqueue(new BaseCallback<State>(new DeleteProjectReplyEvent(uuid)));
        return uuid;
    }
}
