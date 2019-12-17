package adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.petty.ProductListActivity;
import com.example.petty.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.List;

import dto.CategoriesDTO;
import dto.OrderProductDetailsDTO;
import dto.ProductsDTO;
import dto.StoresDTO;

public class Categories2Adapter extends RecyclerView.Adapter<Categories2Adapter.ViewHolder> {
    private List<CategoriesDTO> categoriesList;
    private Context context;
    public Categories2Adapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_category_2, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final CategoriesDTO categoriesDTO = categoriesList.get(position);
        holder.txtCategoryName.setText(categoriesDTO.getName());
        holder.txtCategoryDescription.setText(categoriesDTO.getDescription());
        Glide.with(context)
                .load(categoriesDTO.getImg())
                .into(holder.imgCategory);
        holder.viewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("type", "category");
                bundle.putString("value", categoriesDTO.getId());
                intent.putExtra("data", bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public Categories2Adapter(List<CategoriesDTO> categoriesList, Context context) {
        this.categoriesList = categoriesList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtCategoryName, txtCategoryDescription;
        public ImageView imgCategory;
        public LinearLayout viewLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategoryName = (TextView) itemView.findViewById(R.id.txtCategory);
            txtCategoryDescription = (TextView) itemView.findViewById(R.id.txtCategoryDescription);
            imgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
            viewLayout = (LinearLayout) itemView.findViewById(R.id.viewLayout);
        }
    }
}
