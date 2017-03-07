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

import android.support.annotation.Nullable;
import android.view.View;

import com.github.florent37.expectanim.core.AnimExpectation;
import com.github.florent37.expectanim.core.Expectations;
import com.github.florent37.expectanim.core.position.PositionAnimExpectation;

/**
 * Created by florentchampigny on 17/02/2017.
 */

public abstract class ScaleAnimExpectation extends AnimExpectation {

    protected boolean toDp = false;
    protected boolean keepRatio = false;
    @Nullable
    private Integer gravityHorizontal;
    @Nullable
    private Integer gravityVertical;

    public ScaleAnimExpectation(@Nullable Integer gravityHorizontal, @Nullable Integer gravityVertical) {
        if (gravityHorizontal != null) {
            this.gravityHorizontal = gravityHorizontal;
        }
        if (gravityVertical != null) {
            this.gravityVertical = gravityVertical;
        }
    }

    protected int dpToPx(float value, View view) {
        final int v =  (int) PositionAnimExpectation.dpToPx(view.getContext(), value);
        toDp = false;
        return v;
    }

    public abstract Float getCalculatedValueScaleX(View viewToMove);

    public abstract Float getCalculatedValueScaleY(View viewToMove);

    public Integer getGravityHorizontal() {
        return gravityHorizontal;
    }

    public Integer getGravityVertical() {
        return gravityVertical;
    }

    public ScaleAnimExpectation withGravity(@Expectations.GravityScaleHorizontalIntDef @Nullable Integer gravityHorizontal, @Expectations.GravityScaleVerticalIntDef @Nullable Integer gravityVertical) {
        if (gravityHorizontal != null) {
            this.gravityHorizontal = gravityHorizontal;
        }
        if (gravityVertical != null) {
            this.gravityVertical = gravityVertical;
        }
        return this;
    }

    public ScaleAnimExpectation toDp() {
        this.toDp = true;
        return this;
    }

    public ScaleAnimExpectation keepRatio() {
        this.keepRatio = true;
        return this;
    }
}
