package adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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

import dto.CustomersDTO;
import dto.OrderProductDetailsDTO;
import dto.OrdersDTO;
import dto.ProductsDTO;
import dto.ResponsesDTO;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    private List<ProductsDTO> productsList;
    private Context context;
    final private String RESPONSES ="responses";
    private final String ORDERPRODUCTDETAILS = "order_product_details";
    private float starTemp;
    private int count;
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ProductsDTO productsDTO = productsList.get(position);
        starTemp=0;
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        holder.txtName.setText(productsDTO.getName());
        holder.txtPrice.setText(decimalFormat.format(productsDTO.getPrice()) + " đ");
        Glide.with(context)
                .load(productsDTO.getImg())
                .into(holder.imgProduct);
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
        DatabaseReference orderDetailDatabase = FirebaseDatabase.getInstance().getReference(ORDERPRODUCTDETAILS);
        orderDetailDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot item : dataSnapshot.getChildren()){
                    final OrderProductDetailsDTO orderProductDetailsDTO = item.getValue(OrderProductDetailsDTO.class);
                    if (orderProductDetailsDTO.getProductId().equals(productsDTO.getId())){
                        System.out.println(productsDTO.getId()+"KKKKKKKKK");
                        DatabaseReference responseDatabase = FirebaseDatabase.getInstance().getReference(RESPONSES);
                        responseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot item : dataSnapshot.getChildren()){
                                    ResponsesDTO responsesDTO = item.getValue(ResponsesDTO.class);
                                    if (orderProductDetailsDTO.getId().equals(responsesDTO.getOrderProductDetailId())){
                                        float tempRating = holder.ratingBar.getRating();
                                        int count = Integer.parseInt(holder.txtCount.getText().toString());
                                        holder.txtCount.setText(String.valueOf(++count));
                                        holder.ratingBar.setRating((holder.ratingBar.getRating()*(count-1)+responsesDTO.getRating())/count);
                                    }
                                }
                                if (holder.ratingBar.getRating()==0)
                                {
                                    holder.ratingBar.setVisibility(RatingBar.GONE);
                                } else{
                                    holder.ratingBar.setVisibility(RatingBar.VISIBLE);
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());

                            }
                        });
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
        return productsList.size();
    }

    public ProductsAdapter(Context context, List<ProductsDTO> productsList) {
        this.productsList = productsList;
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtPrice, txtCount;
        public ImageView imgProduct;
        public LinearLayout productItem;
        public RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtProductName);
            txtPrice = (TextView) itemView.findViewById(R.id.txtProductPrice);
            txtCount = (TextView) itemView.findViewById(R.id.txtCount);
            imgProduct = (ImageView) itemView.findViewById(R.id.imgProduct);
            productItem = (LinearLayout) itemView.findViewById(R.id.itemProduct);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingProductBar);

        }
    }
}
