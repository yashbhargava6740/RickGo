package com.example.homepage;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class DriverLoginActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    private FirebaseFirestore db;
    private static final String USER_ID = "USER_ID";
    Button btn, btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);
        SharedPreferences sf = this.getPreferences(Context.MODE_PRIVATE);
        String user_id = getIntent().getStringExtra("UserID");
        String IS_LOGIN = sf.getString("UserID", "1");
        btn = findViewById(R.id.dviewbookingbtn);
        btn1 = findViewById(R.id.dlogoutbtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Intent intent = new Intent(DriverLoginActivity.this, PrevBookings.class);
                startActivity(intent);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                sf.edit().remove("UserID").apply();
                Intent intent = new Intent(DriverLoginActivity.this, loginForm.class);
                startActivity(intent);
                finish();
            }
        });

        if(IS_LOGIN.equals("1")) {
            if(user_id != null) {
                setText(user_id);
                editor = sf.edit();
                editor.putString(USER_ID, user_id);
                editor.apply();
            }
            else {
                Intent intent = new Intent(this, loginForm.class);
                startActivity(intent);
                finish();
            }
        }
        else {
            setText(IS_LOGIN);
        }
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
                            TextView name = (TextView)findViewById(R.id.name);
                            TextView uid = (TextView)findViewById(R.id.uid);
                            TextView phone = (TextView)findViewById(R.id.phone);
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