package com.srm325.budgetshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.Query;
import com.srm325.budgetshop.model_classes.Categories;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String CATEGORY = "category";
    public static final String ENTRY_TYPE = "entryType";
    public static final String BUDGET_VALUE = "budgetValue";

    Button expenseButton;
    Button incomeButton;
    PieChart ringChart;
    TextView balanceRing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //WTF
        setContentView(R.layout.activity_main);

        expenseButton = findViewById(R.id.expense_tabbutton);
        incomeButton = findViewById(R.id.income_tabbutton);
        ringChart = findViewById(R.id.ring_chart);
        balanceRing = findViewById(R.id.balance_ring);

        expenseButton.setBackground(getResources().getDrawable(R.drawable.tab_line));
        ringChart.setNoDataText("");

        Fragment theDefault = new ExpenseFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, theDefault).commit();

    }

    @Override
    protected void onResume() {
        super.onResume();

        getCategoriesListThenSetupRingChart(true);
    }

    //inflate the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
                //todo: open dialog to add income or expense
            case R.id.add_action_mainact:
                AddEntryDialog dialog = new AddEntryDialog();
                dialog.show(getSupportFragmentManager(), "Add Entry Dialog");
                return true;
                //todo: open shopping session activity
            case R.id.shopping_session_action_mainact:
                startActivity(new Intent(MainActivity.this, ShoppingSessionActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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