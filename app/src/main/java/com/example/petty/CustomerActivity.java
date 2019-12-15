package com.example.petty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.firebase.database.DatabaseReference;

public class CustomerActivity extends AppCompatActivity {

    private static final String CUSTOMER = "customers";
    EditText edtName, edtBirthday, edtEmail;
    RadioButton rbtMale, rbtFemale;
    Button btnCreate, btnBack;

    private DatabaseReference mDatabase;
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


    }

    public void clickToBack(View view) {

    }

}
