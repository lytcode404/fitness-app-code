package com.example.myfitnessyard.Fragments;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.myfitnessyard.Activities.CreateUserActivity;
import com.example.myfitnessyard.Activities.MainActivity;
import com.example.myfitnessyard.Adapters.Adapter;
import com.example.myfitnessyard.Models.Users;
import com.example.myfitnessyard.R;
import com.example.myfitnessyard.databinding.FragmentHomeBinding;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {





    public HomeFragment() {
        // Required empty public constructor
    }




    FragmentHomeBinding binding;
    Adapter adapter;

    private MenuItem menuItem;
    private SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();




        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(MainActivity.toolbar);
        activity.getSupportActionBar().setTitle("");


        FirebaseDatabase.getInstance().getReference().child("revenue")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String fund = snapshot.getValue(String.class);
                        MainActivity.funds.setText(fund);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()
                ,LinearLayoutManager.VERTICAL,false));
        binding.recyclerView.setHasFixedSize(true);


        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("users"), Users.class)
                        .build();
        adapter = new Adapter(options);
        binding.recyclerView.setAdapter(adapter);







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
        searchView.setMaxWidth(Integer.MAX_VALUE);
        ImageView icon = searchView.findViewById(com.google.android.material.R.id.search_button);
        Drawable whiteIcon = icon.getDrawable();
        whiteIcon.setTint(Color.WHITE);
        icon.setImageDrawable(whiteIcon);


        EditText searchEditText = searchView.findViewById(com.google.android.material.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));

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
                                .child("users").orderByChild("uNo")
                                .startAt(query).endAt(query+"\uf8ff"), Users.class)
                        .build();
        adapter = new Adapter(options);
        adapter.startListening();
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        adapter.stopListening();
    }

}