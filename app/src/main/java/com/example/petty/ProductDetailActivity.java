package com.example.petty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

import dto.ProductsDTO;

public class ProductDetailActivity extends AppCompatActivity {
    final String PRODUCTS = "products";
    private  TextView txtName, txtPrice, txtStore, txtDescription, txtBrand, txtSize, txtOrigin, txtStore2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Bundle bundle= getIntent().getBundleExtra("data");
        String id = bundle.getString("id_product");
        System.out.println(id);
        txtName = (TextView) findViewById(R.id.txtTitleProduct);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtStore = (TextView) findViewById(R.id.txtStore);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtBrand = (TextView) findViewById(R.id.txtBrand);
        txtSize = (TextView) findViewById(R.id.txtSize);
        txtOrigin = (TextView) findViewById(R.id.txtOrigin);
        txtStore2 = (TextView) findViewById(R.id.txtStore2);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(PRODUCTS);
        mDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProductsDTO dto = dataSnapshot.getValue(ProductsDTO.class);
                txtName.setText(dto.getName());
                DecimalFormat decimalFormat = new DecimalFormat("#,##0");
                txtPrice.setText(decimalFormat.format(dto.getPrice()) + " đ");
                txtDescription.setText(dto.getDescription());
                txtBrand.setText(dto.getBrand());
                txtOrigin.setText(dto.getOrigin());
                txtSize.setText(dto.getSize());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    public void clickAddToCart(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(ProductDetailActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.buying_dialog, null);
        Button btnCancel = mView.findViewById(R.id.btn_cancel);
        Button btnCart = mView.findViewById(R.id.btn_to_cart);
        ImageView btnClose = mView.findViewById(R.id.icon_dismiss);
        TextView txtDialogName = mView.findViewById(R.id.txtProductDialogName);
        ImageView imageDialogView = mView.findViewById(R.id.imgProduct);
        TextView txtDialogStore = mView.findViewById(R.id.txtProductDialogStore);
        TextView txtDialogPrice = mView.findViewById(R.id.txtProductDialogPrice);
        txtDialogName.setText(txtName.getText().toString());
        txtDialogPrice.setText(txtPrice.getText().toString());
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
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);

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
