package com.curryout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReportFragment extends Fragment {

    TextView txt_orderId;
    ImageView back_icon;
    LinearLayout ln_submit;


    String oid;

    public ReportFragment() {

    }
    public ReportFragment(String oid) {
        //Bundle bun = this.getArguments();
        //String resturant_id = bun.getString("RID");
        this.oid = oid;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.report_fragment_layout, container, false);

        init(view);
        listener(view);



        return view;
    }

    private void init(View view) {

        txt_orderId=(TextView)view.findViewById(R.id.txt_orderId);
        back_icon=(ImageView)view.findViewById(R.id.back_icon);
        ln_submit=(LinearLayout)view.findViewById(R.id.ln_submit);

        txt_orderId.setText(oid);

    }


    private void listener(final View view) {

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity)getContext();
                activity.getSupportFragmentManager().popBackStack();
            }
        });

        ln_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)getContext();
                activity.getSupportFragmentManager().popBackStack();
            }
        });

    }


}
