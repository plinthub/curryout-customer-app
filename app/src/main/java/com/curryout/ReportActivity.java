package com.curryout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReportActivity extends AppCompatActivity {

    TextView txt_orderId;
    ImageView back_icon;
    LinearLayout ln_submit;
    String getOID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getOID = getIntent().getStringExtra("OrderIDView");
        init();
        listener();
    }

    private void init() {

        txt_orderId=(TextView)findViewById(R.id.txt_orderId);
        back_icon=(ImageView)findViewById(R.id.back_icon);
        ln_submit=(LinearLayout)findViewById(R.id.ln_submit);

       txt_orderId.setText(getOID);
    }

    private void listener() {

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        ln_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
