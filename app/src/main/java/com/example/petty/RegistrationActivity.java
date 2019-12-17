package com.example.petty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;

import dto.AccountsDTO;
import dto.CustomersDTO;
import sqlite.DatabaseHelper;
import util.DateTimeStamp;

public class RegistrationActivity extends AppCompatActivity {

    private static final String ACCOUNT = "accounts";
    private static final String CUSTOMER = "customers";

    EditText edtUsername, edtPassword, edtConfirm, edtName, edtBirthday, edtEmail;
    Button btnCreate, btnBack;
    TextView tvValidUser, tvValidPass, tvValidConfirm;
    RadioButton rbtMale, rbtFemale;
    private DatabaseReference mDatabase, mDatabaseCustomer;

    DatabaseHelper myDb;
    DatabaseHelper myDbCustomer;

    DateTimeStamp dts = new DateTimeStamp();

    String accountKey = "";
    String customerKey = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtConfirm = (EditText) findViewById(R.id.edtConfirm);

        edtName = (EditText) findViewById(R.id.edtName);
        edtBirthday = (EditText) findViewById(R.id.edtBirthday);
        edtEmail = (EditText) findViewById(R.id.edtEmail);

        rbtMale = (RadioButton) findViewById(R.id.rbtMale);
        rbtFemale = (RadioButton) findViewById(R.id.rbtFemale);

        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnBack = (Button) findViewById(R.id.btnBack);

        tvValidUser = (TextView) findViewById(R.id.tvValidUser);
        tvValidPass = (TextView) findViewById(R.id.tvValidPass);
        tvValidConfirm = (TextView) findViewById(R.id.tvValidConfirm);

        mDatabase = FirebaseDatabase.getInstance().getReference(ACCOUNT);
        mDatabaseCustomer = FirebaseDatabase.getInstance().getReference(CUSTOMER);

        myDb = new DatabaseHelper(this);
        myDbCustomer = new DatabaseHelper(this);

        edtBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dataFragment = new DatePickerFragment(edtBirthday);
                dataFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
    }

    public void clickToRegister(View view) throws ParseException {
        String iUsername = edtUsername.getText().toString();
        String iPassword = edtPassword.getText().toString();
        String iConfirm = edtConfirm.getText().toString();

        String iName = edtName.getText().toString();
        String iBirthday = edtBirthday.getText().toString();
        String iEmail = edtEmail.getText().toString();
        long birthday = dts.convertDateToLong(iBirthday);

        boolean valid = true;

        tvValidUser.setText("");
        tvValidPass.setText("");
        tvValidConfirm.setText("");

        boolean check = false;
        if (rbtMale.isChecked() == true) {
            check = true;
        }

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
            addCustomer(iName, birthday, iEmail, check, true, accountKey);
            Toast.makeText(RegistrationActivity.this, "Đăng ký thành công", Toast.LENGTH_LONG).show();
            myDb.insertAccount(accountKey,iUsername, iPassword);
            myDbCustomer.insertCustomer(customerKey,accountKey);
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

    public void addCustomer(String name, long birthday, String email, boolean gender, boolean active, String accountId) {
        CustomersDTO dto = new CustomersDTO(name, birthday, email, gender, active, accountId);
        customerKey = mDatabaseCustomer.push().getKey();
        dto.setId(customerKey);
        mDatabaseCustomer.child(customerKey).setValue(dto);
    }

}
