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
import android.support.annotation.DimenRes;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;

import com.github.florent37.expectanim.core.AnimExpectation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 17/02/2017.
 */

public abstract class PositionAnimExpectation extends AnimExpectation {
    public abstract Float getCalculatedValueX(View viewToMove);
    public abstract Float getCalculatedValueY(View viewToMove);

    private boolean isForPositionY;
    private boolean isForPositionX;
    private boolean isForTranslationX;
    private boolean isForTranslationY;
    private float margin;

    @Nullable
    private Integer marginRes;

    @Nullable
    private Float marginDp;

    public boolean isForPositionY() {
        return isForPositionY;
    }

    public boolean isForPositionX() {
        return isForPositionX;
    }

    public boolean isForTranslationX() {
        return isForTranslationX;
    }

    public boolean isForTranslationY() {
        return isForTranslationY;
    }

    protected void setForPositionY(boolean forPositionY) {
        isForPositionY = forPositionY;
    }

    protected void setForPositionX(boolean forPositionX) {
        isForPositionX = forPositionX;
    }

    protected void setForTranslationX(boolean forTranslationX) {
        isForTranslationX = forTranslationX;
    }

    protected void setForTranslationY(boolean forTranslationY) {
        isForTranslationY = forTranslationY;
    }

    public float getMargin(View view) {
        if(marginRes != null){
            margin = view.getContext().getResources().getDimension(marginRes);
        } else if(marginDp != null){
            margin = dpToPx(view.getContext(), marginDp);
        }
        return margin;
    }

    public static float dpToPx(Context context, float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public PositionAnimExpectation withMargin(float margin){
        this.margin = margin;
        return this;
    }

    public PositionAnimExpectation withMarginDimen(@DimenRes int marginRes){
        this.marginRes = marginRes;
        return this;
    }

    public PositionAnimExpectation withMarginDp(float marginDp){
        this.marginDp = marginDp;
        return this;
    }

}
