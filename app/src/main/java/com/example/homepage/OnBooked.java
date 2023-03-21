package com.example.homepage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import org.json.JSONObject;
public class OnBooked extends AppCompatActivity implements PaymentResultListener{
    Button btn1, btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_booked);
        btn1 = findViewById(R.id.pay);
        btn2 = findViewById(R.id.track);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                openPay();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                trackit();
            }
        });
    }

    private void trackit() {

    }

    private void openPay() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_SgxyEVBvUW3r6k");
        checkout.setImage(R.drawable.tirri);
        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", "Abhinav Agrawal");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", "30000");//pass amount in currency subunits
            options.put("prefill.email", "abhi.agrwl11@gmail.com");
            options.put("prefill.contact","7417162630");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s)
    {
        Toast.makeText(this, "Payment Completed with payment id " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed " + s, Toast.LENGTH_SHORT).show();
    }
}