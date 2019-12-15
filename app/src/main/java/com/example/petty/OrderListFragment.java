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

import adapter.OrderAdapter;
import adapter.ProductsAdapter;
import dto.OrderProductStoresDTO;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderListFragment extends Fragment {
    private String type;
    private String value;
    final String ORDERPRODUCTSTORES = "orderProductStores";
    private List<OrderProductStoresDTO> orderProductStoresList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;

    public OrderListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ORDERPRODUCTSTORES);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    OrderProductStoresDTO orderProductStoresDTO = item.getValue(OrderProductStoresDTO.class);
                            orderProductStoresList.add(orderProductStoresDTO);
                }
                if (!orderProductStoresList.isEmpty()) {
                    System.out.println(orderProductStoresList.size());
                    orderAdapter = new OrderAdapter(orderProductStoresList, getActivity());
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(orderAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

}
