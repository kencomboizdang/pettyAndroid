package dao;

import android.content.ContentValues;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

import dto.CartsDTO;
import dto.OrdersDTO;

public class CartsDAO implements Serializable {

    private static final String CARTS = "carts";
    //CREATE
    public CartsDTO createCarts (CartsDTO cartDTO) {
        try {
            if (cartDTO != null) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(CARTS);
                String id = mDatabase.push().getKey();
                cartDTO.setId(id);
                mDatabase.child(id).setValue(cartDTO);
                return cartDTO;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    //Retrieve All By Customer Id
    private ArrayList<CartsDTO> cartList;
    public ArrayList<CartsDTO> getCartAllByCustomer(String id){
        Query allCart = FirebaseDatabase.getInstance().getReference().child(CARTS).child(id);
        allCart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cartList = new ArrayList<>();
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    CartsDTO cartsDTO = item.getValue(CartsDTO.class);
                    cartList.add(cartsDTO);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());
            }
        });
        return cartList;
    }

    //UPDATE
    //Update Order Status
    public boolean updateCartQuantity (String id, int quantity){
        try{
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(CARTS);
            mDatabase.child(id).child("quantity").setValue(quantity);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //DELETE
    public boolean deleteCarts(String id){
        try{
            FirebaseDatabase.getInstance().getReference(CARTS).child(id).removeValue();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
