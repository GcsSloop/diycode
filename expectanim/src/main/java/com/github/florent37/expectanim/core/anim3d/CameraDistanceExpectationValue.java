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

package com.github.florent37.expectanim.core.anim3d;

import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by Christian Ringshofer on 17/02/2017.
 * <p>
 * A container for storing the rotation values for the flip animation
 */
public class CameraDistanceExpectationValue extends CameraDistanceExpectation {

    private final float mCameraDistance;

    /**
     * a new camera distance expectation value
     *
     * @param cameraDistance the cameraDistance in densityPixels to use for the view perspective useful for
     *                       animations around the x-axis or y-axis in 3d space
     */
    public CameraDistanceExpectationValue(float cameraDistance) {
        mCameraDistance = cameraDistance;
    }

    @Override
    @Nullable
    public Float getCalculatedCameraDistance(View viewToMove) {
        return mCameraDistance * viewToMove.getResources().getDisplayMetrics().density;
    }

}
