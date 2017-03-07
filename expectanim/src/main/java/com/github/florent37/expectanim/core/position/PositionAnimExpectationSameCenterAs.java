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

import android.view.View;

/**
 * Created by florentchampigny on 17/02/2017.
 */

public class PositionAnimExpectationSameCenterAs extends PositionAnimationViewDependant {

    private final boolean horizontal;
    private final boolean vertical;

    public PositionAnimExpectationSameCenterAs(View otherView, boolean horizontal, boolean vertical) {
        super(otherView);
        setForPositionX(true);
        setForPositionY(true);
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    @Override
    public Float getCalculatedValueX(View viewToMove) {
        if(horizontal) {

            final float x = viewCalculator.finalPositionLeftOfView(otherView);
            final float myWidth = viewToMove.getWidth() / 2f;
            final float hisWidth = viewCalculator.finalWidthOfView(otherView) / 2f;

            if (myWidth > hisWidth) {
                return x - myWidth + hisWidth;
            } else {
                return x - hisWidth + myWidth;
            }
        } else return null;
    }

    @Override
    public Float getCalculatedValueY(View viewToMove) {
        if(vertical) {

            final float y = viewCalculator.finalPositionTopOfView(otherView);
            final float myHeight = viewToMove.getHeight() / 2f;
            final float hisHeight = viewCalculator.finalHeightOfView(otherView) / 2f;

            if (myHeight > hisHeight) {
                return y + myHeight - hisHeight;
            } else {
                return y + hisHeight - myHeight;
            }

        } else return null;
    }

}
