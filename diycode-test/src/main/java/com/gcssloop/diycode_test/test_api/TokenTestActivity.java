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
 * Last modified 2017-02-12 17:28:42
 *
 */

package com.gcssloop.diycode_test.test_api;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gcssloop.diycode_sdk.api.test.bean.Hello;
import com.gcssloop.diycode_sdk.api.test.Event.HelloEvent;
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
    public void getHello(View view){
        String uuid = mDiycode.hello(null);
        Log.e("UUDI-hello", uuid);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHelloEvent(HelloEvent event) {
        Hello hello = event.getBean();
        if (event.isOk() && null != hello){
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
