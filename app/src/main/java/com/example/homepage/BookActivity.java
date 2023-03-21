package com.example.homepage;

import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.homepage.databinding.ActivityBookBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {
    FirebaseFirestore db;
    TextView tv;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        String msg = getIntent().getStringExtra("message");
        String d_id = getIntent().getStringExtra("d_id");
        msg = msg + "\n\nKindly reach pickup location, after reaching click the button below";
        tv = findViewById(R.id.msgbox);
        tv.setText(msg);
        btn = findViewById(R.id.ridebtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Intent intent = new Intent(BookActivity.this, Riding.class);
                intent.putExtra("d_id", d_id);
                startActivity(intent);
                finish();
            }
        });
    }
}