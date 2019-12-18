package com.example.petty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

import dto.AddressesDTO;
import dto.ProductsDTO;
import sqlite.DatabaseHelper;

public class AddressDetailActivity extends AppCompatActivity {
    final String ADDRESSES = "addresses";
    private EditText edtName, edtPhone, edtProvince, edtDistrict, edtWard, edtDetail;
    private TextView txtErrorMessage;
    private  String customerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_detail);
        Bundle bundle= getIntent().getBundleExtra("data");

            edtName = (EditText) findViewById(R.id.edtAddressName);
            edtPhone = (EditText) findViewById(R.id.edtAddressPhone);
            edtProvince = (EditText) findViewById(R.id.edtProvice);
            edtDistrict = (EditText) findViewById(R.id.edtDistrict);
            edtWard = (EditText) findViewById(R.id.edtWard);
            edtDetail = (EditText) findViewById(R.id.edtDetail);
            txtErrorMessage= (TextView) findViewById(R.id.txtErrorMessage);
        if (bundle != null) {
            String id = bundle.getString("id_address");
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ADDRESSES);
            mDatabase.child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    AddressesDTO dto = dataSnapshot.getValue(AddressesDTO.class);
                    System.out.println(dto);
                    if (dto != null) {
                        edtName.setText(dto.getName());
                        edtPhone.setText(dto.getPhone());
                        edtProvince.setText(dto.getProvince());
                        edtDistrict.setText(dto.getDistrict());
                        edtWard.setText(dto.getWard());
                        edtDetail.setText(dto.getDetail());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());
                }
            });
        }
    }

    public void clickToSaveAddress(View view) {

        DatabaseHelper myDb;//
        myDb = new DatabaseHelper(AddressDetailActivity.this);//
        Cursor res = myDb.getKeyCustomer();
        while (res.moveToFirst()) {
            customerId = res.getString(0);
            break;
        }
        if (checkAddressValidation()) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                String id = bundle.getString("id_address");
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ADDRESSES);
                mDatabase.child(id).child("name").setValue(edtName.getText().toString());
                mDatabase.child(id).child("province").setValue(edtProvince.getText().toString());
                mDatabase.child(id).child("district").setValue(edtDistrict.getText().toString());
                mDatabase.child(id).child("ward").setValue(edtWard.getText().toString());
                mDatabase.child(id).child("detail").setValue(edtDetail.getText().toString());
                mDatabase.child(id).child("phone").setValue(edtPhone.getText().toString());
                Toast.makeText(this, "Đã lưu", Toast.LENGTH_LONG);
                finish();
            } else {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ADDRESSES);
                String generatedId = mDatabase.push().getKey();
                AddressesDTO dto = new AddressesDTO(generatedId, edtName.getText().toString(), edtProvince.getText().toString(), edtDistrict.getText().toString(), edtWard.getText().toString(), edtDetail.getText().toString(), edtPhone.getText().toString(), customerId);
                mDatabase.child(generatedId).setValue(dto);
                Toast.makeText(this, "Thêm địa chỉ mới thành công", Toast.LENGTH_LONG);
                finish();
            }
        }

    }

    public void clickToDelete(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(AddressDetailActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_basic, null);
        Button btnCancel = mView.findViewById(R.id.btnCancel);
        Button btnConfirm = mView.findViewById(R.id.btnConfirm);
        ImageView imgDismiss = mView.findViewById(R.id.iconDismiss);
        TextView txtMessage = mView.findViewById(R.id.txtWarningMessage);
        txtMessage.setText("Bạn muốn xóa địa chỉ này");
        alert.setView(mView);
        final AlertDialog alertDialog= alert.create();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle= getIntent().getBundleExtra("data");
                String id = bundle.getString("id_address");
                if (id!=null) {
                    FirebaseDatabase.getInstance().getReference(ADDRESSES).child(id).removeValue();
                }
                finish();
            }
        });
        imgDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.show();
    }
    public boolean checkAddressValidation(){
        boolean result =true;
        if (edtName.getText().toString().trim().length()==0){
            edtName.setHintTextColor(getResources().getColor(R.color.colorRed));
            result=false;
        }
        if (edtPhone.getText().toString().trim().length()==0){
            edtPhone.setHintTextColor(getResources().getColor(R.color.colorRed));
            result=false;
        }
        if (edtProvince.getText().toString().trim().length()==0){
            edtProvince.setHintTextColor(getResources().getColor(R.color.colorRed));
            result=false;
        }
        if (edtDistrict.getText().toString().trim().length()==0){
            edtDistrict.setHintTextColor(getResources().getColor(R.color.colorRed));
            result=false;
        }
        if (edtWard.getText().toString().trim().length()==0){
            edtWard.setHintTextColor(getResources().getColor(R.color.colorRed));
            result=false;
        }
        if (edtDetail.getText().toString().trim().length()==0){
            edtDetail.setHintTextColor(getResources().getColor(R.color.colorRed));
        }
        if (!result){
            txtErrorMessage.setText("Vui lòng điền đầy đủ thông tin");
            txtErrorMessage.setVisibility(TextView.VISIBLE);
        }
        return result;
    }
}
