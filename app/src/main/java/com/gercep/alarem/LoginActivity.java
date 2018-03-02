package com.gercep.alarem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dataClass.User;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText mEmail;
    private EditText mPassword;
    private String email;
    private String password;
    private Context context;
    private DatabaseReference mDatabase;

    private String darurat;
    private Globals g;

    SharedPreferences.Editor userPrefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        g = Globals.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mEmail = findViewById(R.id.input_email);
        mPassword = findViewById(R.id.input_password);
        context = this;
        userPrefEditor = getSharedPreferences("user", MODE_PRIVATE).edit();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        MyFirebaseInstanceIDService fcms = new MyFirebaseInstanceIDService();
        String token = fcms.getToken();
        Log.d("FIREBASE TOKEN HAHAHAHA", token);
        userPrefEditor.putString("fcmtoken", token);
        userPrefEditor.putString("fcmserver", "AIzaSyA599wj76y86zcIw2OM7cLwa8H0H7iMdPw");
        userPrefEditor.apply();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        MyFirebaseInstanceIDService fcms = new MyFirebaseInstanceIDService();
        String token = fcms.getToken();
        Log.d("FIREBASE TOKEN HAHAHAHA", token);
        userPrefEditor.putString("fcmtoken", token);
        userPrefEditor.putString("fcmserver", "AIzaSyA599wj76y86zcIw2OM7cLwa8H0H7iMdPw");
        userPrefEditor.apply();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        MyFirebaseInstanceIDService fcms = new MyFirebaseInstanceIDService();
        String token = fcms.getToken();
        Log.d("FIREBASE TOKEN HAHAHAHA", token);
        userPrefEditor.putString("fcmtoken", token);
        userPrefEditor.putString("fcmserver", "AIzaSyA599wj76y86zcIw2OM7cLwa8H0H7iMdPw");
        userPrefEditor.apply();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    public void sign_in(View view) {
        email = mEmail.getText().toString();
        password = mPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SIGN-IN", "signInWithEmail:success");
                            final FirebaseUser Fuser = mAuth.getCurrentUser();

                            if (Fuser != null) {
                                Log.d("DEBUG EMAILL", Fuser.getEmail());
                                userPrefEditor.putString("email", Fuser.getEmail());

                                darurat = Fuser.getEmail();

                                mDatabase.child("users")
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                //klo udah ada textView di layout, hapus
                                                Log.d("DEBUG EMAILL", darurat);
                                                //mulai cari di database
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    User user = snapshot.getValue(User.class);
                                                    if (user != null) {
                                                        //kalau sama dengan email user, di proses
                                                        if(user.email != null) {
                                                            if (user.email.equals(darurat)) {
                                                                Log.d("DEBUG EMAILD DATABASE", user.email);
                                                                Integer id = 0;
                                                                int waktuSize = user.waktu.size();
                                                                userPrefEditor.putInt("waktuSize", waktuSize);
                                                                for (String alarmTime : user.waktu) {
                                                                    if (alarmTime != null) {
                                                                        userPrefEditor.putString("waktu-" + id.toString(), alarmTime);
                                                                        id++;
                                                                    }
                                                                }
                                                                userPrefEditor.putFloat("langitude", user.langitude);
                                                                userPrefEditor.putFloat("latitude", user.latitude);

                                                                Globals g = Globals.getInstance();

                                                                g.setRingsLatitude(user.latitude);
                                                                g.setRingsLongitude(user.langitude);
                                                                Log.i("hei", String.valueOf(user.langitude));
                                                            }
                                                        }
                                                    }
                                                }
                                                MyFirebaseInstanceIDService fcms = new MyFirebaseInstanceIDService();
                                                String token = fcms.getToken();
                                                Log.d("FIREBASE TOKEN HAHAHAHA", token);
                                                userPrefEditor.putString("fcmtoken", token);
                                                userPrefEditor.putString("fcmserver", "AIzaSyA599wj76y86zcIw2OM7cLwa8H0H7iMdPw");
                                                userPrefEditor.apply();
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("SIGN-IN", "signInWithEmail:failure", task.getException());
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }

                            // ...
                        }
                        }
                    });
                }


    public void to_register(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }
}
