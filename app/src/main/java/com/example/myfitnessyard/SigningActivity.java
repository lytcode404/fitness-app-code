package com.example.myfitnessyard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myfitnessyard.databinding.ActivitySigningBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SigningActivity extends AppCompatActivity {
    ActivitySigningBinding binding;
    FirebaseAuth mAuth;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySigningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(SigningActivity.this);

        dialog.setMessage("Logging to your account...");
        dialog.setCancelable(false);

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(SigningActivity.this, MainActivity.class));
            finishAffinity();
        }

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.emailId.getText().toString().trim();
                String password = binding.pass.getText().toString().trim();

                if (email.isEmpty()){
                    binding.emailId.setError("plz fill the field!");
                    return;
                }
                if (password.isEmpty()){
                    binding.pass.setError("plz fill the field!");
                    return;
                }

//                dialog.show();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SigningActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information

//                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    dialog.cancel();
                                    startActivity(new Intent(SigningActivity.this
                                            , MainActivity.class));

                                    finishAffinity();
                                } else {
//                                    dialog.cancel();
                                    Toast.makeText(SigningActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SigningActivity.this, e.toString(),
                                        Toast.LENGTH_SHORT).show();
//                                dialog.cancel();
                            }
                        });

            }
        });



    }
}