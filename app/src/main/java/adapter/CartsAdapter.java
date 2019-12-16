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
    private final String PRODUCTS = "products";
    private final String CARTS = "carts";
    private TextView txtTotal;
    private float total =0;
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
    public void onBindViewHolder(@NonNull final CartsAdapter.ViewHolder holder, final int position) {
        final CartsDTO cartsDTO = cartsList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
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

        holder.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartsDTO.setQuantity(cartsDTO.getQuantity()+1);
                cartDatabase.child(cartsDTO.getId()).child("quantity").setValue(cartsDTO.getQuantity());
                holder.txtQuantity.setText(String.valueOf(cartsDTO.getQuantity()));
            }
        });
        holder.imgSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartsDTO.setQuantity(cartsDTO.getQuantity()-1);
                cartDatabase.child(cartsDTO.getId()).child("quantity").setValue(cartsDTO.getQuantity());
                holder.txtQuantity.setText(String.valueOf(cartsDTO.getQuantity()));
                if (cartsDTO.getQuantity() == 0){
                    cartsList.remove(position);
                    FirebaseDatabase.getInstance().getReference(CARTS).child(cartsDTO.getId()).removeValue();
                }
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartsList.remove(position);
                FirebaseDatabase.getInstance().getReference(CARTS).child(cartsDTO.getId()).removeValue();
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

    public CartsAdapter(List<CartsDTO> cartsList, Context context, TextView txtTotal) {
        this.cartsList = cartsList;
        this.context = context;
        this.txtTotal = txtTotal;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtPrice, txtQuantity;
        public ImageView imgProduct, imgSubtract, imgPlus, imgDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtCartName);
            txtPrice = (TextView) itemView.findViewById(R.id.txtCartPrice);
            txtQuantity = (TextView) itemView.findViewById(R.id.txtCartQuantity);
            imgProduct = (ImageView) itemView.findViewById(R.id.imgCart);
            imgSubtract= (ImageView) itemView.findViewById(R.id.btn_substract);
            imgPlus= (ImageView) itemView.findViewById(R.id.btn_plus);
            imgDelete= (ImageView) itemView.findViewById(R.id.btn_delete);
        }
    }

}
