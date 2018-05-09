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
package com.android.example.rally;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.example.rally.model.Alert;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.List;

class AlertAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NARROW_MIN_WIDTH_DP = 250;
    private static final int MIN_WIDTH_DP = 310;
    private static final int MIN_HEIGHT_DP = 68;
    private static final int ALERT_FONT_SIZE_SP = 14;
    private static final int LARGE_ALERT_FONT_SIZE_SP = 16;
    private List<Alert> mAlerts;
    private float mDensity;
    private boolean mUseNarrowMinWidth;

    public AlertAdapter(List<Alert> alerts, float density) {
        mAlerts = alerts;
        mDensity = density;
    }

    public void useNarrowMinWidth(boolean narrow) {
        mUseNarrowMinWidth = narrow;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_alert,
                parent, false);
        return new AlertHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AlertHolder ah = (AlertHolder) holder;
        Alert a = mAlerts.get(position);
        ah.text.setText(a.getText());
        ah.icon.setImageResource(a.getIconId());

        ViewGroup.LayoutParams lp = ah.itemView.getLayoutParams();
        if (lp instanceof FlexboxLayoutManager.LayoutParams) {
            FlexboxLayoutManager.LayoutParams flexboxLp = (FlexboxLayoutManager.LayoutParams) lp;
            flexboxLp.setFlexGrow(0.1f);

            if (mUseNarrowMinWidth) {
                flexboxLp.setMinWidth(Math.round(NARROW_MIN_WIDTH_DP * mDensity));
                flexboxLp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                ah.text.setTextSize(TypedValue.COMPLEX_UNIT_SP, LARGE_ALERT_FONT_SIZE_SP);
            } else {
                flexboxLp.setMinWidth(Math.round(MIN_WIDTH_DP * mDensity));
                flexboxLp.height = Math.round(MIN_HEIGHT_DP * mDensity);
                ah.text.setTextSize(TypedValue.COMPLEX_UNIT_SP, ALERT_FONT_SIZE_SP);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mAlerts.size();
    }

    public class AlertHolder extends RecyclerView.ViewHolder {

        public TextView text;
        public ImageView icon;

        public AlertHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}
