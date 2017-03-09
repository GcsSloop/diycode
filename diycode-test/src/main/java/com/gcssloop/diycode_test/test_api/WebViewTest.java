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
 * Last modified 2017-03-08 02:50:32
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode_test.test_api;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gcssloop.diycode_test.R;
import com.gcssloop.diycode_test.widget.MarkdownView;

public class WebViewTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_test);

        MarkdownView markdownView = (MarkdownView) findViewById(R.id.markdown_view);
        markdownView.setMarkDownText("## Markdown \n" +
                "![](http://www.gcssloop.com/assets/siteinfo/friends/gcssloop.jpg)");

     //   markdownView.setMarkDownText("## Markdown2 \n" +
     //           "![](http://www.gcssloop.com/assets/siteinfo/friends/gcssloop中文.jpg)");
    }
}
