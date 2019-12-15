package com.example.petty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import sqlite.DatabaseHelper;

public class HomePageActivity extends AppCompatActivity {
    private FrameLayout mMainFrame;
    private BottomNavigationView navigationView;
    private  HomeFragment homePageFragment;
    private AccountFragment accountFragment;

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        navigationView =  findViewById(R.id.bottom_navigation_bar);
        homePageFragment = new HomeFragment();
        accountFragment = new AccountFragment();
        setFragment(homePageFragment);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.bar_home:
                        setFragment(homePageFragment);
                        return true;
                    case R.id.bar_account:
                        setFragment(accountFragment);
                        return true;
                    default: return false;
                }
            }
        });

        myDb = new DatabaseHelper(this);
        Cursor res = myDb.getKeyCustomer();
        String keyCustomer = "";
        while (res.moveToFirst()) {
            keyCustomer = res.getString(0);
            break;
        }
        Log.d("TAG", keyCustomer);
    }
    private  void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    public void clickToSearch(View view) {
        Intent intent = new Intent(this, SearchProductActivity.class);
        startActivity(intent);
    }

    public void clickToAddressView(View view) {
        Intent intent = new Intent(this, SearchProductActivity.class);
        startActivity(intent);
    }

    public void clickToAddressList(View view) {
        Intent intent = new Intent(this, AddressListActivity.class);
        startActivity(intent);
    }

    public void clickToCart(View view) {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }

    public void clickToSignOut(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}

