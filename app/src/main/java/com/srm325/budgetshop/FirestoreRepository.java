package com.srm325.budgetshop;

import android.util.Log;

import androidx.annotation.NonNull;

import com.srm325.budgetshop.model_classes.Categories;
import com.srm325.budgetshop.model_classes.Entry;
import com.srm325.budgetshop.model_classes.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class FirestoreRepository {
    public static final String TAG = FirestoreRepository.class.getSimpleName();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference categoriesRef = db.collection("categories");
    final CollectionReference sessionsRef = db.collection("sessions");
    public static final String ENTRIES = "entries";
    public static final String CATEGORY_NAME_FIELD = "categoryName";
    public static final String IS_EXPENSE_FIELD = "expense";
    public static final String ENTRY_DATE_FIELD = "entryDate";

    public void createCategoryAndAddEntry(String categoryName, String categoryBudget, boolean hasContent, boolean isExpense,  String entryDescription, double entryAmount, Date entryDate ){
        Categories category = new Categories(categoryName, categoryBudget, hasContent, isExpense);
//What am I doing
        if(isExpense){
            category.setCategorySpent(String.valueOf(entryAmount));
            category.setCategoryRemaining("0");
        }
        else{
            category.setCategoryAmount(String.valueOf(entryAmount));
        }

        categoriesRef.document(categoryName)
                .set(category)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Category added");
                        //add the entry to the created category
                        addEntry(categoryName, entryDescription, entryAmount, entryDate, true, isExpense);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });

    }
    public void addEntry(String categoryName, String entryDescription, double entryAmount, Date entryDate, boolean firstEntry, boolean isExpense){
        Entry entry = new Entry(entryDescription, entryAmount, entryDate);

        categoriesRef.document(categoryName)
                .collection(ENTRIES)
                .add(entry)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i(TAG, "Entry added");
                        //if this is not the first entry, update the spend value
                        if(!firstEntry && isExpense){
                            updateSpent(categoryName, entryAmount);
                        }
                        else if(!firstEntry && !isExpense){
                            updateAmount(categoryName, entryAmount);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    private void updateAmount(String categoryName, double entryAmount) {
        categoriesRef.document(categoryName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Categories categories = documentSnapshot.toObject(Categories.class);

                        double oldAmount = Double.parseDouble(categories.getCategoryAmount());

                        double newAmount = oldAmount + entryAmount;

                        categoriesRef.document(categoryName)
                                .update("categoryAmount", String.valueOf(newAmount))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.i(TAG, "Amount updated");
                                    }
                                });
                    }
                });
    }

    public void updateBudget(String categoryName, String budgetValue){
        categoriesRef.document(categoryName)
                .update("categoryBudget", budgetValue)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Budget updated");
                    }
                });
    }

    public void updateSpent(String categoryName, double spent){
        categoriesRef.document(categoryName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Categories categories = documentSnapshot.toObject(Categories.class);

                        double oldSpent = Double.parseDouble(categories.getCategorySpent());

                        double newSpent = oldSpent + spent;

                        categoriesRef.document(categoryName)
                                .update("categorySpent", String.valueOf(newSpent))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.i(TAG, "Spent updated");
                                    }
                                });
                    }
                });

    }

    public void addProduct(String productName, Double productPrice, String productID){
        Product product = new Product(productName, productPrice, productID);

        sessionsRef.add(product)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i(TAG, "product added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }
}
