package adapter;


import android.content.Context;

import com.bumptech.glide.Glide;
import com.example.petty.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import java.util.List;

public class BannerAdapter extends PagerAdapter {

    private List<String> imageList;
    private LayoutInflater layoutInflater;
    private Context context;

    public BannerAdapter(List<String> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = layoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_banner, container, false);
        ImageView imageView;
        imageView = view.findViewById(R.id.imageBanner);
        Glide.with(context)
                .load(imageList.get(position))
                .into(imageView);
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

}
