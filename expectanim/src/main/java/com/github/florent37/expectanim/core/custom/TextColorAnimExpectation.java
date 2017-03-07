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

package com.github.florent37.expectanim.core.custom;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.TextView;

/**
 * Created by florentchampigny on 21/02/2017.
 */

public class TextColorAnimExpectation extends CustomAnimExpectation {

    private final int textColor;

    public TextColorAnimExpectation(int textColor) {
        this.textColor = textColor;
    }


    @Override
    public Animator getAnimator(View viewToMove) {
        if (viewToMove instanceof TextView) {
            final ObjectAnimator objectAnimator = ObjectAnimator.ofInt(viewToMove, "textColor", ((TextView) viewToMove).getCurrentTextColor(), textColor);
            objectAnimator.setEvaluator(new ArgbEvaluator());
            return objectAnimator;
        } else {
            return null;
        }
    }
}
