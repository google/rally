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

import java.text.NumberFormat;
import java.util.List;

public class Section {
    private List<RowData> mItems;
    private String mName;
    private boolean mShowTotal;
    private NumberFormat mFormatter;

    public Section(List<RowData> items, String name, boolean showTotal) {
        mItems = items;
        mName = name;
        mShowTotal = showTotal;

        mFormatter = NumberFormat.getCurrencyInstance();
    }

    public List<RowData> getItems() {
        return mItems;
    }

    public String getName() {
        return mName;
    }

    public float getTotal1() {
        float total = 0f;
        for (RowData item : mItems) {
            total += item.getRowAmount();
        }

        return total;
    }

    public float getTotal2() {
        float total = 0f;
        for (RowData item : mItems) {
            total += item.getRowLimitAmount();
        }

        return total;
    }

    public String getTotal1String() {
        return mFormatter.format(getTotal1());
    }

    public String getTotal2String() {
        String total2String = mFormatter.format(Math.round(getTotal2()));
        String[] total2DecimalSplit = total2String.split("\\.");
        return " / " + total2DecimalSplit[0];
    }

    public boolean showTotal() {
        return mShowTotal;
    }
}
