package com.example.myfitnessyard.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfitnessyard.Fragments.PendingFragment;
import com.example.myfitnessyard.Fragments.HomeFragment;
import com.example.myfitnessyard.R;
import com.example.myfitnessyard.Fragments.PaidFragment;
import com.example.myfitnessyard.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
     ActivityMainBinding binding;
     public static Toolbar toolbar;
     public static TextView funds;
     public static FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        funds = findViewById(R.id.funds);
        fab = findViewById(R.id.fab);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        replaceFragment(new HomeFragment());
        binding.bottomNav.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_in);
                    break;
                case R.id.attendence:
                    replaceFragment(new PendingFragment());
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_in);
                    break;
                case R.id.user:
                    replaceFragment(new PaidFragment());
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_in);
                    break;

            }
            return true;
        });

        fab.setOnClickListener(view1 -> {
            startActivity(new Intent(getApplicationContext(), CreateUserActivity.class));
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_in);
        });


        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.fund_box, null);

        final EditText fundVal = (EditText)mView.findViewById(R.id.fundValue);
        Button btn_cancel = (Button)mView.findViewById(R.id.cancel);
        Button btn_submit = (Button)mView.findViewById(R.id.submitRev);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);


        funds.setOnClickListener(view1 -> {
            fundVal.setText(funds.getText().toString());
            alertDialog.show();
        });

        btn_cancel.setOnClickListener(view1 -> {
            alertDialog.dismiss();
        });

        btn_submit.setOnClickListener(view1 -> {


            FirebaseDatabase.getInstance().getReference()
                    .child("revenue").setValue(fundVal.getText().toString());
            Toast.makeText(getApplicationContext(), "Successfully updated", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();

        });
        


        
}
        
        



    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);

        fragmentTransaction.commit();
    }
}