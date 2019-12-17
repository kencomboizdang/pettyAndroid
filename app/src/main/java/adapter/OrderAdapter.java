package adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petty.AddressDetailActivity;
import com.example.petty.ConfirmBuyingActivity;
import com.example.petty.OrderProductStoreDetailActivity;
import com.example.petty.ProductDetailActivity;
import com.example.petty.R;

import java.text.DecimalFormat;
import java.util.List;

import dto.OrderProductStoresDTO;
import util.DateTimeStamp;

public class OrderAdapter extends  RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<OrderProductStoresDTO> orderList;
    private Context context;

    public OrderAdapter() {
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        final OrderProductStoresDTO orderProductStoresDTO = orderList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        holder.txtId.setText(orderProductStoresDTO.getId());
        DateTimeStamp dateTimeStamp = new DateTimeStamp();
        holder.txtDate.setText(dateTimeStamp.convertToDateTimeString(orderProductStoresDTO.getDate()));
        holder.txtStatus.setText(orderProductStoresDTO.getOrderStatus());
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderProductStoreDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id_order_product_store", orderProductStoresDTO.getId());
                intent.putExtra("data", bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public OrderAdapter(List<OrderProductStoresDTO> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtId, txtDate, txtStatus;
        public LinearLayout itemLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = (TextView) itemView.findViewById(R.id.txtOrderId);
            txtDate = (TextView) itemView.findViewById(R.id.txtOrderDate);
            txtStatus = (TextView) itemView.findViewById(R.id.txtOrderStatus);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.itemLayout);
        }
    }
}
