package com.example.petty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

import dto.AddressesDTO;
import dto.CartsDTO;
import dto.ProductsDTO;
import dto.StoresDTO;

public class ProductDetailActivity extends AppCompatActivity {
    final String PRODUCTS = "products";
    final String STORES = "stores";
    final String CARTS = "carts";
    private  TextView txtName, txtPrice, txtStore, txtDescription, txtBrand, txtSize, txtOrigin, txtStore2;
    private ImageView imgProduct;
    private String url;
    private CartsDTO tempCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Bundle bundle= getIntent().getBundleExtra("data");
        String id = bundle.getString("id_product");
        txtName = (TextView) findViewById(R.id.txtTitleProduct);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtStore = (TextView) findViewById(R.id.txtStore);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtBrand = (TextView) findViewById(R.id.txtBrand);
        txtSize = (TextView) findViewById(R.id.txtSize);
        txtOrigin = (TextView) findViewById(R.id.txtOrigin);
        txtStore2 = (TextView) findViewById(R.id.txtStore2);
        imgProduct= (ImageView) findViewById(R.id.imgProduct);
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
                loadCart();
                alertDialog.dismiss();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadCart();
                alertDialog.dismiss();
            }
        });
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadCart();
                Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                loadCart();
                alertDialog.dismiss();
            }
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        //add cart to firebase
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(CARTS);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    CartsDTO dto = item.getValue(CartsDTO.class);
                    if (dto.getProductId().equals(id)){
                        tempCart= dto;
                    }
                }
                }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
            CartsDTO cartsDTO = new CartsDTO(generatedId,"null",id, 1);
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
}
