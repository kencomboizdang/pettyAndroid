package com.example.petty;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

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

public class StoreDetailActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String STORES = "stores";
    TextView txtNameStore, txtAddress, txtAdd2;
    DatabaseReference mDatabase;
    ImageView imgStore;

    String id = "";
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        mDatabase = FirebaseDatabase.getInstance().getReference(STORES);

        txtNameStore = (TextView) findViewById(R.id.txtNameStore);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        imgStore = (ImageView) findViewById(R.id.imgStore);
        txtAdd2 = (TextView) findViewById(R.id.txtAdd2);
        Bundle bundle = getIntent().getBundleExtra("dataStore");
        id = bundle.getString("id_store");
        Log.d("TAG", id);
        mDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String ward = dataSnapshot.child("ward").getValue(String.class);
                String district = dataSnapshot.child("district").getValue(String.class);
                String provine = dataSnapshot.child("province").getValue(String.class);
                String img = dataSnapshot.child("logoImg").getValue(String.class);
                txtNameStore.setText(name);
                txtAddress.setText(ward + ", " + ", " + district + ", " + provine);
                txtAdd2.setText(ward + ", " + ", " + district + ", " + provine);
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
}
