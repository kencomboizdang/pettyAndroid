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

import dto.CustomersDTO;
import sqlite.DatabaseHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private final String CUSTOMERS = "customers";
    DatabaseReference mDatabase;
    private TextView txtCustomerName, txtCustomerGmail;
    private TableRow btnAccountDetail;
    private RelativeLayout relativeLayout, btnChangePassword;

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

        Cursor res = myDb.getKeyCustomer();
        String customerId = null;
        while (res.moveToFirst()) {
            customerId = res.getString(0);
            break;
        }

        Log.d("TAG", customerId);
        mDatabase = FirebaseDatabase.getInstance().getReference(CUSTOMERS);
        final String finalCustomerId = customerId;
        mDatabase.child(customerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (finalCustomerId.equals(dataSnapshot.child("id").getValue(String.class))) {
                    txtCustomerName.setText(dataSnapshot.child("name").getValue(String.class));
                    txtCustomerGmail.setText(dataSnapshot.child("email").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());
            }
        });


        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.dropTable1();
                myDb.dropTable2();
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

        return view;


    }
}
