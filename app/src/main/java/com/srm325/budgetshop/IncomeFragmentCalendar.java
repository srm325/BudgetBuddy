package com.srm325.budgetshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.srm325.budgetshop.model_classes.Categories;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IncomeFragmentCalendar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncomeFragmentCalendar extends Fragment {
    String TAG = IncomeFragmentCalendar.class.getSimpleName();
    RecyclerView recyclerView;
    IncomeAdapter adapter;
    FirestoreRepository firestoreRepository;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IncomeFragmentCalendar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IncomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IncomeFragmentCalendar newInstance(String param1, String param2) {
        IncomeFragmentCalendar fragment = new IncomeFragmentCalendar();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"opened Income Calendar");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_income, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestoreRepository = new FirestoreRepository();
        recyclerView = view.findViewById(R.id.income_recyclerview);

        //set up the recyclerview
        setupRecyclerview();
    }

    private void setupRecyclerview() {
        Query query = firestoreRepository.categoriesRef
                .whereEqualTo(FirestoreRepository.IS_EXPENSE_FIELD, false);


        FirestoreRecyclerOptions<Categories> options = new FirestoreRecyclerOptions.Builder<Categories>()
                .setQuery(query, Categories.class)
                .build();

        adapter = new IncomeAdapter(options);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        LinearLayoutManager verticalLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayout);
        recyclerView.setAdapter(adapter);

        //on item click get the category name and go to DisplayEntriesActivity
        adapter.setOnItemClickListener(new IncomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Categories categories = documentSnapshot.toObject(Categories.class);

                Intent intent = new Intent(getContext(), DisplayEntriesActivityCalendar.class);
                intent.putExtra(CalendarPopUpActivity.CATEGORY, categories.getCategoryName());
                startActivity(intent);
            }
        });
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
}