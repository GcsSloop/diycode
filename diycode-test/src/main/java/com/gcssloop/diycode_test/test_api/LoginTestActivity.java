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

package com.gcssloop.diycode_test.test_api;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gcssloop.diycode_sdk.api.bean.Token;
import com.gcssloop.diycode_sdk.api.event.LoginEvent;
import com.gcssloop.diycode_test.R;
import com.gcssloop.diycode_test.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginTestActivity extends BaseActivity {


    @BindView(R.id.edit_name)
    EditText name;

    @BindView(R.id.edit_password)
    EditText password;

    @BindView(R.id.text_state)
    TextView text_state;

    @OnClick(R.id.login)
    public void login(View view) {
        mDiycode.login(name.getText().toString(), password.getText().toString());
    }

    @OnClick(R.id.login_right)
    public void login_right(View view) {
        mDiycode.login("diytest", "slooptest");
    }

    @OnClick(R.id.login_error)
    public void login_error(View view) {
        mDiycode.login("diytest", "slooptest1");
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event) {
        String state = "当前状态：";
        if (event.isOk()) {
            Token token = event.getToken();
            Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
            state = state + "成功\n"
                    + "state         = " + event.getState() + "\n"
                    + "state message = " + event.getStateMsg() + "\n"
                    + "token type    = " + token.getTokenType() + "\n"
                    + "created at    = " + token.getCreatedAt() + "\n"
                    + "expires in    = " + token.getExpiresIn() + "\n"
                    + "access token  = " + token.getAccessToken() + "\n"
                    + "refresh token = " + token.getRefreshToken() + "\n";
        } else {
            Toast.makeText(this, "登录失败", Toast.LENGTH_LONG).show();

            state = state + "失败\n"
                    + "state         = " + event.getState() + "\n"
                    + "state message = " + event.getStateMsg() + "\n";
        }

        text_state.setText(state);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_test);
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
