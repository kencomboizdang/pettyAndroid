package com.example.petty;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dto.ProductsDTO;

public class StoreDetailActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String STORES = "stores";
    TextView txtNameStore, txtAddress, txtEmail, txtPhone;
    DatabaseReference mDatabase;
    ImageView imgStore;
    private RecyclerView recyclerView;
    String id = "";
    GoogleMap ggMap;
    private ProductFilterFragment productFilterFragment ;

    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        Bundle bundle = getIntent().getBundleExtra("dataStore");
        id = bundle.getString("id_store");
        productFilterFragment = new ProductFilterFragment("store", id);
        recyclerView = findViewById(R.id.recycler_view_product);

        mDatabase = FirebaseDatabase.getInstance().getReference(STORES);

        txtNameStore = (TextView) findViewById(R.id.txtNameStore);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtPhone = (TextView) findViewById(R.id.txtPhone);
        imgStore = (ImageView) findViewById(R.id.imgStore);
        setFragment(productFilterFragment);
        mDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String ward = dataSnapshot.child("ward").getValue(String.class);
                String district = dataSnapshot.child("district").getValue(String.class);
                String provine = dataSnapshot.child("province").getValue(String.class);
                String img = dataSnapshot.child("logoImg").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String phone = dataSnapshot.child("phone").getValue(String.class);
                txtNameStore.setText(name);
                txtAddress.setText(ward + ", " + ", " + district + ", " + provine);
                txtEmail.setText(email);
                txtPhone.setText(phone);
                Glide.with(StoreDetailActivity.this).load(img).into(imgStore);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

//    private void fetchLastLocation() {
//        Task<Location> task = fusedLocationProviderClient.getLastLocation()
//    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                map = googleMap;
                double x = dataSnapshot.child("positionX").getValue(double.class);
                double y = dataSnapshot.child("positionY").getValue(double.class);
                LatLng storeXY = new LatLng(x, y);
                map.addMarker(new MarkerOptions().position(storeXY).title("Ho Chi Minh"));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(storeXY, 20));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_products, fragment);
        fragmentTransaction.commit();
    }

    public void clickToBack(View view) {
        finish();
    }

    public void clickToSearch(View view) {
        Intent intent= new Intent(this, SearchProductActivity.class);
        startActivity(intent);
    }

    public void clickToCart(View view) {
        Intent intent= new Intent(this, CartActivity.class);
        startActivity(intent);
    }
    public void loadProduct(){

    }

    public void clickToCall(View view) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", txtPhone.getText().toString(), null)));
    }

    public void clickToDirection(View view) {
    }
}
