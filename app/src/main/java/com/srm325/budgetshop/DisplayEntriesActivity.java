package com.srm325.budgetshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.srm325.budgetshop.model_classes.Entry;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class DisplayEntriesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EntriesAdapter adapter;
    FirestoreRepository firestoreRepository;
    String categoryName;
    String currentBudget;
    EditText budget;
    boolean hasBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_entries);

        getSupportActionBar().setTitle("");
        //What am I doing

        TextView categoryTxtView = findViewById(R.id.entries_category_name);
        budget = findViewById(R.id.entries_budget);
        firestoreRepository = new FirestoreRepository();
        recyclerView = findViewById(R.id.entries_recyclerview);

        Intent intent = getIntent();
        categoryName = intent.getStringExtra(MainActivity.CATEGORY);
        currentBudget = intent.getStringExtra(MainActivity.BUDGET_VALUE);
        categoryTxtView.setText(categoryName);

        if(currentBudget == null){
            hasBudget = false;
            LinearLayout linearLayout = findViewById(R.id.budgetlinearlayout);
            linearLayout.setVisibility(View.GONE);
        }
        else{
            hasBudget = true;
            budget.setText(currentBudget);
        }

        setupRecyclerview();
    }

    private void setupRecyclerview() {
        Query query = firestoreRepository.categoriesRef
                .document(categoryName)
                .collection(FirestoreRepository.ENTRIES)
                .orderBy(FirestoreRepository.ENTRY_DATE_FIELD, Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<Entry> options = new FirestoreRecyclerOptions.Builder<Entry>()
                .setQuery(query, Entry.class)
                .build();

        adapter = new EntriesAdapter(options);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        LinearLayoutManager verticalLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayout);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    //inflate the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.display_entries_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //todo: save budget value
            case R.id.done_button:
                saveBudgetAndClose();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveBudgetAndClose() {
        if(hasBudget){
            String budgetValue;
            if(budget.getText().toString().trim().isEmpty()){
                budgetValue = "0";
            }
            else{
                budgetValue = budget.getText().toString().trim();
            }

            firestoreRepository.updateBudget(categoryName, budgetValue);
        }

        finish();
    }
}