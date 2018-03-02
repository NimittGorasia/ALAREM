package com.gercep.alarem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dataClass.User;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String email;
    private String password;
    private EditText mEmail;
    private EditText mPassword;
    private Context context;

    SharedPreferences.Editor userPrefEditor; //getSharedPreferences("user", MODE_PRIVATE).edit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mEmail = findViewById(R.id.input_email);
        mPassword = findViewById(R.id.input_password);
        context = this;
        userPrefEditor = getSharedPreferences("user", MODE_PRIVATE).edit();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void register(View view) {
        email = mEmail.getText().toString();
        password = mPassword.getText().toString();
        Log.d("HAHAHAHAAHAHAHAHAH",email +":" + password);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SIGN-IN", "registerWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            if(user != null) {
                                userPrefEditor.putString("email", user.getEmail());
                                userPrefEditor.putInt("waktuSize", 1);
                                userPrefEditor.putString("waktu-0", "06:30");
                                MyFirebaseInstanceIDService fcms = new MyFirebaseInstanceIDService();
                                String token = fcms.getToken();
                                userPrefEditor.putString("fcmtoken", token);
                                userPrefEditor.putString("fcmserver", "AIzaSyA599wj76y86zcIw2OM7cLwa8H0H7iMdPw");

                                userPrefEditor.putFloat("langitude", new Float(107.609810));
                                userPrefEditor.putFloat("latitude", new Float(-6.914744));

                                Globals g = Globals.getInstance();
                                g.setRingsLatitude((float)107.609810);
                                g.setRingsLongitude((float)-6.914744);

                                userPrefEditor.apply();
                                User newUser = new User(user.getEmail(), new Float(107.609810), new Float(-6.914744));
                                Log.d("EMAIL REGISTER", user.getEmail());
                                mDatabase.child("users").child(user.getEmail().replace(".", "%")).setValue(newUser);
                            }

                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SIGN-IN", "registerWithEmail:failure", task.getException());
                            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                        }

                        // ...
                    }
                });
    }

    public void to_signin(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}
