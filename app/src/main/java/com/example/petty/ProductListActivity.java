package com.example.petty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.ProductsAdapter;
import dto.ProductsDTO;

public class ProductListActivity extends AppCompatActivity {
    private ProductFilterFragment productFilterFragment ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Bundle bundle= getIntent().getBundleExtra("data");
        final String type = bundle.getString("type");
        final String value = bundle.getString("value");
        productFilterFragment = new ProductFilterFragment(type,value);
        setFragment(productFilterFragment);
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_products, fragment);
        fragmentTransaction.commit();
    }

    public void clickToSearch(View view) {
        Intent intent = new Intent(this, SearchProductActivity.class);
        startActivity(intent);
    }

    public void clickToBack(View view) {
        finish();
    }

    public void clickToCart(View view) {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }
}
