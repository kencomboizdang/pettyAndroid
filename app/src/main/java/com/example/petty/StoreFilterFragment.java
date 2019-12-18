//package com.example.petty;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.DefaultItemAnimator;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import adapter.StoresAdapter;
//import dto.StoresDTO;
//
//
//public class StoreFilterFragment extends Fragment {
//    private String type;
//    private String value;
//    private static final String STORES = "stores";
//
//    private List<StoresDTO> storesList = new ArrayList<>();
//    private RecyclerView recyclerView;
//    private StoresAdapter storesAdapter;
//
//    public StoreFilterFragment() {
//
//    }
//
//    public StoreFilterFragment(String type, String value) {
//        this.type = type;
//        this.value = value;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_store_filter, container, false);
//        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_stores);
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(STORES);
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot item : dataSnapshot.getChildren()) {
//                    StoresDTO dto = item.getValue(StoresDTO.class);
//                    if(type.equals("searchStore")) {
//                        if(dto.getName().toLowerCase().contains(value.toLowerCase())) {
//                            storesList.add(dto);
//                        }
//                    }
//                }
//
//                if(!storesList.isEmpty()) {
//                    System.out.println(storesList.size());
//                    storesAdapter = new StoresAdapter(storesList,getActivity());
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false);
//                    recyclerView.setLayoutManager(layoutManager);
//                    recyclerView.setItemAnimator(new DefaultItemAnimator());
//                    recyclerView.setAdapter(storesAdapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        return view;
//    }
//}
