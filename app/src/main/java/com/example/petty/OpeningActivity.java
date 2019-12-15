package com.example.petty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;

import sqlite.DatabaseHelper;

public class OpeningActivity extends AppCompatActivity {

    DatabaseHelper myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);
        myDB = new DatabaseHelper(this);

        final Cursor res = myDB.checkData();
        final Cursor resCus = myDB.getKeyCustomer();


        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(res.getCount() == 0) {
                    Intent intent = new Intent(OpeningActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else if(resCus.getCount() == 0){
                    Intent intent = new Intent(OpeningActivity.this, CustomerActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent=new Intent(OpeningActivity.this, HomePageActivity.class);
                    startActivity(intent);
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        },1800);
    }
}
