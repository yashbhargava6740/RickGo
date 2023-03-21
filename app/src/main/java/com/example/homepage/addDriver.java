package com.example.homepage;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.example.homepage.Driver;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class addDriver extends AppCompatActivity {
    FirebaseFirestore rootNode;
    TextInputLayout drivername, driverId, driverPassword, driverPhone, driverDob;
    MaterialButton addDet, forgotBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_driver);
        rootNode = FirebaseFirestore.getInstance();
        addDet = findViewById(R.id.driverbtn);      // Button
        drivername = findViewById(R.id.drivername);
        driverId = findViewById(R.id.driverid);
        driverPassword = findViewById(R.id.driverpass);
        driverPhone = findViewById(R.id.phoneno);
        driverDob = findViewById(R.id.driverdob);
        addDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                regDriver();
            }
        });
    }

    private void addData(Driver D, String uid) {
        rootNode.collection("driver")
                .add(D)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Driver Added With ID" + documentReference.getId());
                        Toast.makeText(addDriver.this, "New Driver Added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error Adding Data", e);
                        Toast.makeText(addDriver.this, "Error Loading Data", Toast.LENGTH_SHORT).show();
                    }
                });

                TokenCl tk = new TokenCl();
                rootNode.collection("tokens").document(uid)
                .set(tk)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });


    }
    public void regDriver() {
        String name = drivername.getEditText().getText().toString();
        String id = driverId.getEditText().getText().toString();
        String pass = driverPassword.getEditText().getText().toString();
        String phone = driverPhone.getEditText().getText().toString();
        String dob = driverDob.getEditText().getText().toString();
        Driver D = new Driver(name,id,pass,phone,dob);
        rootNode.collection("driver").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot QS) {
                if(QS.isEmpty()) {
                    addData(D,id);
                }
                else {
                    ArrayList<String>al = new ArrayList<>();
                    for(QueryDocumentSnapshot qds : QS) {
                        Driver Dtemp = qds.toObject(Driver.class);
                        al.add(Dtemp.getId());
                    }
                    int i = 0;
                    int flg = 0;
                    while(al.size() > i) {
                        if(al.get(i).equals(id)) {
                            flg = 1;
                            break;
                        }
                        i++;
                    }
                    if(flg == 1) {
                        Log.d(TAG, "Already Registered, Login!");
                            Toast.makeText(addDriver.this, "Already Registered, Login!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(addDriver.this,loginForm.class);
                            startActivity(intent);
                            finish();
                    }
                    else {
                        addData(D,id);
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
