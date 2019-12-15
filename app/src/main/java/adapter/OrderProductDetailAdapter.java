package adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.petty.ProductDetailActivity;
import com.example.petty.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.List;

import dto.OrderProductDetailsDTO;
import dto.ProductsDTO;
import dto.StoresDTO;

public class OrderProductDetailAdapter extends RecyclerView.Adapter<OrderProductDetailAdapter.ViewHolder> {
    private List<OrderProductDetailsDTO> productDetailsDTOList;
    private Context context;
    private final String PRODUCTS = "products";
    public OrderProductDetailAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_bought_product, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final OrderProductDetailsDTO orderProductDetailsDTO = productDetailsDTOList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        DatabaseReference productDatabase = FirebaseDatabase.getInstance().getReference(PRODUCTS);
        productDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    ProductsDTO productsDTO = item.getValue(ProductsDTO.class);
                    if (productsDTO.getId().equals(orderProductDetailsDTO.getProductId())){
                       holder.txtProductName.setText(productsDTO.getName());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());

            }
        });

    }

    @Override
    public int getItemCount() {
        return productDetailsDTOList.size();
    }

    public OrderProductDetailAdapter(List<OrderProductDetailsDTO> productDetailsDTOList, Context context) {
        this.productDetailsDTOList = productDetailsDTOList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtProductName, txtOrderStoreId, txtOrderDate;
        public ImageView imgProduct;
        public LinearLayout productItem;
        public Button btnEvaluataion, btnReturn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = (TextView) itemView.findViewById(R.id.txtProductName);
            txtOrderDate = (TextView) itemView.findViewById(R.id.txtOrderDate);
            txtOrderStoreId = (TextView) itemView.findViewById(R.id.txtOrderStoreId);
            imgProduct = (ImageView) itemView.findViewById(R.id.imgProductView);
            productItem = (LinearLayout) itemView.findViewById(R.id.itemProduct);
            btnEvaluataion = (Button) itemView.findViewById(R.id.btnEvaluation);
            btnReturn = (Button) itemView.findViewById(R.id.btnReturn);
        }
    }
}
