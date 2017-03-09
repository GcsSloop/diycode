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

package com.gcssloop.diycode_test.test_api;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gcssloop.diycode_sdk.api.base.bean.OAuth;
import com.gcssloop.diycode_sdk.api.login.bean.Token;
import com.gcssloop.diycode_sdk.api.test.bean.Hello;
import com.gcssloop.diycode_sdk.api.test.Event.HelloEvent;
import com.gcssloop.diycode_sdk.api.topic.event.DeleteTopicEvent;
import com.gcssloop.diycode_sdk.log.Logger;
import com.gcssloop.diycode_test.R;
import com.gcssloop.diycode_test.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 测试使用 token 获取数据
 */
public class TokenTestActivity extends BaseActivity {

    @BindView(R.id.text_state)
    TextView text_state;

    @OnClick(R.id.get_hello)
    public void getHello(View view) {
        String uuid = mDiycode.hello(null);
        Log.e("UUDI-hello", uuid);
    }

    @OnClick(R.id.currentToken)
    public void CurrentToken(View view) {
        Token token = mDiycode.getCacheToken();

        String state = "当前状态：";

        if (null != token) {
            state = state + "获取缓存 token 成功\n"
                    + "token type    = " + token.getToken_type() + "\n"
                    + "created at    = " + token.getCreated_at() + "\n"
                    + "expires in    = " + token.getExpires_in() + "\n"
                    + "access token  = " + token.getAccess_token() + "\n"
                    + "refresh token = " + token.getRefresh_token() + "\n";
        } else {
            state = state + "获取缓存 token 失败\n";
        }

        text_state.setText(state);
    }

    @OnClick(R.id.autoRefresh)
    public void AutoRefresh(View view){
        try {
            // 警告！！！
            // 这个参数只用于 debug 模式，需要暂时移除附加在请求上的 token 时调用
            // 在一般情况下，调用这个方法是无效的
            OAuth.removeAutoToken();

            // 故意调用一个没有权限的 api，并移除自动添加的 token 信息
            // 触发 401 强制更新 token
            mDiycode.deleteTopic(651);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteTopic(DeleteTopicEvent event) {
        // 由于 401 错误会被底层进行拦截，拦截后自动附加更新的 token，所以返回结果就变成了 403（没有权限)
        Logger.e("触发 401 强制更新,实际返回值应该为 403，返回结果 => "+event.getCode());

        // 测试结束后立即重置该参数，以防止影响其他 api 的使用
        OAuth.resetAutoToken();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHelloEvent(HelloEvent event) {
        Hello hello = event.getBean();
        if (event.isOk() && null != hello) {
            toast(hello.toString());
            text_state.setText("当前状态：成功\n"
                    + "uuid          = " + event.getUUID() + "\n"
                    + "state         = " + event.getCode() + "\n"
                    + "state message = " + event.getCodeDescribe() + "\n"
                    + hello.toString());
        } else {
            text_state.setText("当前状态：失败\n"
                    + "uuid          = " + event.getUUID() + "\n"
                    + "state         = " + event.getCode() + "\n"
                    + "state message = " + event.getCodeDescribe() + "\n");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_test);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
