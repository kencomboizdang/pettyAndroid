package com.example.petty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dto.AccountsDTO;

public class LoginActivity extends AppCompatActivity {

    private static final String ACCOUNT = "accounts";


    EditText edtUsername, edtPassword;
    TextView txtWarning;

    private DatabaseReference mDatabase;

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

        AccountsDTO dto = new AccountsDTO("Linh", "HN", "user");
        addAccount(dto.getUsername(), dto.getPassword(), dto.getRole());
    }

    public void clickToLogin(View view) {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String dbUsername = ds.child("username").getValue(String.class);
                        String dbPassword = ds.child("password").getValue(String.class);
                        String iUsername = edtUsername.getText().toString();
                        String iPassword = edtPassword.getText().toString();
                        txtWarning.setText("");
                        if (iUsername.equals(dbUsername)&& iPassword.equals(dbPassword)) {
                            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                            startActivity(intent);
                        } else {
                            txtWarning.setText("Invalid Username or Password");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void clickToRegistration(View view) {
    }

    public void addAccount(String username, String password, String role) {
        AccountsDTO dto = new AccountsDTO(username, password, role);
        String accountKey = mDatabase.push().getKey();
        dto.setId(accountKey);
        mDatabase.child(accountKey).setValue(dto);
    }


}
