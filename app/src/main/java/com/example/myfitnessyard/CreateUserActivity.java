package com.example.myfitnessyard;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.myfitnessyard.databinding.ActivityCreateUserBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Locale;

public class CreateUserActivity extends AppCompatActivity {
    ActivityCreateUserBinding binding;
    RadioButton feeStats,gymPlan;
    ActivityResultLauncher<String> launcher;
    FirebaseDatabase database;
    FirebaseStorage storage;

    Uri selectedImage;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Creating User...");
        dialog.setCancelable(false);


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

        binding.submit.setOnClickListener(view -> {
            String   uName, uNo, feeStatus, fee, plan, date;


            uName = binding.uName.getText().toString().trim();
            uNo = binding.uNo.getText().toString().trim();
            feeStatus = feeStats.getText().toString();
            fee = binding.fees.getText().toString();
            plan = gymPlan.getText().toString();

            date = binding.joiningDate.getText().toString();


            if (uName.isEmpty() ){
                binding.uName.setError("Please fill the field!");
                return;
            }if (uNo.isEmpty() ){
                binding.uNo.setError("Please fill the field!");
                return;
            }if ( fee.isEmpty() ){
                binding.fees.setError("Please fill the field!");
                return;
            }if (date.isEmpty()){
                binding.joiningDate.setError("Please fill the field!");
                return;
            }
            dialog.show();

            if(selectedImage != null){
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
                                    Users users = new Users(imageUrl,uName, uNo, feeStatus, fee, plan, date);
                                    database.getReference().child("users").child(uNo+uName)
                                            .setValue(users)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    binding.uName.setText("");
                                                    binding.uNo.setText("");
                                                    binding.fees.setText("");
                                                    binding.joiningDate.setText("");
                                                    dialog.cancel();
                                                    Toast.makeText(CreateUserActivity.this, "task successful", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    dialog.cancel();
                                                    Toast.makeText(CreateUserActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                }
                            });
                        }
                    }
                });

            }else {
                String imageUrl = "No Image";
                Users users = new Users(imageUrl,uName, uNo, feeStatus, fee, plan, date);
                database.getReference().child("users").child(uNo+uName)
                        .setValue(users)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                binding.uName.setText("");
                                binding.uNo.setText("");
                                binding.fees.setText("");
                                binding.joiningDate.setText("");
                                dialog.cancel();
                                Toast.makeText(CreateUserActivity.this, "task successful", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.cancel();
                                Toast.makeText(CreateUserActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                            }
                        });
            }




        });

    }



    public void checkBtn(View view) {
        int radioId = binding.feeStatus.getCheckedRadioButtonId();
        feeStats = findViewById(radioId);

    }

    public void checkBtn2(View view) {
        int radioId = binding.Plan.getCheckedRadioButtonId();
        gymPlan = findViewById(radioId);
    }
}


