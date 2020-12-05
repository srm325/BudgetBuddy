package com.srm325.budgetshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.srm325.budgetshop.model_classes.Product;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShoppingSessionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private FloatingActionButton scanFab;
    FloatingActionButton addToBudgetFab;
    FloatingActionButton searchFab;
    private Activity activity;
    private String scannedUPC;
    private int itemCount = 0;
    private double totalCost = 0;
    TextView sessionName;
    TextView sessionDate;
    TextView sessionItemCountView;
    TextView sessionTotalCostView;
    RecyclerView recyclerView;
    SessionAdapter adapter;
    Date theDate;
    SimpleDateFormat simpleDateFormatter;
    ArrayList<String> groceryStoreList = new ArrayList<>();
    FirestoreRepository firestoreRepository = new FirestoreRepository();
    //What am I doing
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_session);

        getSupportActionBar().hide();

        sessionDate = findViewById(R.id.shopping_session_date);
        sessionItemCountView = findViewById(R.id.session_item_count);
        sessionTotalCostView = findViewById(R.id.session_cost);
        theDate = new Date();
        recyclerView = findViewById(R.id.shopping_session_recyclerview);
        addToBudgetFab = findViewById(R.id.fab_add);
        searchFab = findViewById(R.id.fab_search);
        simpleDateFormatter = new SimpleDateFormat("MM/dd/yyyy");

        sessionDate.setText(simpleDateFormatter.format(theDate));


        //on click of the the date textview open datepickerdialog
        sessionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.srm325.budgetshop.DatePicker datePicker = new com.srm325.budgetshop.DatePicker();
                datePicker.show(getSupportFragmentManager(), "Date Picker");
            }
        });

        searchFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShoppingSessionActivity.this, SearchItemActivity.class));
            }
        });

        readData();
        initalizeCountAndCost();
        setupGlobals();
        scanOnFABClicked();
        addToBudgetFABClicked();
        setupRecyclerview();
    }

    private void initalizeCountAndCost() {
        firestoreRepository.sessionsRef
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Product product = documentSnapshot.toObject(Product.class);
                            itemCount++;
                            totalCost += product.getProductPrice();
                        }

                        sessionItemCountView.setText(String.valueOf(itemCount));
                        sessionTotalCostView.setText("$"+ String.format("%.2f", totalCost));
                    }
                });
    }


    private void setupRecyclerview() {
        Query query = firestoreRepository.sessionsRef;

        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();

        adapter = new SessionAdapter(options);

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

    private void setupGlobals() {
        scanFab = findViewById(R.id.fab_scan);
        activity = this;
    }

    private void scanOnFABClicked() {
        scanFab.setOnClickListener(v -> {
            IntentIntegrator scanIntegrator = new IntentIntegrator(activity);
            scanIntegrator.initiateScan();
        });
    }

    private void addToBudgetFABClicked() {
        addToBudgetFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String categoryName = "Groceries";

                DocumentReference documentReference = firestoreRepository.categoriesRef.document(categoryName);

                documentReference.get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();

                                    if(document.exists()){
                                        //add entry to category
                                        firestoreRepository.addEntry(categoryName, "shopping session",
                                                totalCost, theDate, false, true);
                                    }
                                    else{
                                        //create category and then add entry
                                        firestoreRepository.createCategoryAndAddEntry(categoryName, "0",
                                                true, true, "shopping session",
                                                totalCost, theDate);
                                    }

                                    startActivity(new Intent(ShoppingSessionActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });

    }

    private void readData() {
        Log.i("CSV", "in read data method");
        InputStreamReader is = new InputStreamReader(getResources().openRawResource(R.raw.barcodedata)) ;
        BufferedReader reader = new BufferedReader(is);
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                Log.i("CSV", line);
                groceryStoreList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        getScanResultAndUtilize(scanResult);
    }

    private void getScanResultAndUtilize(IntentResult scanResult) {
        if (scanResult != null) {
            scannedUPC = scanResult.getContents();
            String productMatch = null;

            // TODO: DO SOMETHING WITH SCANNED UPC
            // TODO: AND REMOVE TOASTS

            for (String product : groceryStoreList) {
                if (product.matches("(.*)"+scannedUPC+"(.*)")) {
                    productMatch = product;
                    break;
                }
            }

            if(productMatch == null){
                Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            }
            else{
                String[] productInfo = productMatch.split(",");

                Log.i("PRODUCT INFO", productInfo[0] + productInfo[1] + productInfo[2] );

                firestoreRepository.addProduct(productInfo[1], Double.parseDouble(productInfo[2]), productInfo[0]);

                //increment and set the item count and cost
                itemCount++;
                totalCost += Double.parseDouble(productInfo[2]);

                sessionItemCountView.setText(String.valueOf(itemCount));
                sessionTotalCostView.setText("$"+ String.format("%.2f", totalCost));
            }
        }
        else {
            Toast.makeText(this, "ERR: SCAN FAILED", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
        month += 1;
        String date = month + "/" + day + "/" + year;
        try {
            theDate = simpleDateFormatter.parse(date);
            sessionDate.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}