package com.curryout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.curryout.model.FoodDataModel;
import com.curryout.model.ShowCartFoodItemModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SingleFoodDetailsActivity extends AppCompatActivity {

    ImageView cancel_icon, img_min, img_plus,imgSingleItem;
    TextView txt_count, txtSingleName, txtSICuisine, txtdisprice, txtProductDesc, txtProductIngre, txtadd_tocart, txtRemove;
    LinearLayout ln_addTocart;
    int count = 0;
    String pid;
    String upName, upQty, upCartId, upPrice, upPid;
    ArrayList<FoodDataModel> alist;
    RecyclerView recyclerView;
    SingleFoodDetailsFragment.FoodListAdapter adapter;
    String name = "", url = "", price = "";
    String ItemListproductID = "", ItemListcartquantity = "";
    String upNname = "";
    String addonitem = "", category_name = "", description = "", ingrediants = "", addonitemprice = "";
    String UpCartID = "", upPrice1 = "", addprice;
    int conup = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_food_details);

        pid = getIntent().getStringExtra("PID");
        price = getIntent().getStringExtra("PRICE");

        addprice = price;

        init();
        listener();


        //alist.add(new FoodDataModel("Extra Cheese", "$3"));

        //recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        //adapter = new FoodListAdapter(alist);
        //recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        getProductById(pid);
        callgetCartItemList();

        Log.e("txtadd_tocartonPageLoad", txtadd_tocart.getText().toString());
    }

    private void init() {

        cancel_icon = (ImageView) findViewById(R.id.cancel_icon);
        img_min = (ImageView) findViewById(R.id.img_min);
        img_plus = (ImageView)findViewById(R.id.img_plus);
        txt_count = (TextView)findViewById(R.id.txt_count);
        ln_addTocart = (LinearLayout)findViewById(R.id.ln_addTocart);
        txtdisprice = (TextView)findViewById(R.id.txtdisprice);
        txtSingleName = (TextView)findViewById(R.id.txtSingleName);
        txtSICuisine = (TextView)findViewById(R.id.txtSIcuisine);
        txtProductDesc = findViewById(R.id.txtProductDesc);
        txtProductIngre =findViewById(R.id.txtProductIngre);
        txtadd_tocart = findViewById(R.id.txtadd_tocart);
        txtRemove = findViewById(R.id.txtRemove);
        imgSingleItem = (ImageView)findViewById(R.id.imgSingleItem);
        alist = new ArrayList<>();
        txtRemove.setVisibility(View.INVISIBLE);
        ItemListproductID="";
        try {
            //conup = Integer.parseInt(upCartId);
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }
        Log.e("ItemListproductID",ItemListproductID);
        if(ItemListproductID!=null){}
        //txtRemove.setVisibility(View.VISIBLE);}
        else
            txtRemove.setVisibility(View.INVISIBLE);

    }

    private void listener() {
        ln_addTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("txtadd_tocart", txtadd_tocart.getText().toString());
                Log.e("InsideIf", conup + "");
                if (count == 0 ) {
                    Toast.makeText(SingleFoodDetailsActivity.this, "Please Select Quantity", Toast.LENGTH_SHORT).show();
                }
                Log.e("IDTEST",ItemListproductID+":"+pid);
                if(ItemListproductID.equalsIgnoreCase(pid)){
                    upQty = String.valueOf(count);
                    Log.e("UpdateQtyonRefreshElse", upQty);
                    Log.e("Test", UpCartID + pid + upQty + upPrice1);
                    //if (upCartId != null && pid != null && upQty != null && upPrice1 != null) {
                    //if (TextUtils.isEmpty(upCartId) && TextUtils.isEmpty(pid) && TextUtils.isEmpty(upQty)  && TextUtils.isEmpty(upPrice1)) {
                    callUpdateCart(UpCartID, pid, upQty, upPrice1);
                    startActivityForResult(new Intent(SingleFoodDetailsActivity.this, CartActivity.class), 101);
                    //}

                }
                else{
                    Log.e("Count", count + "");
                    Log.e("InsideElse", conup + "");
//                            if(upCartId==null){
                    Log.e("AddtoCart", pid + ":" + count + ":" + addprice);
                    if (pid != null && addprice != null && count != 0) {
                        calladdtoCartApi(pid, count, addprice);
                        //conup++;
                        startActivityForResult(new Intent(SingleFoodDetailsActivity.this, CartActivity.class), 101);
                    }
                }
            }


        });




        img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                txt_count.setText(String.valueOf(count));
                upQty = count+"";
                Log.e("inImgPlus",upQty);


            }
        });

        img_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(count>0) {
                    count--;
                    txt_count.setText(String.valueOf(count));
                    upQty = count+"";
                    Log.e("inImgMinus",upQty);
                }

            }
        });

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

        txtRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callremoveItemFromCart();
            }
        });

    }

    public void callgetCartItemList(){
        if (!CommonUtilities.isOnline(SingleFoodDetailsActivity.this)) {
            Toast.makeText(SingleFoodDetailsActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
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
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(SingleFoodDetailsActivity.this));
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
        VolleySingleton.getInstance(SingleFoodDetailsActivity.this).addToRequestQueue(sr);
    }

    public void parseResponseItemList(String response) {
        Log.e("CO Product Response", "response " + response);

        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            JSONObject jdata = job.getJSONObject("data");
            JSONArray arr = jdata.optJSONArray("current_address");

            JSONArray cartarr = jdata.getJSONArray("cart");

            String addonitemprice="",addonitem="";
            for (int i = 0; i < cartarr.length(); i++) {
                JSONObject obj = cartarr.getJSONObject(i);
                UpCartID = obj.getString("cartID");
                String user_id = obj.getString("user_id");
                ItemListcartquantity = obj.getString("cart_quantity");
                Log.e("cart quantity",obj.getString("cart_quantity"));
                String cart_price = obj.getString("cart_price");
                String cart_added_at = obj.getString("cart_added_at");
                ItemListproductID = obj.getString("productID");
                String restaurant_id = obj.getString("restaurant_id");
                name = obj.getString("name");
                String category = obj.getString("category");
                String sub_Category = obj.getString("sub_Category");
                String food_type = obj.getString("food_type");
                upPrice1 = obj.getString("price");
                String description = obj.getString("description");
                String quantity = obj.getString("quantity");
                String ingrediants = obj.getString("ingrediants");
                String image = obj.getString("image");
                JSONArray jFaceShape = obj.optJSONArray("available_days");

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
//                }Log.e("AddItem",addonitem);
//                JSONArray addon_item_price = obj.optJSONArray("addon_item_price");
//                for (int jj = 0; jj < addon_item_price.length(); jj++) {
//                    addonitemprice += (String) addon_item_price.get(jj);
//
//                }Log.e("AddItemPrice",addonitemprice);


                String category_name = obj.getString("category_name");
                String sub_category_name = obj.getString("sub_category_name");
                Log.e("Cart_Id", UpCartID);
                Log.e("Revised Name", name);
                Log.e("Cart Qty", ItemListcartquantity);

                //Testing
//                    if(ItemListproductID==null) {
//                        if (count == 0) {
//                            Toast.makeText(getActivity(), "Please Select Quantity", Toast.LENGTH_SHORT).show();
//                        } else {
//                            ln_addTocart.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Log.e("Count", count + "");
//                                    Log.e("AddtoCart", pid + ":" + count + ":" + addprice);
//                                    if (pid != null && addprice != null && count != 0) {
//                                        calladdtoCartApi(pid, count, addprice);
//                                        startActivityForResult(new Intent(getActivity(), CartActivity.class), 101);
//                                    }
//                                }
//                            });
//
//                        }
//                    }else {
//
//                        upQty = String.valueOf(count);
//                        Log.e("UpdateQtyonRefreshElse", upQty);
//                        Log.e("Test", UpCartID + pid + upQty + upPrice1);
//                        if (upCartId != null && pid != null && upQty != null && upPrice != null) {
//                            callUpdateCart(UpCartID, pid, upQty, upPrice1);
//                        }
//                        startActivityForResult(new Intent(getActivity(), CartActivity.class), 101);
//                    }

                if(pid.equalsIgnoreCase(ItemListproductID)) {
                    txtSingleName.setText(name);
                    txtSICuisine.setText(category_name);
                    txtdisprice.setText(upPrice1);
                    txtProductDesc.setText(description);
                    txtProductIngre.setText(ingrediants);

                    txt_count.setText(ItemListcartquantity);
                    final String cart_quantity = ItemListcartquantity;

                    txt_count.setText(cart_quantity);
                    int co = Integer.parseInt(cart_quantity);
                    count = co;

                    Log.e("AddOnItem",addonitem);
//                        if(alist.size()>0){
//                            alist.add(new FoodDataModel(addonitem, addonitemprice));
//                            recyclerView.setAdapter(adapter);
//                        }
//                        else{
//
//                        }
                    txtadd_tocart.setText("Update Cart");
                    txtRemove.setVisibility(View.VISIBLE);
                    if (txtadd_tocart.equals("Update Cart")) {
                        ln_addTocart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (UpCartID == null && pid == null && upQty == null && price == null) {
                                    Toast.makeText(SingleFoodDetailsActivity.this, "Data Not Found", Toast.LENGTH_LONG).show();
                                    ;
                                } else {
                                    if(cart_quantity.equals(upQty)){
                                        Log.e("CountonRef",count+"");
                                        Log.e("UpdateQtyonRefresh",upQty);
                                    }
                                    else{
//                                            upQty = String.valueOf(count);
//                                            Log.e("UpdateQtyonRefreshElse",upQty);
//                                        Log.e("Test", cartID + pid + upQty + upPrice);
//                                        callUpdateCart(cartID, pid, upQty, upPrice);
//                                        startActivityForResult(new Intent(getActivity(), CartActivity.class), 101);
                                    }
                                }
                            }
                        });
                    }

                }

                else{}
            }

            if (data == false) {
                String message = (String) job.get("message");
                Log.e("False", message);
                if (message.equalsIgnoreCase("The Product Id field is required.<br>\n")) {
                    Toast.makeText(SingleFoodDetailsActivity.this, "Product Id Cannot Be Blank", Toast.LENGTH_SHORT).show();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void getProductById(final String pid){
        if (!CommonUtilities.isOnline(SingleFoodDetailsActivity.this)) {
            Toast.makeText(SingleFoodDetailsActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog pd = new ProgressDialog(SingleFoodDetailsActivity.this);
        pd.setTitle("Please Wait");
        pd.setCancelable(false);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "product/getProductById";
        String testurl = "";
        //Log.e("PID in call",pid);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                parseResponse1(response);
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

                        parseResponse1(response.toString());

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
                map.put("productId", pid);
                //map.put("productId", ItemListproductID);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(SingleFoodDetailsActivity.this));
                Log.e("TokenFood",AppSharedPreference.loadTokenPreference(SingleFoodDetailsActivity.this));
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
        VolleySingleton.getInstance(SingleFoodDetailsActivity.this).addToRequestQueue(sr);
    }

    public void parseResponse1(String response) {
        Log.e("CO Product Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            JSONObject obj = job.getJSONObject("data");
            //int length = jarr.length();
            String addonitemprice = "", addonitem = "";
            if (data == true) {
                String productID = obj.getString("productID");
                String restaurant_id = obj.getString("restaurant_id");
                name = obj.getString("name");
                String category = obj.getString("category");
                String sub_Category = obj.getString("sub_Category");
                String food_type1 = obj.getString("food_type");
                price = obj.getString("price");
                String description = obj.getString("description");
                String quantity = obj.getString("quantity");
                String ingrediants = obj.getString("ingrediants");
                String image1 = obj.getString("image");
                //String available_days = obj.getString("available_days");
                JSONArray jFaceShape = obj.optJSONArray("available_days");
                url = "";
                for (int jj = 0; jj < jFaceShape.length(); jj++) {
                    url += (String) jFaceShape.get(jj);
                    Log.d("AD", "url => " + url);
                }
                String available_start_time = obj.getString("available_start_time");
                String available_end_time = obj.getString("available_end_time");
                String createdAt = obj.getString("createdAt");
                String status1 = obj.getString("status");

//                JSONArray addon_item = obj.optJSONArray("addon_item");
//
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

                String category_name = obj.getString("category_name");
                String sub_category_name = obj.getString("sub_category_name");


                Log.e("Tes", name + url + price);
                Log.e("ItemListproductID", ItemListproductID);
                Log.e("productID", productID);


                txtSingleName.setText(name);
                txtSICuisine.setText(category_name);
                txtdisprice.setText(price);
                txtProductDesc.setText(description);
                txtProductIngre.setText(ingrediants);
                new AQuery(SingleFoodDetailsActivity.this).id(imgSingleItem).image(image1);
                //txt_count.setText("");
                //final String cart_quantity = ItemListcartquantity;

                //txt_count.setText(cart_quantity);
//        int co = Integer.parseInt(cart_quantity);
//        count = co;

                //Log.e("AddOnItem", addonitem);

//                if (alist.size() == 0) {
//
//                    alist.add(new FoodDataModel(addonitem, addonitemprice));
//                    recyclerView.setAdapter(adapter);
//                } else {
//                    recyclerView.refreshDrawableState();
//                }
//                if (addon_item != null) {
//                    recyclerView.setAdapter(adapter);
//                } else {
//
//                }


            }
            if (data == false) {
                String message = (String) job.get("message");
                Log.e("False", message);
                if (message.equalsIgnoreCase("The Product Id field is required.<br>\n")) {
                    Toast.makeText(SingleFoodDetailsActivity.this, "Product Id Cannot Be Blank", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void calladdtoCartApi(final String pid, final int q, final String p){
        if (!CommonUtilities.isOnline(SingleFoodDetailsActivity.this)) {
            Toast.makeText(SingleFoodDetailsActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

//        final ProgressDialog pd = new ProgressDialog(SingleFoodDetailsActivity.this);
//        pd.setTitle("Please Wait");
//        pd.setCancelable(false);
//        pd.show();

        final CustomProgressDialogue pd= new CustomProgressDialogue(SingleFoodDetailsActivity.this);
        pd.setCancelable(false);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "cart/addtoCart";
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
                Map<String, String> map = new HashMap<>();
                map.put("productId", pid);
                map.put("quantity", q+"");
                map.put("price", p);
                Log.e("ADDtoCart",pid+":"+q+":"+p);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(SingleFoodDetailsActivity.this));
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
        VolleySingleton.getInstance(SingleFoodDetailsActivity.this).addToRequestQueue(sr);
    }

    public void parseResponse(String response) {
        Log.e("CO SingleItem Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            String message="";
            if (data == true) {
                message = (String)job.get("message");
                if(message.equalsIgnoreCase("Product Added to Cart")){
                    Toast.makeText(SingleFoodDetailsActivity.this, "Product Added to Cart Successfully.", Toast.LENGTH_SHORT).show();
                }
            }
            if(data==false){
                message = (String)job.get("message");
                Log.e("False",message);
                if(message.equalsIgnoreCase("The Quantity field is required.<br>\n")){
                    Toast.makeText(SingleFoodDetailsActivity.this, "Quantity Cannot Be Blank", Toast.LENGTH_SHORT).show();
                }
                if(message.equalsIgnoreCase("The Product field is required.<br>\n")){
                    Toast.makeText(SingleFoodDetailsActivity.this, "Product Cannot Be Blank", Toast.LENGTH_SHORT).show();
                }
                if(message.equalsIgnoreCase("The Product field is required.<br>\n")){
                    Toast.makeText(SingleFoodDetailsActivity.this, "Product Cannot Be Blank", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode==RESULT_OK){
            ShowCartFoodItemModel sc = (ShowCartFoodItemModel) data.getSerializableExtra("CartDetail");
            upCartId = sc.getCart_id();
            upName = sc.getName();
            upPrice = sc.getPrice();
            String upQ = sc.getQuantity();
            upPid = sc.getPid();

            Log.e("BAck",upCartId+upName+upPrice+upPid);
            callgetCartItemById(upCartId);
            txtRemove.setVisibility(View.VISIBLE);
            txtadd_tocart.setText("Update Cart");


        }
    }

    public void callgetCartItemById(final String cart_id){
        if (!CommonUtilities.isOnline(SingleFoodDetailsActivity.this)) {
            Toast.makeText(SingleFoodDetailsActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

//        final ProgressDialog pd = new ProgressDialog(SingleFoodDetailsActivity.this);
//        pd.setTitle("Please Wait");
//        pd.setCancelable(false);
//        pd.show();


        final CustomProgressDialogue pd= new CustomProgressDialogue(SingleFoodDetailsActivity.this);
        pd.setCancelable(false);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "cart/getCartItemById";
        String testurl = "";

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                parseResponse2(response);
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

                        parseResponse2(response.toString());

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
                map.put("cartId", cart_id);

                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(SingleFoodDetailsActivity.this));
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
        VolleySingleton.getInstance(SingleFoodDetailsActivity.this).addToRequestQueue(sr);
    }

    public void parseResponse2(String response) {
        Log.e("CO CartItem Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            String message="";
            //String name = "", price = "", quantity = "", url = "",cartID="";
            if (data == true) {
                JSONObject obj = job.getJSONObject("data");
                for(int i=0;i<obj.length();i++){
                    final String cartID = obj.getString("cartID");
                    String user_id = obj.getString("user_id");
                    final String cart_quantity = obj.getString("cart_quantity");
                    String cart_price = obj.getString("cart_price");
                    String cart_added_at = obj.getString("cart_added_at");
                    final String productID = obj.getString("productID");
                    String restaurant_id = obj.getString("restaurant_id");
                    name = obj.getString("name");
                    String category = obj.getString("category");
                    String sub_Category = obj.getString("sub_Category");
                    String food_type = obj.getString("food_type");
                    price = obj.getString("price");
                    String description = obj.getString("description");
                    final String quantity = obj.getString("quantity");
                    String ingrediants = obj.getString("ingrediants");
                    String image = obj.getString("image");
                    JSONArray jFaceShape = obj.optJSONArray("available_days");

                    for (int jj = 0; jj < jFaceShape.length(); jj++) {
                        url += (String) jFaceShape.get(jj);
                        Log.d("AD", "url => " + url);
                    }
                    String available_start_time = obj.getString("available_start_time");
                    String available_end_time = obj.getString("available_end_time");
                    String createdAt = obj.getString("createdAt");
                    String status1 = obj.getString("status");
                    //String addon_item = obj.getString("addon_item");
                    String category_name = obj.getString("category_name");
                    String sub_category_name = obj.getString("sub_category_name");

                    txtSingleName.setText(name);
                    txtSICuisine.setText(category_name);
                    txtdisprice.setText(price);
                    txtProductDesc.setText(description);
                    txtProductIngre.setText(ingrediants);
                    txt_count.setText(cart_quantity);
                    count = Integer.parseInt(cart_quantity);
                    upQty = count+"";
                    Log.e("UpdateQty",upQty);

                    ln_addTocart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(cartID==null && productID==null && upQty==null && price ==null){
                                Toast.makeText(SingleFoodDetailsActivity.this,"Data Not Found",Toast.LENGTH_LONG).show();;
                            }
                            else {
                                Log.e("Test",cartID+productID+count+upPrice);
                                callUpdateCart(cartID, productID, upQty, upPrice);
                                startActivityForResult(new Intent(SingleFoodDetailsActivity.this, CartActivity.class),101);
                            }
                        }
                    });
                }
            }
            if(data==false){
                message = (String)job.get("message");
                Log.e("False",message);
                if(message.equalsIgnoreCase("The Cart Id field is required.<br>\n")){
                    Toast.makeText(SingleFoodDetailsActivity.this, "Cart Cannot Be Blank", Toast.LENGTH_SHORT).show();
                }
                if(message.equalsIgnoreCase("Data Not Found.")){
                    Toast.makeText(SingleFoodDetailsActivity.this, "Data Not Found", Toast.LENGTH_SHORT).show();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void callUpdateCart(final String cartId, final String pid, final String qty, final String price){
        if (!CommonUtilities.isOnline(SingleFoodDetailsActivity.this)) {
            Toast.makeText(SingleFoodDetailsActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

//        final ProgressDialog pd = new ProgressDialog(SingleFoodDetailsActivity.this);
//        pd.setTitle("Please Wait");
//        pd.setCancelable(false);
//        pd.show();

        final CustomProgressDialogue pd= new CustomProgressDialogue(SingleFoodDetailsActivity.this);
        pd.setCancelable(false);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "cart/addtoCart";
        String testurl = "";

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                parseResponse3(response);
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

                        parseResponse3(response.toString());

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
                map.put("cartId", cartId);
                map.put("productId",pid);
                map.put("quantity",qty);
                map.put("price",price);

                Log.e("Update Data",cartId+":"+pid+":"+qty+":"+price);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(SingleFoodDetailsActivity.this));
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
        VolleySingleton.getInstance(SingleFoodDetailsActivity.this).addToRequestQueue(sr);
    }

    public void parseResponse3(String response) {
        Log.e("CO CartItem Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            String message = (String)job.get("message");
            if (data == true) {
                if(message.equalsIgnoreCase("Cart Updated Successfully")){
                    Toast.makeText(SingleFoodDetailsActivity.this, "Cart Updated Successfully", Toast.LENGTH_SHORT).show();
                }
            }
            //Testing need to be changed...
            if(data==false){
                message = (String)job.get("message");
                Log.e("False",message);
                if(message.equalsIgnoreCase("The Cart Id field is required.<br>\n")){
                    Toast.makeText(SingleFoodDetailsActivity.this, "Cart Cannot Be Blank", Toast.LENGTH_SHORT).show();
                }
                if(message.equalsIgnoreCase("Data Not Found.")){
                    Toast.makeText(SingleFoodDetailsActivity.this, "Data Not Found", Toast.LENGTH_SHORT).show();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void callremoveItemFromCart(){

        if (!CommonUtilities.isOnline(SingleFoodDetailsActivity.this)) {
            Toast.makeText(SingleFoodDetailsActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

//        final ProgressDialog pd = new ProgressDialog(SingleFoodDetailsActivity.this);
//        pd.setTitle("Please Wait");
//        pd.setCancelable(false);
//        pd.show();

        final CustomProgressDialogue pd= new CustomProgressDialogue(SingleFoodDetailsActivity.this);
        pd.setCancelable(false);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "cart/removeItemFromCart";
        String testurl = "";

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                parseResponseRemove(response);
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

                        parseResponseRemove(response.toString());

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
                map.put("cartId", UpCartID);
                Log.e("cartId", UpCartID);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(SingleFoodDetailsActivity.this));
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
        VolleySingleton.getInstance(SingleFoodDetailsActivity.this).addToRequestQueue(sr);
    }

    public void parseResponseRemove(String response) {
        Log.e("CO CartRemove Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            String message= job.getString("message");
            if (data == true) {
                if (message.equalsIgnoreCase("Item Removed from cart Successfully.")) {
                    Toast.makeText(SingleFoodDetailsActivity.this, "Item Removed from cart Successfully.", Toast.LENGTH_SHORT).show();
                    //AppCompatActivity activity = (AppCompatActivity) getApplicationContext();
                    //activity.getSupportFragmentManager().popBackStack();
                    finish();
                }
            }
            else{
                message = (String)job.get("message");
                Log.e("False",message);
                if(message.equalsIgnoreCase("Something went wrong! Please try again later.")){
                    Toast.makeText(SingleFoodDetailsActivity.this, "Something went wrong! Please try again later.", Toast.LENGTH_SHORT).show();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    @Override
    protected void onResume() {
        super.onResume();
        callgetCartItemList();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        callgetCartItemList();
    }
}
