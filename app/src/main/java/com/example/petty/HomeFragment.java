package com.example.petty;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
public class HomeFragment extends Fragment {
    private List<ProductsDTO> productsList= new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductsHistoryAdapter productsHistoryAdapter;

    public HomeFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView txtSeeHistory = view.findViewById(R.id.tvSeeHistory);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("products");
//        String id = mDatabase.push().getKey();
//        ProductsDTO dto = new ProductsDTO(id, "thịt", "ko ngon", 19888, 5 ,"6.2", "561615","Korean", 88, "active", 1085616515, 189616515, "feaffea");
//        mDatabase.child(id).setValue(dto);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    System.out.println(item);
                    ProductsDTO productsDTO = item.getValue(ProductsDTO.class);
                    productsList.add(productsDTO);
                }
                if (!productsList.isEmpty()) {
                    productsHistoryAdapter = new ProductsHistoryAdapter(getActivity(), productsList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.HORIZONTAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(productsHistoryAdapter);
                    txtSeeHistory.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;

    }

}
