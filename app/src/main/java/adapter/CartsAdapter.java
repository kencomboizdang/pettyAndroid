package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petty.ProductDetailActivity;
import com.example.petty.R;

import java.text.DecimalFormat;
import java.util.List;

import dto.CartsDTO;

public class CartsAdapter extends RecyclerView.Adapter <CartsAdapter.ViewHolder> {
    private List<CartsDTO> cartsList;
    private Context context;

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
    public void onBindViewHolder(@NonNull CartsAdapter.ViewHolder holder, int position) {
        CartsDTO cartsDTO = cartsList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        holder.txtName.setText(cartsDTO.getProductId());
        holder.txtPrice.setText("Tạm");
        holder.txtQuantity.setText((String.valueOf(cartsDTO.getQuantity())));
//        holder.imgProduct.setImageResource();
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    context.startActivity(intent);
                }
            });
            imgSubtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            imgPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
