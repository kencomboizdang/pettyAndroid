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

public class CartsConfirmAdapter extends RecyclerView.Adapter <CartsConfirmAdapter.ViewHolder> {
    private List<CartsDTO> cartsList;
    private List<ProductsDTO> productsList;
    private Context context;
    private final String PRODUCTS = "products";
    private final String CARTS = "carts";
    private TextView txtTotal;
    private float total;
    public CartsConfirmAdapter() {
    }

    @NonNull
    @Override
    public CartsConfirmAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_cart_confirm, parent, false);
        return new CartsConfirmAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final CartsConfirmAdapter.ViewHolder holder, int position) {
        final CartsDTO cartsDTO = cartsList.get(position);
        holder.txtName.setText(cartsDTO.getProductId());
        holder.txtQuantity.setText((String.valueOf(cartsDTO.getQuantity())));
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
                        Glide.with(context)
                                .load(productsDTO.getImg())
                                .into(holder.imgProduct);
                        total+=productsDTO.getPrice()*cartsDTO.getQuantity();
                        productsList.add(productsDTO);
                    }
                }
                DecimalFormat decimalFormat = new DecimalFormat("#,##0");
                txtTotal.setText(decimalFormat.format(total) + " đ");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());

            }
        });
        final DatabaseReference cartDatabase = FirebaseDatabase.getInstance().getReference(CARTS);
    }

    @Override
    public int getItemCount() {
        return cartsList.size();
    }

    public CartsConfirmAdapter(List<CartsDTO> cartsList, Context context) {
        this.cartsList = cartsList;
        this.context = context;
    }

    public CartsConfirmAdapter(List<CartsDTO> cartsList, List<ProductsDTO> productsList, Context context, TextView txtTotal) {
        this.cartsList = cartsList;
        this.productsList = productsList;
        this.context = context;
        this.txtTotal = txtTotal;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtPrice, txtQuantity;
        public ImageView imgProduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtCartName);
            txtPrice = (TextView) itemView.findViewById(R.id.txtCartPrice);
            txtQuantity = (TextView) itemView.findViewById(R.id.txtCartQuantity);
            imgProduct = (ImageView) itemView.findViewById(R.id.imgCart);

        }
    }
}
