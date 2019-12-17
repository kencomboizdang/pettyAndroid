package com.example.petty;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dto.AddressesDTO;
import dto.CustomersDTO;
import sqlite.DatabaseHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private TextView txtCustomerName, txtCustomerGmail;
    private final String CUSTOMERS ="customers";
    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        txtCustomerName = (TextView) view.findViewById(R.id.txtCustomerName);
        txtCustomerGmail = (TextView) view.findViewById(R.id.txtCustomerGmail);
        DatabaseHelper myDb;//
        myDb = new DatabaseHelper(getActivity());//
        Cursor res = myDb.getKeyCustomer();
        String customerId = null;
        while (res.moveToFirst()) {
            customerId = res.getString(0);
            break;
        }
        if (customerId!=null){
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(CUSTOMERS);
            mDatabase.child(customerId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    CustomersDTO dto = dataSnapshot.getValue(CustomersDTO.class);
                    if (dto != null) {
                        txtCustomerName.setText(dto.getName());
                        txtCustomerGmail.setText(dto.getEmail());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());
                }
            });
        }



        return view;

    }


}
