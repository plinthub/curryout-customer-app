package com.curryout.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.curryout.CartActivity;
import com.curryout.CustomLinearLayoutManager;
import com.curryout.MainActivity;
import com.curryout.MyOrdersFragment;
import com.curryout.R;
import com.curryout.ReOrderPlaceActivity;
import com.curryout.ReportActivity;
import com.curryout.ReportFragment;
import com.curryout.SingleItemRestuFragment;
import com.curryout.VerificationActivity;
import com.curryout.VolleySingleton;
import com.curryout.WelcomePhoneActivity;
import com.curryout.YourOrderActivity;
import com.curryout.appsharedpreference.AppGlobalConstants;
import com.curryout.appsharedpreference.AppSharedPreference;
import com.curryout.appsharedpreference.CommonUtilities;
import com.curryout.model.MyOrderParentItemModel;
import com.curryout.model.MyOrdersParentDM;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyOrdersListAdapter extends RecyclerView.Adapter<MyOrdersListAdapter.ViewHolder> {

    static private ArrayList<MyOrdersParentDM> listData;
    static private ArrayList<MyOrderParentItemModel> alist;

    static Context context;
    //static ListView textList;
    AppCompatActivity mContext;
    static MyOrdersParentDM myOrdersListData;
    //static MyOrderParentItemModelAdapter adap;

    public MyOrdersListAdapter(ArrayList<MyOrdersParentDM> listData,Context ctx) {
        this.listData = listData;
        context = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.my_order_listlayout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        myOrdersListData = listData.get(position);
        holder.txtRestName.setText(myOrdersListData.getRestName());
        holder.txtFoodName.setText(myOrdersListData.getFoodName());
        Glide.with(context).load(Uri.parse(myOrdersListData.getRestImg()))
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);
        Log.e("Position",position+"");
        Log.e("DataIDOut",myOrdersListData.getOrderId());
        Log.e("DataTestOUT",myOrdersListData.getRestName());
        //RecyclerView.LayoutManager layoutManager = new CustomLinearLayoutManager(context);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        holder.myOrderRecycleTest.setLayoutManager(layoutManager);
        //layoutManager.setAutoMeasureEnabled(true);
        //alist = new ArrayList<>();
        //alist = MyOrdersFragment.sendData();
        MyOrderChildAdapter adap = new MyOrderChildAdapter(myOrdersListData.getAlist(),context);
        holder.myOrderRecycleTest.setAdapter(adap);


        String oid = myOrdersListData.getOrderId();
        holder.ln_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"Test",Toast.LENGTH_LONG).show();
                Log.e("DataTestIn",myOrdersListData.getRestName());
                Log.e("DataIDIn",myOrdersListData.getOrderId());
                Intent i= new Intent(context, YourOrderActivity.class);
                    i.putExtra("OrderIDView",oid);
                    Log.e("OrderIDViewFragment",oid);
                    context.startActivity(i);
            }
        });

        holder.ln_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String oid = myOrdersListData.getOrderId();
                Intent i= new Intent(context, ReportActivity.class);
                i.putExtra("OrderIDView",oid);
                context.startActivity(i);

            }
        });
        holder.ln_reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String oid = myOrdersListData.getOrderId();
                //Log.e("OID for Re-order",oid);
                Intent i= new Intent(context, ReOrderPlaceActivity.class);
                i.putExtra("OrderIDView",oid);
                Log.e("OrderIDViewFragmentReOrder",oid);
                context.startActivity(i);


                //callConfirmDialog(oid);
                //Toast.makeText(itemView.getContext(), "Re-order", Toast.LENGTH_SHORT).show();
            }
        });




        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        //Context context;
        public ImageView imageView, fav_icon, fav_filled_icon;
        public TextView txtRestName, txtFoodName;
        public LinearLayout linearLayout, ln_view, ln_reorder, ln_report;
        public RecyclerView myOrderRecycleTest;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.img_Restaurant);
            this.txtRestName = (TextView) itemView.findViewById(R.id.txt_RestaurantsName);
            this.txtFoodName = (TextView) itemView.findViewById(R.id.txt_FoodName);
            myOrderRecycleTest = itemView.findViewById(R.id.myOrderRecycleTest);
            myOrderRecycleTest.setHasFixedSize(true);
            //ArrayList<MyOrderParentItemModel> s=new ArrayList();
            //ArrayAdapter<String> ada = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,s);
//            alist = new ArrayList<MyOrderParentItemModel>();
//            alist = MyOrdersFragment.sendData();
//            //s.add(new MyOrderParentItemModel("R","20"));
//            MyOrderParentItemModelAdapter adap = new MyOrderParentItemModelAdapter(alist,context);
//            textList.setAdapter(adap);
//            adap.notifyDataSetChanged();


//            alist = new ArrayList<MyOrderParentItemModel>();
//            alist = MyOrdersFragment.sendData();
//            adap = MyOrdersFragment.sendAdap();
//            //adap = new MyOrderParentItemModelAdapter(alist,context);

//            textList.setAdapter(adap);
//            adap.notifyDataSetChanged();
            //adap.notifyDataSetInvalidated();



            ln_view =(LinearLayout)itemView.findViewById(R.id.ln_view);
            ln_reorder = (LinearLayout)itemView.findViewById(R.id.ln_reorder);
            ln_report = (LinearLayout)itemView.findViewById(R.id.ln_report);

            fav_icon = (ImageView) itemView.findViewById(R.id.fav_icon);
            fav_filled_icon = (ImageView) itemView.findViewById(R.id.fav_filled_icon);

//            int p = getAdapterPosition();
//            Log.e("Position in VH",p+"");
//            try {
//                MyOrdersParentDM demo = listData.get(p);
//                Log.e("DemoID", demo.getOrderId());
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            ln_view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    Toast.makeText(itemView.getContext(), "View", Toast.LENGTH_SHORT).show();
//                    Intent i= new Intent(context, YourOrderActivity.class);
//                    i.putExtra("OrderIDView",myOrdersListData.getOrderId());
//                    Log.e("OrderIDViewFragment",myOrdersListData.getOrderId());
//                    context.startActivity(i);
////                    FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
////
////                    YourOrderDialogFragment yourOrderDialogFragment= new YourOrderDialogFragment();
////
////                    yourOrderDialogFragment.show(manager, "Your Order Fragment");
//
//                }
//            });



//            ln_report.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String oid = myOrdersListData.getOrderId();
//                    Intent i= new Intent(context, ReportActivity.class);
//                    context.startActivity(i);
//
//
//
////                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
////                    Fragment myFragment = new ReportFragment(oid);
////                    //bun.putString("RID",rid);
////                    //myFragment.setArguments(bun);
////                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
//
//
//                }
//            });
//            ln_reorder.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //String oid = myOrdersListData.getOrderId();
//                    //Log.e("OID for Re-order",oid);
//                    Intent i= new Intent(context, ReOrderPlaceActivity.class);
//                    i.putExtra("OrderIDView",myOrdersListData.getOrderId());
//                    Log.e("OrderIDViewFragment",myOrdersListData.getOrderId());
//                    context.startActivity(i);
//
//
//                    //callConfirmDialog(oid);
//                    //Toast.makeText(itemView.getContext(), "Re-order", Toast.LENGTH_SHORT).show();
//                }
//            });
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


        //public  ListView returnId(){ return textList; }
    }


    public static void callReOrderApi(final String oid){
        if (!CommonUtilities.isOnline(context)) {
            Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }


        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "order/reOrder";

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponseReOrder(response);
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

                        parseResponseReOrder(response.toString());

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("order_id",oid);
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

    public static void parseResponseReOrder(String response){
        Log.e("CO Reorder Response", "response " + response);
        try{
            JSONObject job = new JSONObject(response);
            boolean data = (boolean)job.get("status");
            String message="";
            if(data==true){
                message = job.getString("message");
                if(message.equals("Order Placed Successfully."))
                Toast.makeText(context,"Order Placed Successfully.",Toast.LENGTH_LONG).show();
                Intent in = new Intent(context,MainActivity.class);
                context.startActivity(in);


            }
            else{
                Toast.makeText(context,"Something Went Wrong.",Toast.LENGTH_LONG).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void callConfirmDialog(final String oid){

        final Dialog dialog = new Dialog(context, R.style.FullScreenDialogStyleTransparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.confirm_dialog_layout);
        dialog.show();


        LinearLayout ln_outside=(LinearLayout)dialog.findViewById(R.id.ln_outside);
        ImageView cancel=(ImageView)dialog.findViewById(R.id.img_cancel);
        LinearLayout ln_Yes=(LinearLayout)dialog.findViewById(R.id.ln_Yes);
        LinearLayout ln_No=(LinearLayout)dialog.findViewById(R.id.ln_No);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ln_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callReOrderApi(oid);

            }
        });

        ln_No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ln_outside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

//    public static class YourOrderDialogFragment extends DialogFragment {
//        public static YourOrderDialogFragment newInstance(int myIndex) {
//
//            YourOrderDialogFragment yourOrderDialogFragment= new YourOrderDialogFragment();
//
//            return yourOrderDialogFragment;
//        }
//
//
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
//        }
//
//        @Override
//        public void onStart() {
//            super.onStart();
//            Dialog d = getDialog();
//            if (d!=null){
//                int width = ViewGroup.LayoutParams.MATCH_PARENT;
//                int height = ViewGroup.LayoutParams.MATCH_PARENT;
//                d.getWindow().setLayout(width, height);
//            }
//        }
//
//        ImageView back_icon;
//
//        @Nullable
//        @Override
//        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//            View view= inflater.inflate(R.layout.activity_your_order, null);
//
//
//            init(view);
//            listener();
//
//
//            return view;
//        }
//
//        private void listener() {
//
//            back_icon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                }
//            });
//        }
//
//        private void init(View view) {
//
//            back_icon=(ImageView)view.findViewById(R.id.back_icon);
//
//        }
//    }
}




