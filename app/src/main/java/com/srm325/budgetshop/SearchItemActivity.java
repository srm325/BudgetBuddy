package com.srm325.budgetshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.srm325.budgetshop.model_classes.Product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SearchItemActivity extends AppCompatActivity {
    ImageView searchIcon;
    EditText searchText;
    SearchProductsAdapter adapter;
    ArrayList<String> groceryStoreList = new ArrayList<>();
    ArrayList<String> searchMatchesString = new ArrayList<>();
    ArrayList<Product> searchMatchesProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        getSupportActionBar().setTitle("");

        readData();

        searchIcon = findViewById(R.id.search_icon);
        searchText = findViewById(R.id.item_search_bar);
        FirestoreRepository firestoreRepository = new FirestoreRepository();
        RecyclerView recyclerView = findViewById(R.id.search_item_recyclerview);


        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchStore();
            }
        });

        //setup recyclerview
       adapter = new SearchProductsAdapter(searchMatchesProducts);
       recyclerView.setAdapter(adapter);
       recyclerView.setLayoutManager(new LinearLayoutManager(this));
       adapter.setOnItemClickListener(new SearchProductsAdapter.OnItemClickListener() {
           @Override
           public void onItemClick(int position) {
               Product product = searchMatchesProducts.get(position);
               firestoreRepository.addProduct(product.getProductName(), product.getProductPrice(), product.getProductID());
               Toast.makeText(SearchItemActivity.this, "Product Added", Toast.LENGTH_SHORT).show();
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

    private void searchStore() {
        Log.i("search", "in search store method");

        String productMatch = null;
        String query = searchText.getText().toString().trim();
        searchMatchesString.clear();
        searchMatchesProducts.clear();

        Log.i("query", query);
        if(query.isEmpty()){
            return;
        }

        for (String product : groceryStoreList) {
            if (product.matches("(.*)"+query+"(.*)")) {
                searchMatchesString.add(product);
                Log.i("string match", product);
            }
        }

        if(searchMatchesString.size() == 0){
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
        }
        else{
            for(String product : searchMatchesString){
                String[] productInfo = product.split(",");
                searchMatchesProducts.add(new Product(productInfo[1], Double.parseDouble(productInfo[2]), productInfo[0]));
            }

            adapter.notifyDataSetChanged();
        }
    }


}