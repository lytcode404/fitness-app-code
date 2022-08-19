package com.example.myfitnessyard.Activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myfitnessyard.Models.EditModel;
import com.example.myfitnessyard.Models.Users;
import com.example.myfitnessyard.R;
import com.example.myfitnessyard.databinding.ActivityCreateUserBinding;
import com.example.myfitnessyard.databinding.ActivityEditBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {
    ActivityEditBinding binding;
    RadioButton feeStats,gymPlan;
    ActivityResultLauncher<String> launcher;
    FirebaseDatabase database;
    FirebaseStorage storage;

    Uri selectedImage;
    ProgressDialog dialog;

    Users model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        model = (Users) getIntent().getSerializableExtra("model");

        dialog = new ProgressDialog(this);
        dialog.setMessage("Updating User...");
        dialog.setCancelable(false);



        Glide.with(EditActivity.this).load(model.getImageUrl())
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(binding.profileImage);

        binding.uName.setText(model.getuName());
        binding.uNo.setText(model.getuNo());
        binding.uPhnumber.setText(model.getuPhno());
        binding.uPhnumber.setText(model.getuPhno());

        binding.joiningDate.setText(model.getDate());

        Toast.makeText(EditActivity.this, model.getFeeStatus(), Toast.LENGTH_SHORT).show();

        getImage();


        binding.update.setOnClickListener(view -> {

            getInstanceViews();

        });

    }

    private void getImage() {
        launcher = registerForActivityResult(new ActivityResultContracts.GetContent()
                , new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if(uri != null){
                            binding.profileImage.setImageURI(uri);
                            selectedImage = uri;

                        }

                    }
                });
        binding.profileImage.setOnClickListener(view -> {

            launcher.launch("image/*");

        });
    }

    private void getInstanceViews() {
        String   uName, uNo,uPhno, date;


        uName = binding.uName.getText().toString().trim();
        uNo = binding.uNo.getText().toString().trim();




        date = binding.joiningDate.getText().toString();
        uPhno = binding.uPhnumber.getText().toString();



        if (uName.isEmpty() ){
            binding.uName.setError("Please fill the field!");
            return;
        }if (uNo.isEmpty() ){
            binding.uNo.setError("Please fill the field!");
            return;
        }if (date.isEmpty()){
            binding.joiningDate.setError("Please fill the field!");
            return;
        }if (uPhno.isEmpty()){
            binding.joiningDate.setError("Please fill the field!");
            return;
        }


        if(selectedImage != null){
            dialog.show();
            StorageReference reference = storage.getReference().child("profiles").child(uNo+uName);
            reference.putFile(selectedImage)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();


                                        Map<String,Object> map = new HashMap<>();
                                        map.put("imageUrl", imageUrl);
                                        map.put("uName",uName);
                                        map.put("uNo",uNo);
                                        map.put("uPhno",uPhno);
                                        map.put("date",date);

                                        if(model.getFeeStatus().equals("Paid")){
                                            FirebaseDatabase.getInstance().getReference().child("paid")
                                                    .child(model.getuNo()+model.getuName())
                                                    .updateChildren(map);
                                        }

                                        if(model.getFeeStatus().equals("Pending")){
                                            FirebaseDatabase.getInstance().getReference().child("pending")
                                                    .child(model.getuNo()+model.getuName())
                                                    .updateChildren(map);
                                        }





                                        FirebaseDatabase.getInstance().getReference().child("users")
                                            .child(model.getuNo()+model.getuName())
                                            .updateChildren(map).addOnCompleteListener(runnable -> {
                                                    Toast.makeText(EditActivity.this, "Successfully updated"
                                                            , Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                });

                                    }
                                });
                            }
                        }
                    });

        }else{
            dialog.show();
            Map<String,Object> map = new HashMap<>();

            map.put("uName",uName);
            map.put("uNo",uNo);
            map.put("uPhno",uPhno);
            map.put("date",date);

            FirebaseDatabase.getInstance().getReference().child("users")
                    .child(model.getuNo()+model.getuName())
                    .updateChildren(map).addOnCompleteListener(runnable -> {
                        Toast.makeText(EditActivity.this, "Successfully updated"
                                , Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });
        }

    }


}