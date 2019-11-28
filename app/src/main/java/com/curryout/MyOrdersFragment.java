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
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.curryout.adapter.MyOrderParentItemModelAdapter;
import com.curryout.adapter.MyOrdersListAdapter;
import com.curryout.appsharedpreference.AppGlobalConstants;
import com.curryout.appsharedpreference.AppSharedPreference;
import com.curryout.appsharedpreference.CommonUtilities;
import com.curryout.model.MyOrderParentItemModel;
import com.curryout.model.MyOrdersParentDM;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;


public class MyOrdersFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<MyOrdersParentDM> alist;
    MyOrdersListAdapter adapter;

    ListView myList;
   // static ArrayList<MyOrderParentItemModel> alist1;

    static MyOrderParentItemModelAdapter adap;
    LinearLayout linearViewCartOrder;
    TextView txtViewDetailOrder,txtViewItOrder;

    String GlobalorderId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_orders, container, false);


        listener(view);
        alist = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.myOrderRecyListView);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getContext());
        //layout.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layout);

        init(view);
        return view;
    }


    private void init(View view) {
       // alist1 = new ArrayList<>();
        callGetCustomerOrderList();
        linearViewCartOrder = view.findViewById(R.id.linearViewCartOrder);
        //txtViewDetailOrder = view.findViewById(R.id.txtViewDetailOrder);
        txtViewItOrder = view.findViewById(R.id.txtViewItOrder);
        linearViewCartOrder.setVisibility(View.GONE);
        callgetCartItemList();

    }

    private void listener(View view) {

    }

    public void callGetCustomerOrderList(){
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

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "order/getCustomerOrdersList";
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
//                map.put("cartId", UpCartID);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(getActivity()));
                //header.put("X-Auth-Token", "4ocswcos00s884gook00s44kcgooo40sgksc4ggs");
                Log.e("Token in Order",AppSharedPreference.loadTokenPreference(getActivity()));
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


    public void parseResponse(String response) {
        Log.e("CO MyOrder Response", "response " + response);
        try{
            JSONObject job = new JSONObject(response);
            boolean status = (boolean) job.get("status");
            String ordered_product_id="",ordered_item_quantity="",ordered_item_price="",
            product_name="",food_type="",category="",sub_category="",restraunt_name="",
                    restraunt_image="",orderId="";
            JSONArray o_id = null;
            if(status==true) {

                JSONArray jarr = job.getJSONArray("data");
                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject arr = jarr.getJSONObject(i);
                    Log.e("Data MyOrder", arr.toString());
                    orderId = arr.getString("orderID");
                    String customer_id = arr.getString("customer_id");
                    String instruction_to_delivery_boy = arr.getString("instruction_to_delivery_boy");
                    String instruction_for_order = arr.getString("instruction_for_order");
                    String sub_total = arr.getString("sub_total");
                    String delivery_fee = arr.getString("delivery_fee");
                    String discount = arr.getString("discount");
                    String total = arr.getString("total");
                    String order_datetime = arr.getString("order_datetime");
                    String houseno = arr.getString("houseno");
                    String street = arr.getString("street");
                    String landmark = arr.getString("landmark");
                    String city = arr.getString("city");
                    String state = arr.getString("state");
                    String country = arr.getString("country");
                    String postalCode = arr.getString("postalCode");
                    String payment_method = arr.getString("payment_method");
                    String order_status = arr.getString("order_status");
                    String mobile = arr.getString("mobile");
                    String customer_name = arr.getString("customer_name");
                   JSONArray o1_id = arr.getJSONArray("order_item_details");



                    ArrayList<MyOrderParentItemModel> alist1;
                    alist1 = new ArrayList<>();
                for (int x = 0; x < o1_id.length(); x++) {
                    JSONObject obj = o1_id.getJSONObject(x);
                    ordered_product_id = obj.getString("ordered_product_id");
                    ordered_item_quantity = obj.getString("ordered_item_quantity");
                    ordered_item_price = obj.getString("ordered_item_price");
                    product_name = obj.getString("product_name");
                    food_type = obj.getString("food_type");
                    category = obj.getString("category");
                    restraunt_name = obj.getString("restraunt_name");
                    restraunt_image = obj.getString("restraunt_image");
                    sub_category = obj.getString("sub_category");

                    Log.e("PN", product_name);
                    Log.e("OrderedItem", ordered_item_quantity);

                    alist1.add(new MyOrderParentItemModel(product_name, ordered_item_quantity));

                   // Log.e("X", o_id.length() + "");
                    Log.e("Data Inside MyOrder", obj.toString());
                    //getData(alist1);
                }

                    //GlobalorderId = orderId;
                    Log.e("Sub_Cat", sub_category);

                    MyOrdersParentDM dm = new MyOrdersParentDM(orderId, restraunt_name, sub_category, restraunt_image,alist1);
                    dm.setOrderId(orderId);
                    alist.add(dm);

            }
                adapter = new MyOrdersListAdapter(alist, getActivity());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


            }
            else{

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //onResume();
        //adapter.getListData(adap);
    }

    public static void getData(ArrayList<MyOrderParentItemModel> a){
        //alist1 = a;
    }
    public static void getAdap(MyOrderParentItemModelAdapter a){
        adap = a;
    }
//    public static ArrayList<MyOrderParentItemModel> sendData(){
//        return alist1;
//    }
    public static MyOrderParentItemModelAdapter sendAdap(){
        return adap;
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
                Log.e("X-Auth-TokenOrders", AppSharedPreference.loadTokenPreference(getActivity()));
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
                linearViewCartOrder.setVisibility(View.GONE);
            }
            else{
                linearViewCartOrder.setVisibility(View.VISIBLE);
                txtViewItOrder.setText(cartarr.length()+"");

                linearViewCartOrder.setOnClickListener(new View.OnClickListener() {
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

//    @Override
//    public void onResume() {
//        super.onResume();
//        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
//        //callGetCustomerOrderList();
//
//    }
    boolean allowRefresh;
    @Override
    public void onResume() {
        super.onResume();
        callgetCartItemList();
        if (allowRefresh)
        {
            allowRefresh = false;
            callGetCustomerOrderList();
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();

        }
    }



//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (getFragmentManager() != null) {
//
//            getFragmentManager()
//                    .beginTransaction()
//                    .detach(this)
//                    .attach(this)
//                    .commit();
//            callgetCartItemList();
//        }
//
//    }

    public void testData(){
        /*JSONArray ordered_product = arr.optJSONArray("ordered_product_id");

                    for (int jj = 0; jj < ordered_product.length(); jj++) {
                        ordered_product_id += (String) ordered_product.get(jj);
                        Log.d("OP", "url => " + ordered_product_id);
                    }

                    JSONArray ordered_item_quant = arr.optJSONArray("ordered_item_quantity");

                    for (int jj = 0; jj < ordered_item_quant.length(); jj++) {
                        ordered_item_quantity += (String) ordered_item_quant.get(jj);
                        Log.d("OQ", "url => " + ordered_item_quantity);
                    }

                    JSONArray ordered_item_pric = arr.optJSONArray("ordered_item_price");

                    for (int jj = 0; jj < ordered_item_pric.length(); jj++) {
                        ordered_item_price += (String) ordered_item_pric.get(jj);
                        Log.d("OIP", "url => " + ordered_item_price);
                    }

                    JSONArray product_nam = arr.optJSONArray("product_name");

                    for (int jj = 0; jj < product_nam.length(); jj++) {
                        product_name += (String) product_nam.get(jj);
                        Log.d("PN", "url => " + product_name);
                    }

                    JSONArray food_typ = arr.optJSONArray("food_type");

                    for (int jj = 0; jj < food_typ.length(); jj++) {
                        food_type += (String) food_typ.get(jj);
                        Log.d("FT", "url => " + food_type);
                    }

                    JSONArray categor = arr.optJSONArray("category");

                    for (int jj = 0; jj < categor.length(); jj++) {
                        category += (String) categor.get(jj);
                        Log.d("Category", "url => " + category);
                    }

                    JSONArray sub_categor = arr.optJSONArray("sub_category");

                    for (int jj = 0; jj < sub_categor.length(); jj++) {
                        sub_category = (String) sub_categor.get(jj)+" | ";
                        Log.d("Category", "url => " + sub_category);
                    }

                    JSONArray restraunt_nam = arr.optJSONArray("restraunt_name");

                    for (int jj = 0; jj < restraunt_nam.length(); jj++) {
                        restraunt_name = (String) restraunt_nam.get(jj);
                        Log.d("RN", "url => " + restraunt_name);
                    }

                    JSONArray restraunt_imag = arr.optJSONArray("restraunt_image");

                    for (int jj = 0; jj < restraunt_imag.length(); jj++) {
                        restraunt_image += (String) restraunt_imag.get(jj);
                        Log.d("RI", "url => " + restraunt_image);
                    }*/
    }
}
