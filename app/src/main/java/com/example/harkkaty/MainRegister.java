package com.example.harkkaty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainRegister extends AppCompatActivity {
    TextInputEditText NameText, EmailText, CityText, Password1Text, Password2Text;
    TextView text1, text2, text3, text4, text5;
    ImageView image1, image2, image3, image4, image5;
    Button registerBtn;
    Boolean isAtleast12 = false, hasUpperCase = false, hasLowerCase = false, hasNumber = false, hasSymbol = false, isRegisterationClickble = false;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_registerpage);


        NameText = findViewById(R.id.Name);
        EmailText = findViewById(R.id.userMail);
        CityText = findViewById(R.id.HomeCity);
        Password1Text = findViewById(R.id.Password1);
        Password2Text = findViewById(R.id.Password2);
        text1 = findViewById(R.id.UpperCase);
        image1 = findViewById(R.id.UpperCaseCheck);
        text2 = findViewById(R.id.LowerCase);
        image2 = findViewById(R.id.LowerCaseCheck);
        text3 = findViewById(R.id.Lenght12);
        image3 = findViewById(R.id.imageViewLenght12Check);
        text4 = findViewById(R.id.oneNumber);
        image4 = findViewById(R.id.OneNumberCheck);
        text5 = findViewById(R.id.OneSymbol);
        image5 = findViewById(R.id.OneSpecialCheck);
        registerBtn = findViewById(R.id.RegisterButton);
        registerBtn.setEnabled(false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        // Here checking are all the fields filled correctly so the button can be pressed and registeration is ok

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = NameText.getText().toString(),
                        mail = EmailText.getText().toString(),
                        city = CityText.getText().toString(),
                        password1 = Password1Text.getText().toString(),
                        password2 = Password2Text.getText().toString();
                if (name.length() > 0 && mail.length() > 0 && city.length() > 0 && password1.length() > 0 && password1.equals(password2) && mail.contains("@")) {
                    isRegisterationClickble = true;
                    if (isRegisterationClickble) {
                        registerUser(mail,password1, name, city);

                    }
                } else {
                    checkAllFields(name, mail, city, password1, password2);
                }
            }
        });
        inputChangePass1();
        inputChangePass2();
    }

    // Password checking, does password fulfill the necessary requirements


    public void passWordCheck() {
        System.out.println(isAtleast12 + "" + "" + "" + hasNumber + "" + hasLowerCase + "" + hasUpperCase + "");
        if (Password1Text.getText().toString().length() >= 12) {
            isAtleast12 = true;
            image3.setBackgroundColor(0xff00ff00);
        } else {
            isAtleast12 = false;
            image3.setBackgroundColor(0xffffffff);
        }
        // For uppercase

        if (Password1Text.getText().toString().matches("(.*[A-Z].*)")) {
            hasUpperCase = true;
            image1.setBackgroundColor(0xff00ff00);
        } else {
            hasUpperCase = false;
            image1.setBackgroundColor(0xffffffff);
        }

        // for numbers
        if (Password1Text.getText().toString().matches("(.*[0-9].*)")) {
            image4.setBackgroundColor(0xff00ff00);
            hasNumber = true;
        } else {
            hasNumber = false;
            image4.setBackgroundColor(0xffffffff);
        }

        if (Password1Text.getText().toString().matches("^(?=.*[_.()]).*")) {
            hasSymbol = true;
            image5.setBackgroundColor(0xff00ff00);
        } else {
            hasSymbol = false;
            image5.setBackgroundColor(0xffffffff);
        }

        if (Password1Text.getText().toString().matches("(.*[a-z].*)")) {
            hasLowerCase = true;
            image2.setBackgroundColor(0xff00ff00);
        } else {
            hasLowerCase = false;
            image2.setBackgroundColor(0xffffffff);
        }
        if (hasLowerCase && hasSymbol && hasNumber && hasUpperCase && isAtleast12 && Password1Text.getText().toString().equals(Password2Text.getText().toString())) {
            registerBtn.setEnabled(true);
        }
    }

    /* Everytime password text gets something, the watcher enables passwordchecking method
     */
    public void inputChangePass1() {
        Password1Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passWordCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void inputChangePass2() {
        Password2Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passWordCheck();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void checkAllFields(String name, String mail, String city, String password1, String password2) {

        if (name.length() <= 0) {
            Toast.makeText(this, "Name field is empty", Toast.LENGTH_SHORT).show();

        }
        if (city.length() <= 0) {
            Toast.makeText(this, "City field is empty", Toast.LENGTH_SHORT).show();
        }
        if (mail.length() <= 0) {
            Toast.makeText(this, "Mail field is empty", Toast.LENGTH_SHORT).show();

        }
        if (mail.contains("@") == false) {
            Toast.makeText(this, "Mail field not contain '@'", Toast.LENGTH_SHORT).show();
        }

        if (name.length() > 0 && mail.length() > 0 && city.length() > 0 && password1.length() > 0 && password1.equals(password2) && mail.contains("@")) {
            System.out.println("Kaikki oko");
            registerBtn.setEnabled(true);
        }
    }

    private void registerUser(String mail, String password, String name, String city){
        auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(MainRegister.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    String userID;
                    userID = auth.getCurrentUser().getUid();
                    DocumentReference documentReference = db.collection("users").document(userID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("name", name);
                    user.put("city", city);
                    user.put("email", mail);

                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("TAG", "Success: Profile is created for "+ userID);
                        }
                    });
                    NameText.setText("");
                    EmailText.setText("");
                    CityText.setText("");
                    Password1Text.setText("");
                    Password2Text.setText("");
                    Toast.makeText(MainRegister.this, "Registerating new user successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainRegister.this, MainLogin.class);
                    Intent intent_password = new Intent(MainRegister.this,MainProfilli.class);
                    intent_password.putExtra("Password", password);
                    intent.putExtra("UserMail", mail);
                    intent.putExtra("Password", password);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainRegister.this, "Registeration failed ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
