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

import adapter.ProductsAdapter;
import adapter.ProductsHistoryAdapter;
import dto.ProductsDTO;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFilterFragment extends Fragment {
    private String type;
    private String value;
    final String PRODUCTS = "products";

    private List<ProductsDTO> productsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductsAdapter productsAdapter;

    public ProductFilterFragment() {
        // Required empty public constructor
    }

    public ProductFilterFragment(String type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_filter, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(PRODUCTS);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    ProductsDTO productsDTO = item.getValue(ProductsDTO.class);
                    if (type.equals("search")) {
                        if (productsDTO.getName().toLowerCase().contains(value.toLowerCase()))
                            productsList.add(productsDTO);
                    }
                    if (type.equals("category")) {
                        if (productsDTO.getCategoriesId().contains(value))
                            productsList.add(productsDTO);
                    }
                }
                if (!productsList.isEmpty()) {
                    System.out.println(productsList.size());
                    productsAdapter = new ProductsAdapter(getActivity(), productsList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(productsAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

}
