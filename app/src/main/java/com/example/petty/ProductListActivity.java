package com.example.petty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
    private List<ProductsDTO> productsList= new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductsAdapter productsAdapter;
    private final String PRODUCTS = "products";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(PRODUCTS);
//        String id = mDatabase.push().getKey();
//        ProductsDTO dto = new ProductsDTO(id, "Kẹo", "ko ngon", 19888, 5 ,"6.2", "561615","Korean", 88, "active", 1085616515, 189616515, "feaffea");
//        mDatabase.child(id).setValue(dto);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    ProductsDTO productsDTO = item.getValue(ProductsDTO.class);
                    productsList.add(productsDTO);
                    productsAdapter = new ProductsAdapter(ProductListActivity.this,productsList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(productsAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
