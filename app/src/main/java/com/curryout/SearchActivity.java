package com.curryout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.androidquery.AQuery;
import com.curryout.adapter.SearchFootItemName;
import com.curryout.adapter.SearchRestaurantNameAdapter;
import com.curryout.appsharedpreference.AppGlobalConstants;
import com.curryout.appsharedpreference.AppSharedPreference;
import com.curryout.appsharedpreference.CommonUtilities;
import com.curryout.model.SearchFoodItemModel;
import com.curryout.model.SearchRestaurantNameModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends Activity {

    RecyclerView recyclerViewHori;
    ArrayList<SearchFoodItemModel> alistHori;
    SearchFootItemName adapterHoti;
    EditText et_search;
    ImageView search_icon;

    RecyclerView recyclerViewVerti;
    ArrayList<SearchRestaurantNameModel> alistVerti;
    SearchRestaurantNameAdapter adapterVerti;
    String search="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_fullpage_layout);
        search = getIntent().getStringExtra("SearchData");

        if(search!=null){
            callSearchRestrauntAndProduct(search);
        }

        init();
        listener();
    }

    public void init(){
        recyclerViewHori = findViewById(R.id.recyclerViewHori);
        recyclerViewHori.setHasFixedSize(true);
//        recyclerViewHori.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        recyclerViewHori.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        alistHori = new ArrayList<>();
        adapterHoti = new SearchFootItemName(SearchActivity.this,alistHori);
        adapterHoti.notifyDataSetChanged();
        et_search = findViewById(R.id.et_search);
        search_icon = findViewById(R.id.search_icon);

        recyclerViewVerti = findViewById(R.id.recyclerViewVerti);
        recyclerViewVerti.setHasFixedSize(true);
        recyclerViewVerti.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        alistVerti = new ArrayList<>();
        adapterVerti = new SearchRestaurantNameAdapter(SearchActivity.this,alistVerti);
        adapterVerti.notifyDataSetChanged();
        et_search.setText(search);
    }

    public void listener(){


        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String key = et_search.getText().toString();
                    callSearchRestrauntAndProduct(key);
                    onResume();
                    return true;
                }
                return false;
            }
        });

        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String key = et_search.getText().toString();
                callSearchRestrauntAndProduct(key);
                onResume();

            }
        });
    }

    public void callSearchRestrauntAndProduct(final String k){

        if (!CommonUtilities.isOnline(SearchActivity.this)) {
            Toast.makeText(SearchActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

//        final ProgressDialog pd = new ProgressDialog(SearchActivity.this);
//        pd.setTitle("Please Wait");
//        pd.setCancelable(false);
//        pd.show();

        final CustomProgressDialogue pd= new CustomProgressDialogue(SearchActivity.this);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "restraunt/searchRestrauntAndProduct";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                parseResponseSearch(response);
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

                        parseResponseSearch(response.toString());

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("keyword",k);
                Log.e("Search",k);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(SearchActivity.this));
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
        VolleySingleton.getInstance(SearchActivity.this).addToRequestQueue(sr);
    }

    public void parseResponseSearch(String response) {
        Log.e("CO Search Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            JSONObject jarr1 = job.getJSONObject("data");
            if (data == true) {

                try {
                    JSONArray jpro = jarr1.getJSONArray("products");
                    if(jpro.length()>0){
                    for (int j = 0; j < jpro.length(); j++) {
                    JSONObject ob = jpro.getJSONObject(j);
                        //Getting product Data
                    String productID = ob.getString("productID");
                    String restaurant_id = ob.getString("restaurant_id");
                    String name1 = ob.getString("name");
                    String category = ob.getString("category");
                    String sub_Category = ob.getString("sub_Category");
                    String food_type1 = ob.getString("food_type");
                    String price = ob.getString("price");
                    String description = ob.getString("description");
                    String quantity = ob.getString("quantity");
                    String ingrediants = ob.getString("ingrediants");
                    String image1 = ob.getString("image");
                    //String available_days = ob.getString("available_days");

                    JSONArray jFaceShape = ob.optJSONArray("available_days");
                    for (int jj = 0; jj < jFaceShape.length(); jj++) {
                        String url = (String) jFaceShape.get(jj);
                        Log.d("AD", "url => " + url);
                    }
                    String available_start_time = ob.getString("available_start_time");
                    String available_end_time = ob.getString("available_end_time");
                    String createdAt = ob.getString("createdAt");
                    String status1 = ob.getString("status");
                    //String addon_item = ob.getString("addon_item");
                    String category_name = ob.getString("category_name");
                    String sub_category_name = ob.getString("sub_category_name");
                    //String entity = ob.getString("entity");

                    SearchFoodItemModel sd = new SearchFoodItemModel(productID,name1, "20Min", price, category_name, image1);
                    alistHori.add(sd);
                }

                        recyclerViewHori.setAdapter(adapterHoti);
                        recyclerViewHori.invalidate();;
                        adapterHoti.notifyDataSetChanged();
                    }else{
                        alistHori.clear();
                        recyclerViewHori.setAdapter(adapterHoti);
                        recyclerViewHori.invalidate();;
                        adapterHoti.notifyDataSetChanged();
                    }

            }catch (Exception e){e.printStackTrace();}
                    JSONArray jres = jarr1.getJSONArray("restraunts");
                    for (int j = 0; j < jres.length(); j++) {
                        JSONObject jobj1 = jres.getJSONObject(j);
                        SearchRestaurantNameModel sr = null;
                        if(jobj1.length()>0) {
                            String restrauntID = jobj1.getString("restrauntID");
                            String user_id = jobj1.getString("user_id");
                            String name = jobj1.getString("name");
                            String city = jobj1.getString("city");
                            String owner_name = jobj1.getString("owner_name");
                            String phone = jobj1.getString("phone");
                            String email = jobj1.getString("email");
                            String address = jobj1.getString("address");
                            String image = jobj1.getString("image");
                            String food_type = jobj1.getString("food_type");
                            String provide_delivery = jobj1.getString("provide_delivery");
                            String status = jobj1.getString("status");

                            //opt Json Array..
                            String cn = "";
                            JSONArray cuisine_name = jobj1.optJSONArray("cuisine_name");
                            for (int jj = 0; jj < cuisine_name.length(); jj++) {
                                cn += (String) cuisine_name.get(jj) + " | ";

                            }
                            Log.e("cuisine_name", cn);
                            //String entity = jobj1.getString("entity");
                            sr = new SearchRestaurantNameModel(restrauntID, image, name, cn, "30Min", address, "Offers");
                            Log.e("VertiTest", name);

                            
                            alistVerti.add(sr);
                            //adapterVerti.addApplications(alistVerti);
                            recyclerViewVerti.setAdapter(adapterVerti);
                            recyclerViewVerti.invalidate();
                            adapterVerti.notifyDataSetChanged();

                        }else{
                            //adapterVerti.clearApplications();
                            recyclerViewVerti.setAdapter(adapterVerti);
                            recyclerViewVerti.invalidate();
                            adapterVerti.notifyDataSetChanged();
                        }
                    }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //recyclerViewVerti.invalidate();
        //recyclerViewHori.invalidate();
        this.onCreate(null);


    }
}




