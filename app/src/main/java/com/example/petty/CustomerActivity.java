package com.example.petty;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dto.CustomersDTO;
import sqlite.DatabaseHelper;

public class CustomerActivity extends AppCompatActivity {

    private static final String CUSTOMER = "customers";
    EditText edtName, edtBirthday, edtEmail;
    RadioButton rbtMale, rbtFemale;
    Button btnCreate, btnBack;
    DatabaseHelper myDb;
    private DatabaseReference mDatabase;
    String customerKey = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        edtName = (EditText) findViewById(R.id.edtName);
        edtBirthday = (EditText) findViewById(R.id.edtBirthday);
        edtEmail = (EditText) findViewById(R.id.edtEmail);

        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnBack = (Button) findViewById(R.id.btnBack);
        rbtMale = (RadioButton) findViewById(R.id.rbtMale);
        rbtFemale = (RadioButton) findViewById(R.id.rbtFemale);

        mDatabase = FirebaseDatabase.getInstance().getReference(CUSTOMER);
        myDb = new DatabaseHelper(this);
    }


    public void createCustomer(View view) {
        String iName = edtName.getText().toString();
        String iBirthday = edtBirthday.getText().toString();
        String iEmail = edtEmail.getText().toString();
        long birthday = Long.parseLong(iBirthday);

        boolean check = false;
        if (rbtMale.isChecked() == true) {
            check = true;
        }

        Cursor res = myDb.checkData();
        String accountId = "";
        while (res.moveToFirst()) {
            accountId = res.getString(0);
            break;
        }
        addCustomer(iName, birthday, iEmail, check, true, accountId);
        myDb.insertCustomer(customerKey, accountId);
    }

    public void addCustomer(String name, long birthday, String email, boolean gender, boolean active, String accountId) {
        CustomersDTO dto = new CustomersDTO(name, birthday, email, gender, active, accountId);
        customerKey = mDatabase.push().getKey();
        dto.setId(customerKey);
        mDatabase.child(customerKey).setValue(dto);
    }

}
