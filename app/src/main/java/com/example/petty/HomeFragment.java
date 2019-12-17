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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapter.CategoriesAdapter;
import adapter.ProductsHistoryAdapter;
import adapter.Stores2Adapter;
import dto.CategoriesDTO;
import dto.HistoriesDTO;
import dto.ProductsDTO;
import dto.StoresDTO;
import dto.User;
import sqlite.DatabaseHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private List<ProductsDTO> productsList= new ArrayList<>();
    private List<StoresDTO> storesList= new ArrayList<>();
    private List<CategoriesDTO> categoriesList= new ArrayList<>();
    private RecyclerView historyRecyclerView, storeRecyclerView, categoryRecyclerView;
    private ProductsHistoryAdapter productsHistoryAdapter;
    private Stores2Adapter stores2Adapter;
    private CategoriesAdapter categoriesAdapter;
    private String customerId = null;
    private final String PRODUCTS= "products";
    private final String STORES= "stores";
    private final String CATEGORIES= "categories";
    private final String HISTORIES= "histories";
    public HomeFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        DatabaseHelper myDb;//
        myDb = new DatabaseHelper(getActivity());//
        Cursor res = myDb.getKeyCustomer();

        while (res.moveToFirst()) {
            customerId = res.getString(0);
            break;
        }
        historyRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        storeRecyclerView = (RecyclerView) view.findViewById(R.id.store_recycler_view);
        categoryRecyclerView = (RecyclerView) view.findViewById(R.id.categories_recycler_view);
        ImageView imgQR = (ImageView) view.findViewById(R.id.imgQRCode);
        loadHistory(view);
        loadStore(view);
        loadCategory(view);
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("categories");
//        String userId = mDatabase.push().getKey(); // random key
//        User user = new User("Ravi Tamada", "ravi@androiddhive.info");
//        CategoriesDTO dto = new CategoriesDTO(userId, "Phụ kiện thú cưng","","https://firebasestorage.googleapis.com/v0/b/petty-418a3.appspot.com/o/images%2Ficon_category_assessory.png?alt=media&token=e45f796e-3c96-4e63-ac07-9825e78a929f");
//        mDatabase.child(userId).setValue(dto);



        return view;
    }
    public void loadHistory(View view){
        productsList.clear();
        final TextView txtSeeHistory = view.findViewById(R.id.tvSeeHistory);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(HISTORIES);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    final HistoriesDTO historiesDTO = item.getValue(HistoriesDTO.class);
                   if (historiesDTO.getCustomersId().equals(customerId)){
                       DatabaseReference productDatabase = FirebaseDatabase.getInstance().getReference(PRODUCTS);
                       productDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {
                               for (DataSnapshot item : dataSnapshot.getChildren()){
                                   ProductsDTO productsDTO = item.getValue(ProductsDTO.class);
                                   if (productsDTO.getId().equals(historiesDTO.getProductsId())){
                                        productsList.add(productsDTO);
                                   }
                               }
                               if (!productsList.isEmpty()) {
//                                   Collections.sort(productsList, Collections.reverseOrder());
                                   productsHistoryAdapter = new ProductsHistoryAdapter(getActivity(), productsList);
                                   RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.HORIZONTAL, false);
                                   historyRecyclerView.setLayoutManager(layoutManager);
                                   historyRecyclerView.setItemAnimator(new DefaultItemAnimator());
                                   historyRecyclerView.setAdapter(productsHistoryAdapter);
                                   txtSeeHistory.setVisibility(View.VISIBLE);
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
    public void loadStore(View view){
        storesList.clear();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(STORES);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    StoresDTO storesDTO = item.getValue(StoresDTO.class);
                    storesList.add(storesDTO);
                }
                if (!storesList.isEmpty()) {
                    stores2Adapter = new Stores2Adapter(storesList, getActivity());
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.HORIZONTAL, false);
                    storeRecyclerView.setLayoutManager(layoutManager);
                    storeRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    storeRecyclerView.setAdapter(stores2Adapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                     categoriesAdapter= new CategoriesAdapter(categoriesList, getActivity());
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.HORIZONTAL, false);
                    categoryRecyclerView.setLayoutManager(layoutManager);
                    categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    categoryRecyclerView.setAdapter(categoriesAdapter);
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
    }
}
