package com.example.homepage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DialogView extends AppCompatActivity {
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String d_id = getIntent().getStringExtra("d_id");
        dialog = new Dialog(this);
        String phone = "";
        setContentView(R.layout.activity_dialog_view);
                                                String text = "";
                                        text += "Your E-Riskhaw has been booked";
                                        text += "\n\n";
                                        text += "Driver Details\n";
                                        text += "Driver ID : " + d_id;
                                        text += "Driver Phone : " + phone;
                                        text += "\n";
                                        text += "Your E-Rikshaw will reach you shortly";
                                        dialog.setContentView(R.layout.customdialog);
                                        dialog.setCancelable(false);
                                        Button btn = dialog.findViewById(R.id.okbtn);
                                        btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Toast.makeText(DialogView.this, "", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        });
                                        dialog.show();
    }
}