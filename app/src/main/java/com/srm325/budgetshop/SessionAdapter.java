package com.srm325.budgetshop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.srm325.budgetshop.model_classes.Product;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SessionAdapter extends FirestoreRecyclerAdapter<Product, SessionAdapter.SessionHolder> {

public SessionAdapter (FirestoreRecyclerOptions<Product> options){
        super(options);
        }

class SessionHolder extends RecyclerView.ViewHolder{
    TextView productName;
    TextView productID;
    TextView productPrice;

    public SessionHolder(@NonNull View itemView) {
        super(itemView);

        productName = itemView.findViewById(R.id.product_name);
        productID = itemView.findViewById(R.id.product_id);
        productPrice = itemView.findViewById(R.id.product_price);

    }
}
//What am I doing

    @Override
    protected void onBindViewHolder(@NonNull SessionAdapter.SessionHolder holder, int position, @NonNull Product model) {

        holder.productPrice.setText(String.valueOf("$" + model.getProductPrice()));

        holder.productName.setText(model.getProductName());

        holder.productID.setText(model.getProductID());



    }

    @NonNull
    @Override
    public SessionAdapter.SessionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
        return new SessionAdapter.SessionHolder(v);
    }
}