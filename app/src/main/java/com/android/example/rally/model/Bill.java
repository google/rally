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

import android.text.format.DateFormat;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Date;

public class Bill extends RowData {
    private String mName;
    private Date mDueDate;
    private float mAmount;
    private int mColor;
    private DecimalFormat mFormatter;

    public Bill(String name, float amount, Date dueDate, int color) {
        mName = name;
        mAmount = amount;
        mDueDate = dueDate;
        mColor = color;

        mFormatter = (DecimalFormat) NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols symbols = mFormatter.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        mFormatter.setDecimalFormatSymbols(symbols);
    }

    @Override
    public String getRowName() {
        return mName;
    }

    @Override
    public String getRowSecondaryString() {
        return "Due " + DateFormat.format("MMM dd", mDueDate);
    }

    @Override
    public float getRowAmount() {
        return mAmount;
    }

    @Override
    public float getRowLimitAmount() {
        return mAmount;
    }

    @Override
    public String getRowAmountString() {
        return mFormatter.format(mAmount);
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
        return true;
    }

    @Override
    public boolean showFractionBar() {
        return false;
    }
}
