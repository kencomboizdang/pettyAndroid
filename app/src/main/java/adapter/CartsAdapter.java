package adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
    private LinearLayout linearLayout;
    private Button btnBuying;
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
        final DecimalFormat decimalFormat = new DecimalFormat("#,##0");
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
                System.out.println(total);
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
                DatabaseReference productDatabase = FirebaseDatabase.getInstance().getReference(PRODUCTS);
                productDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot item : dataSnapshot.getChildren()){
                            ProductsDTO productsDTO = item.getValue(ProductsDTO.class);
                            if (productsDTO.getId().equals(cartsDTO.getProductId())){
                                total+=productsDTO.getPrice();
                                txtTotal.setText(decimalFormat.format(total)+ " đ");
                            }
                        }
                        System.out.println(total);
                        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
                        txtTotal.setText(decimalFormat.format(total) + " đ");
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());

                    }
                });

            }
        });
        holder.imgSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartsDTO.setQuantity(cartsDTO.getQuantity()-1);
                cartDatabase.child(cartsDTO.getId()).child("quantity").setValue(cartsDTO.getQuantity());
                holder.txtQuantity.setText(String.valueOf(cartsDTO.getQuantity()));

                DatabaseReference productDatabase = FirebaseDatabase.getInstance().getReference(PRODUCTS);
                productDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot item : dataSnapshot.getChildren()){
                            ProductsDTO productsDTO = item.getValue(ProductsDTO.class);
                            if (productsDTO.getId().equals(cartsDTO.getProductId())){
                                total-=productsDTO.getPrice();
                                txtTotal.setText(decimalFormat.format(total)+ " đ");
                            }
                        }
                        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
                        txtTotal.setText(decimalFormat.format(total) + " đ");
                        if (cartsDTO.getQuantity() == 0){
                            cartsList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,cartsList.size());
                            FirebaseDatabase.getInstance().getReference(CARTS).child(cartsDTO.getId()).removeValue();
                        }
                        if (cartsList.isEmpty()){
                            linearLayout.setVisibility(LinearLayout.VISIBLE);
                            btnBuying.setBackground(context.getDrawable(R.drawable.btn_white_stroke_grey));
                            btnBuying.setText("VUI LÒNG TIẾP TỤC MUA HÀNG");
                            btnBuying.setEnabled(false);
                            btnBuying.setTextColor(context.getResources().getColor(R.color.colorGrey));
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());

                    }
                });
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference productDatabase = FirebaseDatabase.getInstance().getReference(PRODUCTS);
                productDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot item : dataSnapshot.getChildren()){
                            ProductsDTO productsDTO = item.getValue(ProductsDTO.class);
                            if (productsDTO.getId().equals(cartsDTO.getProductId())){
                                total-=productsDTO.getPrice()*cartsDTO.getQuantity();
                                txtTotal.setText(decimalFormat.format(total)+ " đ");
                            }
                        }
                        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
                        txtTotal.setText(decimalFormat.format(total) + " đ");
                        cartsList.remove(position);
                        FirebaseDatabase.getInstance().getReference(CARTS).child(cartsDTO.getId()).removeValue();
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,cartsList.size());
                        if (cartsList.isEmpty()){
                            linearLayout.setVisibility(LinearLayout.VISIBLE);
                            btnBuying.setBackground(context.getDrawable(R.drawable.btn_white_stroke_grey));
                            btnBuying.setText("VUI LÒNG TIẾP TỤC MUA HÀNG");
                            btnBuying.setEnabled(false);
                            btnBuying.setTextColor(context.getResources().getColor(R.color.colorGrey));
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());

                    }
                });

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

    public CartsAdapter(List<CartsDTO> cartsList, Context context, TextView txtTotal, LinearLayout linearLayout, Button btnBuying) {
        this.cartsList = cartsList;
        this.context = context;
        this.txtTotal = txtTotal;
        this.linearLayout = linearLayout;
        this.btnBuying = btnBuying;
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
