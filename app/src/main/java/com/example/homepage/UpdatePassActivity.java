package com.example.homepage;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UpdatePassActivity extends AppCompatActivity {

    FirebaseFirestore rootNode;
    MaterialButton update;
    TextInputLayout newpass, cnfpass;
    String u_id,newp,cnfp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pass);
        u_id = getIntent().getStringExtra("d_id");
        newpass = findViewById(R.id.newpass);
        cnfpass = findViewById(R.id.cnfpass);
        newp = newpass.getEditText().getText().toString();
        cnfp = cnfpass.getEditText().getText().toString();
        if(!newp.equals(cnfp)) {
            Toast.makeText(this, "New password and confirm password donot match!", Toast.LENGTH_SHORT).show();
        }
        else {
            if(updatePass(newp)) {
                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdatePassActivity.this, loginForm.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private boolean updatePass(String newp) {
        return true;
    }
}