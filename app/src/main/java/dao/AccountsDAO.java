package dao;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dto.AccountsDTO;

public class AccountsDAO implements Serializable {

    private static final String ACCOUNT = "accounts";
    private List<AccountsDTO> result = null;

    // CREATE
    public AccountsDTO createAccount(AccountsDTO dto) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ACCOUNT);
        String accountId = mDatabase.push().getKey();
        dto.setId(accountId);
        mDatabase.child(accountId).setValue(dto);
        return dto;
    }

    //UPDATE
    public AccountsDTO updateAccount(AccountsDTO dto, String newPassword) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ACCOUNT);
        mDatabase.child("-Lvs8_uru8gXaAq0LtZN").child("password").setValue(newPassword);
        return dto;
    }

    //SELECT ONE
    public AccountsDTO getAccountById(String id) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ACCOUNT);
        AccountsDTO dto = null;
        mDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AccountsDTO dto = dataSnapshot.getValue(AccountsDTO.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return dto;
    }

    //SELECT ALL
    public List<AccountsDTO> getAllAccounts() {
        Query allAccounts = FirebaseDatabase.getInstance().getReference(ACCOUNT);
        allAccounts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                result = new ArrayList<>();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    AccountsDTO dto = item.getValue(AccountsDTO.class);
                    result.add(dto);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return result;
    }

    //DELETE
    public boolean deleteAccount(String id) {
        try {
            FirebaseDatabase.getInstance().getReference(ACCOUNT).child(id).removeValue();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
