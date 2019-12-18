package com.example.petty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.AddressAdapter;
import dto.AddressesDTO;
import sqlite.DatabaseHelper;

public class AddressBuyingActivity extends AppCompatActivity {
    private List<AddressesDTO> addressesList= new ArrayList<>();
    private RecyclerView recyclerView;
    private AddressAdapter addressAdapter;
    private final String ADDRESSES = "addresses";
    private String customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_buying);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_address);
        loadAddressDatabase();
    }
    public void loadAddressDatabase(){
        DatabaseHelper myDb;//
        myDb = new DatabaseHelper(this);//
        Cursor res = myDb.getKeyCustomer();
        while (res.moveToFirst()) {
            customerId = res.getString(0);
            break;
        }

    }
    public void loadAddress(){
        final LinearLayout viewEmpty = (LinearLayout) findViewById(R.id.viewEmpty);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ADDRESSES);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                addressesList.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    AddressesDTO addressesDTO = item.getValue(AddressesDTO.class);
                    if (customerId.equals(addressesDTO.getCustomerId())) {
                        addressesList.add(addressesDTO);
                    }
                    if (!addressesList.isEmpty()) {
                        addressAdapter = new AddressAdapter(addressesList, AddressBuyingActivity.this,"buying");
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(addressAdapter);
                        viewEmpty.setVisibility(LinearLayout.GONE);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void clickToAddAddress(View view) {
        Intent intent = new Intent(AddressBuyingActivity.this, AddressDetailActivity.class);
        startActivity(intent);

    }

    public void clickToBack(View view) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAddress();
    }
}
