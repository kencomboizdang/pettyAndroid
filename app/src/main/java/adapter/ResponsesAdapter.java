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
import dto.OrderProductStoresDTO;
import dto.OrdersDTO;
import dto.ProductsDTO;
import dto.ResponsesDTO;
import util.DateTimeStamp;

public class ResponsesAdapter extends RecyclerView.Adapter<ResponsesAdapter.ViewHolder> {
    private List<ResponsesDTO> responseList;
    private Context context;
    final private String RESPONSES ="responses";
    private final String ORDERPRODUCTDETAILS = "order_product_details";
    private final String ORDERPRODUCTSTORES= "order_product_stores";
    private final String ORDERS = "orders";
    private final String CUSTOMERS = "customers";



    public ResponsesAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_response, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ResponsesDTO responsesDTO = responseList.get(position);
        DatabaseReference productDatabase = FirebaseDatabase.getInstance().getReference(ORDERPRODUCTDETAILS);
        productDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    final OrderProductDetailsDTO orderProductDetailsDTO = item.getValue(OrderProductDetailsDTO.class);
                    if (responsesDTO.getOrderProductDetailId().equals(orderProductDetailsDTO.getId())){
                        DateTimeStamp dateTimeStamp = new DateTimeStamp();
                        holder.txtDate.setText(dateTimeStamp.convertToDateTimeString(orderProductDetailsDTO.getDate()));
                        DatabaseReference productDatabase = FirebaseDatabase.getInstance().getReference(ORDERPRODUCTSTORES);
                        productDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot item : dataSnapshot.getChildren()){
                                    final OrderProductStoresDTO orderProductStoresDTO = item.getValue(OrderProductStoresDTO.class);
                                    if (orderProductDetailsDTO.getOrderProductStoreId().equals(orderProductStoresDTO.getId())){
                                        DatabaseReference productDatabase = FirebaseDatabase.getInstance().getReference(ORDERS);
                                        productDatabase.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot item : dataSnapshot.getChildren()){
                                                    final OrdersDTO ordersDTO = item.getValue(OrdersDTO.class);
                                                    if (orderProductStoresDTO.getOrderId().equals(ordersDTO.getId())){
                                                        DatabaseReference productDatabase = FirebaseDatabase.getInstance().getReference(CUSTOMERS);
                                                        productDatabase.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                for (DataSnapshot item : dataSnapshot.getChildren()){
                                                                    CustomersDTO customersDTO = item.getValue(CustomersDTO.class);
                                                                    if (ordersDTO.getCustomerId().equals(customersDTO.getId())){
                                                                            holder.txtCustomerName.setText(customersDTO.getName());
                                                                            break;
                                                                    }
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
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());
                            }
                        });
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", databaseError.toException());

            }
        });
        holder.txtCustomerResponse.setText(responsesDTO.getContent());
        holder.ratingBar.setRating(responsesDTO.getRating());
        if (responsesDTO.getImg()=="" || responsesDTO.getImg()==null){
            holder.imgProduct.setVisibility(ImageView.GONE);
        }
        Glide.with(context)
                .load(responsesDTO.getImg())
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public ResponsesAdapter(List<ResponsesDTO> responseList, Context context) {
        this.responseList = responseList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtCustomerName, txtCustomerResponse, txtDate;
        public ImageView imgProduct;
        public RatingBar ratingBar;
        public LinearLayout productItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCustomerName = (TextView) itemView.findViewById(R.id.txtCustomerName);
            txtCustomerResponse = (TextView) itemView.findViewById(R.id.txtResponseMessage);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            imgProduct = (ImageView) itemView.findViewById(R.id.imgResponse);
            productItem = (LinearLayout) itemView.findViewById(R.id.itemProduct);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingProductBar);
        }
    }
}
