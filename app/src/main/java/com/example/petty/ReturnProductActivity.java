package com.example.petty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import adapter.OrderProductsAdapter;
import dto.CartsDTO;
import dto.OrderProductDetailsDTO;
import dto.ProductsDTO;
import dto.ReturnsDTO;
import util.DateTimeStamp;

public class ReturnProductActivity extends AppCompatActivity {
    private ImageView imgProduct, imgReturn;
    private TextView txtProductName, txtOrderId, txtOrderDate;
    private EditText edtReasonReturn;
    private Button btnReturn;
    private final String ORDERPRODUCTDETAILS = "order_product_details";
    private final String ORDERS = "orders";
    private final String PRODUCTS = "products";
    private final String RETURNS = "returns";
    private final int PICK_IMAGE_REQUEST = 71;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 1;
    private Uri filePath;
    private String urlImage;
    StorageReference storageReference;
    FirebaseStorage storage;
    Bitmap imageCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_product);
    edtReasonReturn =(EditText) findViewById(R.id.edtReasonReturn);
        imgProduct = (ImageView) findViewById(R.id.imgProduct);
        imgReturn = (ImageView) findViewById(R.id.imgReasonReturn);
        txtProductName = (TextView) findViewById(R.id.txtProductName);
        txtOrderId = (TextView) findViewById(R.id.txtOrderId);
        txtOrderDate = (TextView) findViewById(R.id.txtOrderDate);
        btnReturn = (Button) findViewById(R.id.btnReturn);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        loadReturnExisted();
        loadProductDetail();
    }
    public void loadReturnExisted(){
        Bundle bundle = getIntent().getBundleExtra("data");
        final String id = bundle.getString("id_order_detail");
        DatabaseReference productDetailDatabase = FirebaseDatabase.getInstance().getReference(RETURNS);
        productDetailDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    final ReturnsDTO returnsDTO = item.getValue(ReturnsDTO.class);
                    if (returnsDTO.getOrderDetailId().equals(id)) {
                        edtReasonReturn.setText(returnsDTO.getReason());
                        btnReturn.setBackground(getDrawable(R.drawable.btn_white_stroke_grey));
                        btnReturn.setText("ĐÃ YÊU CẦU HOÀN TRẢ");
                        btnReturn.setEnabled(false);
                        btnReturn.setTextColor(getResources().getColor(R.color.colorGrey));
                        edtReasonReturn.setEnabled(false);
                        Glide.with(ReturnProductActivity.this)
                                .load(returnsDTO.getImg())
                                .into(imgReturn);
                        Button btnSelect = (Button) findViewById(R.id.btnSelectImage);
                        Button btnCapture = (Button) findViewById(R.id.btnCapture);
                        btnSelect.setVisibility(Button.GONE);
                        btnCapture.setVisibility(Button.GONE);
                        TextView txtMessage = (TextView) findViewById(R.id.txtMessage);
                        txtMessage.setVisibility(TextView.GONE);
                    }
                }
                btnReturn.setVisibility(Button.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void loadProductDetail() {
        Bundle bundle = getIntent().getBundleExtra("data");
        final String id = bundle.getString("id_order_detail");
        DatabaseReference productDetailDatabase = FirebaseDatabase.getInstance().getReference(ORDERPRODUCTDETAILS);
        productDetailDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    final OrderProductDetailsDTO orderProductDetailsDTO = item.getValue(OrderProductDetailsDTO.class);
                    if (orderProductDetailsDTO.getId().equals(id)) {
                        DateTimeStamp dateTimeStamp = new DateTimeStamp();
                        System.out.println(orderProductDetailsDTO.getOrderProductStoreId());
                        txtOrderId.setText(orderProductDetailsDTO.getOrderProductStoreId());
                        txtOrderDate.setText(dateTimeStamp.convertToDateTimeString(orderProductDetailsDTO.getDate()));
                        DatabaseReference productDatabase = FirebaseDatabase.getInstance().getReference(PRODUCTS);
                        productDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot item : dataSnapshot.getChildren()) {
                                    ProductsDTO productsDTO = item.getValue(ProductsDTO.class);
                                    if (productsDTO.getId().equals(orderProductDetailsDTO.getProductId())) {
                                        txtProductName.setText(productsDTO.getName());
                                        Glide.with(ReturnProductActivity.this)
                                                .load(productsDTO.getImg())
                                                .into(imgProduct);
                                    }
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

    public void saveReturn() {
        Bundle bundle = getIntent().getBundleExtra("data");
        final String id = bundle.getString("id_order_detail");
        Toast.makeText(ReturnProductActivity.this, "Cửa hàng sẽ phản hồi trong thời gian sớm nhất", Toast.LENGTH_SHORT).show();
        finish();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(RETURNS);
        String generatedId = mDatabase.push().getKey();
        DateTimeStamp dateTimeStamp = new DateTimeStamp();
        ReturnsDTO returnsDTO = new ReturnsDTO(generatedId, dateTimeStamp.getCurrentTime(), edtReasonReturn.getText().toString(), urlImage, "pending", id);
        mDatabase.child(generatedId).setValue(returnsDTO);

    }

    public void clickToAddImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Vui lòng chọn hình ảnh"), PICK_IMAGE_REQUEST);
    }
    public void clickToCapture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }

    }
    public void clickToReturn(View view) {
        if (edtReasonReturn.getText().toString().trim().length()==0){
            TextView error = (TextView) findViewById(R.id.txtErrorMessage);
            error.setVisibility(TextView.VISIBLE);
            return;
        }
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            String uid = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("images/" + uid);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            urlImage = taskSnapshot.getDownloadUrl().toString();
                            saveReturn();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
        } else if (imageCapture!=null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageCapture.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            String uid = UUID.randomUUID().toString();
            byte[] b = stream.toByteArray();
            StorageReference ref = storageReference.child("images/"+uid);
            //StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(userID);
            ref.putBytes(b).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    urlImage = downloadUrl.toString();
                    saveReturn();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ReturnProductActivity.this,"failed",Toast.LENGTH_LONG).show();


                }
            });
        } else {
            saveReturn();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            imageCapture=null;
            System.out.println("GG"+filePath);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgReturn.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else  if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            filePath=null;
            imageCapture = (Bitmap) data.getExtras().get("data");
            imgReturn.setImageBitmap(imageCapture);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }


    }


    public void clickToBack(View view) {
        finish();
    }
}