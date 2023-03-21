package com.example.homepage;

import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.Session;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

public class loginForm extends AppCompatActivity {

    FirebaseFirestore db;
    MaterialButton login;
    Button forgot;
    TextInputLayout userid, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_form);
        //Hooks
        db = FirebaseFirestore.getInstance();
        login = (MaterialButton) findViewById(R.id.loginbtn);
        userid = findViewById(R.id.userid);
        pass = findViewById(R.id.password);
        forgot = findViewById(R.id.frgtbtn);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                changePass();
            }
        });
    }

    private void changePass() {
        Intent intent = new Intent(loginForm.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
    public void loginMan(android.view.View view) {
        String uid = userid.getEditText().getText().toString();
        String password = pass.getEditText().getText().toString();
        String Token = null;
        db.collection("driver").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot QS) {
                boolean flag = false;
                if(QS.isEmpty()) {
                    Log.d(TAG, "No Data Found");
                    return;
                }
                else {
                    for(QueryDocumentSnapshot qds : QS) {
                        Driver D = qds.toObject(Driver.class);
                        if ((D.getId()).equals(uid) && (D.getPassword()).equals(password)) {
                            Log.d(TAG, "User Found");
                            flag = true;
                            Toast.makeText(loginForm.this, "Login SuccessFull", Toast.LENGTH_SHORT).show();
                            if(uid.charAt(0) == 'A') {
                                Intent intent = new Intent(loginForm.this,AdminLoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("UserID", uid);
                                startActivity(intent);
                                finish();
                            }
                            else if(uid.charAt(0) == 'D') {
                                FirebaseMessaging.getInstance().getToken()
                                        .addOnCompleteListener(new OnCompleteListener<String>() {
                                            @Override
                                            public void onComplete(@NonNull Task<String> task) {
                                                if (!task.isSuccessful()) {
                                                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                                    return;
                                                }

                                                // Get new FCM registration token
                                                String token = task.getResult();

                                                // Log and toast
                                                //String msg = getString(R.string.msg_token_fmt, token);
                                                Log.d(TAG, token);
                                                FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                                                DocumentReference docref = db1.collection("tokens").document(uid);
                                                docref
                                                        .update("tok", token)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w(TAG, "Error updating document", e);
                                                            }
                                                        });
                                                    docref.update("flag", true)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w(TAG, "Error updating document", e);
                                                            }
                                                        });
                                                //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                Intent intent = new Intent(loginForm.this,DriverLoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("UserID", uid);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Intent intent = new Intent(loginForm.this, loggedIn.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("UserID", uid);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                    if(flag == false) {
                        Log.d(TAG, "User Not Found");
                        Toast.makeText(loginForm.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error Getting Data",e);
            }
        });
    }
}