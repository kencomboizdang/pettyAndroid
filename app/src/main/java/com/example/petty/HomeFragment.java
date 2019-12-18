package com.example.petty;


import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
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
import java.util.Timer;
import java.util.TimerTask;

import adapter.BannerAdapter;
import adapter.CategoriesAdapter;
import adapter.ProductsHistoryAdapter;
import adapter.Stores2Adapter;
import dto.CategoriesDTO;
import dto.HistoriesDTO;
import dto.ImagesDTO;
import dto.ProductsDTO;
import dto.StoresDTO;
import dto.User;
import sqlite.DatabaseHelper;
import util.DateTimeStamp;


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
    private final String IMAGES= "images";
    ViewPager viewPager;
    BannerAdapter bannerAdapter;
    List<String> imageList = new ArrayList<>();
    private Timer timer;
    private int currentPosition = 0;
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
        viewPager = view.findViewById(R.id.viewPagerHome);
        ImageView imgQR = (ImageView) view.findViewById(R.id.imgQRCode);
//        loadHistory(view);
        loadStore(view);
        loadCategory(view);
        loadBanner(view);
        createSlideShow();
//        /ing id = mDatabase.push().getKey(); // random key
//        ImagesDTO dto = new ImagesDTO(id,"https://firebasestorage.googleapis.com/v0/b/petty-418a3.appspot.com/o/images%2Fbanner_1.png?alt=media&token=c4fca6b7-8c07-4857-bca4-ba8804801df7");
//        DateTimeStamp dateTimeStamp = new DateTimeStamp();
//        String name ="Lồng vận chuyển máy bay cho chó mèo IRIS size XS";
//        String description ="Lồng vận chuyển máy bay cho chó mèo IRIS size XS màu sắc ngẫu nhiên";
//        float price=67000;
//        int quantity =100;
//        String size="150g";
//        String img="https://firebasestorage.googleapis.com/v0/b/petty-418a3.appspot.com/o/images%2Faccessory_cat_001.PNG?alt=media&token=32b12170-13ab-4cb9-8016-d135fbfccd43";
//        String origin="UK";
//        String brand="IRIS";
//        float netWeight = 100;
//        String status="buying";
//        long startDate=dateTimeStamp.getCurrentTime();
//        long expiration=dateTimeStamp.getCurrentTime();
//        String categoriesId="-LwI9yQ_8ZQoWjCyzy8Y";
//        String storeId="-Lvtnyxt2wnX8R5QNab3";
//
//        ProductsDTO dto = new ProductsDTO(id, name, description,price , quantity,  size,  img,  origin,  brand,  netWeight,  status,  startDate,  expiration,  categoriesId,  storeId);
//        mDatabase.child(id).setValue(dto);

        return view;
    }
    public void loadBanner(final View view){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(IMAGES);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()){
                   ImagesDTO imagesDTO = item.getValue(ImagesDTO.class);
                   imageList.add(imagesDTO.getImg());
                }
                if (!imageList.isEmpty()){
                    bannerAdapter = new BannerAdapter(imageList, view.getContext());
                    viewPager.setAdapter(bannerAdapter);
                    viewPager.setPadding(10, 0, 10, 0);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void createSlideShow() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(currentPosition==imageList.size()-1) { ;
                    currentPosition = 0;
                    viewPager.setCurrentItem(currentPosition,true);
                }
                viewPager.setCurrentItem(currentPosition++,true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 2000, 4000);
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
                                   Collections.reverse(productsList);
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
        loadHistory(getView());
    }
}
