package com.example.petty;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
    GoogleMap ggMap;

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
        // google map

        SupportMapFragment mapFragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
