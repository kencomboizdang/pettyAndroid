package com.example.petty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import adapter.CartsAdapter;
import adapter.CartsConfirmAdapter;
import dto.AddressesDTO;
import dto.CartsDTO;
import dto.OrderProductDetailsDTO;
import dto.OrderProductStoresDTO;
import dto.OrdersDTO;
import dto.ProductsDTO;
import dto.StoresDTO;
import sqlite.DatabaseHelper;
import util.DateTimeStamp;

public class ConfirmBuyingActivity extends AppCompatActivity {
    private final String ADDRESSES = "addresses";
    private TextView txtAddressName, txtAddressPhoneNumber, txtAddressDetail, txtTotal;
    private final String CARTS = "carts";
    private List<CartsDTO> cartsList= new ArrayList<>();
    private List<ProductsDTO> productsList= new ArrayList<>();
    private List<String> storesIDList= new ArrayList<>();
    private RecyclerView recyclerView;
    private CartsConfirmAdapter cartsAdapter;
    private final String ORDERS="orders";
    private final String ORDERPRODUCTSTORES= "order_product_stores";
    private final String ORDERPRODUCTDETAILS = "order_product_details";
    private String customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_buying);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        txtAddressName = (TextView) findViewById(R.id.txtAddressName);
        txtAddressPhoneNumber = (TextView) findViewById(R.id.txtAddressPhoneNumber);
        txtAddressDetail = (TextView) findViewById(R.id.txtAddressDetail);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        productsList = new ArrayList<>();
        storesIDList = new ArrayList<>();
        DatabaseHelper myDb;//
        myDb = new DatabaseHelper(this);//
        Cursor res = myDb.getKeyCustomer();
        while (res.moveToFirst()) {
            customerId = res.getString(0);
            break;
        }
        loadAddressData();
        loadCartData();
    }
    public void loadCartData(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(CARTS);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cartsList.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    CartsDTO cartsDTO = item.getValue(CartsDTO.class);

                    if (customerId.equals(cartsDTO.getCustomerId())){
                        cartsList.add(cartsDTO);
                    }
                    cartsAdapter = new CartsConfirmAdapter(cartsList,productsList, ConfirmBuyingActivity.this, txtTotal);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setAdapter(cartsAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void loadAddressData(){
        Bundle bundle= getIntent().getBundleExtra("data");
        String id = bundle.getString("id_address");

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ADDRESSES);
        mDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AddressesDTO dto = dataSnapshot.getValue(AddressesDTO.class);
                txtAddressName.setText(dto.getName());
                txtAddressPhoneNumber.setText(dto.getPhone());
                txtAddressDetail.setText(dto.getDetail()+", "+dto.getWard()+", "+dto.getDistrict()+", "+dto.getProvince());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
    public void loadStoreData(){
        if (!productsList.isEmpty()) {
            for (ProductsDTO productsDTO : productsList) {
                if (!storesIDList.isEmpty()) {
                    boolean isExisted =false;
                    for (String item : storesIDList) {
                        if (productsDTO.getStoreId().equals(item)) {
                            isExisted = true;
                        }
                    }
                    if (!isExisted){
                        storesIDList.add(productsDTO.getStoreId());
                    }
                } else {
                    storesIDList.add(productsDTO.getStoreId());
                }
            }
        }
    }

    public void clickToPaymentSuccess(View view) {
        loadStoreData();
        Bundle bundle= getIntent().getBundleExtra("data");
        String addressId = bundle.getString("id_address");
        DatabaseReference orderDatabase = FirebaseDatabase.getInstance().getReference(ORDERS);
        DatabaseReference orderProductStoreDatabase = FirebaseDatabase.getInstance().getReference(ORDERPRODUCTSTORES);
        DatabaseReference orderProductDetailDatabase = FirebaseDatabase.getInstance().getReference(ORDERPRODUCTDETAILS);
        String generateOrderID = orderDatabase.push().getKey();
        DateTimeStamp dateTimeStamp = new DateTimeStamp();
        float totalOrder=0;
        for (String storeID : storesIDList){
            String generateOrderProductStoreID =orderProductStoreDatabase.push().getKey();
            float totalOrderStore=0;
            for (ProductsDTO productsDTO: productsList){
               for (CartsDTO cartsDTO: cartsList){
                   if (cartsDTO.getProductId().equals(productsDTO.getId()) && productsDTO.getStoreId().equals(storeID)){
                       String generateOrderProductDetailID = orderProductDetailDatabase.push().getKey();
                       OrderProductDetailsDTO orderProductDetailsDTO = new OrderProductDetailsDTO(generateOrderProductDetailID, dateTimeStamp.getCurrentTime(),cartsDTO.getQuantity(), productsDTO.getPrice(), "pending", generateOrderProductStoreID, productsDTO.getId());
                       totalOrderStore+=productsDTO.getPrice()*cartsDTO.getQuantity();
                       orderProductDetailDatabase.child(generateOrderProductDetailID).setValue(orderProductDetailsDTO);
                   }
               }
            }
            OrderProductStoresDTO orderProductStoresDTO = new OrderProductStoresDTO(generateOrderProductStoreID, dateTimeStamp.getCurrentTime(), totalOrderStore, "pending", storeID, generateOrderID);
            totalOrder+=totalOrderStore;
            orderProductStoreDatabase.child(generateOrderProductStoreID).setValue(orderProductStoresDTO);
        }
        OrdersDTO ordersDTO = new OrdersDTO(generateOrderID, dateTimeStamp.getCurrentTime(),totalOrder,"pending", customerId, addressId);
        orderDatabase.child(generateOrderID).setValue(ordersDTO);
        Intent intent = new Intent(this, SuccessfulBuyingActivity.class);
        startActivity(intent);
    }
}
