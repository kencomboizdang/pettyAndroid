package adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import dto.CartsDTO;
import dto.ProductsDTO;
import dto.StoresDTO;

public class CartsAdapter extends RecyclerView.Adapter <CartsAdapter.ViewHolder> {
    private List<CartsDTO> cartsList;
    private Context context;
    private final String PRODUCTS = "product";

    public CartsAdapter() {
    }

    @NonNull
    @Override
    public CartsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_cart, parent, false);
        return new CartsAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final CartsAdapter.ViewHolder holder, int position) {
        final CartsDTO cartsDTO = cartsList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        holder.txtName.setText(cartsDTO.getProductId());
        holder.txtPrice.setText("Tạm");
        holder.txtQuantity.setText((String.valueOf(cartsDTO.getQuantity())));
//        holder.imgProduct.setImageResource();
        DatabaseReference productDatabase = FirebaseDatabase.getInstance().getReference(PRODUCTS);
        productDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    ProductsDTO productsDTO = item.getValue(ProductsDTO.class);
                    if (productsDTO.getId().equals(cartsDTO.getProductId())){
                        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
                       holder.txtName.setText(productsDTO.getName());
                       holder.txtPrice.setText(decimalFormat.format(productsDTO.getPrice()) + " đ");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());

            }
        });
        holder.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.imgSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return cartsList.size();
    }

    public CartsAdapter(List<CartsDTO> cartsList, Context context) {
        this.cartsList = cartsList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtPrice, txtQuantity;
        public ImageView imgProduct, imgSubtract, imgPlus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtProductName);
            txtPrice = (TextView) itemView.findViewById(R.id.txtProductPrice);
            imgProduct = (ImageView) itemView.findViewById(R.id.imgProduct);
            imgSubtract= (ImageView) itemView.findViewById(R.id.btn_substract);
            imgPlus= (ImageView) itemView.findViewById(R.id.btn_plus);
        }
    }
}
