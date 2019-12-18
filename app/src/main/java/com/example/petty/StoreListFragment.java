package com.example.petty;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

import adapter.StoresAdapter;
import dto.StoresDTO;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoreListFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<StoresDTO> listStore;
    private StoresAdapter adapter;
    private DatabaseReference mDatabase;
    private final String STORES = "stores";
    public StoreListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_list, container, false);
        recyclerView =  view.findViewById(R.id.recycler_view_stores);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listStore = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference(STORES);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    StoresDTO dto = postSnapshot.getValue(StoresDTO.class);
                    listStore.add(dto);
                }
                adapter= new StoresAdapter(getContext(), listStore);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

}
