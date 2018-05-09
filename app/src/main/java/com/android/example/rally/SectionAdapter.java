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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.example.rally.model.RowData;
import com.android.example.rally.model.Section;
import com.android.example.rally.view.FractionBarView;

import java.util.List;

class SectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 100;
    private static final int TYPE_ROW = 101;
    private static final int TYPE_ROW_HORIZONTAL_BAR = 102;
    private static final int TYPE_SEE_ALL = 103;

    private static final int MAX_ROWS = 3;

    private Section mSection;

    private boolean mUseHorizontalBar;

    public SectionAdapter(Section section) {
        mSection = section;
    }

    public void useHorizontalBar(boolean useHorizontalBar) {
        mUseHorizontalBar = useHorizontalBar;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        RecyclerView.ViewHolder vh = null;

        if (viewType == TYPE_HEADER) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_section_header,
                    parent, false);
            vh = new HeaderViewHolder(v);
        } else if (viewType == TYPE_ROW) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_account_row,
                    parent, false);
            vh = new RowViewHolder(v);
        } else if (viewType == TYPE_ROW_HORIZONTAL_BAR) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_account_row_horizontal_bar,
                    parent, false);
            vh = new RowViewHolder(v);
        } else if (viewType == TYPE_SEE_ALL) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_see_all,
                    parent, false);
            vh = new SeeAllViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder hvh = (HeaderViewHolder) holder;
            hvh.sectionName.setText(mSection.getName());
            hvh.sectionTotal1.setText(mSection.getTotal1String());

            if (mSection.showTotal()) {
                hvh.sectionTotal2.setVisibility(View.VISIBLE);
                hvh.sectionTotal2.setText(mSection.getTotal2String());
            } else {
                hvh.sectionTotal2.setVisibility(View.GONE);
            }

            List<RowData> items = mSection.getItems();
            int[] colors = new int[items.size()];
            float[] fractions = new float[items.size()];
            float total = mSection.getTotal2();
            for (int i = 0; i < items.size(); i++) {
                colors[i] = items.get(i).getRowColor();
                fractions[i] = items.get(i).getRowAmount() / total;
            }
            hvh.barView.setData(colors, fractions);
        } else if (holder instanceof RowViewHolder) {
            RowViewHolder rvh = (RowViewHolder) holder;
            RowData rowData = mSection.getItems().get(position - 1);
            rvh.primaryText.setText(rowData.getRowName());
            rvh.secondaryText.setText(rowData.getRowSecondaryString());
            rvh.secondaryTextObfuscation.setVisibility(rowData.showSecondaryStringObfuscation() ?
                    View.VISIBLE : View.GONE);

            rvh.amountText.setText(rowData.getRowAmountString());

            int[] colors = new int[1];
            float[] fractions = new float[1];
            colors[0] = rowData.getRowColor();
            fractions[0] = rowData.getRowAmount() / rowData.getRowLimitAmount();

            rvh.barView.setData(colors, fractions);

            if (rowData.getAmountQualifierString() == null) {
                rvh.amountQualifierText.setVisibility(View.GONE);
                rvh.arrow.setVisibility(View.VISIBLE);
            } else {
                rvh.amountQualifierText.setVisibility(View.VISIBLE);
                rvh.amountQualifierText.setText(rowData.getAmountQualifierString());
                rvh.arrow.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(mSection.getItems().size(), MAX_ROWS) + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (mSection.getItems() != null && position < Math.min(mSection.getItems().size(),
                MAX_ROWS) + 1) {
            if (mUseHorizontalBar) {
                return TYPE_ROW_HORIZONTAL_BAR;
            } else {
                return TYPE_ROW;
            }
        } else {
            return TYPE_SEE_ALL;
        }
    }

    public class RowViewHolder extends RecyclerView.ViewHolder {

        public TextView primaryText;
        public TextView secondaryText;
        public TextView secondaryTextObfuscation;
        public TextView amountText;
        public TextView amountQualifierText;
        public FractionBarView barView;
        public ImageView arrow;

        public RowViewHolder(View itemView) {
            super(itemView);
            primaryText = itemView.findViewById(R.id.account_primary_text);
            secondaryText = itemView.findViewById(R.id.account_secondary_text);
            secondaryTextObfuscation = itemView.findViewById(R.id
                    .account_secondary_text_obfuscation);
            barView = itemView.findViewById(R.id.row_bar_chart);
            amountText = itemView.findViewById(R.id.amount_text);
            amountQualifierText = itemView.findViewById(R.id.amount_qualifier_text);
            arrow = itemView.findViewById(R.id.arrow);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        public TextView sectionName;
        public TextView sectionTotal1;
        public TextView sectionTotal2;
        public FractionBarView barView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.section_name);
            sectionTotal1 = itemView.findViewById(R.id.section_total_1);
            sectionTotal2 = itemView.findViewById(R.id.section_total_2);
            barView = itemView.findViewById(R.id.section_bar_chart);
        }
    }

    public class SeeAllViewHolder extends RecyclerView.ViewHolder {
        public SeeAllViewHolder(View itemView) {
            super(itemView);
        }
    }
}
