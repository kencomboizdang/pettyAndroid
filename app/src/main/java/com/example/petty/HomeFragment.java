package com.example.petty;


import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.List;

import adapter.ProductsAdapter;
import adapter.ProductsHistoryAdapter;
import dto.ProductsDTO;
import dto.StoresDTO;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private List<ProductsDTO> productsList= new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductsHistoryAdapter productsHistoryAdapter;

    public HomeFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView txtSeeHistory = view.findViewById(R.id.tvSeeHistory);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        ImageView imgQR = (ImageView) view.findViewById(R.id.imgQRCode);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("products");
//        String id = mDatabase.push().getKey();
//        ProductsDTO dto = new ProductsDTO(id, "Bánh thưởng cho chó JerHigh Strawberry (70g)", "Bánh rất ngon", 20500, 5 ,"15x15cm","6.2", "561615","Korean", 88, "active", 1085616515, 189616515, "feaffea","fedaw");
//        mDatabase.child(id).setValue(dto);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    System.out.println(item);
                    ProductsDTO productsDTO = item.getValue(ProductsDTO.class);
                    productsList.add(productsDTO);
                }
                if (!productsList.isEmpty()) {
                    productsHistoryAdapter = new ProductsHistoryAdapter(getActivity(), productsList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.HORIZONTAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(productsHistoryAdapter);
                    txtSeeHistory.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        String text="Hello"; // Whatever you need to encode in the QR code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imgQR.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

//        DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference("stores");
//        String id = mDatabase2.push().getKey();
//        StoresDTO dto = new StoresDTO(id, "Petmart City", 199186415, "Pert Mart City là cửa hàng chuyên cung cấp các loại sản phẩm dành riêng cho thú cưng", "https://firebasestorage.googleapis.com/v0/b/petty-418a3.appspot.com/o/images%2Flogo_store_petmart.PNG?alt=media&token=8a3c55b7-8cd3-4006-8b9a-37877ca15fd9", "Tp.HCM", "Q.12","Tân Chánh Hiệp", "132 Dương Thị Mười", "0969336526", "petmart@gmail.com", true, "098561", 1896541,18653, "null");
//        mDatabase2.child(id).setValue(dto);

        return view;

    }

}
