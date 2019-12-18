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
import android.widget.TableRow;
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
    private TableRow btnAccountDetail;
    private RelativeLayout relativeLayout, btnChangePassword;

    DatabaseReference mDatabase;
    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        final DatabaseHelper myDb;
        myDb = new DatabaseHelper(getActivity());
        txtCustomerName = (TextView) view.findViewById(R.id.txtCustomerName);
        txtCustomerGmail = (TextView) view.findViewById(R.id.txtCustomerGmail);
        btnAccountDetail = (TableRow) view.findViewById(R.id.btnAccountDetail);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.btnSignOut);
        btnChangePassword = (RelativeLayout) view.findViewById(R.id.btnChangePassword);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.dropTable();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        btnAccountDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountDetailActivity.class);
                startActivity(intent);
            }
        });


        Cursor res = myDb.getKeyCustomer();
        String customerId = null;
        while (res.moveToFirst()) {
            customerId = res.getString(0);
            break;
        }

        Log.d("TAG", customerId);
            mDatabase = FirebaseDatabase.getInstance().getReference(CUSTOMERS);
            final String finalCustomerId = customerId;
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            if(finalCustomerId.equals(ds.child("id").getValue(String.class))) {
                                txtCustomerName.setText(ds.child("name").getValue(String.class));
                                txtCustomerGmail.setText(ds.child("email").getValue(String.class));
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());
                }
            });
        return view;
    }
}
