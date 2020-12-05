package com.srm325.budgetshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ExpenseCategoryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_category);
    }

    //when a category is clicked go to AddEntry activity with the name of the category and the type of entry that was clicked
    public void onExpenseCategoryClick(View view){
        Intent intent = new Intent(ExpenseCategoryActivity.this, AddEntryActivity.class);
        intent.putExtra(MainActivity.ENTRY_TYPE, "expense");
        intent.putExtra(MainActivity.CATEGORY, view.getContentDescription().toString());
        startActivity(intent);
        finish();
    }

}