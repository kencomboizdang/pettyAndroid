package com.example.petty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dao.AccountsDAO;
import dto.AccountsDTO;
import dto.User;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    private Button btnChoose, btnUpload;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        imageView = (ImageView) findViewById(R.id.imgView);
//        AccountsDTO dto = new AccountsDTO("AAAAAAA","781239","user");
//        AccountsDAO dao = new AccountsDAO();
//        dao.createAccount(dto);

    }

    public void clickToCreate(View view) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        String userId = mDatabase.push().getKey(); // random key
        final User user = new User("Ravi Tamada", "ravi@androiddhive.info");
        mDatabase.child(userId).setValue(user);
        TextView view1 = findViewById(R.id.txtDetail);
        view1.setText("dfesfesf");
    }

    public void clickToRead(final View view) { // read only 1 object
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child("-Lv_5xDkgQzFzIYqm2wm").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                Log.d(ContentValues.TAG, "User name: " + user.getName() + ", email " + user.getEmail());
                TextView view1 = findViewById(R.id.txtDetail);
                view1.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    public void clickToReadAll(View view) { // read all object in one table

        Query allUser = FirebaseDatabase.getInstance().getReference().child("users");
        allUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<User> users = new ArrayList<>();
                String result ="ss";
                TextView view1 = findViewById(R.id.txtDetail);
                for (DataSnapshot item : dataSnapshot.getChildren())
                {
                    User user = item.getValue(User.class);
                    users.add(user);
                    result+= item.getKey();
                }
                view1.setText(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void ClickToUpdate(View view) { // update object in one table
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        String newEmail = "ahihi@gmail.com";
        mDatabase.child("-Lv_69SF7L6zoResKarV").child("email").setValue(newEmail);
    }

    public void ClickToDelete(View view) {
        FirebaseDatabase.getInstance().getReference("users").child("bebebe").removeValue();
    }

    public void clickToChooseImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void clickToUploadImage(View view) {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            String uid = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("images/"+ uid);
            System.out.println(uid);
            TextView view1 = findViewById(R.id.txtDetail);
            view1.setText(uid + "\n");
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
