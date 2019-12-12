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

public class OrderProductDetailsDAO implements Serializable {
    private static final String ORDERPRODUCTDETAILS = "order_product_details";


    //CREATE
    public OrderProductDetailsDTO createOrderProductDetails (OrderProductDetailsDTO orderProductDetailsDTO) {
        try {
            if (orderProductDetailsDTO != null) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ORDERPRODUCTDETAILS);
                String id = mDatabase.push().getKey();
                orderProductDetailsDTO.setId(id);
                mDatabase.child(id).setValue(orderProductDetailsDTO);
                return orderProductDetailsDTO;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    //RETRIEVE

    //Retrieve by ID
    private OrderProductDetailsDTO orderProductDetailResult;
    public OrderProductDetailsDTO getOrderProductDetailByID(String id)
    {
        OrderProductDetailsDTO result;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ORDERPRODUCTDETAILS);
        mDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OrderProductDetailsDTO result = dataSnapshot.getValue(OrderProductDetailsDTO.class);
                orderProductDetailResult = result;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());
            }
        });
        return orderProductDetailResult;
    }
    //Retrieve All
    private ArrayList<OrderProductDetailsDTO> orderProductDetailList;
    public ArrayList<OrderProductDetailsDTO> getOrderProductDetailAll(){
        Query allOrder = FirebaseDatabase.getInstance().getReference().child(ORDERPRODUCTDETAILS);
        allOrder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderProductDetailList = new ArrayList<>();
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    OrderProductDetailsDTO ordersDTO = item.getValue(OrderProductDetailsDTO.class);
                    orderProductDetailList.add(ordersDTO);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());
            }
        });
        return orderProductDetailList;
    }
     //UPDATE
     //Update Order Status
     public boolean updateOrderProductDetailStatus (String id, String orderStatus){
        try{
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(ORDERPRODUCTDETAILS);
            mDatabase.child(id).child("orderStatus").setValue(orderStatus);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
     }

    //DELETE
    public boolean deleteOrderProductDetail(String id){
        try{
            FirebaseDatabase.getInstance().getReference(ORDERPRODUCTDETAILS).child(id).removeValue();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
