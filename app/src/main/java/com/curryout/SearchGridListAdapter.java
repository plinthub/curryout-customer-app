package com.curryout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.curryout.appsharedpreference.AppGlobalConstants;
import com.curryout.appsharedpreference.AppSharedPreference;
import com.curryout.appsharedpreference.CommonUtilities;
import com.curryout.model.CartIdTestModel;
import com.curryout.model.ShowCartFoodItemModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class SearchGridListAdapter extends RecyclerView.Adapter<SearchGridListAdapter.ViewHolder> {


    static private ArrayList<SearchGridDataModel> listData;
    static Context context;
    static String productid, pri;
    public static String cartId = "";
    String pid;
    String qty;
    String price;
    static int pos, count;
    static ImageView plus;
    static TextView txt_c;
    static SearchGridDataModel searchGridDataModel,searchGridDataModelpro;
    public static String CartItemListCartId="",proId="",qtyDisp="";
    static LinearLayout ln_add1, ln_plusMin1;
    static TextView txt_count1;
    static ArrayList<ShowCartFoodItemModel> cartPro;
    static CartIdTestModel testModel;
    public static String id;
    ShowCartFoodItemModel scm;

    public SearchGridListAdapter(Context context){
        this.context = context;
        callgetCartItemList();
        notifyDataSetChanged();
        try {
            id = testModel.getCartId();
            Log.e("TestModel", "Test"+ id);
        }catch (Exception e){

        }
    }
    public SearchGridListAdapter(ArrayList<SearchGridDataModel> listData, Context context) {
        this.listData = listData;
        this.context = context;
        callgetCartItemList();
        notifyDataSetChanged();
    }
    public SearchGridListAdapter(SearchGridDataModel searchGridDataModelpro){
        this.searchGridDataModelpro = searchGridDataModelpro;
        Log.e("searchGridDataModelpro",searchGridDataModelpro.getTitle());
        //callgetCartItemList();
        notifyDataSetChanged();

    }
    public SearchGridListAdapter(ArrayList<ShowCartFoodItemModel> cartPro){
       this.cartPro = cartPro;
        Log.e("cartpro",cartPro+"");
        //callgetCartItemList();
        scm = new ShowCartFoodItemModel();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public SearchGridListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.grid_fooditem_layout, parent, false);
        SearchGridListAdapter.ViewHolder viewHolder = new SearchGridListAdapter.ViewHolder(listItem);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull SearchGridListAdapter.ViewHolder holder, final int position) {
        Log.e("first","fir");

        searchGridDataModel = listData.get(position);
        pos = holder.getAdapterPosition();
        holder.txt_name.setText(searchGridDataModel.getTitle());
        holder.txt_price.setText(searchGridDataModel.getPrice());

        Glide.with(context).load(Uri.parse(searchGridDataModel.getImg()))
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img_title);


        final String pid = searchGridDataModel.getProductID();
        //Log.e("productid",productid = pid);
        final String price = searchGridDataModel.getPrice();
        //Log.e("Price",pri = price);
        Log.e("AdapterPos", pos + "");

        holder.img_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new SingleFoodDetailsFragment(pid, price);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();

//                Intent in = new Intent(context,SingleFoodDetailsActivity.class);
//                in.putExtra("PID",pid);
//                in.putExtra("PRICE",price);
//                context.startActivity(in);


            }
        });

        try {
            Log.e("onBindViewHolder", "onBindViewHolder");
            Log.e("CartItemListCartIdVH", CartItemListCartId);
            Log.e("cartIdVH", cartId);
            Log.e("Condition", CartItemListCartId.contains(cartId)+"");

            String p = searchGridDataModel.getProductID();
            for (Iterator<ShowCartFoodItemModel> it = cartPro.iterator(); it.hasNext(); ) {
                ShowCartFoodItemModel f = it.next();
                final String pidd = f.getPid();
                final String cartid = f.getCart_id();
                Log.e("CartContains",cartid);
                Log.e("ShowCartFoodItemModel","Test"+f.toString());
                if (pidd.equals(p)){
                    Log.e("SetData",pid+":"+p);
                    holder.ln_add.setVisibility(View.GONE);
                    holder.ln_plusMin.setVisibility(View.VISIBLE);
                    holder.txt_count.setText(f.getQuantity());
                    count = Integer.parseInt(f.getQuantity());
                    Log.e("Count in Bind",count+"");
                    Log.e("CartContainsIF",cartid);
                    cartId = cartid;
                }}









            //if (CartItemListCartId!=null) {
            if (CartItemListCartId.contains(cartId)) {
                Log.e("TryClicked", searchGridDataModel.getProductID()+"");
                //String p = searchGridDataModel.getProductID();
                Log.e("ModelPro",p);
                Log.e("ProForTesttttt","ProForTesttttt"+proId);
                for (Iterator<ShowCartFoodItemModel> it = cartPro.iterator(); it.hasNext(); ) {
                    ShowCartFoodItemModel f = it.next();
                    final String pidd = f.getPid();
                    final String cartid = f.getCart_id();
                    Log.e("CartContains",cartid);
                    Log.e("ShowCartFoodItemModel","Test"+f.toString());
                    if (pidd.equals(p)){
                        Log.e("SetData",pid+":"+p);
                        holder.ln_add.setVisibility(View.GONE);
                        holder.ln_plusMin.setVisibility(View.VISIBLE);
                        holder.txt_count.setText(f.getQuantity());
                        count = Integer.parseInt(f.getQuantity());
                        Log.e("Count in Bind",count+"");
                        Log.e("CartContainsIF",cartid);
                        cartId = cartid;
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }




    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_title, img_min, img_plus;
        public TextView txt_name, txt_price, txt_count, addToCarttxt;
        public LinearLayout linearLayout, ln_add, ln_plusMin;
        private int count;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.img_title = (ImageView) itemView.findViewById(R.id.imgTitle);
            this.txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            this.txt_price = (TextView) itemView.findViewById(R.id.txt_price);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            ln_add = (LinearLayout) itemView.findViewById(R.id.ln_add);
            ln_plusMin = (LinearLayout) itemView.findViewById(R.id.ln_plusMin);
            img_plus = (ImageView) itemView.findViewById(R.id.img_plus);
            img_min = (ImageView) itemView.findViewById(R.id.img_min);
            txt_count = (TextView) itemView.findViewById(R.id.txt_count);
            addToCarttxt = (TextView) itemView.findViewById(R.id.addToCarttxt);
            final AppCompatActivity activity = (AppCompatActivity) itemView.getContext();
            plus = img_plus;
            txt_count1 = txt_count;
            //ln_add1 = ln_add;
            ln_plusMin1=ln_plusMin;
            //callgetCartItemList();

            //String piid = ;

            //Log.e("ViewHolderPrice",txt_price+"");

            try{
            searchGridDataModel = listData.get(pos);
            final String pp = searchGridDataModel.getProductID();
            final String pi = searchGridDataModel.getPrice();
            //Log.e("ViewHolderPos", pos + "");
            Log.e("ViewHolderProID", searchGridDataModel.getProductID());
            Log.e("ViewHolderPrice", searchGridDataModel.getPrice());
            Log.e("ViewHolderName", searchGridDataModel.getTitle());




                ln_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition();
                    final SearchGridDataModel clickedDataItem = listData.get(pos);
                   // Toast.makeText(context, "test"+clickedDataItem.getTitle(), Toast.LENGTH_SHORT).show();
                    ln_add.setVisibility(View.GONE);
                    ln_plusMin.setVisibility(View.VISIBLE);
                    count = 1;
                    Log.e("CartIdLinearOuter",cartId);
                    if (count==1) {
                        Log.e("CartIdLinearIf",cartId);
                        img_min.setEnabled(false);
                        txt_count.setText(String.valueOf(count));
                        img_min.setEnabled(false);
                        calladdtoCartApi(clickedDataItem.getProductID(), count, clickedDataItem.getPrice());
                        //Toast.makeText(activity, "One Product Added to Cart.", Toast.LENGTH_SHORT).show();

                    }
                    if(count>1 && cartId!=null){
                        Log.e("CartIdLinearElse",cartId);
                        callupdatecartapi(cartId, clickedDataItem.getProductID(), count + "", clickedDataItem.getPrice());
                        //Toast.makeText(activity, "Cart Updated...Linear", Toast.LENGTH_SHORT).show();

                    }



                }
            });

            img_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        final SearchGridDataModel clickedDataItem = listData.get(pos);
                        Log.e("Count in Plus",count+"");
                        if(txt_count.getText().toString().trim().isEmpty())
                        {count=1;}
                        else{ count=Integer.parseInt(txt_count.getText().toString().trim())+1;
                        }
//                        count=txt_count.getText().toString().trim()
//                        count++;count
                        Log.e("Count in Plus",count+"");
                        txt_count.setText(String.valueOf(count));
                        if (cartId==null) {
                            Log.e("PosinIF",pos+"");
                            txt_count.setText(String.valueOf(count));
                            img_min.setEnabled(false);
                            Log.e("In CountPlus IF", clickedDataItem.getProductID() + " " + count + " " + clickedDataItem.getPrice());
                            calladdtoCartApi(clickedDataItem.getProductID(), count, clickedDataItem.getPrice());

                            //context.startActivity(new Intent(context, CartActivity.class));
                            //Toast.makeText(context, clickedDataItem.getProductID() + "Img Plus If", Toast.LENGTH_LONG).show();
                            //callgetCartItemList();
                        }
                        if (count>1 && cartId!=null) {
                            Log.e("cartIdImgPlus",cartId);
                            img_min.setEnabled(true);
                            Log.e("In CountPlus 1", cartId + " " + clickedDataItem.getProductID() + " " + count + " " + clickedDataItem.getPrice());
                            callupdatecartapi(cartId, clickedDataItem.getProductID(), count + "", clickedDataItem.getPrice());
                            //context.startActivity(new Intent(context, CartActivity.class));
                            //Toast.makeText(context, clickedDataItem.getProductID() + "Img Plus Else", Toast.LENGTH_LONG).show();
                            //callgetCartItemList();
                        }

                    }


                }
            });

            img_min.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        final SearchGridDataModel clickedDataItem = listData.get(pos);
                        if(txt_count.getText().toString().trim().isEmpty())
                        {count=1;}
                        else{ count=Integer.parseInt(txt_count.getText().toString().trim())-1;
                        }
                        txt_count.setText(String.valueOf(count));
                    if (count==1) {
                        //count--;
                        txt_count.setText(String.valueOf(count));
                        img_min.setEnabled(false);
                    }

                    if (count > 1&& cartId!=null) {
                        Log.e("In CountMin 1", cartId + " " + productid + " " + count + " " + pri);

                        callupdatecartapi(cartId, clickedDataItem.getProductID(), count + "", clickedDataItem.getPrice());
                        //callgetCartItemList();
                    }
                }
                }
            });



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    // check if item still exists
                    if (pos != RecyclerView.NO_POSITION) {
                        final SearchGridDataModel clickedDataItem = listData.get(pos);
                        //Toast.makeText(context, "You clicked " + clickedDataItem.getTitle(), Toast.LENGTH_SHORT).show();
                        //callapi(clickedDataItem);
//                        img_plus.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                count++;
//                                txt_count.setText(String.valueOf(count));
//                                if (cartId==null) {
//                                    //Log.e("PosinIF",pos+"");
//                                    txt_count.setText(String.valueOf(count));
//                                    img_min.setEnabled(false);
//                                    calladdtoCartApi(clickedDataItem.getProductID(), count, clickedDataItem.getPrice());
//                                    //context.startActivity(new Intent(context, CartActivity.class));
//                                    //Toast.makeText(context, clickedDataItem.getProductID() + "Img Plus If", Toast.LENGTH_LONG).show();
//                                    //callgetCartItemList();
//                                }
//                                if (count > 1 && cartId!=null) {
//                                    Log.e("cartIdImgPlus",cartId);
//                                    img_min.setEnabled(true);
//                                    Log.e("In CountPlus 1", cartId + " " + clickedDataItem.getProductID() + " " + count + " " + clickedDataItem.getPrice());
//                                    callupdatecartapi(cartId, clickedDataItem.getProductID(), count + "", clickedDataItem.getPrice());
//                                    //context.startActivity(new Intent(context, CartActivity.class));
//                                    //Toast.makeText(context, clickedDataItem.getProductID() + "Img Plus Else", Toast.LENGTH_LONG).show();
//                                    //callgetCartItemList();
//                                }

//                            }
//                        });

//                        img_min.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (count > 0) {
//                                    count--;
//                                    txt_count.setText(String.valueOf(count));
//                                    img_min.setEnabled(false);
//                                }
//
//                                if (count > 1) {
//                                    Log.e("In CountMin 1", cartId + " " + productid + " " + count + " " + pri);
//                                    callupdatecartapi(cartId, clickedDataItem.getProductID(), count + "", clickedDataItem.getPrice());
//                                    //callgetCartItemList();
//                                }
//
//                            }
//                        });
//                        ln_add.setFocusable(false);
//                        ln_add.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                ln_add.setVisibility(View.GONE);
//                                ln_plusMin.setVisibility(View.VISIBLE);
//                                if (count == 0) {
//                                    Toast.makeText(activity, "Please Select Quantity", Toast.LENGTH_SHORT).show();
//                                }
//                                count = 1;
//                                Log.e("CartIdLinearOuter",cartId);
//                                if (count==1) {
//                                    Log.e("CartIdLinearIf",cartId);
//                                    img_min.setEnabled(false);
//                                    txt_count.setText(String.valueOf(count));
//                                    img_min.setEnabled(false);
//                                    calladdtoCartApi(clickedDataItem.getProductID(), count, clickedDataItem.getPrice());
//                                    Toast.makeText(activity, "One Product Added to Cart.", Toast.LENGTH_SHORT).show();
//
//                                }
//                                if(count>1){
//                                    Log.e("CartIdLinearElse",cartId);
//                                    callupdatecartapi(cartId, clickedDataItem.getProductID(), count + "", clickedDataItem.getPrice());
//                                    //Toast.makeText(activity, "Cart Updated...Linear", Toast.LENGTH_SHORT).show();
//
//                                }
//                            }
//                        });
                    }
                }
            });

//            try {
//                Log.e("CartItemListCartIdVH", CartItemListCartId);
//                Log.e("cartIdVH", cartId);
//                if (CartItemListCartId.equals(cartId)) {
//                    //ln_add1.setVisibility(View.GONE);
//                    ln_plusMin1.setVisibility(View.VISIBLE);
//                    txt_count1.setText("Q111111");
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }


//            img_plus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    count++;
//                    txt_count.setText(String.valueOf(count));
//                    if(count==1){
//                        Log.e("PosinIF",pos+"");
//                        txt_count.setText(String.valueOf(count));
//                        calladdtoCartApi(pp, count, pi);
//                        context.startActivity(new Intent(context, CartActivity.class));
//                    }
//                    if(count>1){
//                        Log.e("In Count 1",cartId+" "+productid+" "+count+" "+pri);
//                        //callupdatecartapi(cartId,productid,count+"",pri);
//                    }
//
//                }
//            });


//            ln_add.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ln_add.setVisibility(View.GONE);
//                    ln_plusMin.setVisibility(View.VISIBLE);
//                    if (count == 0) {
//                        Toast.makeText(activity, "Please Select Quantity", Toast.LENGTH_SHORT).show();
//                    }
//                    if (count == 1) {
//                        img_min.setEnabled(false);
//                        txt_count.setText(String.valueOf(count));
//                        img_min.setEnabled(false);
//                        calladdtoCartApi(clickedDataItem.getProductID(), count, clickedDataItem.getPrice());
//                    } else {
//                        Toast.makeText(activity, "Please Click on Quantity", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });


        }catch (Exception e){
            e.printStackTrace();
            }
        }


    }



    //Get Cart Item List
    public static void callgetCartItemList() {
        Log.e("aghgg","gfdhfghfg");

        if (!CommonUtilities.isOnline(context)) {
            Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        final CustomProgressDialogue pd= new CustomProgressDialogue(context);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "cart/getCartItemList";
        String testurl = "";

        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(context));
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
        VolleySingleton.getInstance(context).addToRequestQueue(sr);
    }

    public static void parseResponse1(String response) {
        Log.e("CO Product Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            JSONObject jdata = job.getJSONObject("data");
            JSONArray arr = jdata.optJSONArray("current_address");

            JSONArray cartarr = jdata.getJSONArray("cart");
            String name = "", price = "", url = "", cartID = "", cart_quantity = "", productID = "";
            for (int i = 0; i < cartarr.length(); i++) {
                JSONObject obj = cartarr.getJSONObject(i);
                cartID = obj.getString("cartID");
                String user_id = obj.getString("user_id");
                cart_quantity = obj.getString("cart_quantity");
                qtyDisp = cart_quantity;
                Log.e("cart quantity", obj.getString("cart_quantity"));
                String cart_price = obj.getString("cart_price");
                String cart_added_at = obj.getString("cart_added_at");
                productID = obj.getString("productID");
                proId += productID;
                Log.e("aghgg","gfdhfghfg");

                Log.e("ProForTestttttonLoad",proId);
                String restaurant_id = obj.getString("restaurant_id");
                name = obj.getString("name");
                String category = obj.getString("category");
                String sub_Category = obj.getString("sub_Category");
                String food_type = obj.getString("food_type");
                price = obj.getString("price");
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
                //String addon_item = obj.getString("addon_item");
                String category_name = obj.getString("category_name");
                String sub_category_name = obj.getString("sub_category_name");
                Log.e("Product_Id", productID);
                Log.e("CartItemListCartId1", CartItemListCartId);
                Log.e("Revised Name", name);
                Log.e("Cart Qty", cart_quantity);
//                productid = productID;
//                pri = price;
                CartItemListCartId = cartID;

                testModel = new CartIdTestModel(CartItemListCartId);

                Log.e("CartItemListCartId2", CartItemListCartId);




            }
            if (data == false) {
                String message = (String) job.get("message");
                Log.e("False", message);
                if (message.equalsIgnoreCase("The Product Id field is required.<br>\n")) {
                    Toast.makeText(context, "Product Id Cannot Be Blank", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //Add to Cart
    public static void calladdtoCartApi(final String p, final int q, final String paise) {
        if (!CommonUtilities.isOnline(context)) {
            Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        final CustomProgressDialogue pd= new CustomProgressDialogue(context);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
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
                map.put("productId", p);
                map.put("quantity", q + "");
                map.put("price", paise);
                Log.e("TrackAdap", p + " " + q + paise);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(context));
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
        VolleySingleton.getInstance(context).addToRequestQueue(sr);
    }

    public static void parseResponse(String response) {
        Log.e("CO SingleItem Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            JSONObject cartid = job.getJSONObject("data");

            for (int i = 0; i < cartid.length(); i++) {
                cartId = cartid.getString("cartId");
                Log.e("AddApiCartId",cartId);
            }
            if (cartId == null) {
                //Toast.makeText(context, "If Block", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(context, "Else Block", Toast.LENGTH_LONG).show();
            }
            String message = "";
            if (data == true) {
                message = (String) job.get("message");
                if (message.equalsIgnoreCase("Product Added to Cart")) {
                    Toast.makeText(context, "Product Added to Cart Successfully.", Toast.LENGTH_SHORT).show();
                }
            }
            if (data == false) {
                message = (String) job.get("message");
                Log.e("False", message);
                if (message.equalsIgnoreCase("The Quantity field is required.<br>\n")) {
                    Toast.makeText(context, "Quantity Cannot Be Blank", Toast.LENGTH_SHORT).show();
                }
                if (message.equalsIgnoreCase("The Product field is required.<br>\n")) {
                    Toast.makeText(context, "Product Cannot Be Blank", Toast.LENGTH_SHORT).show();
                }
                if (message.equalsIgnoreCase("The Product field is required.<br>\n")) {
                    Toast.makeText(context, "Product Cannot Be Blank", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void callupdatecartapi(final String cartId, final String productId, final String quantity, final String price) {
        if (!CommonUtilities.isOnline(context)) {
            Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        final CustomProgressDialogue pd= new CustomProgressDialogue(context);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
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
                map.put("productId", productId);
                map.put("quantity", quantity);
                map.put("price", price);

                Log.e("Update Data", cartId + ":" + productId + ":" + quantity + ":" + price);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(context));
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
        VolleySingleton.getInstance(context).addToRequestQueue(sr);
    }

    public static void parseResponse3(String response) {
        Log.e("CO CartItem Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            String message = (String) job.get("message");
            if (data == true) {
                if (message.equalsIgnoreCase("Cart Updated Successfully")) {
                    Toast.makeText(context, "Cart Updated Successfully.", Toast.LENGTH_SHORT).show();
                }
            }
            //Testing need to be changed...
            if (data == false) {
                message = (String) job.get("message");
                Log.e("False", message);
                if (message.equalsIgnoreCase("The Cart Id field is required.<br>\n")) {
                    Toast.makeText(context, "Cart Cannot Be Blank", Toast.LENGTH_SHORT).show();
                }
                if (message.equalsIgnoreCase("Data Not Found.")) {
                    Toast.makeText(context, "Data Not Found", Toast.LENGTH_SHORT).show();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public void refresh() {
//        Intent intent = context.getIntent();
//        context.overridePendingTransition(0, 0);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        context.finish();
//        context.overridePendingTransition(0, 0);
//        context.startActivity(intent);
//    }
}
