package com.example.petty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import adapter.CartsConfirmAdapter;
import adapter.OrderProductsAdapter;
import dto.AddressesDTO;
import dto.CartsDTO;
import dto.OrderProductDetailsDTO;
import dto.OrderProductStoresDTO;
import dto.OrdersDTO;
import dto.ProductsDTO;
import util.DateTimeStamp;

public class OrderProductStoreDetailActivity extends AppCompatActivity {
    private List<OrderProductDetailsDTO> orderProductDetailsList;
    private final String ORDERPRODUCTSTORES= "order_product_stores";
    private final String ORDERPRODUCTDETAILS = "order_product_details";
    private final String ORDERS = "orders";
    private final String ADDRESSES = "addresses";
    private TextView txtProductStoreId, txtProductStoreDate, txtAddressName, txtAddressPhoneNumber, txtAddressDetail, txtTotal;
    private ImageView imgQRCode, imgOrderConfirm, imgStoreConfirm, imgDeliver, imgComplete;
    private Button btnCancel;
    private OrderProductsAdapter orderProductsAdapter;
    private RecyclerView recyclerView;
    private String orderId;
    private String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_product_store_detail);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_product);
        txtAddressName = (TextView) findViewById(R.id.txtAddressName);
        txtAddressPhoneNumber = (TextView) findViewById(R.id.txtAddressPhoneNumber);
        txtAddressDetail = (TextView) findViewById(R.id.txtAddressDetail);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtProductStoreId = (TextView) findViewById(R.id.txtOrderStoreId);
        txtProductStoreDate = (TextView) findViewById(R.id.txtOrderDate);
        imgQRCode = (ImageView) findViewById(R.id.imgQRCode);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        imgOrderConfirm = (ImageView) findViewById(R.id.imgOrderConfirm);
        imgStoreConfirm = (ImageView) findViewById(R.id.imgStoreConfirm);
        imgDeliver = (ImageView) findViewById(R.id.imgDeliver);
        imgComplete = (ImageView) findViewById(R.id.imgComplete);
        orderProductDetailsList = new ArrayList<>();
        loadProductStoreDetail();
        loadProductList();

    }
    public void loadProductStoreDetail(){
        Bundle bundle= getIntent().getBundleExtra("data");
        final String id = bundle.getString("id_order_product_store");
        DatabaseReference productDatabase = FirebaseDatabase.getInstance().getReference(ORDERPRODUCTSTORES);
        productDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OrderProductStoresDTO dto = dataSnapshot.getValue(OrderProductStoresDTO.class);
                loadOrder(dto.getOrderId());
                DecimalFormat decimalFormat = new DecimalFormat("#,##0");
                txtTotal.setText(decimalFormat.format(dto.getTotal()) + " đ");
                status = dto.getOrderStatus();
                loadOrderStatus(status);
                txtProductStoreId.setText(dto.getId());
                DateTimeStamp dateTimeStamp = new DateTimeStamp();
                txtProductStoreDate.setText(dateTimeStamp.convertToDateTimeString(dto.getDate()));
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(dto.getId(), BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    imgQRCode.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
    public void loadOrderStatus(String status){
        if (status.equals("cancel")){
            btnCancel.setBackground(getDrawable(R.drawable.btn_white_stroke_grey));
            btnCancel.setText("Đã hủy đơn hàng");
            btnCancel.setEnabled(false);
            btnCancel.setTextColor(getResources().getColor(R.color.colorGrey));
        } if (status.equals("pending") || status.equals("store_confirm") || status.equals("deliver") || status.equals("complete") ){
            imgOrderConfirm.setImageResource(R.drawable.icon_process_start_active);
        } if (status.equals("store_confirm") || status.equals("deliver") || status.equals("complete")){
            imgStoreConfirm.setImageResource(R.drawable.icon_process_between_active);
        } if (status.equals("deliver") || status.equals("complete")){
            imgDeliver.setImageResource(R.drawable.icon_process_between_active);
            btnCancel.setBackground(getDrawable(R.drawable.btn_white_stroke_grey));
            btnCancel.setText("ĐÃ HOÀN TẤT ĐƠN HÀNG");
            btnCancel.setEnabled(false);
            btnCancel.setTextColor(getResources().getColor(R.color.colorGrey));
        } if (status.equals("complete")){
            imgComplete.setImageResource(R.drawable.icon_process_end_active);
            btnCancel.setBackground(getDrawable(R.drawable.btn_white_stroke_grey));
            btnCancel.setText("ĐÃ HOÀN TẤT ĐƠN HÀNG");
            btnCancel.setEnabled(false);
            btnCancel.setTextColor(getResources().getColor(R.color.colorGrey));

        }
    }
    public void loadOrder(final String id){

        DatabaseReference orderDatabase = FirebaseDatabase.getInstance().getReference(ORDERS);
        orderDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    OrdersDTO ordersDTO = item.getValue(OrdersDTO.class);
                    if (ordersDTO.getId().equals(id)){
                        orderId=id;
                       loadAddressData(ordersDTO.getAddressId());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
    public void loadAddressData(String id){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ADDRESSES);
        mDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AddressesDTO dto = dataSnapshot.getValue(AddressesDTO.class);
                txtAddressName.setText(dto.getName());
                txtAddressPhoneNumber.setText(dto.getPhone());
                txtAddressDetail.setText(dto.getDetail()+", "+dto.getWard()+", "+dto.getDistrict()+", "+dto.getProvince());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
    public void loadProductList(){
        Bundle bundle= getIntent().getBundleExtra("data");
        final String id = bundle.getString("id_order_product_store");
        DatabaseReference productDetailDatabase = FirebaseDatabase.getInstance().getReference(ORDERPRODUCTDETAILS);
        productDetailDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderProductDetailsList.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    OrderProductDetailsDTO orderProductDetailsDTO = item.getValue(OrderProductDetailsDTO.class);
                    if (orderProductDetailsDTO.getOrderProductStoreId().equals(id))
                    {
                        orderProductDetailsList.add(orderProductDetailsDTO);
                    }

                    orderProductsAdapter = new OrderProductsAdapter(orderProductDetailsList,OrderProductStoreDetailActivity.this);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setAdapter(orderProductsAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void clickToCancelation(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_cancelation, null);
        Button btn_cancel = mView.findViewById(R.id.btn_cancel);
        Button btn_to_cancelation = mView.findViewById(R.id.btn_to_cancelation);
        ImageView btn_close = mView.findViewById(R.id.icon_dismiss);
        final TextView txtWarning= mView.findViewById(R.id.txtWarning);
        final EditText edtReason = mView.findViewById(R.id.edtReason);
        alert.setView(mView);
        final AlertDialog alertDialog= alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        btn_to_cancelation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtReason.getText().toString().trim().length()>0){
                    Bundle bundle= getIntent().getBundleExtra("data");
                    final String id = bundle.getString("id_order_product_store");
                    DatabaseReference orderStoreDatabase = FirebaseDatabase.getInstance().getReference(ORDERPRODUCTSTORES);
                    DatabaseReference productDatabase = FirebaseDatabase.getInstance().getReference(ORDERPRODUCTDETAILS);
                    orderStoreDatabase.child(id).child("orderStatus").setValue("cancel");
                    orderStoreDatabase.child(id).child("note").setValue(edtReason.getText().toString());
                    alertDialog.dismiss();
                    finish();
                    Toast.makeText(OrderProductStoreDetailActivity.this, "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show();
                } else {
                    txtWarning.setVisibility(TextView.VISIBLE);
                }

            }
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public void clickToBack(View view) {
        finish();
    }
}
