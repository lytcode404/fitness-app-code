package com.example.myfitnessyard.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.myfitnessyard.Fragments.PendingFragment;
import com.example.myfitnessyard.Fragments.HomeFragment;
import com.example.myfitnessyard.R;
import com.example.myfitnessyard.Fragments.PaidFragment;
import com.example.myfitnessyard.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
     ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());
        binding.bottomNav.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.attendence:
                    replaceFragment(new PendingFragment());
                    break;
                case R.id.user:
                    replaceFragment(new PaidFragment());
                    break;

            }
            return true;
        });
        

//

        
}
        
        



    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);

        fragmentTransaction.commit();
    }
}