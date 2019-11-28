package com.curryout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArraySet;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.curryout.appsharedpreference.AppGlobalConstants;
import com.curryout.appsharedpreference.AppSharedPreference;
import com.curryout.appsharedpreference.CommonUtilities;
import com.curryout.model.RestaurantDataModel;
import com.curryout.model.ShowCartFoodItemModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RestaurantListFragment extends Fragment {

    boolean check_ScrollingUp = false;
    ArrayList<RestaurantDataModel> alist;
    RecyclerView recyclerView;
    RestaurantListAdapter recyAdapter;
    RestaurantDataModel restaurantDataModel;
    String cid;
    LinearLayout linearViewCartResturant;
    TextView txtViewDetailResturant,txtSearchHome;
    ImageView search_iconRes;
    ArrayList<ShowCartFoodItemModel> cartproduct;
    public RestaurantListFragment() {

    }
    public RestaurantListFragment(String cid) {
        this.cid = cid;
        Log.e("Cusine_Id",cid);
        Log.e("RestaurantListFragment","RestaurantListFragment");
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.restaurant_list_fragment, container, false);

        init(view);
        listener(view);

        final RelativeLayout rl_toolbar = (RelativeLayout) view.findViewById(R.id.rl_toolbar);
        alist = new ArrayList<>();

        callGetAllRestaurants();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyAdapter = new RestaurantListAdapter(alist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        recyclerView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (recyclerView != null) {
                    if (recyclerView.getChildAt(0).getBottom() <= (recyclerView.getHeight() + recyclerView.getScrollY())) {
                        rl_toolbar.setVisibility(View.VISIBLE);
                    } else {
                        rl_toolbar.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        return view;
    }

    private void init(View view) {
        linearViewCartResturant = view.findViewById(R.id.linearViewCartResturant);
        txtSearchHome = view.findViewById(R.id.txtSearchHome);
        search_iconRes = view.findViewById(R.id.search_iconRes);
//        txtViewDetailResturant = view.findViewById(R.id.txtViewDetailResturant);
        linearViewCartResturant.setVisibility(View.GONE);
       callgetCartItemList();
        cartproduct = new ArrayList<>();
    }

    private void listener(View view) {

        search_iconRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = txtSearchHome.getText().toString();
                Intent in = new Intent(getActivity(),SearchActivity.class);
                in.putExtra("SearchData",data);
                startActivity(in);
            }
        });
    }

    public List<ImageSliderObject> getTestData() {
        List<ImageSliderObject> mTestData = new ArrayList<ImageSliderObject>();
        mTestData.add(new ImageSliderObject(R.drawable.fiftypercent_img));
        mTestData.add(new ImageSliderObject(R.drawable.fiftypercent_img));
        mTestData.add(new ImageSliderObject(R.drawable.fiftypercent_img));
        return mTestData;
    }

    @Override
    public void onResume() {
        super.onResume();
//        handler.postDelayed(runnable, delay);
    }

    @Override
    public void onPause() {
        super.onPause();
//        handler.removeCallbacks(runnable);
    }

    public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {

        private ArrayList<RestaurantDataModel> listdata;

        public RestaurantListAdapter(ArrayList<RestaurantDataModel> listdata) {
            this.listdata = listdata;
        }

        @NonNull
        @Override
        public RestaurantListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.resturant_listitem_layout, parent, false);
            RestaurantListAdapter.ViewHolder viewHolder = new RestaurantListAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final RestaurantListAdapter.ViewHolder holder, int position) {
            final RestaurantDataModel restauListData = alist.get(position);
            holder.txtRestauName.setText(restauListData.getRestauName());
            holder.txtFoodName.setText(restauListData.getFoodName());
            holder.txtAddress.setText(restauListData.getAddress());
            //holder.img_Restaurant.setImageResource(restauListData.getImgRestaurant());

            Glide.with(getActivity()).load(Uri.parse(restauListData.getImgRestauran()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img_Restaurant);


            String ft = restauListData.getFoodType();
            Log.e("FOODHOME",ft);
            String test[] = ft.split(",");
            try {
                for (int i = 0; i < test.length; i++) {
                    Log.e("TESTFT:---" + i, test[i]);

                if (test[0].equalsIgnoreCase("Veg") || test[1].equalsIgnoreCase("Veg")) {
                    holder.imghomeft.setImageResource(R.drawable.veg_green_icon);
                }
                if (test[1].trim().equalsIgnoreCase("Non-Veg") || test[1].equalsIgnoreCase("VegNon-VegNon-Veg") || test[1].equalsIgnoreCase("VegNon-Veg")) {
                    holder.imghomeft.setImageResource(R.drawable.non_veg_icon);
                }}
            }catch (Exception e){
                e.printStackTrace();
            }
            Log.e("RES",restauListData.getRestrauntID());
            final String rid = restauListData.getRestrauntID();
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Fragment myFragment = new SingleItemRestuFragment(rid,cartproduct);
//                    Bundle bun = new Bundle();
//                    bun.putString("RID","1");
//                    myFragment.setArguments(bun);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
                }
            });

        }

        @Override
        public int getItemCount() {
            return alist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView img_Restaurant, fav_icon, fav_filled_icon,imghomeft;
            public TextView txtRestauName, txtFoodName, txtAddress;
            public LinearLayout linearLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                this.txtRestauName = (TextView) itemView.findViewById(R.id.txt_RestaurantsName);
                this.txtFoodName = (TextView) itemView.findViewById(R.id.txt_FoodName);
                this.txtAddress = (TextView) itemView.findViewById(R.id.txt_Address);
                this.img_Restaurant = (ImageView) itemView.findViewById(R.id.img_Restaurant);
                this.imghomeft = itemView.findViewById(R.id.imghomeft);
                fav_icon = (ImageView) itemView.findViewById(R.id.fav_icon);
                fav_filled_icon = (ImageView) itemView.findViewById(R.id.fav_filled_icon);
                linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);

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
                        fav_filled_icon.setVisibility(View.GONE);
                        fav_icon.setVisibility(View.VISIBLE);
                    }
                });


            }
        }
    }

    public void callGetAllRestaurants() {
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

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "restraunt/getAllRestrauntByCategory";
        //String testurl = "http://www.mocky.io/v2/5cc4019f3400006500765495";
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
                map.put("category",cid);
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
        Log.e("CO Cuisi_ID Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            if (data == true) {
                JSONArray jarr = job.getJSONArray("data");

                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject jobj = jarr.getJSONObject(i);

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
                    String cuisine_name = jobj.getString("cuisine_name");
                    String url="";
                    JSONArray jFaceShape = jobj.optJSONArray("cuisine_name");
                    for(int jj=0; jj<jFaceShape.length(); jj++)
                    {
                        url += (String) jFaceShape.get(jj) +" | ";
                        Log.d("CN", "url => " + url);
                    }
                    String st = removeLastChar(url);
                    Log.e("RD", restrauntID);
                    Log.e("RD1", name);
                    Log.e("RD2", food_type);
                    Log.e("RD3", st);
                    restaurantDataModel = new RestaurantDataModel(restrauntID,name, st, address,image);
                    restaurantDataModel.setFoodType(food_type);
                    alist.add(restaurantDataModel);
                }
            }

            recyclerView.setAdapter(recyAdapter);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 2);
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

//            if(cartarr.length()==0){
//                linearViewCartResturant.setVisibility(View.GONE);
//            }
//            else{
//                linearViewCartResturant.setVisibility(View.VISIBLE);
//                txtViewDetailResturant.setText("View Items: "+cartarr.length()+"");
//                linearViewCartResturant.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent in = new Intent(getActivity(),CartActivity.class);
//                        startActivity(in);
//                    }
//                });
//            }

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

                ShowCartFoodItemModel scm = new ShowCartFoodItemModel(ItemListproductID,ItemListcartquantity,name,UpCartID);
                Log.e("IDCart",scm.getCart_id());
                Log.e("IDCart",scm.getQuantity());
                Log.e("IDCart",scm.getName());
                cartproduct.add(scm);


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


}

