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

package com.github.florent37.expectanim.core.rotation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.florent37.expectanim.ViewCalculator;
import com.github.florent37.expectanim.core.AnimExpectation;
import com.github.florent37.expectanim.core.ExpectAnimManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 17/02/2017.
 */

public class ExpectAnimRotationManager extends ExpectAnimManager {

    @Nullable
    private Float rotation = null;

    @Nullable
    private Float rotationX;

    @Nullable
    private Float rotationY;

    public ExpectAnimRotationManager(List<AnimExpectation> animExpectations, View viewToMove, ViewCalculator viewCalculator) {
        super(animExpectations, viewToMove, viewCalculator);
    }

    @Override
    public void calculate() {
        for (AnimExpectation expectation : animExpectations) {
            if (expectation instanceof RotationExpectation) {
                final Float rotation = ((RotationExpectation) expectation).getCalculatedRotation(viewToMove);
                if (rotation != null) {
                    this.rotation = rotation;
                }

                final Float rotationX = ((RotationExpectation) expectation).getCalculatedRotationX(viewToMove);
                if (rotationX != null) {
                    this.rotationX = rotationX;
                }

                final Float rotationY = ((RotationExpectation) expectation).getCalculatedRotationY(viewToMove);
                if (rotationY != null) {
                    this.rotationY = rotationY;
                }
            }
        }
    }

    @Override
    public List<Animator> getAnimators() {
        final List<Animator> animations = new ArrayList<>();

        calculate();

        if (rotation != null) {
            animations.add(ObjectAnimator.ofFloat(viewToMove, View.ROTATION, rotation));
        }

        if (rotationX != null) {
            animations.add(ObjectAnimator.ofFloat(viewToMove, View.ROTATION_X, rotationX));
        }
        if (rotationY != null) {
            animations.add(ObjectAnimator.ofFloat(viewToMove, View.ROTATION_Y, rotationY));
        }

        return animations;
    }

    public Float getRotation() {
        return rotation;
    }

    @Nullable
    public Float getRotationX() {
        return rotationX;
    }

    @Nullable
    public Float getRotationY() {
        return rotationY;
    }
}
