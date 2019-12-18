package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petty.ProductDetailActivity;
import com.example.petty.R;

import java.text.DecimalFormat;
import java.util.List;

import dto.StoresDTO;

public class StoresAdapter  extends RecyclerView.Adapter<StoresAdapter.ViewHolder> {
    private List<StoresDTO> storesList;
    private Context context;

    public StoresAdapter() {
    }

    public StoresAdapter(Context context, List<StoresDTO> storesList) {
        this.context = context;
        this.storesList = storesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_store, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StoresAdapter.ViewHolder holder, int position) {
        StoresDTO storesDTO = storesList.get(position);
        holder.txtNameStore.setText(storesDTO.getName());
        holder.txtAddress.setText(storesDTO.getWard()+", "+storesDTO.getDistrict()+", "+storesDTO.getProvince());
        holder.imgStore.setImageURI();
    }

    @Override
    public int getItemCount() {
        return storesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNameStore, txtAddress;
        public ImageView imgStore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameStore = (TextView) itemView.findViewById(R.id.txtNameStore);
            txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);
            //imgStore = (ImageView) itemView.findViewById(R.id.imgStore);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context, ProductDetailActivity.class);
//                    context.startActivity(intent);
//                }
//            });
        }
    }
}
