package com.example.petty;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dto.StoresDTO;

public class StoreDetailActivity extends AppCompatActivity {

    TextView txtNameStore, txtAddress, txtAdd2;
    private static final String STORES = "stores";
    DatabaseReference mDatabase;
    ImageView imgStore;

    String id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        mDatabase = FirebaseDatabase.getInstance().getReference(STORES);

        txtNameStore = (TextView) findViewById(R.id.txtNameStore);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        imgStore = (ImageView) findViewById(R.id.imgStore);
        txtAdd2 = (TextView) findViewById(R.id.txtAdd2);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        StoresDTO dto = ds.child(id).getValue(StoresDTO.class);
                        String name = ds.child("name").getValue(String.class);
                        String ward = ds.child("ward").getValue(String.class);
                        String district = ds.child("district").getValue(String.class);
                        String provine = ds.child("province").getValue(String.class);
                        String img = ds.child("logoImg").getValue(String.class);
                        txtNameStore.setText(name);
                        txtAddress.setText(ward + ", " + ", " + district + ", " + provine);
                        txtAdd2.setText(ward + ", " + ", " + district + ", " + provine);
                        Glide.with(StoreDetailActivity.this).load(img).into(imgStore);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        loadStore();
    }

    public void loadStore() {
        Bundle bundle = getIntent().getBundleExtra("dataStore");
        id = bundle.getString("id_store");
        Log.d("TAG", id);
    }
}
