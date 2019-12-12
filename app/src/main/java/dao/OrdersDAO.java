package dao;

import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

public class OrdersDAO implements Serializable {
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    public void ClickToDelete(String orderId) {
        FirebaseDatabase.getInstance().getReference("users").child(orderId).removeValue();
    }
}
