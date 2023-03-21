package com.example.homepage;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;

public class BookRikshaw extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button btn;
    Spinner spinner, spinner1;
    String source, dest;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_rikshaw);
        btn = (Button) findViewById(R.id.book);
        String id = getIntent().getStringExtra("id");
        spinner = findViewById(R.id.sourcespin);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.Locations, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
        dialog = new Dialog(this);
        spinner1 = findViewById(R.id.destinationspin);
        ArrayAdapter<CharSequence> arrayAdapter1 = ArrayAdapter.createFromResource(this, R.array.Locations, android.R.layout.simple_spinner_item);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(arrayAdapter1);
        spinner1.setOnItemSelectedListener(this);
        FirebaseFirestore rootNode = FirebaseFirestore.getInstance();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            // Main Function To be Implemented
            public void onClick(android.view.View view) {
                rootNode.collection("tokens").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot QS) {
                        if (QS.isEmpty()) {
                            Toast.makeText(BookRikshaw.this, "No Data", Toast.LENGTH_SHORT).show();
                        } else {
                            ArrayList<String> al = new ArrayList<>();
                            for (QueryDocumentSnapshot qds : QS) {
                                TokenCl Dtemp = qds.toObject(TokenCl.class);
                                String d_id = qds.getId();
                                Log.d(TAG, d_id);
                                if (Dtemp.getFlag()) {
                                    boolean status = sendNotification(Dtemp.getTok(), d_id);
                                    if (status) {
                                        Log.d(TAG, "Inside Status if");
                                        FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                                        DocumentReference docref = db1.collection("tokens").document(d_id);
                                        docref
                                                .update("flag", false)
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
                                        // To be changed
                                        Dialog dialog = new Dialog(BookRikshaw.this);
                                        String text = "";
                                        text += "Your E-Riskhaw has been booked";
                                        text += "\n\n";
                                        text += "Driver Details\n";
                                        text += "Driver ID : " + d_id;
                                        text += "Driver Phone : ";
                                        text += "\n";
                                        text += "Your E-Rikshaw will reach you shortly";

                                        dialog.setContentView(R.layout.customdialog);
                                        dialog.setCancelable(false);
                                        Button btn = dialog.findViewById(R.id.okbtn);
                                        btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(BookRikshaw.this, OnBooked.class);
                                                startActivity(intent);
                                            }
                                        });
                                        dialog.show();
                                    }
                                    //System.exit(0);
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error Getting Data", e);
                    }
                });
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sourcespin:
                source = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), source, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Source " + source);
                break;
            case R.id.destinationspin:
                dest = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), dest, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "dest " + dest);
                break;
            default:
                Toast.makeText(parent.getContext(), "Select source and destinations", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "No option selected");
                break;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(parent.getContext(), "Select Source", Toast.LENGTH_SHORT).show();
    }

    public boolean sendNotification(String token, String d_id) {
        FcmNotificationsSender notificationsSender = null;
        FirebaseMessaging.getInstance();
        String title = "Riskhaw Request";
        String message = d_id + "PickupLocation : " + source + "\n\n" + "Destination :" + dest;
        if (!title.isEmpty() && !message.isEmpty() && !token.isEmpty()) {
            notificationsSender = new FcmNotificationsSender(token, title, message, getApplicationContext());
            notificationsSender.SendNotifications();
        } else {
            Toast.makeText(this, "Messege Cannot Be Empty", Toast.LENGTH_SHORT).show();
        }
        if (notificationsSender != null) return true;
        else return false;
    }
}