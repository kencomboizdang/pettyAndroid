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
import com.example.petty.ProductDetailActivity;
import com.example.petty.R;

import java.util.List;

import dto.AddressesDTO;

public class AddressAdapter extends  RecyclerView.Adapter<AddressAdapter.ViewHolder>  {
    private List<AddressesDTO> addressList;
    private Context context;
    private String type;
    public AddressAdapter() {
    }

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_address, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {
        final AddressesDTO addressesDTO = addressList.get(position);
        holder.txtName.setText(addressesDTO.getName());
        holder.txtPhone.setText(String.valueOf(addressesDTO.getPhone()));
        holder.txtAddress.setText(addressesDTO.getWard()+ ", "+addressesDTO.getDistrict()+", "+addressesDTO.getProvince());
        holder.addressItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=null;
                Bundle bundle = new Bundle();
                bundle.putString("id_address", addressesDTO.getId());
                if (type.equals("edit")) {
                    intent = new Intent(context, AddressDetailActivity.class);

                } else if (type.equals("buying")) {
                    intent = new Intent(context, ConfirmBuyingActivity.class);
                }
                intent.putExtra("data", bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public AddressAdapter(List<AddressesDTO> addressList, Context context, String type) {
        this.addressList = addressList;
        this.context = context;
        this.type = type;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtPhone, txtAddress;
        public LinearLayout addressItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtAddressName);
            txtPhone = (TextView) itemView.findViewById(R.id.txtAddressPhoneNumber);
            txtAddress = (TextView) itemView.findViewById(R.id.txtAddressDetail);
            addressItem = (LinearLayout) itemView.findViewById(R.id.itemAddress);
        }
    }
}
