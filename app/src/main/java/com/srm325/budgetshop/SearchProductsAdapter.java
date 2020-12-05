package com.srm325.budgetshop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.srm325.budgetshop.model_classes.Product;

import java.util.ArrayList;

public class SearchProductsAdapter extends  RecyclerView.Adapter<SearchProductsAdapter.SearchProductsViewHolder> {
    private ArrayList<Product> mProducts = new ArrayList<>();

    private SearchProductsAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(SearchProductsAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public SearchProductsAdapter(ArrayList<Product> products) {
        mProducts = products;
    }

    public class SearchProductsViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productID;
        TextView productPrice;

        public SearchProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productID = itemView.findViewById(R.id.product_id);
            productPrice = itemView.findViewById(R.id.product_price);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public SearchProductsAdapter.SearchProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
        SearchProductsViewHolder viewHolder = new SearchProductsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchProductsAdapter.SearchProductsViewHolder holder, int position) {
        Product product = mProducts.get(position);

        holder.productPrice.setText(String.valueOf("$" + product.getProductPrice()));

        holder.productName.setText(product.getProductName());

        holder.productID.setText(product.getProductID());
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }
}