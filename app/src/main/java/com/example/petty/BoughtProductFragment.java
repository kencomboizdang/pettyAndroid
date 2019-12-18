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
import adapter.OrderProductDetailAdapter;
import dto.OrderProductDetailsDTO;
import dto.OrderProductStoresDTO;
import dto.OrdersDTO;
import sqlite.DatabaseHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class BoughtProductFragment extends Fragment {

    private final String ORDERPRODUCTSTORES = "order_product_stores";
    private final String ORDERPRODUCTDETAILS = "order_product_details";
    private final String ORDERS = "orders";
    private LinearLayout viewEmpty;

    private List<OrderProductDetailsDTO> orderProductDetailsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OrderProductDetailAdapter orderProductDetailAdapter;
    private String customerId;
    public BoughtProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_bought_product, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_bought_product);
        viewEmpty = (LinearLayout) view.findViewById(R.id.viewEmptyBought);
        DatabaseHelper myDb;//
        myDb = new DatabaseHelper(getActivity());//
        Cursor res = myDb.getKeyCustomer();
        while (res.moveToFirst()) {
            customerId = res.getString(0);
            break;
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        loadListDetail();
        return view;
    }

    public void loadListDetail(){
        DatabaseReference orderDatabase = FirebaseDatabase.getInstance().getReference(ORDERS);
        orderDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderProductDetailsList.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    final OrdersDTO ordersDTO = item.getValue(OrdersDTO.class);
                    System.out.println(ordersDTO);
                    if(ordersDTO.getCustomerId().equals(customerId)){
                        DatabaseReference orderStoresDatabase = FirebaseDatabase.getInstance().getReference(ORDERPRODUCTSTORES);
                        orderStoresDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot item : dataSnapshot.getChildren()){
                                    final OrderProductStoresDTO orderProductStoresDTO = item.getValue(OrderProductStoresDTO.class);
                                    if (orderProductStoresDTO.getOrderId().equals(ordersDTO.getId()))
                                    {
                                        System.out.println(orderProductStoresDTO);
                                        DatabaseReference orderDatabase = FirebaseDatabase.getInstance().getReference(ORDERPRODUCTDETAILS);
                                        orderDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot item : dataSnapshot.getChildren()){
                                                    OrderProductDetailsDTO orderProductDetailsDTO = item.getValue(OrderProductDetailsDTO.class);
                                                    if (orderProductDetailsDTO.getOrderProductStoreId().equals(orderProductStoresDTO.getId()))
                                                    {
                                                        orderProductDetailsList.add(orderProductDetailsDTO);
                                                    }
                                                }
                                                if (!orderProductDetailsList.isEmpty())
                                                {
                                                    viewEmpty.setVisibility(LinearLayout.GONE);
                                                    orderProductDetailAdapter = new OrderProductDetailAdapter(orderProductDetailsList, getActivity());
                                                    recyclerView.setAdapter(orderProductDetailAdapter);
                                                }

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        }
                                        );
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

}
