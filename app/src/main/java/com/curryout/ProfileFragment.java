package com.curryout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.curryout.model.RestaurantDataModel;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    EditText et_phNumber, et_address1, et_address2,edProName,edProEmail,edProAddress;
    ImageView imgNumberClean,imgAddress1Clean, imgAddress2Clean, logout;
    LinearLayout linearProSave;
    String name="",mobile="",email="";
    TextView txtProAddress;
    String userAddressId="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        init(view);
        listener(view);
        getUserData();
        return view;

    }

    private void init(View view) {
        edProName = (EditText)view.findViewById(R.id.edProName);
        edProEmail = (EditText) view.findViewById(R.id.edProEmail);
        //et_address1=(EditText)view.findViewById(R.id.et_address1);
        //et_address2=(EditText)view.findViewById(R.id.et_address2);
        et_phNumber=(EditText)view.findViewById(R.id.et_phNumber);
        imgNumberClean=(ImageView)view.findViewById(R.id.imgNumberClean);
        logout=(ImageView)view.findViewById(R.id.logout);
        //imgAddress1Clean=(ImageView)view.findViewById(R.id.imgAddress1Clean);
        //imgAddress2Clean=(ImageView)view.findViewById(R.id.imgAddress2Clean);
        linearProSave = (LinearLayout) view.findViewById(R.id.linearProSave);
        et_phNumber.setEnabled(false);
        edProAddress = view.findViewById(R.id.edProAddress);
        edProAddress.setEnabled(false);
        txtProAddress = view.findViewById(R.id.txtProAddress);
    }

    private void listener(View view) {

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity(), R.style.FullScreenDialogStyleTransparent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.logout_dialog_layout);
                dialog.show();

                ImageView cancel=(ImageView)dialog.findViewById(R.id.img_cancel);
                LinearLayout ln_yes=(LinearLayout)dialog.findViewById(R.id.ln_yes);
                LinearLayout ln_outer = (LinearLayout)dialog.findViewById(R.id.ln_outside);

                ln_outer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                ln_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callLogoutApi();
                    }
                });



            }
        });


        et_phNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(charSequence.length() > 0){
                    imgNumberClean.setVisibility(View.VISIBLE);
                }else{
                    imgNumberClean.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgNumberClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_phNumber.setText("");
            }
        });


       /* et_address1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(charSequence.length() > 0){
                    imgAddress1Clean.setVisibility(View.VISIBLE);
                }else{
                    imgAddress1Clean.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgAddress1Clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et_address1.setText("");
            }
        });

        et_address2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(charSequence.length() > 0){
                    imgAddress2Clean.setVisibility(View.VISIBLE);
                }else{
                    imgAddress2Clean.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgAddress2Clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et_address2.setText("");
            }
        });
    */

       txtProAddress.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                Intent in = new Intent(getActivity(),UpdateAddressActivity.class);
                in.putExtra("AddressID",userAddressId);
                startActivity(in);
           }
       });
        linearProSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String em = edProEmail.getText().toString();
                String revised_name = edProName.getText().toString();
                Log.e("Email",em);
                Log.e("Name",revised_name);
                callupdateProfile(em,revised_name);
            }
        });
    }
    public void getUserData(){
        if (!CommonUtilities.isOnline(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        final CustomProgressDialogue pd= new CustomProgressDialogue(getActivity());
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();


        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "auth/getUserData";
        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(getActivity()));
                return header;
            }
            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };

        sr.setRetryPolicy(new DefaultRetryPolicy(
                AppGlobalConstants.WEBSERVICE_TIMEOUT_VALUE_IN_MILLIS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        sr.setShouldCache(false);
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(sr);
    }

    public void parseResponse(String response) {
        Log.e("CurOut Profile Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean)job.get("status");
            if(data==true) {
                JSONObject jobj = job.getJSONObject("data");

//                for (int i = 0; i < jarr.length(); i++) {
//                    JSONObject jobj = jarr.
//                    Log.e("PF",jobj.toString());

                    String userId = jobj.getString("userID");
                    mobile = jobj.getString("mobile");
                    name = jobj.getString("name");
                    email = jobj.getString("email");
                    Log.e("PName",name);
                    String user_addressID="",houseno="",street="",landmark="",city="",state="",postalCode="",country="",current_address="";
                    JSONArray address_list = jobj.getJSONArray("address_list");
                    for (int i = 0; i < address_list.length(); i++) {
                        JSONObject addObj = address_list.getJSONObject(i);
                        user_addressID = addObj.getString("user_addressID");
                        String user_id = addObj.getString("user_id");
                        houseno = addObj.getString("houseno");
                        street = addObj.getString("street");
                        landmark = addObj.getString("landmark");
                        city = addObj.getString("city");
                        state = addObj.getString("state");
                        postalCode = addObj.getString("postalCode");
                        country = addObj.getString("country");
                        current_address = addObj.getString("current_address");
                        String city_name = addObj.getString("city_name");
                        String state_name = addObj.getString("state_name");
                        String country_name = addObj.getString("country_name");

                        if (current_address.equals("1")) {
                            userAddressId = user_addressID;
                            Log.e("userAddressId", userAddressId);
                            edProAddress.setText(houseno + " " + street + " " + landmark + " " + city_name + " " + state_name + " " + postalCode + " " + country_name);
                            Log.e("TestAddress", houseno + " " + street + " " + landmark + " " + city + " " + state + " " + postalCode + " " + country);
                        }
                    }
                String subStrMob = mobile.substring(3);
                Log.e("SubString",subStrMob);
                String message = job.getString("message");
                if(message.equals("User Profile Data.")){
                        edProName.setText(name);
                        et_phNumber.setText(subStrMob);

                        if(edProEmail==null){
                            edProEmail.setHint("Email");
                        }
                        else{
                            edProEmail.setText(email);
                        }
                }
                else{
                    Toast.makeText(getActivity(), "Data Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public void callupdateProfile(final String email,final String rev_name){

        if (!CommonUtilities.isOnline(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        final CustomProgressDialogue pd= new CustomProgressDialogue(getActivity());
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();
        Log.e("Email in Call", email);
        Log.e("Name in Call", rev_name);
        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "auth/updateProfile";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pd.dismiss();
                parseResponseUpdate(response);
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

                        parseResponseUpdate(response.toString());

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("name",rev_name);
                map.put("email",email);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(getActivity()));
                return header;
            }
            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };

        sr.setRetryPolicy(new DefaultRetryPolicy(
                AppGlobalConstants.WEBSERVICE_TIMEOUT_VALUE_IN_MILLIS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        sr.setShouldCache(false);
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(sr);
    }

    public void parseResponseUpdate(String response) {
        Log.e("CurOut Profile Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean)job.get("status");
            String message="";
            if(data==true) {
                 message = (String)job.get("message");

                if(message.equals("User Profile Updated Successfully.")){
                    Toast.makeText(getActivity(), "User Profile Updated Successfully.", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(getActivity(), "User Profile Not Updated Successfully.", Toast.LENGTH_SHORT).show();
                }
            }
            if(data==false){
                message = (String)job.get("message");
                Log.e("False",message);
                 if(message.equalsIgnoreCase("The Email field must contain a valid email address.<br>\n")){
                    Toast.makeText(getActivity(), "The Email field must contain a valid email address", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void callLogoutApi(){
        if (!CommonUtilities.isOnline(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }


        final CustomProgressDialogue pd= new CustomProgressDialogue(getActivity());
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();


        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "auth/logout";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pd.dismiss();
                parseResponseLogout(response);
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

                        parseResponseLogout(response.toString());

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }

            }
        })

        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> map = new HashMap<>();
//                map.put("name",name);
//                map.put("email",email);
//                return map;
//            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(getActivity()));
                return header;
            }
            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };

        sr.setRetryPolicy(new DefaultRetryPolicy(
                AppGlobalConstants.WEBSERVICE_TIMEOUT_VALUE_IN_MILLIS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        sr.setShouldCache(false);
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(sr);
    }
    public void parseResponseLogout(String response) {
        Log.e("CurOut Logout Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean)job.get("status");
            String message="";
            if(data==true) {
                message = (String)job.get("message");

                if(message.equals("user logged out successfully.")){
                    Toast.makeText(getActivity(), "User Logged Out Successfully.", Toast.LENGTH_SHORT).show();
                    AppSharedPreference.saveTokenPreferences(getActivity(),"NA");
                    AppSharedPreference.saveUserPhoneToPreferences(getActivity(),"NA");
                    Intent in = new Intent(getActivity(),WelcomePhoneActivity.class);
                    startActivity(in);
                    getActivity().finishAffinity();
                }

                else{
                    Toast.makeText(getActivity(), "User Not Logged Out Successfully.", Toast.LENGTH_SHORT).show();
                }
            }
    }catch (Exception e){
        e.printStackTrace();
        }
    }

    boolean allowRefresh;
    @Override
    public void onResume() {
        super.onResume();
        getUserData();
        if (allowRefresh)
        {
            allowRefresh = false;
            getUserData();
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();

        }
    }


}
