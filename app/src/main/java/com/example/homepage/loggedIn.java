package com.example.homepage;

import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import android.view.View.OnClickListener;

public class loggedIn extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    private FirebaseFirestore db;
    private static final String USER_ID = "USER_ID";
    Button btn, btn1, btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        SharedPreferences sf = this.getPreferences(Context.MODE_PRIVATE);
        String user_id = getIntent().getStringExtra("UserID");
        String IS_LOGIN = sf.getString("UserID", "1");
        btn = findViewById(R.id.logoutbtn);
        btn1 = findViewById(R.id.bookbtn);
        btn2 = findViewById(R.id.viewbookingbtn);

        btn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Intent intent = new Intent(loggedIn.this, BookRikshaw.class);
                intent.putExtra("id", user_id);
                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Intent intent = new Intent(loggedIn.this, PrevBookings.class);
                startActivity(intent);
            }
        });

        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                SessionManager sf = new SessionManager(loggedIn.this);
                sf.removeSession();
                Intent intent = new Intent(loggedIn.this, loginForm.class);
                startActivity(intent);
                finish();
            }
        });

//        if(IS_LOGIN.equals("1")) {
//            if(user_id != null) {
//                setText(user_id);
//                editor = sf.edit();
//                editor.putString(USER_ID, user_id);
//                editor.apply();
//            }
//            else {
//                Intent intent = new Intent(this, loginForm.class);
//                startActivity(intent);
//                finish();
//            }
//        }
//        else {
//            setText(IS_LOGIN);
//        }
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

