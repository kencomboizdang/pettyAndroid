package com.example.petty;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.Categories2Adapter;
import adapter.CategoriesAdapter;
import dto.CategoriesDTO;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment {

    private List<CategoriesDTO> categoriesList= new ArrayList<>();
    private RecyclerView categoryRecyclerView;
    private Categories2Adapter categories2Adapter;
    private final String CATEGORIES= "categories";

    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        categoryRecyclerView = (RecyclerView) view.findViewById(R.id.categories_recycler_view);
        loadCategory(view);
        return view;
    }
    public void loadCategory(View view){
        categoriesList.clear();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(CATEGORIES);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    CategoriesDTO categoriesDTO = item.getValue(CategoriesDTO.class);
                    categoriesList.add(categoriesDTO);
                }
                if (!categoriesList.isEmpty()) {
                    categories2Adapter= new Categories2Adapter(categoriesList, getActivity());
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false);
                    categoryRecyclerView.setLayoutManager(layoutManager);
                    categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    categoryRecyclerView.setAdapter(categories2Adapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
