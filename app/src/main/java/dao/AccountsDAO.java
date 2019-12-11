package dao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

import dto.AccountsDTO;

public class AccountsDAO implements Serializable {

    private static final String ACCOUNT = "accounts";
    public AccountsDTO createAccount(AccountsDTO accounts) {
        accounts = null;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ACCOUNT);
        String accountId = mDatabase.push().getKey();
        accounts = new AccountsDTO(accountId, "Linh","123456","user");
        mDatabase.child(accountId).setValue(accounts);
        return accounts;
    }
}
