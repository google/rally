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

import android.content.res.Configuration;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintsChangedListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.example.rally.model.Account;
import com.android.example.rally.model.Alert;
import com.android.example.rally.model.Bill;
import com.android.example.rally.model.Budget;
import com.android.example.rally.model.RowData;
import com.android.example.rally.model.Section;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int LARGE_TAB_TEXT_SIZE = 14;
    private static final int SMALL_TAB_TEXT_SIZE = 10;
    private static final int ALERTS_PHONE_HEIGHT_DP = 68;
    private static final int ALERTS_LAPTOP_HEIGHT_DP = 800;

    private ArrayList<Alert> mAlerts;
    private Section mAccountsSection;
    private Section mBillsSection;
    private Section mBudgetsSection;

    private ConstraintLayout mCl;
    private ConstraintLayout mTabsCl;
    private ConstraintLayout mSectionCl;
    private ConstraintLayout mAlertHeaderCl;
    private RecyclerView mAlertsRv;
    private RecyclerView mAccountsRV;
    private RecyclerView mBillsRV;
    private RecyclerView mBudgetsRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;

        // Initialize our model with hard-coded sample data
        initializeData();

        // Setup the RecyclerViews
        mAlertsRv = findViewById(R.id.alerts_rv);
        FlexboxLayoutManager flexboxLayoutManager = new NonScrollableFlexboxLayoutManager(this);
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        mAlertsRv.setLayoutManager(flexboxLayoutManager);
        AlertAdapter alertsAdapter = new AlertAdapter(mAlerts, density);
        mAlertsRv.setAdapter(alertsAdapter);

        mAccountsRV = findViewById(R.id.accounts);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mAccountsRV.setLayoutManager(layoutManager);
        RecyclerView.Adapter accountsAdapter = new SectionAdapter(mAccountsSection);
        mAccountsRV.setAdapter(accountsAdapter);

        mBillsRV = findViewById(R.id.bills);
        layoutManager = new LinearLayoutManager(this);
        mBillsRV.setLayoutManager(layoutManager);
        RecyclerView.Adapter billsAdapter = new SectionAdapter(mBillsSection);
        mBillsRV.setAdapter(billsAdapter);

        mBudgetsRV = findViewById(R.id.budgets);
        layoutManager = new LinearLayoutManager(this);
        mBudgetsRV.setLayoutManager(layoutManager);
        SectionAdapter budgetsAdapter = new SectionAdapter(mBudgetsSection);
        mBudgetsRV.setAdapter(budgetsAdapter);


        // Get a reference to the constraint layouts we want to change as the app resizes and set
        // a layout description file pointing to the alternate constraint sets we want to swap in
        // at different breakpoints.
        mCl = findViewById(R.id.main);
        mCl.setLayoutDescription(R.xml.main_resize);

        mTabsCl = findViewById(R.id.inner_tab_layout);
        mTabsCl.setLayoutDescription(R.xml.tabs_resize);

        mSectionCl = findViewById(R.id.section_container);
        mSectionCl.setLayoutDescription(R.xml.section_resize);

        mAlertHeaderCl = findViewById(R.id.alert_header);
        mAlertHeaderCl.setLayoutDescription(R.xml.alert_header_resize);

        TextView tabText = findViewById(R.id.overview_text);
        RecyclerView alertsRv = findViewById(R.id.alerts_rv);
        LinearLayout.LayoutParams alertsParams = (LinearLayout.LayoutParams) alertsRv
                .getLayoutParams();


        // Listen for changes to the root ConstraintLayout's constraint state and set the
        // state of the nested layouts to reflect this new state. Also adjust the properties of
        // other views as necessary.
        mCl.setOnConstraintsChanged(new ConstraintsChangedListener() {
            @Override
            public void preLayoutChange(int state, int layoutId) {
                Log.v(TAG, "change state " + state);
                if (layoutId == R.layout.activity_main) {
                    mTabsCl.setState(R.id.tabs_landscape, 0, 0);
                    mSectionCl.setState(R.id.section_phone, 0, 0);
                    mAlertHeaderCl.setState(R.id.alert_header_phone, 0, 0);
                    tabText.setTextSize(TypedValue.COMPLEX_UNIT_SP, LARGE_TAB_TEXT_SIZE);
                    alertsParams.height = Math.round(ALERTS_PHONE_HEIGHT_DP * density);
                    alertsAdapter.useNarrowMinWidth(false);
                    budgetsAdapter.useHorizontalBar(false);
                } else if (layoutId == R.layout.activity_main_tablet) {
                    mTabsCl.setState(R.id.tabs_portrait, 0, 0);
                    mSectionCl.setState(R.id.section_tablet, 0, 0);
                    mAlertHeaderCl.setState(R.id.alert_header_phone, 0, 0);
                    tabText.setTextSize(TypedValue.COMPLEX_UNIT_SP, SMALL_TAB_TEXT_SIZE);
                    alertsParams.height = Math.round(ALERTS_PHONE_HEIGHT_DP * density);
                    alertsAdapter.useNarrowMinWidth(false);
                    budgetsAdapter.useHorizontalBar(true);
                } else {
                    mTabsCl.setState(R.id.tabs_portrait, 0, 0);
                    mSectionCl.setState(R.id.section_laptop, 0, 0);
                    mAlertHeaderCl.setState(R.id.alert_header_laptop, 0, 0);
                    tabText.setTextSize(TypedValue.COMPLEX_UNIT_SP, SMALL_TAB_TEXT_SIZE);
                    alertsParams.height = Math.round(ALERTS_LAPTOP_HEIGHT_DP * density);
                    alertsAdapter.useNarrowMinWidth(true);
                    budgetsAdapter.useHorizontalBar(true);
                }

                alertsAdapter.notifyDataSetChanged();
                budgetsAdapter.notifyDataSetChanged();
            }

            @Override
            public void postLayoutChange(int state, int layoutId) {
                // RequestLayout necessary to avoid flashes of bad layout state during some resizes
                mCl.requestLayout();
                mSectionCl.requestLayout();
                mTabsCl.requestLayout();
                mAlertHeaderCl.requestLayout();
            }
        });


        // View tree observer necessary to make sure app starts in the proper state regardless of
        // whether it starts in a tablet or phone size as onConfigurationChanged is not invoked
        // at startup
        ViewTreeObserver vto = mCl.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mCl.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = mCl.getMeasuredWidth();
                int height = mCl.getMeasuredHeight();
                int widthDp = Math.round(width / density);
                int heightDp = Math.round(height / density);
                mCl.setState(-1, widthDp, heightDp);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.v(TAG, "onConfigurationChanged " + newConfig.screenWidthDp + "," + newConfig.screenHeightDp);

        // Where the root ConstraintLayout has it's state set, based on the rules in the layout
        // description in xml/main_resize.xml. Because of the flags set in our manifest,
        // onConfigurationChanged is called every time the app window is resized.
        mCl.setState(-1, newConfig.screenWidthDp, newConfig.screenHeightDp);
    }


    private void initializeData() {
        mAlerts = new ArrayList<Alert>();
        mAlerts.add(new Alert("Heads up, you’ve used up 90% of your Shopping budget for this month.",
                R.drawable.ic_sort_24px));
        mAlerts.add(new Alert("You've spent $120 on Restaurants this week",
                R.drawable.ic_sort_24px));
        mAlerts.add(new Alert("You've spent $24 in ATM fees this month",
                R.drawable.ic_credit_card_24px));
        mAlerts.add(new Alert("Good work! Your checking account is 4% higher than this time last month",
                R.drawable.ic_attach_money_24px));
        mAlerts.add(new Alert("Increase your potential tax deduction! Assign categories to 16 unassigned transactions",
                R.drawable.ic_not_interested_24px));
        mAlerts.add(new Alert("Get every tax deduction you’re entitled to. Assign categories to your 16 uncategorized transactions.",
                R.drawable.ic_pie_chart_outlined_24px));
        mAlerts.add(new Alert("Your ABC Loan payment of $325.81 was received!",
                R.drawable.ic_attach_money_24px));
        mAlerts.add(new Alert("Open an IRA account and get $100 bonus.",
                R.drawable.ic_attach_money_24px));

        List<RowData> accounts = new ArrayList<RowData>();
        accounts.add(new Account("Checking", 2215.13f, "1234", 0xFF005F57));
        accounts.add(new Account("Home Savings", 8676.88f, "5678", 0xFF00BD7A));
        accounts.add(new Account("Car Savings", 987.48f, "9012", 0xFF00F4B6));
        accounts.add(new Account("Vacation", 253f, "3456", 0xFF00804D));
        mAccountsSection = new Section(accounts, "Accounts", false);

        List<RowData> bills = new ArrayList<RowData>();
        bills.add(new Bill("RedPay Credit", 45.36f, new Date(), 0xFFFFDC65));
        bills.add(new Bill("Rent", 1200f, new Date(), 0xFFFF5C45));
        bills.add(new Bill("TabFine Credit", 87.33f, new Date(), 0xFFFFD5CE));
        bills.add(new Bill("ABC Loans", 400f, new Date(), 0xFFFFA900));
        mBillsSection = new Section(bills, "Bills", false);

        List<RowData> budgets = new ArrayList<RowData>();
        budgets.add(new Budget("Coffee Shops", 45.49f, 70f, 0xFF43E1FF));
        budgets.add(new Budget("Groceries", 16.45f, 170f, 0xFFBE50FF));
        budgets.add(new Budget("Restaurants", 123.25f, 170f, 0xFFA1F4FF));
        budgets.add(new Budget("Clothing", 19.45f, 70f, 0xFF0081FF));
        mBudgetsSection = new Section(budgets, "January Budget", true);
    }
}
