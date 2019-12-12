package dao;

import android.content.ContentValues;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

import dto.OrderProductDetailsDTO;
import dto.OrderProductStoresDTO;

public class OrderProductStoresDAO implements Serializable {

    private static final String ORDERPRODUCTSTORE = "order_product_stores";

    //CREATE
    public OrderProductStoresDTO createOrderProductDetails (OrderProductStoresDTO orderProductStoresDTO) {
        try {
            if (orderProductStoresDTO != null) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ORDERPRODUCTSTORE);
                String id = mDatabase.push().getKey();
                orderProductStoresDTO.setId(id);
                mDatabase.child(id).setValue(orderProductStoresDTO);
                return orderProductStoresDTO;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    //RETRIEVE

    //Retrieve by ID
    private OrderProductStoresDTO orderProductStoreResult;
    public OrderProductStoresDTO getOrderProductStoreByID(String id)
    {
        OrderProductDetailsDTO result;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ORDERPRODUCTSTORE);
        mDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OrderProductStoresDTO result = dataSnapshot.getValue(OrderProductStoresDTO.class);
                orderProductStoreResult = result;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());
            }
        });
        return orderProductStoreResult;
    }
    //Retrieve All
    private ArrayList<OrderProductStoresDTO> orderProductStoreList;
    public ArrayList<OrderProductStoresDTO> getOrderProductStoreAll(){
        Query allOrder = FirebaseDatabase.getInstance().getReference().child(ORDERPRODUCTSTORE);
        allOrder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderProductStoreList = new ArrayList<>();
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    OrderProductStoresDTO ordersDTO = item.getValue(OrderProductStoresDTO.class);
                    orderProductStoreList.add(ordersDTO);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());
            }
        });
        return orderProductStoreList;
    }

    // UPDATE
    //Update Order Status
    public boolean updateOrderProductStoreStatus (String id, String orderStatus){
        try{
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ORDERPRODUCTSTORE);
            mDatabase.child(id).child("orderStatus").setValue(orderStatus);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    //DELETE
    public boolean deleteOrderProductStore(String id){
        try{
            FirebaseDatabase.getInstance().getReference(ORDERPRODUCTSTORE).child(id).removeValue();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
