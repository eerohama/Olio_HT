package com.example.harkkaty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainLogin extends AppCompatActivity {
    TextView textViewRegister;
    EditText mail, password;
    Button loginbtn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainlogin);
        textViewRegister = findViewById(R.id.register);
        mail = findViewById(R.id.InputMail);
        password = findViewById(R.id.InputPassword);
        loginbtn = findViewById(R.id.LoginButton);
        firebaseAuth = FirebaseAuth.getInstance();


        mail.setText(getIntent().getStringExtra("UserMail"));
        password.setText(getIntent().getStringExtra("Password"));
        String user_password = getIntent().getStringExtra("Password");


        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainLogin.this,MainRegister.class);
                startActivity(intent);
                finish();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    public void loginUser(){
        /* Checking that all the details is filled correctly that user can log in

         */
        String login_mail = mail.getText().toString(),
                login_password = password.getText().toString();
        if(login_mail.length() <= 0){
            Toast.makeText(this, "Mail field is empty", Toast.LENGTH_SHORT).show();
        }
        else if(login_password.length()<= 0){
            Toast.makeText(this, "Password field is empty", Toast.LENGTH_SHORT).show();
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(login_mail,login_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(MainLogin.this, "Login successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainLogin.this, MainPage.class));
                    }
                    else{
                        Toast.makeText(MainLogin.this, "Login error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}