package com.srm325.budgetshop;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class ScanBarcodeActivity extends AppCompatActivity  {
    private Button scanBtn, addtocartBtn;
    private TextView contentTxt, name, price;
    FirestoreRepository firestoreRepository = new FirestoreRepository();
    private String namelookup;
    private String pricelookup;

    ArrayList<String> storedContent = new ArrayList<String>();
    ArrayList<String> contentArray = new ArrayList<String>();
    ArrayList<String> barcodeData = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);
        scanBtn = findViewById(R.id.scan_button);
        addtocartBtn = findViewById(R.id.add_to_cart_button);
        contentTxt = findViewById(R.id.scan_content);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        ArrayList<String> storedContent = new ArrayList<String>();
        readData();
        scanBtn.setOnClickListener(this::onClick);
        addtocartBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //add to cart command later
            }
        });

    }
    public void onClick(View v){
        if(v.getId()==R.id.scan_button){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }
    private void readData() {
        Log.i("CSV", "in read data method");
        InputStreamReader is = new InputStreamReader(getResources().openRawResource(R.raw.barcodedata)) ;
        BufferedReader reader = new BufferedReader(is);
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                Log.i("CSV", line);
                storedContent.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    


    @SuppressLint("SetTextI18n")
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String productMatch = null;
            String scanContent = scanningResult.getContents();

            contentTxt.setText("Barcode: " + scanContent);

            Log.i("Products Arraylist", storedContent.toString());

//            String searchText = scanContent;
            for (String product : storedContent) {
                if (product.matches("(.*)"+scanContent+"(.*)")) {
                    productMatch = product;
                }
            }

            Log.i("MATCH", productMatch);

            if(productMatch == null){
                Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            }
            else{
                String[] productInfo = productMatch.split(",");

                Log.i("PRODUCT INFO", productInfo[0] + productInfo[1] + productInfo[2] );

                firestoreRepository.addProduct(productInfo[1], Double.parseDouble(productInfo[2]), productInfo[0]);
            }



//            namelookup = contentArray.get(0);
//            pricelookup = contentArray.get(0);
//            String productname = namelookup;
//            String productprice = pricelookup;
//
//            if (namelookup != null) {
//                name.setText("Product Name:"+ productname);
//                price.setText("Price: $" + productprice );
//            } else { name.setText("Product Not Found");
//                price.setText("Product Not Found");}


        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}