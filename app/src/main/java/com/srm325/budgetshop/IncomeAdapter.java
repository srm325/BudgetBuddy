package com.srm325.budgetshop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.srm325.budgetshop.model_classes.Categories;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class IncomeAdapter extends FirestoreRecyclerAdapter<Categories, IncomeAdapter.IncomeHolder> {

    private IncomeAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(IncomeAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public IncomeAdapter (FirestoreRecyclerOptions<Categories> options){
        super(options);
    }

    class IncomeHolder extends RecyclerView.ViewHolder{
        ImageView categoryIcon;
        TextView categoryName;
        TextView percentage;
        TextView amount;

        public IncomeHolder(@NonNull View itemView) {
            super(itemView);

            categoryIcon = itemView.findViewById(R.id.inc_category_img);
            categoryName = itemView.findViewById(R.id.inc_category_txt);
            percentage = itemView.findViewById(R.id.percentage_txt);
            amount = itemView.findViewById(R.id.inc_amount_txt);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull IncomeAdapter.IncomeHolder holder, int position, @NonNull Categories model) {

        holder.categoryName.setText(model.getCategoryName());

        if(model.getCategoryPercentage() == null){
            holder.percentage.setText("0%");
        }
        else{
            holder.percentage.setText(model.getCategorySpent() + "%");
        }

        if(model.getCategoryAmount() == null){
            holder.amount.setText("0");
        }
        else{
            holder.amount.setText(model.getCategoryAmount());
        }



        switch(model.getCategoryName()){
            case "Other":
                holder.categoryIcon.setImageResource(R.drawable.ic_other_category);
                break;
            case "Paycheck":
                holder.categoryIcon.setImageResource(R.drawable.ic_paycheck_category);
                break;
            case "Gift":
                holder.categoryIcon.setImageResource(R.drawable.ic_gift_category);
                break;
            case "Interest":
                holder.categoryIcon.setImageResource(R.drawable.ic_interest_category);
                break;

        }
    }

    @NonNull
    @Override
    public IncomeAdapter.IncomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_item_layout, parent, false);
        return new IncomeAdapter.IncomeHolder(v);
    }
}
