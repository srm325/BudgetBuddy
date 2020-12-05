package com.srm325.budgetshop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.srm325.budgetshop.model_classes.Entry;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EntriesAdapter extends FirestoreRecyclerAdapter<Entry, EntriesAdapter.EntriesHolder> {

    public EntriesAdapter (FirestoreRecyclerOptions<Entry> options){
        super(options);
    }

    class EntriesHolder extends RecyclerView.ViewHolder{
        TextView date;
        TextView entryName;
        TextView amount;

        public EntriesHolder(@NonNull View itemView) {
            super(itemView);

            entryName = itemView.findViewById(R.id.entry_name);
            date = itemView.findViewById(R.id.display_entry_date);
            amount = itemView.findViewById(R.id.display_entry_amount);

        }
    }

    @Override
    protected void onBindViewHolder(@NonNull EntriesAdapter.EntriesHolder holder, int position, @NonNull Entry model) {

        holder.entryName.setText(model.getEntryDescription());

        holder.amount.setText(String.valueOf("$" + model.getEntryAmount()));

        Date date = model.getEntryDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

        holder.date.setText(simpleDateFormat.format(date));



    }

    @NonNull
    @Override
    public EntriesAdapter.EntriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entries_item_layout, parent, false);
        return new EntriesAdapter.EntriesHolder(v);
    }
}