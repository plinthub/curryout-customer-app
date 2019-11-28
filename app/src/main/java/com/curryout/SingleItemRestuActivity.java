package com.curryout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.androidquery.AQuery;
import com.curryout.appsharedpreference.AppGlobalConstants;
import com.curryout.appsharedpreference.AppSharedPreference;
import com.curryout.appsharedpreference.CommonUtilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SingleItemRestuActivity extends AppCompatActivity {

//    public SingleItemRestuActivity(String rid) {
//        this.rid = rid;
//    }

    ImageView cancel_icon, share_icon, fav_icon, fav_filled_icon,img_Single;
    ArrayList<SearchGridDataModel> alist;
    RecyclerView recyclerView;
    SearchGridListAdapter adapter;
    String rid;
    TextView txtResName_Single,txtResSinglecuisine_name,txtViewDetailSingleRes,txtViewItSingle;
    LinearLayout linearViewCartSingleRes;

//    public SingleItemRestuActivity(String rid) {
//        //Bundle bun = this.getArguments();
//        //String resturant_id = bun.getString("RID");
//        this.rid = rid;
//        Log.e("Rest_Id",rid);
//        Log.e("SingleItemRestuFragment","SingleItemRestuFragment");
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_item_restu);
        rid = getIntent().getStringExtra("RID");

        alist = new ArrayList<>();
        callgetRestrauntAllProduct();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        //adapter = new SearchGridListAdapter(alist,SingleItemRestuActivity.this);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(SingleItemRestuActivity.this,2);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        //recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();



        init();
        listener();
    }



    private void listener() {


        cancel_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppCompatActivity activity = (AppCompatActivity)getContext();
////                Fragment myFragment = new SingleItemRestuFragment();
////                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
//                activity.getSupportFragmentManager().popBackStack();
                finish();
            }
        });


        fav_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fav_icon.setVisibility(View.GONE);
                fav_filled_icon.setVisibility(View.VISIBLE);
            }
        });

        fav_filled_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fav_icon.setVisibility(View.VISIBLE);
                fav_filled_icon.setVisibility(View.GONE);
            }
        });

    }

    private void init() {

        cancel_icon=(ImageView)findViewById(R.id.cancel_icon);
        share_icon=(ImageView)findViewById(R.id.share_icon);
        fav_icon=(ImageView)findViewById(R.id.fav_icon);
        fav_filled_icon=(ImageView)findViewById(R.id.fav_filled_icon);
        img_Single = (ImageView)findViewById(R.id.img_Single);
        txtResName_Single = (TextView)findViewById(R.id.txtResName_Single);
        txtResSinglecuisine_name = (TextView) findViewById(R.id.txtResSinglecuisine_name);

        linearViewCartSingleRes = findViewById(R.id.linearViewCartSingleRes);
//        txtViewDetailSingleRes = findViewById(R.id.txtViewDetailSingleRes);
        linearViewCartSingleRes.setVisibility(View.GONE);
        txtViewItSingle = findViewById(R.id.txtViewItSingle);
        callgetCartItemList();

    }



    public void callgetRestrauntAllProduct(){
        if (!CommonUtilities.isOnline(SingleItemRestuActivity.this)) {
            Toast.makeText(SingleItemRestuActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

//        final ProgressDialog pd = new ProgressDialog(SingleItemRestuActivity.this);
//        pd.setTitle("Please Wait");
//        pd.setCancelable(false);
//        pd.show();

        final CustomProgressDialogue pd= new CustomProgressDialogue(SingleItemRestuActivity.this);
        pd.setCancelable(false);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "product/getRestrauntAllProduct";
        //String testurl = "http://www.mocky.io/v2/5cc691be3200006700b94c9e";
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
                Map<String, String> map = new HashMap<>();
                map.put("restrauntId",rid);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(SingleItemRestuActivity.this));
                Log.e("X-Auth-Token", AppSharedPreference.loadTokenPreference(SingleItemRestuActivity.this));
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
        VolleySingleton.getInstance(SingleItemRestuActivity.this).addToRequestQueue(sr);
    }

    public void parseResponse(String response) {
        Log.e("CO SingleItem Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            JSONObject jarr = job.getJSONObject("data");
            int length = job.length();
            if (data == true) {
                JSONObject jobj = jarr.getJSONObject("restraunt");
                //Getting resturant Data
                String restrauntID = jobj.getString("restrauntID");
                String user_id = jobj.getString("user_id");
                String name = jobj.getString("name");
                String city = jobj.getString("city");
                String owner_name = jobj.getString("owner_name");
                String phone = jobj.getString("phone");
                String email = jobj.getString("email");
                String address = jobj.getString("address");
                String image = jobj.getString("image");
                String food_type = jobj.getString("food_type");
                String provide_delivery = jobj.getString("provide_delivery");
                String status = jobj.getString("status");
                //String cuisine_name = jobj.getString("cuisine_name");
                JSONArray cuisine_name = jobj.optJSONArray("cuisine_name");
                String cn="";
                for (int jj = 0; jj < cuisine_name.length(); jj++) {
                    cn += cuisine_name.getString(jj)+" | ";
                    Log.d("AD", "url => " + cn);
                }
                //Bitmap bmp = BitmapFactory.decodeFile(image);
                //img_Single.setImageBitmap(bmp);
                new AQuery(SingleItemRestuActivity.this).id(img_Single).image(image);


                txtResName_Single.setText(name);
                txtResSinglecuisine_name.setText(cn);
                Log.e("RD", restrauntID);
                Log.e("RD1", name);
                Log.e("RD2", food_type);

                JSONArray jarr1 = jarr.getJSONArray("products");
                for (int j = 0; j < jarr1.length(); j++) {
                    JSONObject jobj1 = jarr1.getJSONObject(j);
                    String productID = jobj1.getString("productID");
                    String restaurant_id = jobj1.getString("restaurant_id");
                    String name1 = jobj1.getString("name");
                    String category = jobj1.getString("category");
                    String sub_Category = jobj1.getString("sub_Category");
                    String food_type1 = jobj1.getString("food_type");
                    String price = jobj1.getString("price");
                    String description = jobj1.getString("description");
                    String quantity = jobj1.getString("quantity");
                    String ingrediants = jobj1.getString("ingrediants");
                    String image1 = jobj1.getString("image");
                    //String available_days = jobj1.getString("available_days");

                    JSONArray jFaceShape = jobj1.optJSONArray("available_days");
                    for (int jj = 0; jj < jFaceShape.length(); jj++) {
                        String url = (String) jFaceShape.get(jj);
                        Log.d("AD", "url => " + url);
                    }

                    String available_start_time = jobj1.getString("available_start_time");
                    String available_end_time = jobj1.getString("available_end_time");
                    String createdAt = jobj1.getString("createdAt");
                    String status1 = jobj1.getString("status");
                    //String addon_item = jobj1.getString("addon_item");
                    String category_name = jobj1.getString("category_name");
                    String sub_category_name = jobj1.getString("sub_category_name");

                    //Uri myUri = Uri.parse(image1);
                    SearchGridDataModel sd = new SearchGridDataModel(productID, name1, price, image1);
                    alist.add(sd);
                }
            }
            adapter = new SearchGridListAdapter(alist,SingleItemRestuActivity.this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void callgetCartItemList(){
        if (!CommonUtilities.isOnline(SingleItemRestuActivity.this)) {
            Toast.makeText(SingleItemRestuActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }


        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "cart/getCartItemList";

        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponseItemList(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

                        parseResponseItemList(response.toString());

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
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(SingleItemRestuActivity.this));
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
        VolleySingleton.getInstance(SingleItemRestuActivity.this).addToRequestQueue(sr);
    }

    public void parseResponseItemList(String response) {
        Log.e("CO Product Response", "response " + response);

        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            JSONObject jdata = job.getJSONObject("data");
            JSONArray arr = jdata.optJSONArray("current_address");

            JSONArray cartarr = jdata.getJSONArray("cart");

            if(cartarr.length()==0){
                linearViewCartSingleRes.setVisibility(View.GONE);
            }
            else{

                linearViewCartSingleRes.setVisibility(View.VISIBLE);
                txtViewItSingle.setText(cartarr.length()+"");

                linearViewCartSingleRes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(SingleItemRestuActivity.this,CartActivity.class);
                        startActivity(in);
                    }
                });
            }

            String addonitemprice="",addonitem="";
            for (int i = 0; i < cartarr.length(); i++) {
                JSONObject obj = cartarr.getJSONObject(i);
                String UpCartID = obj.getString("cartID");
                String user_id = obj.getString("user_id");
                String ItemListcartquantity = obj.getString("cart_quantity");
                Log.e("cart quantity", obj.getString("cart_quantity"));
                String cart_price = obj.getString("cart_price");
                String cart_added_at = obj.getString("cart_added_at");
                String ItemListproductID = obj.getString("productID");
                String restaurant_id = obj.getString("restaurant_id");
                String name = obj.getString("name");
                String category = obj.getString("category");
                String sub_Category = obj.getString("sub_Category");
                String food_type = obj.getString("food_type");
                String upPrice1 = obj.getString("price");
                String description = obj.getString("description");
                String quantity = obj.getString("quantity");
                String ingrediants = obj.getString("ingrediants");
                String image = obj.getString("image");
                JSONArray jFaceShape = obj.optJSONArray("available_days");
                String url="";
                for (int jj = 0; jj < jFaceShape.length(); jj++) {
                    url += (String) jFaceShape.get(jj);
                    Log.d("AD", "url => " + url);
                }
                String available_start_time = obj.getString("available_start_time");
                String available_end_time = obj.getString("available_end_time");
                String createdAt = obj.getString("createdAt");
                String status1 = obj.getString("status");
//                JSONArray addon_item = obj.optJSONArray("addon_item");
//                for (int jj = 0; jj < addon_item.length(); jj++) {
//                    addonitem += (String) addon_item.get(jj);
//                }
//                Log.e("AddItem", addonitem);
//                JSONArray addon_item_price = obj.optJSONArray("addon_item_price");
//                for (int jj = 0; jj < addon_item_price.length(); jj++) {
//                    addonitemprice += (String) addon_item_price.get(jj);
//
//                }
//                Log.e("AddItemPrice", addonitemprice);
                    SearchGridDataModel sdm = new SearchGridDataModel(name,ItemListproductID,quantity);
                    SearchGridListAdapter dd = new SearchGridListAdapter(sdm);
            }

            if (data == false) {
                String message = (String) job.get("message");
                Log.e("False", message);
                if (message.equalsIgnoreCase("The Product Id field is required.<br>\n")) {
                    Toast.makeText(SingleItemRestuActivity.this, "Product Id Cannot Be Blank", Toast.LENGTH_SHORT).show();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        callgetCartItemList();
    }


}
