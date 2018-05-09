/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.example.rally.view;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FractionBarDrawable extends Drawable {

    private Paint mPaint;
    private int[] mColors;
    private float[] mFractions;

    public FractionBarDrawable() {
        mPaint = new Paint();
    }

    public void setData(int[] colors, float[] fractions) {
        mColors = colors;
        mFractions = fractions;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect b = getBounds();
        int width = b.width();
        int height = b.height();
        boolean horizontal = width > height;

        float currentFraction = 0f;
        int w;
        if (mFractions != null && mColors != null && mColors.length >= mFractions.length) {
            for (int i = 0; i < mFractions.length; i++) {
                mPaint.setColor(mColors[i]);
                if (horizontal) {
                    w = Math.round(mFractions[i] * width);
                    canvas.drawRect(
                            currentFraction,
                            b.top,
                            currentFraction + w,
                            b.bottom,
                            mPaint
                    );
                } else {
                    w = Math.round(mFractions[i] * height);
                    canvas.drawRect(
                            b.left,
                            height - currentFraction - w,
                            b.right,
                            height - currentFraction,
                            mPaint
                    );
                }
                currentFraction += w;
            }
        }

        mPaint.setColor(0xff26282F);

        if (horizontal) {
            canvas.drawRect(currentFraction, b.top, b.right, b.bottom, mPaint);
        } else {
            canvas.drawRect(b.left, b.top, b.right, height - currentFraction, mPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        // Do nothing
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
