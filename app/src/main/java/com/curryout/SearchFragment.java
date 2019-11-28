package com.curryout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.curryout.model.SearchDataModel;
import com.yayandroid.parallaxrecyclerview.ParallaxRecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchFragment extends Fragment {

    ArrayList<SearchDataModel> alist;
    ParallaxRecyclerView recyclerView;
    LinearLayout linearViewCartSearch;
    TextView txtViewDetailSearch,txtViewItSearch, txtSearchHome;
    ImageView search_iconSer;
    public SearchFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_search, container, false);

        alist = new ArrayList<>();
        callGetAllCuisines();
        recyclerView = (ParallaxRecyclerView) view.findViewById(R.id.searchRecyListView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        init(view);
        listener(view);
        return view;
    }

    private void listener(View view) {
//        txtSearchHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(getActivity(),SearchActivity.class);
//                startActivity(in);
//            }
//        });

        search_iconSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = txtSearchHome.getText().toString();
                Intent in = new Intent(getActivity(),SearchActivity.class);
                in.putExtra("SearchData",data);
                startActivity(in);
            }
        });

    }

    public void init(View view){
        txtSearchHome=(TextView)view.findViewById(R.id.txtSearchHome);
        search_iconSer = view.findViewById(R.id.search_iconSer);
        linearViewCartSearch = view.findViewById(R.id.linearViewCartSearch);
        //txtViewDetailSearch = view.findViewById(R.id.txtViewDetailSearch);
        txtViewItSearch = view.findViewById(R.id.txtViewItSearch);
        linearViewCartSearch.setVisibility(View.GONE);
        callgetCartItemList();
    }
    public void callGetAllCuisines(){
        if (!CommonUtilities.isOnline(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

//        final ProgressDialog pd = new ProgressDialog(getActivity());
//        pd.setTitle("Please Wait");
//        pd.setCancelable(false);
//        pd.show();

        final CustomProgressDialogue pd= new CustomProgressDialogue(getActivity());
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "common/getAllCuisines";
        //String testurl="http://www.mocky.io/v2/5cc3fc743400006500765489";
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
                public Map<String, String> getHeaders () throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(getActivity()));
                return header;
            }
                @Override
                public Priority getPriority () {
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
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(sr);
    }

    public void parseResponse(String response) {
        Log.e("CurryOut Cuisi Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean)job.get("status");
            if(data==true){
                JSONArray jarr = job.getJSONArray("data");

                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject jobj = jarr.getJSONObject(i);
                    String cuisine_id = jobj.getString("cuisineID");
                    String name = jobj.getString("name");
                    String parent_id = jobj.getString("parent_id");
                    String status = jobj.getString("status");
                    String image = jobj.getString("image");
                    String restraunt_count = jobj.getString("restraunt_count");

                    Log.e("RD", cuisine_id);
                    Log.e("RD1", name);
                    SearchDataModel searchDataModel = new SearchDataModel(cuisine_id,name,restraunt_count,image);
                    alist.add(searchDataModel);
                }
            }
            SearchListAdapter adapter = new SearchListAdapter(alist,getActivity());
            recyclerView.setAdapter(adapter);



        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void callgetCartItemList(){
        if (!CommonUtilities.isOnline(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
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
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(getActivity()));
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
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(sr);
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
                linearViewCartSearch.setVisibility(View.GONE);
            }
            else{
                linearViewCartSearch.setVisibility(View.VISIBLE);
                txtViewItSearch.setText(cartarr.length()+"");
                linearViewCartSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(getActivity(),CartActivity.class);
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

            }

            if (data == false) {
                String message = (String) job.get("message");
                Log.e("False", message);
                if (message.equalsIgnoreCase("The Product Id field is required.<br>\n")) {
                    Toast.makeText(getActivity(), "Product Id Cannot Be Blank", Toast.LENGTH_SHORT).show();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        callgetCartItemList();
    }
}
