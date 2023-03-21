package com.example.homepage;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ForgotPasswordActivity extends AppCompatActivity {
    MaterialButton request;
    TextInputLayout uid, dob;
    String u_id, d_ob;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        uid = findViewById(R.id.fuserid);
        dob = findViewById(R.id.fdob);
        request = findViewById(R.id.reqbtn);
        u_id = uid.getEditText().getText().toString();
        d_ob = dob.getEditText().getText().toString();
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
               moveTonext();
            }
        });
    }

    private void moveTonext() {
        // Some logic
        db.collection("driver").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot QS) {
                boolean flag = false;
                if (QS.isEmpty()) {
                    Log.d(TAG, "No Data Found");
                    return;
                } else {
                    for (QueryDocumentSnapshot qds : QS) {
                        Driver D = qds.toObject(Driver.class);
                        if ((D.getId()).equals(u_id) && (D.getDob()).equals(d_ob)) {
                            Intent intent = new Intent(ForgotPasswordActivity.this, UpdatePassActivity.class);
                            intent.putExtra("d_id", u_id);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(ForgotPasswordActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
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