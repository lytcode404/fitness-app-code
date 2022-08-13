package com.example.myfitnessyard;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.example.myfitnessyard.databinding.AttendenceFragmentBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class AttendenceFragment extends Fragment {





    public AttendenceFragment() {
        // Required empty public constructor
    }




    AttendenceFragmentBinding binding;
    Adapter adapter;
    Toolbar toolbar;
    private MenuItem menuItem;
    private SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        binding = AttendenceFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()
                ,LinearLayoutManager.VERTICAL,false));
        binding.recyclerView.setHasFixedSize(true);

        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("pending"), Users.class)
                        .build();
        adapter = new Adapter(options);
        binding.recyclerView.setAdapter(adapter);


        binding.fab.setOnClickListener(view1 -> {
            startActivity(new Intent(view.getContext(), CreateUserActivity.class));
        });


        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.menu_item,menu);
        menuItem = menu.findItem(R.id.searchId);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setIconified(true);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mySearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mySearch(query);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void mySearch(String query) {
        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("pending").orderByChild("uNo")
                                .startAt(query).endAt(query+"\uf8ff"), Users.class)
                        .build();
        adapter = new Adapter(options);
        adapter.startListening();
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }



    //    @Override
//    public void onResume() {
//        super.onStop();
//        adapter.stopListening();
//    }
}