package com.example.petty;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sqlite.DatabaseHelper;
import util.DateTimeStamp;

public class AccountDetailActivity extends AppCompatActivity {
    private static final String CUSTOMER = "customers";
    DatabaseReference mDatabase;
    DatabaseHelper myDb;
    EditText edtUpdateName, edtUpdateEmail, edtUpdateBirthday;
    RadioButton rbtUpdateMale, rbtUpdateFemale;
    String keyCustomer = "";

    DateTimeStamp dts = new DateTimeStamp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        edtUpdateBirthday = (EditText) findViewById(R.id.edtUpdateBirthday);
        edtUpdateBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDatePickerFragment picker = new UpdateDatePickerFragment(edtUpdateBirthday);
                picker.show(getSupportFragmentManager(), "datePicker");
            }
        });

        myDb = new DatabaseHelper(this);
        final DateTimeStamp dts = new DateTimeStamp();
        Cursor resCus = myDb.getKeyCustomer();

        while (resCus.moveToFirst()) {
            keyCustomer = resCus.getString(0);
            break;
        }

        edtUpdateName = (EditText) findViewById(R.id.edtUpdateName);

        edtUpdateEmail = (EditText) findViewById(R.id.edtUpdateEmail);

        rbtUpdateMale = (RadioButton) findViewById(R.id.rbtUpdateMale);
        rbtUpdateFemale = (RadioButton) findViewById(R.id.rbtUpdateFemale);
        mDatabase = FirebaseDatabase.getInstance().getReference(CUSTOMER);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.child("id").getValue(String.class).equals(keyCustomer)) {
                            String dbName = ds.child("name").getValue(String.class);
                            String dbEmail = ds.child("email").getValue(String.class);
                            long dbDate = ds.child("dayOfBirth").getValue(long.class);
                            boolean gender = ds.child("gender").getValue(boolean.class);
                            if (gender) {
                                rbtUpdateMale.setChecked(true);
                            } else {
                                rbtUpdateFemale.setChecked(true);
                            }

                            //parse long to Date
                            Date date = new Date(dbDate);

                            //format date to dd/MM/yyyy
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            String realDate = sdf.format(date);

                            edtUpdateName.setText(dbName);
                            edtUpdateEmail.setText(dbEmail);
                            edtUpdateBirthday.setText(realDate);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void clickToUpdateCustomer(View view) throws ParseException {
        final String iName = edtUpdateName.getText().toString();
        final String iEmail = edtUpdateEmail.getText().toString();
        final String iBirthday = edtUpdateBirthday.getText().toString();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.child("id").getValue(String.class).equals(keyCustomer)) {
                            String dbName = ds.child("name").getValue(String.class);
                            String dbEmail = ds.child("email").getValue(String.class);
                            long longdbDate = ds.child("dayOfBirth").getValue(long.class);

                            Date date = new Date(longdbDate);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            String stringdbDate = sdf.format(date);

                            boolean gender = ds.child("gender").getValue(boolean.class);

                            boolean check = false;
                            if(rbtUpdateMale.isChecked()) {
                                check = true;
                            }
                            mDatabase.child(keyCustomer).child("gender").setValue(check);

                            if (iName.equals(dbName) == false) {
                                mDatabase.child(keyCustomer).child("name").setValue(iName);
                            }

                            if (iEmail.equals(dbEmail) == false) {
                                mDatabase.child(keyCustomer).child("email").setValue(iEmail);
                            }

                            if (iBirthday.equals(stringdbDate) == false) {
                                try {
                                    long newBirthday = dts.convertDateToLong(iBirthday);
                                    mDatabase.child(keyCustomer).child("dayOfBirth").setValue(newBirthday);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }

                            Toast.makeText(AccountDetailActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
