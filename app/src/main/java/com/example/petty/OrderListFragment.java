package com.example.petty;


import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
import dto.OrdersDTO;
import sqlite.DatabaseHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderListFragment extends Fragment {
    private final String ORDERPRODUCTSTORES = "order_product_stores";
    private final String ORDERS = "orders";
    private LinearLayout viewEmpty;
    private List<OrderProductStoresDTO> orderProductStoresList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private String customerId;

    public OrderListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        viewEmpty = (LinearLayout) view.findViewById(R.id.viewEmpty);
        DatabaseHelper myDb;//
        myDb = new DatabaseHelper(getActivity());//
        Cursor res = myDb.getKeyCustomer();
        while (res.moveToFirst()) {
            customerId = res.getString(0);
            break;
        }
        return view;
    }
    public void loadListOrder(){
        DatabaseReference orderDatabase = FirebaseDatabase.getInstance().getReference(ORDERS);
        orderDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderProductStoresList.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    final OrdersDTO ordersDTO = item.getValue(OrdersDTO.class);
                    if(ordersDTO.getCustomerId().equals(customerId)){
                        DatabaseReference orderStoresDatabase = FirebaseDatabase.getInstance().getReference(ORDERPRODUCTSTORES);
                        orderStoresDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot item : dataSnapshot.getChildren()){
                                    OrderProductStoresDTO orderProductStoresDTO = item.getValue(OrderProductStoresDTO.class);
                                    if (orderProductStoresDTO.getOrderId().equals(ordersDTO.getId()))
                                    {
                                        orderProductStoresList.add(orderProductStoresDTO);
                                    }
                                    if (!orderProductStoresList.isEmpty()) {
                                        viewEmpty.setVisibility(LinearLayout.GONE);
                                        orderAdapter = new OrderAdapter(orderProductStoresList, getActivity());
                                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                                        recyclerView.setAdapter(orderAdapter);
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("ONRESUME");
        loadListOrder();
    }
}
