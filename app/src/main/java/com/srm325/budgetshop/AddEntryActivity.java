package com.srm325.budgetshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEntryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Date entryDate;
    SimpleDateFormat simpleDateFormatter;
    TextView dateTxtView;
    FirestoreRepository firestoreRepository;
    boolean isExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        //initialize view and global variables
        //What am I doing
        TextView entryTypeTxtView = findViewById(R.id.add_entry_title);
        TextView categoryTypeTxtView = findViewById(R.id.choose_category);
        EditText description = findViewById(R.id.entry_description);
        EditText amount = findViewById(R.id.entry_amount);
        dateTxtView = findViewById(R.id.entry_date);
        Button save = findViewById(R.id.entry_save_button);
        firestoreRepository = new FirestoreRepository();
        entryDate = new Date();
        simpleDateFormatter = new SimpleDateFormat("MM/dd/yyyy");


        //get the intent and then get the category name and entry type
        Intent intent = getIntent();
        String entryType = intent.getStringExtra(MainActivity.ENTRY_TYPE);
        String categoryName = intent.getStringExtra(MainActivity.CATEGORY);

        //initialize isExpense variable
        if(entryType.equalsIgnoreCase("expense")){
            isExpense = true;
        }
        else{
            isExpense = false;
        }

        //set date, entry, and entry title
        entryTypeTxtView.setText("Add " + entryType);
        categoryTypeTxtView.setText(categoryName);
        dateTxtView.setText(simpleDateFormatter.format(entryDate));

        //on click of categoryType textview open corresponding category activity
        categoryTypeTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(entryType.equalsIgnoreCase("expense")){
                    startActivity(new Intent(AddEntryActivity.this, ExpenseCategoryActivity.class));

                }
                else{
                    startActivity(new Intent(AddEntryActivity.this, IncomeCategoryActivity.class));
                }
                finish();
            }

        });

        //on click of the the date textview open datepickerdialog
        dateTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.srm325.budgetshop.DatePicker datePicker = new com.srm325.budgetshop.DatePicker();
                datePicker.show(getSupportFragmentManager(), "Date Picker");
            }
        });

        //on click of save button save the entry information to database
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if the edit texts are empty
                if(description.getText().toString().trim().isEmpty()){
                    Toast.makeText(AddEntryActivity.this, "enter valid description", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(amount.getText().toString().trim().isEmpty()){
                    Toast.makeText(AddEntryActivity.this, "enter valid amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                //check if the category already exists in the database and act accordingly
                DocumentReference documentReference = firestoreRepository.categoriesRef.document(categoryName);

                documentReference.get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();

                                    if(document.exists()){
                                        //add entry to category
                                        firestoreRepository.addEntry(categoryName, description.getText().toString(),
                                                Double.parseDouble(amount.getText().toString()), entryDate, false, isExpense);
                                    }
                                    else{
                                        //create category and then add entry
                                        firestoreRepository.createCategoryAndAddEntry(categoryName, "0",
                                                true, isExpense,  description.getText().toString(),
                                                Double.parseDouble(amount.getText().toString()), entryDate);
                                    }

                                    startActivity(new Intent(AddEntryActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });
    }



    //set the date variable and textview on date change
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month += 1;
        String date = month + "/" + day + "/" + year;
        try {
            entryDate = simpleDateFormatter.parse(date);
            dateTxtView.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}