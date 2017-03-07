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

package com.github.florent37.expectanim.core.scale;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.Gravity;
import android.view.View;

import com.github.florent37.expectanim.ViewCalculator;
import com.github.florent37.expectanim.core.AnimExpectation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 17/02/2017.
 */

public class ExpectAnimScaleManager {

    private final List<AnimExpectation> animExpectations;
    private final View viewToMove;
    private final ViewCalculator viewCalculator;

    private Float scaleX = null;
    private Float scaleY = null;

    private Float pivotX = null;
    private Float pivotY = null;

    public ExpectAnimScaleManager(List<AnimExpectation> animExpectations, View viewToMove, ViewCalculator viewCalculator) {
        this.animExpectations = animExpectations;
        this.viewToMove = viewToMove;
        this.viewCalculator = viewCalculator;
    }

    public void calculate() {

        for (AnimExpectation animExpectation : animExpectations) {
            if (animExpectation instanceof ScaleAnimExpectation) {
                final ScaleAnimExpectation expectation = (ScaleAnimExpectation) animExpectation;

                expectation.setViewCalculator(viewCalculator);

                final Float scaleX = expectation.getCalculatedValueScaleX(viewToMove);
                if (scaleX != null) {
                    this.scaleX = scaleX;
                }

                final Float scaleY = expectation.getCalculatedValueScaleY(viewToMove);
                if (scaleY != null) {
                    this.scaleY = scaleY;
                }

                final Integer gravityHorizontal = expectation.getGravityHorizontal();
                if (gravityHorizontal != null) {
                    switch (gravityHorizontal){
                        case Gravity.LEFT:
                        case Gravity.START:
                            pivotX = (float)viewToMove.getLeft();
                            break;
                        case Gravity.RIGHT:
                        case Gravity.END:
                            pivotX = (float)viewToMove.getRight();
                            break;
                        case Gravity.CENTER_HORIZONTAL:
                        case Gravity.CENTER : pivotX = viewToMove.getLeft() + viewToMove.getWidth()/2f; break;
                    }
                }

                final Integer gravityVertical = expectation.getGravityVertical();
                if (gravityVertical != null) {
                    switch (gravityVertical){
                        case Gravity.TOP : pivotY = (float)viewToMove.getTop(); break;
                        case Gravity.BOTTOM : pivotY = (float)viewToMove.getBottom(); break;
                        case Gravity.CENTER_VERTICAL:
                        case Gravity.CENTER : pivotY = viewToMove.getTop() + viewToMove.getHeight()/2f; break;
                    }
                }

            }
        }
    }

    public Float getScaleX() {
        return scaleX;
    }

    public Float getScaleY() {
        return scaleY;
    }

    public List<Animator> getAnimators() {
        final List<Animator> animations = new ArrayList<>();

        if(viewToMove != null){
            if (pivotX != null) {
                viewToMove.setPivotX(pivotX);
            }
            if (pivotY != null) {
                viewToMove.setPivotY(pivotY);
            }
        }

        if (scaleX != null) {
            animations.add(ObjectAnimator.ofFloat(viewToMove, View.SCALE_X, scaleX));
        }

        if (scaleY != null) {
            animations.add(ObjectAnimator.ofFloat(viewToMove, View.SCALE_Y, scaleY));
        }

        return animations;
    }
}
