package com.example.homepage;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class OnRideCompletion extends AppCompatActivity {
    private FirebaseFirestore db;
    private static final String USER_ID = "USER_ID";
    Button btn, btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_ride_completion);
        String user_id = getIntent().getStringExtra("uid");
        btn = findViewById(R.id.cviewbookingbtn);
        btn1 = findViewById(R.id.clogoutbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Intent intent = new Intent(OnRideCompletion.this, PrevBookings.class);
                startActivity(intent);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Intent intent = new Intent(OnRideCompletion.this, loginForm.class);
                startActivity(intent);
                finish();
            }
        });
        setText(user_id);
    }
    private void setText(String user_id) {
        db = FirebaseFirestore.getInstance();
        db.collection("driver").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot QS) {
                if(QS.isEmpty()) {
                    Log.d(TAG, "No Data Found");
                    return;
                }
                else {
                    for(QueryDocumentSnapshot qds : QS) {
                        Driver D = qds.toObject(Driver.class);
                        if ((D.getId()).equals(user_id)) {
                            TextView name = (TextView)findViewById(R.id.cname);
                            TextView uid = (TextView)findViewById(R.id.cuid);
                            TextView phone = (TextView)findViewById(R.id.cphone);
                            name.setText(qds.get("name").toString());
                            uid.setText(qds.get("id").toString());
                            phone.setText(qds.get("phone").toString());
                            break;
                        }
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