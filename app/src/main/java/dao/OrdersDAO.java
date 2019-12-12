package dao;

import android.content.ContentValues;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.constraintlayout.solver.widgets.Snapshot;

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
import dto.OrdersDTO;

public class OrdersDAO implements Serializable {
    private static final String ORDERS = "orders";

    //CREATE
    public OrdersDTO createOrders (OrdersDTO ordersDTO) {
        try {
            if (ordersDTO != null) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ORDERS);
                String id = mDatabase.push().getKey();
                ordersDTO.setId(id);
                mDatabase.child(id).setValue(ordersDTO);
                return ordersDTO;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    //RETRIEVE

    //Retrieve by ID
    private OrdersDTO orderResult;
    public OrdersDTO getOrderByID(String id)
    {
        OrderProductDetailsDTO result;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ORDERS);
        mDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OrdersDTO result = dataSnapshot.getValue(OrdersDTO.class);
                orderResult = result;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());
            }
        });
        return orderResult;
    }

    //Retrieve All
    private ArrayList<OrdersDTO> orderList;
    public ArrayList<OrdersDTO> getOrderAll(){
        Query allOrder = FirebaseDatabase.getInstance().getReference().child(ORDERS);
        allOrder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList = new ArrayList<>();
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    OrdersDTO ordersDTO = item.getValue(OrdersDTO.class);
                    orderList.add(ordersDTO);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());
            }
        });
        return orderList;
    }

    //UPDATE
    //Update Order Status
    public boolean updateOrderStatus (String id, String orderStatus){
        try{
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(orderStatus);
            mDatabase.child(id).child("orderStatus").setValue(orderStatus);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    //DELETE
    public boolean deleteOrder(String id){
        try{
            FirebaseDatabase.getInstance().getReference(ORDERS).child(id).removeValue();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
