package com.example.petty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import adapter.ProductsAdapter;
import adapter.ResponsesAdapter;
import dto.AddressesDTO;
import dto.CartsDTO;
import dto.HistoriesDTO;
import dto.OrderProductDetailsDTO;
import dto.ProductsDTO;
import dto.ResponsesDTO;
import dto.StoresDTO;
import dto.User;
import sqlite.DatabaseHelper;
import util.DateTimeStamp;

public class ProductDetailActivity extends AppCompatActivity {
    final String PRODUCTS = "products";
    final String STORES = "stores";
    final String CARTS = "carts";
    final String RESPONSES = "responses";
    final String ORDERPRODUCTDETAILS = "order_product_details";
    final String HISTORIES = "histories";
    private List<ResponsesDTO> responsesList= new ArrayList<>();
    private ResponsesAdapter responsesAdapter;
    private LinearLayout viewEmpty;
    private  TextView txtName, txtPrice, txtStore, txtDescription, txtBrand, txtSize, txtOrigin, txtStore2, txtNumberResponse, txtNumberResponse2;
    private ImageView imgProduct;
    private RatingBar ratingBar;
    private String url;
    private String storeID;
    private float star;
    private int count;
    private CartsDTO tempCart;
    private String customerId;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        star=0;
        count=0;
        DatabaseHelper myDb;//
        myDb = new DatabaseHelper(this);//
        Cursor res = myDb.getKeyCustomer();
        while (res.moveToFirst()) {
            customerId = res.getString(0);
            break;
        }
        txtName = (TextView) findViewById(R.id.txtTitleProduct);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtStore = (TextView) findViewById(R.id.txtStore);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtBrand = (TextView) findViewById(R.id.txtBrand);
        txtSize = (TextView) findViewById(R.id.txtSize);
        txtOrigin = (TextView) findViewById(R.id.txtOrigin);
        txtStore2 = (TextView) findViewById(R.id.txtStore2);
        txtNumberResponse = (TextView) findViewById(R.id.txtNumberResponse);
        txtNumberResponse2 = (TextView) findViewById(R.id.txtNumberResponse2);
        imgProduct= (ImageView) findViewById(R.id.imgProduct);
        recyclerView= (RecyclerView) findViewById(R.id.recycler_view);
        ratingBar = (RatingBar) findViewById(R.id.ratingProductBar);
        viewEmpty = (LinearLayout) findViewById(R.id.viewEmpty);

        loadProduct();
        loadStar();
        loadResponse();
        saveHistory();
        loadExistedCart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExistedCart();
        System.out.println(customerId);
    }

    public void loadExistedCart(){
        tempCart=null;
        Bundle bundle= getIntent().getBundleExtra("data");
        final String id = bundle.getString("id_product");
        DatabaseReference cartDatabase = FirebaseDatabase.getInstance().getReference(CARTS);
        cartDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    CartsDTO dto = item.getValue(CartsDTO.class);
                    if (dto.getProductId()!=null)
                        if (dto.getProductId().equals(id) && customerId.equals(dto.getCustomerId())){
                            tempCart= dto;
                            System.out.println(tempCart);
                        }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void loadProduct(){
        Bundle bundle= getIntent().getBundleExtra("data");
        final String id = bundle.getString("id_product");
        DatabaseReference productsDatabase = FirebaseDatabase.getInstance().getReference(PRODUCTS);
        productsDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ProductsDTO productDto = dataSnapshot.getValue(ProductsDTO.class);
                txtName.setText(productDto.getName());
                DecimalFormat decimalFormat = new DecimalFormat("#,##0");
                txtPrice.setText(decimalFormat.format(productDto.getPrice()) + " đ");
                txtDescription.setText(productDto.getDescription());
                txtBrand.setText(productDto.getBrand());
                txtOrigin.setText(productDto.getOrigin());
                txtSize.setText(productDto.getSize());
                Glide.with(ProductDetailActivity.this)
                        .load(productDto.getImg())
                        .into(imgProduct);
                url = productDto.getImg();
                DatabaseReference storeDatabase = FirebaseDatabase.getInstance().getReference(STORES);
                storeDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot item : dataSnapshot.getChildren()){
                            StoresDTO storesDTO = item.getValue(StoresDTO.class);
                            if (storesDTO.getId().equals(productDto.getStoreId())){
                                txtStore.setText(storesDTO.getName());
                                txtStore2.setText(storesDTO.getName());
                                storeID=storesDTO.getId();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
    public void loadStar(){
        Bundle bundle= getIntent().getBundleExtra("data");
        final String id = bundle.getString("id_product");
        DatabaseReference orderDetailDatabase = FirebaseDatabase.getInstance().getReference(ORDERPRODUCTDETAILS);
        orderDetailDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    final OrderProductDetailsDTO orderProductDetailsDTO = item.getValue(OrderProductDetailsDTO.class);
                    if (orderProductDetailsDTO.getProductId().equals(id)){
                        DatabaseReference responseDatabase = FirebaseDatabase.getInstance().getReference(RESPONSES);
                        responseDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot item : dataSnapshot.getChildren()){
                                    ResponsesDTO responsesDTO = item.getValue(ResponsesDTO.class);

                                    if (orderProductDetailsDTO.getId().equals(responsesDTO.getOrderProductDetailId())){
                                        System.out.println(responsesDTO.getId()+"-"+responsesDTO.getRating());
                                        star+= responsesDTO.getRating();
                                        count++;
                                    }
                                }
                                if (star==0){
                                    ratingBar.setVisibility(RatingBar.GONE);
                                    txtNumberResponse.setText("Chưa có đánh giá nào");

                                } else{
                                    ratingBar.setVisibility(RatingBar.VISIBLE);
                                   ratingBar.setRating(star/count);
                                   txtNumberResponse.setText("(Có "+count+" người đánh giá)");
                                   txtNumberResponse2.setText("("+count+")");
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());

            }
        });
    }
    public void saveHistory(){
        Bundle bundle= getIntent().getBundleExtra("data");
        final String productId = bundle.getString("id_product");
        DateTimeStamp dateTimeStamp = new DateTimeStamp();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(HISTORIES);
        String generateId = mDatabase.push().getKey(); // random key
        HistoriesDTO dto = new HistoriesDTO( generateId, dateTimeStamp.getCurrentTime(),  customerId, productId);
        mDatabase.child(generateId).setValue(dto);
    }
    public void loadResponse(){
        Bundle bundle= getIntent().getBundleExtra("data");
        final String id = bundle.getString("id_product");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ORDERPRODUCTDETAILS);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    final OrderProductDetailsDTO orderProductDetailsDTO = item.getValue(OrderProductDetailsDTO.class);
                   if (orderProductDetailsDTO.getProductId().equals(id)){
                       DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(RESPONSES);
                       mDatabase.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {
                               for (DataSnapshot item : dataSnapshot.getChildren()){
                                   ResponsesDTO responsesDTO = item.getValue(ResponsesDTO.class);
                                   if (responsesDTO.getOrderProductDetailId().equals(orderProductDetailsDTO.getId())){
                                            responsesList.add(responsesDTO);
                                   }
                               }
                               if(!responsesList.isEmpty()){
                                   responsesAdapter = new ResponsesAdapter(responsesList, ProductDetailActivity.this);
                                   RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProductDetailActivity.this, RecyclerView.VERTICAL, false);
                                   recyclerView.setLayoutManager(layoutManager);
                                   recyclerView.setItemAnimator(new DefaultItemAnimator());
                                   recyclerView.setNestedScrollingEnabled(false);
                                   recyclerView.setAdapter(responsesAdapter);
                                   viewEmpty.setVisibility(LinearLayout.GONE);
                               }
                           }
                           @Override
                           public void onCancelled(DatabaseError databaseError) {

                           }
                       });
                   }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void clickAddToCart(View view) {
        Bundle bundle= getIntent().getBundleExtra("data");
        final String id = bundle.getString("id_product");
        final AlertDialog.Builder alert = new AlertDialog.Builder(ProductDetailActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.buying_dialog, null);
        Button btnCancel = mView.findViewById(R.id.btn_cancel);
        Button btnCart = mView.findViewById(R.id.btn_to_cart);
        ImageView btnClose = mView.findViewById(R.id.icon_dismiss);
        TextView txtDialogName = mView.findViewById(R.id.txtProductDialogName);
        ImageView imageDialogView = mView.findViewById(R.id.imgProduct);
        TextView txtDialogStore = mView.findViewById(R.id.txtProductDialogStore);
        TextView txtDialogPrice = mView.findViewById(R.id.txtProductDialogPrice);
        ImageView imgBuyingProduct= (ImageView) mView.findViewById(R.id.imgBuyingProduct);
        txtDialogName.setText(txtName.getText().toString());
        txtDialogPrice.setText(txtPrice.getText().toString());
        Glide.with(ProductDetailActivity.this)
                .load(url)
                .into(imgBuyingProduct);
        alert.setView(mView);
        final AlertDialog alertDialog= alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                alertDialog.dismiss();
            }
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        //add cart to firebase

        loadCart();

    }
    public void loadCart()
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(CARTS);
        if (tempCart!=null){
            mDatabase.child(tempCart.getId()).child("quantity").setValue(tempCart.getQuantity()+1);
        }else{
            Bundle bundle= getIntent().getBundleExtra("data");
            final String id = bundle.getString("id_product");
            String generatedId = mDatabase.push().getKey();
            CartsDTO cartsDTO = new CartsDTO(generatedId,customerId,id, 1);
            mDatabase.child(generatedId).setValue(cartsDTO);
        }

    }
    public void clickToCart(View view) {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public void clickToBack(View view) {
        finish();
    }

    public void clickToStoreDetail(View view) {
        Intent intent = new Intent(ProductDetailActivity.this, StoreDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id_store", storeID);
        intent.putExtra("dataStore", bundle);
        startActivity(intent);
    }
}
