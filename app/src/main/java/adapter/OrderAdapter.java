package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petty.ProductDetailActivity;
import com.example.petty.R;

import java.text.DecimalFormat;
import java.util.List;

import dto.OrderProductStoresDTO;

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
        OrderProductStoresDTO orderProductStoresDTO = orderList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        holder.txtId.setText(orderProductStoresDTO.getId());
        holder.txtDate.setText(String.valueOf(orderProductStoresDTO.getDate()));
        holder.txtStatus.setText(orderProductStoresDTO.getOrderStatus());
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = (TextView) itemView.findViewById(R.id.txtOrderId);
            txtDate = (TextView) itemView.findViewById(R.id.txtOrderDate);
            txtStatus = (TextView) itemView.findViewById(R.id.txtOrderStatus);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}
