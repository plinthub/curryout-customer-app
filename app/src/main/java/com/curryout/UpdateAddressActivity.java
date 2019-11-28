package com.curryout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.curryout.appsharedpreference.AppGlobalConstants;
import com.curryout.appsharedpreference.AppSharedPreference;
import com.curryout.appsharedpreference.CommonUtilities;
import com.curryout.model.UserAddressModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;

public class UpdateAddressActivity extends AppCompatActivity {

    EditText edplotNumber,edStreet,edLandmark,edCity,edState,edPostal,edCountry;
    String plot,street,landmark,city,state,postal,country;
    LinearLayout ln_updateAddress,linearProSave;
    EditText et_address1,et_address2,et_address3;
    ImageView img_check1,img_check2,img_check3,back_icon;
    TextView txtAddNew_Address;
    String address_id="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_address_layout);
        init();
        address_id = getIntent().getStringExtra("AddressID");
        Log.e("UpdateActivity",address_id);
        //getAddress();
        listener();
    }

    public void init(){
        /*edplotNumber = findViewById(R.id.edplotNumber);
        edStreet = findViewById(R.id.edStreet);
        edLandmark = findViewById(R.id.edLandmark);
        edCity = findViewById(R.id.edCity);
        edState = findViewById(R.id.edState);
        edPostal = findViewById(R.id.edPostal);
        edCountry = findViewById(R.id.edCountry);
        ln_updateAddress = findViewById(R.id.ln_updateAddress);*/

        et_address1 = findViewById(R.id.et_address1);
        et_address2 = findViewById(R.id.et_address2);
        et_address3 = findViewById(R.id.et_address3);

        img_check1 = findViewById(R.id.img_check1);
        img_check2 = findViewById(R.id.img_check2);
        img_check3 = findViewById(R.id.img_check3);
        back_icon = findViewById(R.id.back_icon);
        txtAddNew_Address = findViewById(R.id.txtAddNew_Address);
        //linearProSave = findViewById(R.id.linearProSave);
        getCustomerAllAddress();
    }

    public void getAddress(){
        UserAddressModel uam = (UserAddressModel) getIntent().getSerializableExtra("UpdateModel");
        /*edplotNumber.setText(uam.getHouseno());
        edStreet.setText(uam.getStreet());
        edLandmark.setText(uam.getLandmark());
        edCity.setText(uam.getCity());
        edState.setText(uam.getState());
        edPostal.setText(uam.getPostalCode());
        edCountry.setText(uam.getCountry());*/
        et_address1.setText(uam.getHouseno()+" "+uam.getStreet()+" "+
                uam.getLandmark()+" "+uam.getCity()+" "+uam.getState()
                +" "+uam.getPostalCode()+" "+uam.getCountry()
                );


    }
    public void listener(){
        /*ln_updateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plot = edplotNumber.getText().toString();
                street = edStreet.getText().toString();
                landmark = edLandmark.getText().toString();
                city = edCity.getText().toString();
                state = edState.getText().toString();
                postal = edPostal.getText().toString();
                country = edCountry.getText().toString();*/

                //callAddAddressapi(plot,street,landmark,city,state,postal,country);

            //}
     //   });

        txtAddNew_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(UpdateAddressActivity.this,AddressActivity.class);
                startActivity(in);
            }
        });
//        img_check1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                img_check1.setImageResource(R.drawable.check_icon);
//            }
//        });
//        img_check2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateCustomerCurrentAddress(address_id);
//            }
//        });
//        img_check3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateCustomerCurrentAddress(address_id);
//            }
//        });

//        linearProSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();;
            }
        });

    }

    public void getCustomerAllAddress(){
        if (!CommonUtilities.isOnline(UpdateAddressActivity.this)) {
            Toast.makeText(UpdateAddressActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

//        final ProgressDialog pd = new ProgressDialog(UpdateAddressActivity.this);
//        pd.setTitle("Please Wait");
//        pd.setCancelable(false);
//        pd.show();

        final CustomProgressDialogue pd= new CustomProgressDialogue(UpdateAddressActivity.this);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "auth/getCustomerAllAddress";
        String testurl = "";

        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                parseResponseAllAddress(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                NetworkResponse response = error.networkResponse;

                Log.e("com.curryout", "error response " + response);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Log.e("mls", "VolleyError TimeoutError error or NoConnectionError");
                } else if (error instanceof AuthFailureError) {                    //TODO
                    Log.e("mls", "VolleyError AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.e("mls", "VolleyError ServerError");
                } else if (error instanceof NetworkError) {
                    Log.e("mls", "VolleyError NetworkError");
                } else if (error instanceof ParseError) {
                    Log.e("mls", "VolleyError TParseError");
                }
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        Log.e("com.curryout", "error response " + res);

                        parseResponseAllAddress(response.toString());

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(UpdateAddressActivity.this));
                return header;
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

        };

        sr.setRetryPolicy(new DefaultRetryPolicy(
                AppGlobalConstants.WEBSERVICE_TIMEOUT_VALUE_IN_MILLIS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        sr.setShouldCache(false);
        VolleySingleton.getInstance(UpdateAddressActivity.this).addToRequestQueue(sr);
    }

    public void parseResponseAllAddress(String response){
        Log.e("CO AllAddress Response", "response " + response);
        try{
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            if(data==true) {
                JSONArray jarr = job.getJSONArray("data");
                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject obj = jarr.getJSONObject(i);
                    String user_addressID = obj.getString("user_addressID");
                    String user_id = obj.getString("user_id");
                    String houseno = obj.getString("houseno");
                    String street = obj.getString("street");
                    String landmark = obj.getString("landmark");
                    String city = obj.getString("city");
                    String state = obj.getString("state");
                    String postalCode = obj.getString("postalCode");
                    String country = obj.getString("country");
                    String current_address = obj.getString("current_address");
                    String city_name = obj.getString("city_name");
                    String state_name = obj.getString("state_name");
                    String country_name = obj.getString("country_name");
                    UserAddressModel addm = new UserAddressModel(houseno,street,landmark,city_name,state_name,postalCode,country_name);
                    Log.e("Length",jarr.length()+"");
                    address_id = user_addressID;
                    //Log.e("AddId",address_id);
//                    if(i==jarr.length()-3){
                    if(i==0){
                        Log.e("AddId1",address_id);
                        final String add1 = address_id;
                        et_address1.setText(addm.toString());
                        if(current_address.equals("1")) {
                            img_check1.setImageResource(R.drawable.check_icon);

                        }
                        img_check1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                img_check1.setImageResource(R.drawable.check_icon);
                                updateCustomerCurrentAddress(add1);
                                img_check2.setImageResource(R.drawable.uncheck_icon);
                                img_check3.setImageResource(R.drawable.uncheck_icon);
                            }
                        });

                    }
//                    if(i==jarr.length()-2){
                    if(i==1){
                        Log.e("AddId2",address_id);
                        final String add2 = address_id;
                        et_address2.setText(addm.toString());
                        if(current_address.equals("1")) {
                            img_check2.setImageResource(R.drawable.check_icon);

                        }
                        img_check2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                img_check2.setImageResource(R.drawable.check_icon);
                                updateCustomerCurrentAddress(add2);
                                img_check1.setImageResource(R.drawable.uncheck_icon);
                                img_check3.setImageResource(R.drawable.uncheck_icon);
                            }
                        });

                    }
//                    if(i==jarr.length()-1){
                    if(i==2){
                        Log.e("AddId3",address_id);
                        final String add3 = address_id;
                        et_address3.setText(addm.toString());
                        if(current_address.equals("1")) {
                            img_check3.setImageResource(R.drawable.check_icon);

                        }
                        img_check3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                img_check3.setImageResource(R.drawable.check_icon);
                                updateCustomerCurrentAddress(add3);
                                img_check1.setImageResource(R.drawable.uncheck_icon);
                                img_check2.setImageResource(R.drawable.uncheck_icon);
                            }
                        });

                    }

//                    if(current_address.equals("1")){
//                        img_check1.setImageResource(R.drawable.check_icon);
//                        img_check2.setImageResource(R.drawable.check_icon);
//                        img_check3.setImageResource(R.drawable.check_icon);
//                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
}
    public void updateCustomerCurrentAddress(final String addressId){
        if (!CommonUtilities.isOnline(UpdateAddressActivity.this)) {
            Toast.makeText(UpdateAddressActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

//        final ProgressDialog pd = new ProgressDialog(UpdateAddressActivity.this);
//        pd.setTitle("Please Wait");
//        pd.setCancelable(false);
//        pd.show();

        final CustomProgressDialogue pd= new CustomProgressDialogue(UpdateAddressActivity.this);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "auth/updateCustomerCurrentAddress";
        String testurl = "";

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                parseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                NetworkResponse response = error.networkResponse;

                Log.e("com.curryout", "error response " + response);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Log.e("mls", "VolleyError TimeoutError error or NoConnectionError");
                } else if (error instanceof AuthFailureError) {                    //TODO
                    Log.e("mls", "VolleyError AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.e("mls", "VolleyError ServerError");
                } else if (error instanceof NetworkError) {
                    Log.e("mls", "VolleyError NetworkError");
                } else if (error instanceof ParseError) {
                    Log.e("mls", "VolleyError TParseError");
                }
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        Log.e("com.curryout", "error response " + res);

                        parseResponse(response.toString());

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                Log.e("AddressId",addressId);
                map.put("addressId",addressId);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(UpdateAddressActivity.this));
                return header;
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

        };

        sr.setRetryPolicy(new DefaultRetryPolicy(
                AppGlobalConstants.WEBSERVICE_TIMEOUT_VALUE_IN_MILLIS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        sr.setShouldCache(false);
        VolleySingleton.getInstance(UpdateAddressActivity.this).addToRequestQueue(sr);
    }

    public void parseResponse(String response) {
        Log.e("CO Product Response", "response " + response);
        try{
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            String message = job.getString("message");
            if (data == true) {
                if (message.equals("User Current Address Updated Successfully")) {
                    Toast.makeText(UpdateAddressActivity.this, "User Current Address Updated Successfully.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            else{
                Toast.makeText(UpdateAddressActivity.this, "User Current Address Not Updated.", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
