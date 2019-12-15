package com.example.petty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class CartActivity extends AppCompatActivity {
    private List<CartsDTO> cartsList= new ArrayList<>();
    private RecyclerView recyclerView;
    private CartsAdapter cartsAdapter;
    private LinearLayout linearLayout;
    private TextView txtTotal;
    private final String PRODUCTS = "products";
    private final String CARTS = "carts";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        linearLayout = (LinearLayout) findViewById(R.id.mainLayout);
        txtTotal =(TextView) findViewById(R.id.txtTotal);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadCartData();
            }
        });
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
                    //EDIT
                    if (true){
                        cartsList.add(cartsDTO);
                    }
                    cartsAdapter = new CartsAdapter(cartsList, CartActivity.this, txtTotal);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(cartsAdapter);
                }
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
        System.out.println("Aaaa");
    }

    public void clickToAddress(View view) {
        Intent intent = new Intent(CartActivity.this, AddressBuyingActivity.class);
        startActivity(intent);
    }
}
