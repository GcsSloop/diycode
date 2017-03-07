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

package com.github.florent37.expectanim;

import android.view.View;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by florentchampigny on 20/02/2017.
 */

public class ViewCalculator {
    private final Map<View, ViewExpectation> expectationForView;

    private boolean enableRotation = true;
    private boolean enableScale = true;

    public ViewCalculator() {
        expectationForView = new HashMap<>();
    }

    public void setExpectationForView(View view, ViewExpectation viewExpectation) {
        expectationForView.put(view, viewExpectation);
    }

    public void wasCalculated(ViewExpectation viewExpectation) {
        //no-op
    }

    public float finalPositionLeftOfView(View view, boolean itsMe) {
        Float finalX = null;

        final ViewExpectation viewExpectation = expectationForView.get(view);
        if (viewExpectation != null) {
            final Float futurPositionX = viewExpectation.getFuturPositionX();
            if (futurPositionX != null) {
                finalX = futurPositionX;
            }
        }

        if (finalX == null) {
            finalX = view.getX();
        }

        if (itsMe) {
            finalX -= (view.getWidth() - this.finalWidthOfView(view)) / 2f;
        }

        return finalX;
    }

    public float finalPositionLeftOfView(View view) {
        return finalPositionLeftOfView(view, false);
    }

    public float finalPositionRightOfView(View view) {
        return finalPositionLeftOfView(view) + finalWidthOfView(view);
    }

    public float finalPositionTopOfView(View view, boolean itsMe) {
        Float finalTop = null;

        final ViewExpectation viewExpectation = expectationForView.get(view);
        if (viewExpectation != null) {
            final Float futurPositionY = viewExpectation.getFuturPositionY();
            if (futurPositionY != null) {
                finalTop = futurPositionY;
            }
        }

        if (finalTop == null) {
            finalTop = 1f * view.getTop();
        }

        if (itsMe) {
            finalTop -= (view.getHeight() - this.finalHeightOfView(view)) / 2f;
        }

        return finalTop;
    }

    public float finalPositionTopOfView(View view) {
        return finalPositionTopOfView(view, false);
    }

    public float finalPositionBottomOfView(View view) {
        return finalPositionTopOfView(view) + finalHeightOfView(view);
    }

    public float finalCenterXOfView(View view) {
        if (expectationForView.containsKey(view)) {
            return expectationForView.get(view).getFuturPositionX() + finalWidthOfView(view) / 2f;
        } else {
            return view.getLeft() + view.getWidth() / 2f;
        }
    }

    public float finalCenterYOfView(View view) {
        if (expectationForView.containsKey(view)) {
            return expectationForView.get(view).getFuturPositionY() + finalHeightOfView(view) / 2f;
        } else {
            return view.getTop() + view.getHeight() / 2f;
        }
    }

    public float finalWidthOfView(View view) {
        float with = view.getWidth();
        if (expectationForView.containsKey(view)) {
            final ViewExpectation viewExpectation = expectationForView.get(view);

            with = widthScaled(viewExpectation, view, with);

            if (enableRotation) {
                with = widthRotated(viewExpectation, view, with);
            }
        }
        return with;
    }

    private float widthScaled(ViewExpectation viewExpectation, View view, float width) {
        final Float scaleX = viewExpectation.getWillHasScaleX();
        if (scaleX != 1f) {
            width = scaleX * width; // + view.getPivotX() * scaleX;
        }
        return width;
    }

    private float widthRotated(ViewExpectation viewExpectation, View view, float width) {
        final Float willHaveRotationX = viewExpectation.getWillHaveRotation();
        if (willHaveRotationX != null) {
            final double radians = Math.toRadians(90 - willHaveRotationX);
            width = (float) (abs(width * sin(radians)) + abs(heightScaled(viewExpectation, view, view.getHeight()) * cos(radians)));
        }
        return width;
    }

    public float finalHeightOfView(View view) {
        float height = view.getHeight();
        if (expectationForView.containsKey(view)) {
            final ViewExpectation viewExpectation = expectationForView.get(view);

            height = heightScaled(viewExpectation, view, height);

            if (enableRotation) {
                height = heightRotated(viewExpectation, view, height);
            }
        }
        return height;
    }

    private float heightScaled(ViewExpectation viewExpectation, View view, float height) {
        final Float scaleY = viewExpectation.getWillHasScaleY();
        if (scaleY != 1) {
            height = scaleY * height; // + view.getPivotY() * scaleY;
        }
        return height;
    }

    private float heightRotated(ViewExpectation viewExpectation, View view, float height) {
        final Float willHaveRotationX = viewExpectation.getWillHaveRotation();
        if (willHaveRotationX != null) {
            final double radians = Math.toRadians(willHaveRotationX);
            height = (float) (abs(height * cos(radians)) + abs(widthScaled(viewExpectation, view, view.getWidth()) * sin(radians)));
        }
        return height;
    }


}
