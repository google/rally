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

public class Alert {
    private String mText;
    private int mIconId;

    public Alert(String text, int iconId) {
        mText = text;
        mIconId = iconId;
    }

    public String getText() {
        return mText;
    }

    public int getIconId() {
        return mIconId;
    }

}
