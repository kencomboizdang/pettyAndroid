package com.example.petty;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import sqlite.DatabaseHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private RelativeLayout btnSignOut;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        DatabaseHelper myDb;//
        myDb = new DatabaseHelper(getActivity());//
        Cursor res = myDb.getKeyCustomer();
        String accountId = "";
        while (res.moveToFirst()) {
            accountId = res.getString(0);
            break;
        }
        System.out.println(accountId);
        return view;
    }


}
