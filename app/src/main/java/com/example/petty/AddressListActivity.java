package com.example.petty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.AddressAdapter;
import adapter.ProductsAdapter;
import dto.AddressesDTO;
import dto.ProductsDTO;

public class AddressListActivity extends AppCompatActivity {
    private List<AddressesDTO> addressesList= new ArrayList<>();
    private RecyclerView recyclerView;
    private AddressAdapter addressAdapter;
    private final String ADDRESSES = "addresses";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_address);
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ADDRESSES);
//        String id = mDatabase.push().getKey();
//        AddressesDTO dto = new AddressesDTO(id,"DDD","dawdwad","dadawd","dwadawdwa","dwdad","dwadad","dwada");
//        mDatabase.child(id).setValue(dto);
        loadAddressDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAddressDatabase();
    }
    public void loadAddressDatabase(){
//        addressesList.clear();
        final TextView txtMessage = (TextView) findViewById(R.id.tvMessage);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ADDRESSES);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                addressesList.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    System.out.println(item);
                    AddressesDTO addressesDTO = item.getValue(AddressesDTO.class);
                    addressesList.add(addressesDTO);
                    if (!addressesList.isEmpty()) {
                        addressAdapter = new AddressAdapter(addressesList, AddressListActivity.this, "edit");
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(addressAdapter);
                    } else{
                        txtMessage.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void clickToAddAddress(View view) {
        Intent intent = new Intent(AddressListActivity.this, AddressDetailActivity.class);
        startActivity(intent);
    }
}
