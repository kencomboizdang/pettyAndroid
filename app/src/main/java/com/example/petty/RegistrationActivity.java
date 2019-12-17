package com.example.petty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dto.AccountsDTO;
import sqlite.DatabaseHelper;

public class RegistrationActivity extends AppCompatActivity {

    private static final String ACCOUNT = "accounts";
    EditText edtUsername, edtPassword, edtConfirm;
    Button btnCreate, btnBack;
    TextView tvValidUser, tvValidPass, tvValidConfirm;
    private DatabaseReference mDatabase;
    DatabaseHelper myDb;

    String accountKey = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtConfirm = (EditText) findViewById(R.id.edtConfirm);

        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnBack = (Button) findViewById(R.id.btnBack);

        tvValidUser = (TextView) findViewById(R.id.tvValidUser);
        tvValidPass = (TextView) findViewById(R.id.tvValidPass);
        tvValidConfirm = (TextView) findViewById(R.id.tvValidConfirm);

        mDatabase = FirebaseDatabase.getInstance().getReference(ACCOUNT);
        myDb = new DatabaseHelper(this);
    }

    public void clickToRegister(View view) {
        String iUsername = edtUsername.getText().toString();
        String iPassword = edtPassword.getText().toString();
        String iConfirm = edtConfirm.getText().toString();

        boolean valid = true;

        tvValidUser.setText("");
        tvValidPass.setText("");
        tvValidConfirm.setText("");

        if(iUsername.trim().equals("")) {
            tvValidUser.setText("Tên đăng nhập không được trống");
            valid = false;
        } else if(iUsername.length() < 6 || iUsername.length() > 15) {
            tvValidUser.setText("Tên đăng nhập phải từ 6 tới 15 kí tự");
            valid = false;
        }

        if(iPassword.trim().equals("")) {
            tvValidPass.setText("Mật khẩu không được trống");
            valid = false;
        } else if(iPassword.length() < 6 || iPassword.length() > 15) {
            tvValidPass.setText("Mật khẩu phải từ 6 tới 15 kí tự");
            valid = false;
        } else if(iPassword.equals(iConfirm) == false) {
            tvValidConfirm.setText("Xác nhận mật khẩu sai, xin thử lại");
            valid = false;
        }

        if(valid) {
            addAccount(iUsername, iPassword, "user");
            Toast.makeText(RegistrationActivity.this, "Đăng ký thành công", Toast.LENGTH_LONG).show();
            myDb.insertAccount(iUsername,iPassword, accountKey);
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(RegistrationActivity.this, HomePageActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            },1800);
        }
    }

    public void goBack(View view) {
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void addAccount(String username, String password, String role) {
        AccountsDTO dto = new AccountsDTO(username, password, role);
        accountKey = mDatabase.push().getKey();
        dto.setId(accountKey);
        mDatabase.child(accountKey).setValue(dto);
    }

}
