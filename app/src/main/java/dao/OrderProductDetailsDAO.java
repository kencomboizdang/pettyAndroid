package dao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

import dto.OrderProductDetailsDTO;

public class OrderProductDetailsDAO implements Serializable {
    public void createOrderProductDetailsDAO(OrderProductDetailsDTO dto) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("order_product_detail");
        String id = mDatabase.push().getKey();

    }
}
