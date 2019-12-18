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

    //private StoreFilterFragment storeFilterFragment;
    private RecyclerView recyclerView;
    private List<StoresDTO> listStore;
    private StoresAdapter adapter;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
//        Bundle bundle = getIntent().getBundleExtra("dataStore");
//        final String type = bundle.getString("typeStore");
//        final String value = bundle.getString("valueStore");
//        storeFilterFragment = new StoreFilterFragment(type, value);
        //setFragment(storeFilterFragment);

        recyclerView = findViewById(R.id.recycler_view_stores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listStore = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference("stores");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    StoresDTO dto = postSnapshot.getValue(StoresDTO.class);
                    listStore.add(dto);
                }
                adapter= new StoresAdapter(StoreListActivity.this, listStore);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    private void setFragment(Fragment fragment){
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
//        fragmentTransaction.replace(R.id.frame_stores, fragment);
//        fragmentTransaction.commit();
//    }

    public void clickToCart(View view) {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }

    public void clickToSearch(View view) {
        Intent intent = new Intent(this, SearchProductActivity.class);
        startActivity(intent);
    }
}
