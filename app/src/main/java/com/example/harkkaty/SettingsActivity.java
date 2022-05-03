package com.example.harkkaty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private FirebaseAuth auth;
    private NavigationView navigationBarView;
    private Button backButton, savebtn, resetbtn, verify;
    private TextInputEditText new_name, new_city, currentPassword, currentEmail;
    private FirebaseFirestore db;
    private FirebaseUser USER;
    Boolean nam, cit, mail, pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        drawerLayout = findViewById(R.id.drawerlayout);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        actionBarDrawerToggle.syncState();
        navigationBarView = findViewById(R.id.navigationbarmenu);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        backButton = findViewById(R.id.BackButton);
        savebtn = findViewById(R.id.SaveButtonNewEMailPassword);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        USER = auth.getCurrentUser();

        new_name = findViewById(R.id.textViewNewEmail);
        new_city = findViewById(R.id.textViewNewPassword);


        savebtn.setEnabled(false);
        inputCheck();
        inputCheck2();

        navigationBarView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int index = item.getItemId();
                if (index == R.id.LogOutItemFromNavTopBar) {
                    Toast.makeText(SettingsActivity.this, "Log out working", Toast.LENGTH_SHORT).show();
                    auth.signOut();
                    startActivity(new Intent(SettingsActivity.this, MainLogin.class));
                    return true;
                }
                if (index == R.id.Settings) {
                    startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                    return true;
                }
                return true;
            }

        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, MainPage.class));
                overridePendingTransition(0, 0);
                finish();

            }
        });


        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (new_name.getText().toString().length() < 1 && new_city.getText().toString().length() < 1) {
                    Toast.makeText(SettingsActivity.this, "One of the fields id empty!", Toast.LENGTH_SHORT).show();
                } else {
                    savebtn.setEnabled(true);
                    String userID = auth.getCurrentUser().getUid();
                    DocumentReference documentReference = db.collection("users").document(userID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("name", new_name.getText().toString());
                    user.put("city", new_city.getText().toString());
                    documentReference.set(user);

                    Toast.makeText(SettingsActivity.this, "Details changed", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SettingsActivity.this, MainProfilli.class);
                    startActivity(intent);


                    //AuthCredential credential = EmailAuthProvider.getCredential(auth.getCurrentUser().getEmail(),currentPassword.getText().toString());
                    //TODO tee tähän se salasana juttu, jolla confirmataan sähköpostin vaihto
/*
                    USER.reauthenticate(credential).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SettingsActivity.this, "Given password was wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("TAG", "Success: Profile is created for " + userID);
                        }
                    });

 */
                    Toast.makeText(SettingsActivity.this, "New details saved", Toast.LENGTH_SHORT).show();
                    new_name.setText("");
                    new_city.setText("");
                    savebtn.setEnabled(false);
                }
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(SettingsActivity.this, MainLogin.class));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void inputCheck(){
        new_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(new_name.getText().toString().length() > 1 && new_city.getText().toString().length() > 1){
                    savebtn.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    public void inputCheck2(){
        new_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(new_name.getText().toString().length() > 1 && new_city.getText().toString().length() > 1){
                    savebtn.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}
/*

    }
    public void checkAllfields(Boolean name, Boolean city, Boolean mail, Boolean password){
        if(name && city && mail && password){
            verify.setEnabled(true);
        }
        else{
            verify.setEnabled(false);
        }
    }
}

 */