package com.example.petty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import adapter.ProductsAdapter;
import dto.ProductsDTO;

public class ProductListActivity extends AppCompatActivity {
    private List<ProductsDTO> productsList= new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        prepareData();
        productsAdapter = new ProductsAdapter(ProductListActivity.this,productsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(productsAdapter);
    }
    public void prepareData(){
        productsList.add(new ProductsDTO("aaa",156156));
        productsList.add(new ProductsDTO("aaa",156156));

    }
}
