package dao;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

import dto.CustomersDTO;

public class CustomersDAO implements Serializable {

    private static final String CUSTOMER = "customers";

    public CustomersDTO createCustomer(CustomersDTO dto) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(CUSTOMER);
        String customerId = mDatabase.push().getKey();
        dto.setId(customerId);
        mDatabase.child(customerId).setValue(dto);
        return dto;
    }

    public CustomersDTO getCustomerById(String id) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(CUSTOMER);
        CustomersDTO dto = null;
        mDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CustomersDTO dto = dataSnapshot.getValue(CustomersDTO.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return dto;
    }

//    public CustomersDTO updateCustomer(CustomersDTO dto){
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(CUSTOMER);
//        mDatabase
//    }
}
