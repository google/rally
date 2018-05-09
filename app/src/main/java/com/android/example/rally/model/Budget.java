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
package com.android.example.rally.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class Budget extends RowData {
    private String mName;
    private float mCurrAmount;
    private float mLimitAmount;
    private int mColor;
    private DecimalFormat mFormatter;
    private NumberFormat mCurrencyFormatter;

    public Budget(String name, float currAmount, float limitAmount, int color) {
        mName = name;
        mCurrAmount = currAmount;
        mLimitAmount = limitAmount;
        mColor = color;

        mFormatter = (DecimalFormat) NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols symbols = mFormatter.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        mFormatter.setDecimalFormatSymbols(symbols);
        mCurrencyFormatter = NumberFormat.getCurrencyInstance();
    }

    @Override
    public String getRowName() {
        return mName;
    }

    @Override
    public String getRowSecondaryString() {
        String limitString = mCurrencyFormatter.format(Math.round(mLimitAmount));
        String[] limitDecimalSplit = limitString.split("\\.");
        return mCurrencyFormatter.format(mCurrAmount) + " / " + limitDecimalSplit[0];
    }

    @Override
    public float getRowAmount() {
        return mCurrAmount;
    }

    @Override
    public float getRowLimitAmount() {
        return mLimitAmount;
    }

    @Override
    public String getRowAmountString() {
        // Show the remaining budget where other data types show amount
        return mFormatter.format(mLimitAmount - mCurrAmount);
    }

    @Override
    public int getRowColor() {
        return mColor;
    }

    @Override
    public boolean showSecondaryStringObfuscation() {
        return false;
    }

    @Override
    public boolean showAccentBar() {
        return false;
    }

    @Override
    public boolean showFractionBar() {
        return true;
    }

    @Override
    public String getAmountQualifierString() {
        return "Left";
    }
}
