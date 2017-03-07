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

package com.github.florent37.expectanim.core.position;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by florentchampigny on 17/02/2017.
 */

public class PositionAnimExpectationOutOfScreen extends PositionAnimExpectation {

    private WindowManager windowManager;
    private int[] gravities;

    public PositionAnimExpectationOutOfScreen(int[] gravities) {
        this.gravities = gravities;
        setForPositionX(true);
        setForPositionY(true);
    }

    private boolean contains(int gravity) {
        for (int g : gravities) {
            if (g == gravity)
                return true;
        }
        return false;
    }

    @Override
    public Float getCalculatedValueX(View viewToMove) {
        if (contains(Gravity.RIGHT) || contains(Gravity.END)) {
            if (windowManager == null) {
                windowManager = (WindowManager) viewToMove.getContext().getSystemService(Context.WINDOW_SERVICE);
            }
            return (float) windowManager.getDefaultDisplay().getWidth();
        } else if (contains(Gravity.LEFT) || contains(Gravity.START)) {
            return -1f * viewToMove.getWidth();
        } else
            return null;
    }

    @Override
    public Float getCalculatedValueY(View viewToMove) {
        if (contains(Gravity.BOTTOM)) {
            if (windowManager == null) {
                windowManager = (WindowManager) viewToMove.getContext().getSystemService(Context.WINDOW_SERVICE);
            }
            return (float) windowManager.getDefaultDisplay().getHeight();
        } else if (contains(Gravity.TOP)) {
            return -1f * viewToMove.getHeight();
        } else
            return null;
    }
}
