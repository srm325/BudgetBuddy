package com.srm325.budgetshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.firestore.Query;
import com.srm325.budgetshop.model_classes.Categories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarPopUpActivity extends AppCompatActivity {
    public static final String CATEGORY = "category";
    public static final String ENTRY_TYPE = "entryType";
    public static final String BUDGET_VALUE = "budgetValue";

    Button expenseButton;
    Button incomeButton;
    PieChart ringChart;
    TextView balanceRing;
    TextView dateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //WTF
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_calendar_pop_up);
        //Get the bundle
        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        String date = bundle.getString("stuff");
        dateView = findViewById(R.id.textView);
        dateView.setText(date);
        expenseButton = findViewById(R.id.expense_tabbutton);
        incomeButton = findViewById(R.id.income_tabbutton);
        ringChart = findViewById(R.id.ring_chart);
        balanceRing = findViewById(R.id.balance_ring);

        expenseButton.setBackground(getResources().getDrawable(R.drawable.tab_line));
        ringChart.setNoDataText("");

        Fragment theDefault = new ExpenseFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, theDefault).commit();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.6));
    }

    @Override
    protected void onResume() {
        super.onResume();

        getCategoriesListThenSetupRingChart(true);
    }

    public void SwitchFragments(View view){
        Fragment selected = null;
        switch(view.getId()){
            case R.id.expense_tabbutton:
                selected = new ExpenseFragment();
                view.setBackground(getResources().getDrawable(R.drawable.tab_line));
                incomeButton.setBackground(getResources().getDrawable(android.R.color.transparent));
                getCategoriesListThenSetupRingChart(true);
                break;
            case R.id.income_tabbutton:
                selected = new IncomeFragment();
                view.setBackground(getResources().getDrawable(R.drawable.tab_line));
                expenseButton.setBackground(getResources().getDrawable(android.R.color.transparent));
                getCategoriesListThenSetupRingChart(false);
                break;
        }

        if(selected != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected).commit();
        }
    }

    private void getCategoriesListThenSetupRingChart(boolean isExpense) {
        FirestoreRepository firestoreRepository = new FirestoreRepository();
        //Get the bundle
        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        int day = bundle.getInt("day");
        int month = bundle.getInt("month");
        int years = bundle.getInt("year");
        int year = years+1;
        String startdate = years + "-" + month +"-" + day;
        String enddate = year + "-" + month +"-" + day;

        Query categoriesQuery;
        if (isExpense) {
            categoriesQuery = firestoreRepository
                    .categoriesRef
                    .whereEqualTo(FirestoreRepository.IS_EXPENSE_FIELD, true);
        }
        else {
            categoriesQuery = firestoreRepository
                    .categoriesRef
                    .whereEqualTo(FirestoreRepository.IS_EXPENSE_FIELD, false);
        }

        categoriesQuery.get().addOnSuccessListener(result -> {
            setupRingChart(result.toObjects(Categories.class), isExpense);
        });
    }

    private void setupRingChart(List<Categories> categories, boolean isExpense) {
        List<PieEntry> entries = new ArrayList<>();
        List<Integer> colorEntries = new ArrayList<>();
        populatePieAndColorEntriesList(categories, isExpense, entries, colorEntries);

        PieDataSet dataSet = new PieDataSet(entries, "");
        int[] primitiveColorInts = new int[colorEntries.size()];
        for (int c = 0; c < primitiveColorInts.length; c++) {
            primitiveColorInts[c] = colorEntries.get(c).intValue();
        }
        dataSet.setColors(primitiveColorInts);
        dataSet.setDrawValues(false);

        PieData data = new PieData(dataSet);
        ringChart.setData(data);
        ringChart.invalidate();

        ringChart.getLegend().setEnabled(false);
        ringChart.setDescription(null);
        ringChart.setHoleRadius(75);
        ringChart.setDrawCenterText(false);
        ringChart.setDrawHoleEnabled(true);
        ringChart.setHoleColor(getResources().getColor(R.color.light_blue));
        ringChart.setDrawRoundedSlices(true);
    }

    private void populatePieAndColorEntriesList(List<Categories> categories, boolean isExpense, List<PieEntry> entries, List<Integer> colorEntries) {
        float total = getTotalCurrency(categories, isExpense);
        balanceRing.setText("$" + total);
        for (Categories category : categories) {
            float percentage = getPercentageCurrency(category, total, isExpense);
            entries.add(new PieEntry(percentage));
            switch (category.getCategoryName()) {
                case "Auto":
                    colorEntries.add(ColorsUtil.AUTO_CATEGORY_COLOR);
                    break;
                case "Beauty":
                    colorEntries.add(ColorsUtil.BEAUTY_CATEGORY_COLOR);
                    break;
                case "Clothing":
                    colorEntries.add(ColorsUtil.CLOTHING_CATEGORY_COLOR);
                    break;
                case "Entertainment":
                    colorEntries.add(ColorsUtil.ENTERTAINMENT_CATEGORY_COLOR);
                    break;
                case "Financial":
                    colorEntries.add(ColorsUtil.FINANCIAL_CATEGORY_COLOR);
                    break;
                case "General":
                    colorEntries.add(ColorsUtil.GENERAL_CATEGORY_COLOR);
                    break;
                case "Groceries":
                    colorEntries.add(ColorsUtil.GROCERIES_CATEGORY_COLOR);
                    break;
                case "Home":
                    colorEntries.add(ColorsUtil.HOME_CATEGORY_COLOR);
                    break;
                case "Industry":
                    colorEntries.add(ColorsUtil.INDUSTRY_CATEGORY_COLOR);
                    break;
                case "Learning":
                    colorEntries.add(ColorsUtil.LEARNING_CATEGORY_COLOR);
                    break;
                case "Medical":
                    colorEntries.add(ColorsUtil.MEDICAL_CATEGORY_COLOR);
                    break;
                case "Nightlife":
                    colorEntries.add(ColorsUtil.NIGHTLIFE_CATEGORY_COLOR);
                    break;
                case "Restaurants":
                    colorEntries.add(ColorsUtil.RESTAURANTS_CATEGORY_COLOR);
                    break;
                case "Services":
                    colorEntries.add(ColorsUtil.SERVICES_CATEGORY_COLOR);
                    break;
                case "Shop":
                    colorEntries.add(ColorsUtil.SHOP_CATEGORY_COLOR);
                    break;
                case "Spiritual":
                    colorEntries.add(ColorsUtil.SPIRITUAL_CATEGORY_COLOR);
                    break;
                case "Sports":
                    colorEntries.add(ColorsUtil.SPORTS_CATEGORY_COLOR);
                    break;
                case "Transport":
                    colorEntries.add(ColorsUtil.TRANSPORT_CATEGORY_COLOR);
                    break;
                case "Travel":
                    colorEntries.add(ColorsUtil.TRAVEL_CATEGORY_COLOR);
                    break;
                case "Paycheck":
                    colorEntries.add(ColorsUtil.PAYCHECK_CATEGORY_COLOR);
                    break;
                case "Gift":
                    colorEntries.add(ColorsUtil.GIFT_CATEGORY_COLOR);
                    break;
                case "Interest":
                    colorEntries.add(ColorsUtil.INTEREST_CATEGORY_COLOR);
                    break;
                case "Other":
                    colorEntries.add(ColorsUtil.OTHER_CATEGORY_COLOR);
                    break;
            }
        }
    }

    private float getPercentageCurrency(Categories category, float total, boolean isExpense) {
        if (isExpense && category.getCategorySpent() != null) {
                return Float.parseFloat(category.getCategorySpent()) / total;
        }
        else if (!isExpense && category.getCategoryAmount() != null) {
                return Float.parseFloat(category.getCategoryAmount()) / total;
        }

        return 0f;
    }

    private float getTotalCurrency(List<Categories> categories, boolean isExpense) {
        float total = 0f;
        for (Categories category : categories) {
            if (isExpense) {
                if (category.getCategorySpent() != null) {
                    total += Float.parseFloat(category.getCategorySpent());
                }
            }
            else {
                if (category.getCategoryAmount() != null) {
                    total += Float.parseFloat(category.getCategoryAmount());
                }
            }
        }

        return total;
    }
}