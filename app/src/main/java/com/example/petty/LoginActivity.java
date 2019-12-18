package com.example.petty;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dto.AccountsDTO;
import sqlite.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    DatabaseHelper myDb;

    private static final String ACCOUNT = "accounts";
    private static final String CUSTOMER = "customers";

    EditText edtUsername, edtPassword;
    TextView txtWarning;

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseCustomer;

    Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        txtWarning = (TextView) findViewById(R.id.txtWarning);

        mDatabase = FirebaseDatabase.getInstance().getReference(ACCOUNT);
        mDatabaseCustomer = FirebaseDatabase.getInstance().getReference(CUSTOMER);

        myDb = new DatabaseHelper(this);

        Cursor resCus = myDb.getKeyCustomer();
    }

    public void clickToLogin(View view) {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txtWarning.setText("");
                boolean check =false;
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String dbUsername = ds.child("username").getValue(String.class);
                        String dbPassword = ds.child("password").getValue(String.class);
                        final String dbId = ds.child("id").getValue(String.class);
                        String iUsername = edtUsername.getText().toString();
                        String iPassword = edtPassword.getText().toString();
                        txtWarning.setText("");
                        if (iUsername.equals(dbUsername)&& iPassword.equals(dbPassword)) {
                            check =true;
                            myDb.insertAccount(dbId, iUsername, iPassword);
                            mDatabaseCustomer.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot noteDataSnapshotCus : dataSnapshot.getChildren()) {
                                        for(DataSnapshot dsCus : dataSnapshot.getChildren()) {
                                            String dbCustomerId = dsCus.child("id").getValue(String.class);
                                            String dbAccountId = dsCus.child("accountsId").getValue(String.class);
                                                myDb.insertCustomer(dbCustomerId, dbAccountId);
                                                finish();
                                                Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                                                startActivity(intent);
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
                if (!check){
                        txtWarning.setText("Sai tên đăng nhập hoặc mật khẩu");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                txtWarning.setText("Xin thử lại");
            }
        });
    }

    public void clickToRegistration(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

}
