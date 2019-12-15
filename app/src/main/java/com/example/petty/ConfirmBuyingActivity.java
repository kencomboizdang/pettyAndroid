package com.example.petty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

import dto.AddressesDTO;
import dto.ProductsDTO;

public class ConfirmBuyingActivity extends AppCompatActivity {
    private final String ADDRESSES = "addresses";
    private TextView txtAddressName, txtAddressPhoneNumber, txtAddressDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_buying);
        Bundle bundle= getIntent().getBundleExtra("data");
        String id = bundle.getString("id_product");
        txtAddressName = (TextView) findViewById(R.id.txtAddressName);
        txtAddressPhoneNumber = (TextView) findViewById(R.id.txtAddressPhoneNumber);
        txtAddressDetail = (TextView) findViewById(R.id.txtAddressDetail);
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
}
