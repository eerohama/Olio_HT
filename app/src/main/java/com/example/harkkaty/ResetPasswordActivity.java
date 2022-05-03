package com.example.harkkaty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ResetPasswordActivity extends AppCompatActivity {
    private TextInputEditText password;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db;
    Button svbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        //password = findViewById(R.id.passwordresetinputlayout);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        svbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.updateEmail(password.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ResetPasswordActivity.this, "New Password created", Toast.LENGTH_SHORT).show();
                        System.out.println("New password: " + user.getEmail());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ResetPasswordActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}