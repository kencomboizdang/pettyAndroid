package com.example.petty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.StoresAdapter;
import dto.StoresDTO;

public class StoreListActivity extends AppCompatActivity {

    private StoreListFragment storeListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        storeListFragment = new StoreListFragment();
        setFragment(storeListFragment);
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_stores, fragment);
        fragmentTransaction.commit();
    }

    public void clickToCart(View view) {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }

    public void clickToSearch(View view) {
        Intent intent = new Intent(this, SearchProductActivity.class);
        startActivity(intent);
    }
}
