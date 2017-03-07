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
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by florentchampigny on 21/02/2017.
 */

public class ViewBackgroundAlphaAnimExpectation extends CustomAnimExpectation {

    private final float alpha;

    public ViewBackgroundAlphaAnimExpectation(float alpha) {
        this.alpha = alpha;
    }


    @Override
    public Animator getAnimator(View viewToMove) {
        final Drawable background = viewToMove.getBackground();
        if (background != null) {
            final ValueAnimator valueAnimator = ValueAnimator.ofFloat(1f, alpha);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    final float value = (float)animation.getAnimatedValue();
                    final int alpha = (int)(255*value);
                    background.setAlpha(alpha);
                }
            });
            return valueAnimator;
        }
        return null;
    }
}
