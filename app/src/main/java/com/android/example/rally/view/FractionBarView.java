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

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class FractionBarView extends View {

    private FractionBarDrawable mFbd;

    public FractionBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mFbd = new FractionBarDrawable();
        setBackground(mFbd);
    }

    public void setData(int[] colors, float[] fractions) {
        mFbd.setData(colors, fractions);
    }
}
