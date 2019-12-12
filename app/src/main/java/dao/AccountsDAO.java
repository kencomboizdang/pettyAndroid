package dao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

import dto.AccountsDTO;

public class AccountsDAO implements Serializable {

    private static final String ACCOUNT = "accounts";

    public AccountsDTO createAccount(AccountsDTO dto) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ACCOUNT);
        String accountId = mDatabase.push().getKey();
        mDatabase.child(accountId).setValue(dto);
        return dto;   
    }

//    public AccountsDTO updateAccount(AccountsDTO dto, String newPassword) {
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ACCOUNT);
//        mDatabase.child("-LvrmQFeXMwONfN1TBXz").child("password").setValue(newPassword);
//        return dto;
//    }
}
