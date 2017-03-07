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

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.florent37.expectanim.core.AnimExpectation;
import com.github.florent37.expectanim.ViewCalculator;
import com.github.florent37.expectanim.core.ExpectAnimManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 17/02/2017.
 */

public class ExpectAnimPositionManager extends ExpectAnimManager {

    private Float positionX = null;
    private Float positionY = null;

    private Float translationX = null;
    private Float translationY = null;

    public ExpectAnimPositionManager(List<AnimExpectation> animExpectations, View viewToMove, ViewCalculator viewCalculator) {
        super(animExpectations, viewToMove, viewCalculator);
    }


    public Float getPositionX() {
        if(translationX != null){
            return viewToMove.getX() + translationX;
        } else {
            return positionX;
        }
    }

    public Float getPositionY() {
        if(translationX != null){
            return viewToMove.getY() + translationY;
        } else {
            return positionY;
        }
    }

    public void calculate() {
        for (AnimExpectation animExpectation : animExpectations) {
            if(animExpectation instanceof PositionAnimExpectation) {
                PositionAnimExpectation expectation = (PositionAnimExpectation)animExpectation;

                    expectation.setViewCalculator(viewCalculator);

                    final Float calculatedValueX = expectation.getCalculatedValueX(viewToMove);
                    if (calculatedValueX != null) {
                        if (expectation.isForPositionX()) {
                            positionX = calculatedValueX;
                        }
                        if (expectation.isForTranslationX()) {
                            translationX = calculatedValueX;
                        }
                    }

                    final Float calculatedValueY = expectation.getCalculatedValueY(viewToMove);
                    if (calculatedValueY != null) {
                        if (expectation.isForPositionY()) {
                            positionY = calculatedValueY;
                        }
                        if (expectation.isForTranslationY()) {
                            translationY = calculatedValueY;
                        }
                    }
                }
        }
    }

    @Override
    public List<Animator> getAnimators() {
        final List<Animator> animations = new ArrayList<>();

        if (positionX != null) {
            animations.add(ObjectAnimator.ofFloat(viewToMove, View.X, viewCalculator.finalPositionLeftOfView(viewToMove, true)));
        }

        if (positionY != null) {
            animations.add(ObjectAnimator.ofFloat(viewToMove, View.Y, viewCalculator.finalPositionTopOfView(viewToMove, true)));
        }

        if (translationX != null) {
            animations.add(ObjectAnimator.ofFloat(viewToMove, View.TRANSLATION_X, translationX));
        }

        if (translationY != null) {
            animations.add(ObjectAnimator.ofFloat(viewToMove, View.TRANSLATION_Y, translationY));
        }

        return animations;
    }
}
