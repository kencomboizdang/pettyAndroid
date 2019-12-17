package com.example.petty;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import sqlite.DatabaseHelper;


public class UpdateDatePickerFragment extends DialogFragment {
    public static String date;
    private TextView textView;

    private static final String CUSTOMER = "customers";
    DatabaseReference mDatabase;
    DatabaseHelper dbSqlite;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

//        mDatabase = FirebaseDatabase.getInstance().getReference(CUSTOMER);
//        dbSqlite = new DatabaseHelper(getActivity());
//        Cursor res = dbSqlite.getKeyCustomer();
//        String customerId = null;
//        while (res.moveToFirst()) {
//            customerId = res.getString(0);
//            break;
//        }

//        mDatabase.child(customerId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
//                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
//                        String date = ds.child("dayOfBirth").getValue(String.class);
//                        String[] arr = date.split("/");
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR) - 18;
        int month = cal.get(Calendar.MONTH) +1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                textView.setText(i2+"/"+(i1+1)+"/"+i);
            }
        }, year, month, day);
    }

    public UpdateDatePickerFragment(TextView t) {
        textView = t;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_date_picker, container, false);
    }
}
