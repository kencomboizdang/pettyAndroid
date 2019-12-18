package com.example.petty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class SearchProductActivity extends AppCompatActivity {
    private SearchView edtSearch;
    final SearchScreenFragment searchScreenFragment = new SearchScreenFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        edtSearch = findViewById(R.id.edt_search);
        edtSearch.setIconifiedByDefault(true);
        edtSearch.setFocusable(true);
        edtSearch.setIconified(false);
        edtSearch.requestFocusFromTouch();
        setFragment(searchScreenFragment);
        edtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setFragment(new ProductFilterFragment("search",edtSearch.getQuery().toString().trim()));
                setFragment2(new StoreListFragment("search", edtSearch.getQuery().toString().trim()));
                if(edtSearch.getQuery().toString().length()==0) {
                    setFragment(searchScreenFragment);
                    setFragment2(new EmptyFragment());
                }
                return true;
            }
        });

    }
    public void clickToBackHome(View view) {
        finish();
    }
    private  void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.search_frame, fragment);
        fragmentTransaction.commit();
    }
    private  void setFragment2(Fragment fragment){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(R.id.search_frame_2, fragment);
            fragmentTransaction.commit();

    }
    public void clickToPasteSearch(View view) {
        TextView textView= view.findViewById(R.id.txtHintSearch);
        edtSearch.setQuery(textView.getText().toString(),false);
    }
}
