package com.example.petty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import adapter.CartsAdapter;
import adapter.ProductsAdapter;
import dto.CartsDTO;
import dto.ProductsDTO;
import sqlite.DatabaseHelper;

public class CartActivity extends AppCompatActivity {
    private List<CartsDTO> cartsList= new ArrayList<>();
    private RecyclerView recyclerView;
    private CartsAdapter cartsAdapter;
    private LinearLayout linearEmptyCart;
    private TextView txtTotal;
    private final String PRODUCTS = "products";
    private final String CARTS = "carts";
    private Button btnBuying;
    private String customerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        linearEmptyCart = (LinearLayout) findViewById(R.id.viewEmptyCart);
        txtTotal =(TextView) findViewById(R.id.txtTotal);
        btnBuying = (Button) findViewById(R.id.btnBuying);
        loadCartData();
    }
    public void loadCartData(){
        DatabaseHelper myDb;//
        myDb = new DatabaseHelper(this);//
        Cursor res = myDb.getKeyCustomer();
        while (res.moveToFirst()) {
            customerId = res.getString(0);
            break;
        }
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(CARTS);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cartsList.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    CartsDTO cartsDTO = item.getValue(CartsDTO.class);
                    //EDIT
                    if (customerId.equals(cartsDTO.getCustomerId())){
                        cartsList.add(cartsDTO);
                    }
                    cartsAdapter = new CartsAdapter(cartsList, CartActivity.this, txtTotal, linearEmptyCart, btnBuying);
                    cartsAdapter.notifyDataSetChanged();
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(cartsAdapter);
                }
                if (!cartsList.isEmpty()){
                    linearEmptyCart.setVisibility(LinearLayout.GONE);
                } else {
                    btnBuying.setBackground(getDrawable(R.drawable.btn_white_stroke_grey));
                    btnBuying.setText("VUI LÒNG TIẾP TỤC MUA HÀNG");
                    btnBuying.setEnabled(false);
                    btnBuying.setTextColor(getResources().getColor(R.color.colorGrey));
                }
                btnBuying.setVisibility(Button.VISIBLE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void clickToBack(View view) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void clickToAddress(View view) {

            Intent intent = new Intent(CartActivity.this, AddressBuyingActivity.class);
            startActivity(intent);
        }
}
