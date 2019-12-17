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

import com.bumptech.glide.Glide;
import com.example.petty.ProductDetailActivity;
import com.example.petty.R;

import java.text.DecimalFormat;
import java.util.List;

import dto.StoresDTO;

public class Stores2Adapter  extends RecyclerView.Adapter<Stores2Adapter.ViewHolder> {
    private List<StoresDTO> storesList;
    private Context context;

    public Stores2Adapter() {
    }

    @NonNull
    @Override
    public Stores2Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_store_2, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Stores2Adapter.ViewHolder holder, int position) {
        StoresDTO storesDTO = storesList.get(position);
        Glide.with(context)
                .load(storesDTO.getLogoImg())
                .into(holder.imgStore);
        //holder.imgProduct.setImageResource();
    }

    @Override
    public int getItemCount() {
        return storesList.size();
    }

    public Stores2Adapter(List<StoresDTO> storesList, Context context) {
        this.storesList = storesList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgStore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgStore = (ImageView) itemView.findViewById(R.id.imgStore);
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
