package com.example.harkkaty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NewEmailPasswordActivity extends AppCompatActivity {
    private TextView email, password;
    private Button mailbtn, passwordbtn, bothbtn, savebtn;

    FirebaseUser user;
    FirebaseAuth auth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_email_password);

        email = findViewById(R.id.textViewNewEmail);
        passwordbtn = findViewById(R.id.passwordbtn);
        password = findViewById(R.id.textViewNewPassword);
        passwordbtn = findViewById(R.id.passwordbtn);
        mailbtn = findViewById(R.id.mailbtn);
        bothbtn = findViewById(R.id.bothbtn);
        savebtn = findViewById(R.id.SaveButtonNewEMailPassword);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        passwordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password.setEnabled(true);
                email.setEnabled(false);
                if(password.getText().toString().length() < 12){
                    Toast.makeText(NewEmailPasswordActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
                }
                else{
                    savebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AuthCredential credential = EmailAuthProvider.getCredential(getIntent().getStringExtra("email"), getIntent().getStringExtra("password"));
                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        user.updatePassword(password.getText().toString());
                                        Toast.makeText(NewEmailPasswordActivity.this, "New password have been changed", Toast.LENGTH_SHORT).show();
                                        password.setText("");
                                        startActivity(new Intent(NewEmailPasswordActivity.this, MainPage.class));
                                        overridePendingTransition(0,0);
                                        finish();
                                    }
                                    else {
                                        try {
                                            throw task.getException();
                                        } catch (Exception e) {
                                            Toast.makeText(NewEmailPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                        }
                    });

                }
            }
        });
        mailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email.setEnabled(true);
                password.setEnabled(false);
                if(email.getText().toString().contains("@") == false){
                    Toast.makeText(NewEmailPasswordActivity.this, "Email does not contain '@' short", Toast.LENGTH_SHORT).show();
                }
                else{
                    savebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AuthCredential credential = EmailAuthProvider.getCredential(getIntent().getStringExtra("email"), getIntent().getStringExtra("password"));
                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        user.updateEmail(email.getText().toString());
                                        Toast.makeText(NewEmailPasswordActivity.this, "New email have been changed", Toast.LENGTH_SHORT).show();
                                        email.setText("");
                                        startActivity(new Intent(NewEmailPasswordActivity.this, MainPage.class));
                                        overridePendingTransition(0,0);
                                        finish();
                                    }
                                    else {
                                        try {
                                            throw task.getException();
                                        } catch (Exception e) {
                                            Toast.makeText(NewEmailPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                        }
                    });

                }
            }
        });
        bothbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password.setEnabled(true);
                email.setEnabled(true);
                if(password.getText().toString().length() < 12){
                    Toast.makeText(NewEmailPasswordActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
                }
                else if (email.getText().toString().contains("@") == false){
                    Toast.makeText(NewEmailPasswordActivity.this, "Email does not contain '@' short", Toast.LENGTH_SHORT).show();
                }
                else{
                    savebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AuthCredential credential = EmailAuthProvider.getCredential(getIntent().getStringExtra("email"), getIntent().getStringExtra("password"));
                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        user.updatePassword(password.getText().toString());
                                        user.updateEmail(email.getText().toString());
                                        password.setText("");
                                        email.setText("");
                                        Toast.makeText(NewEmailPasswordActivity.this, "New password have been changed", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(NewEmailPasswordActivity.this, MainPage.class));
                                        overridePendingTransition(0,0);
                                        finish();
                                    }
                                    else {
                                        try {
                                            throw task.getException();
                                        } catch (Exception e) {
                                            Toast.makeText(NewEmailPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });

    }
}