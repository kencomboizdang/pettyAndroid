package com.example.petty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dto.AccountsDTO;

public class RegistrationActivity extends AppCompatActivity {

    private static final String ACCOUNT = "accounts";
    EditText edtUsername, edtPassword, edtConfirm;
    Button btnCreate, btnBack;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtConfirm = (EditText) findViewById(R.id.edtConfirm);

        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnBack = (Button) findViewById(R.id.btnBack);

        mDatabase = FirebaseDatabase.getInstance().getReference(ACCOUNT);
    }

    public void clickToRegister(View view) {
        String iUsername = edtUsername.getText().toString();
        String iPassword = edtPassword.getText().toString();
        String iConfirm = edtConfirm.getText().toString();
        addAccount(iUsername, iPassword, "user");
    }

    public void addAccount(String username, String password, String role) {
        AccountsDTO dto = new AccountsDTO(username, password, role);
        String accountKey = mDatabase.push().getKey();
        dto.setId(accountKey);
        mDatabase.child(accountKey).setValue(dto);
    }

}
