package adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petty.ProductDetailActivity;
import com.example.petty.R;

import java.text.DecimalFormat;
import java.util.List;

import dto.ProductsDTO;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    private List<ProductsDTO> productsList;
    private Context context;

    public ProductsAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ProductsDTO productsDTO = productsList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        holder.txtName.setText(productsDTO.getName());
        holder.txtPrice.setText(decimalFormat.format(productsDTO.getPrice()) + " đ");
        //holder.imgProduct.setImageResource();
        holder.productItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id_product", productsDTO.getId());
                intent.putExtra("data", bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public ProductsAdapter(Context context, List<ProductsDTO> productsList) {
        this.productsList = productsList;
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtPrice;
        public ImageView imgProduct;
        public LinearLayout productItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtProductName);
            txtPrice = (TextView) itemView.findViewById(R.id.txtProductPrice);
            imgProduct = (ImageView) itemView.findViewById(R.id.imgProduct);
            productItem = (LinearLayout) itemView.findViewById(R.id.itemProduct);
        }
    }
}
