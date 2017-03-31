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
 * Last modified 2017-03-28 04:45:19
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.gcssloop.diycode.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.gcssloop.diycode.activity.WebActivity;
import com.gcssloop.diycode_sdk.log.Logger;

public class IntentUtil {


    /**
     * 打开链接
     * 根据设置判断是用那种方式打开
     *
     * @param context 上下文
     * @param url     url
     */
    public static void openUrl(Context context, String url) {
        if (null == url || url.isEmpty()) {
            Log.i("Diyocde", "Url地址错误");
            return;
        }
        Boolean useInside = true;
        try {
            useInside = Config.getSingleInstance().isUseInsideBrowser();
        } catch (Exception e){
            Logger.e("类型转换错误");
        }
        if (useInside) {
            WebActivity.newInstance(context, url);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        }
    }

    /**
     * 打开支付宝
     */
    public static void openAlipay(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String QRCode = "HTTPS://QR.ALIPAY.COM/FKX07101FYSJGTNCAPQW39";
        intent.setData(Uri.parse("alipayqr://platformapi/startapp?saId=10000007&qrcode=" + QRCode));
        context.startActivity(intent);
    }

}
