package com.example.petty;

import android.database.Cursor;
import android.os.Bundle;
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

import sqlite.DatabaseHelper;

public class ChangePasswordActivity extends AppCompatActivity {

    private static final String ACCOUNT = "accounts";
    DatabaseHelper myDb;
    DatabaseReference mDatabase;
    private EditText edtOldPass, edtNewPass, edtConfirmPass;
    private Button btnConfirmChange;
    private TextView txtWarningChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        edtOldPass = (EditText) findViewById(R.id.edtOldPass);
        edtNewPass = (EditText) findViewById(R.id.edtNewPass);
        edtConfirmPass = (EditText) findViewById(R.id.edtConfirmPass);

        btnConfirmChange = (Button) findViewById(R.id.btnConfirmChange);

        txtWarningChange = (TextView) findViewById(R.id.txtWarningChange);

        mDatabase = FirebaseDatabase.getInstance().getReference(ACCOUNT);

        myDb = new DatabaseHelper(this);
        Cursor resKey = myDb.getKeyCustomer();
        String keyAcc = "";
        while (resKey.moveToFirst()) {
            keyAcc = resKey.getString(1);
            break;
        }

        final String finalKeyAcc = keyAcc;
        btnConfirmChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child(finalKeyAcc).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String dbPassword = dataSnapshot.child("password").getValue(String.class);
                        String oldPassword = edtOldPass.getText().toString().trim();
                        boolean check = true;
                        if (txtWarningChange.getVisibility()!=TextView.GONE) {
                            if (dbPassword.trim().equals(oldPassword) == false) {
                                check = false;
                                txtWarningChange.setText("Sai mật khẩu");
                            }

                            if (edtNewPass.getText().toString().trim().equals(edtConfirmPass.getText().toString().trim()) == false) {
                                check = false;
                                txtWarningChange.setText("Xác nhận mật khẩu sai");
                            }
                            if(check == true) {
                                mDatabase.child(finalKeyAcc).child("password").setValue(edtNewPass.getText().toString().trim());
                                txtWarningChange.setText("");
                                txtWarningChange.setVisibility(TextView.GONE);
                                Toast.makeText(ChangePasswordActivity.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();

                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
