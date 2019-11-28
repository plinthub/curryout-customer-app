package com.curryout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.curryout.appsharedpreference.AppGlobalConstants;
import com.curryout.appsharedpreference.AppSharedPreference;
import com.curryout.appsharedpreference.CommonUtilities;
import com.curryout.model.PaymentItemModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.curryout.SearchGridListAdapter.context;

public class PaymentMethodActivity extends AppCompatActivity {

    String[] ItemName;
    List<PaymentItemModel> rowItem;
    ListView list;
    CustomListActivity adapter;
    ImageView back_icon;
    LinearLayout ln_save;
    String getName="",getPayId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);


        init();
        listener();

        rowItem = new ArrayList<PaymentItemModel>();
        ItemName = getResources().getStringArray(R.array.name);

//        for(int i = 0 ; i < rowItem.length() ; i++)
//        {
//            Items itm = new Items(ItemName[i]);
//            rowItem.add(itm);
//        }
        for(int i=0;i<rowItem.size();i++){
            PaymentItemModel pit = rowItem.get(i);
            rowItem.add(pit);
        }
        list = (ListView) findViewById(R.id.lvCheckBox);
        adapter = new CustomListActivity(this, rowItem);

    }

    private void init() {

        back_icon=(ImageView)findViewById(R.id.back_icon);
        ln_save=(LinearLayout)findViewById(R.id.ln_save);
        callgGetAllPaymentMethods();
    }

    private void listener() {

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ln_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

    }

    private class CustomListActivity  extends BaseAdapter {

        Context context;
        List<PaymentItemModel> rowItem;
        View listView;
        boolean checkState[];
        int selectedPosition=-1;
        ViewHolder holder;

        public CustomListActivity(Context context, List<PaymentItemModel> rowItem) {

            this.context = context;
            this.rowItem = rowItem;
            checkState = new boolean[rowItem.size()];

        }

        @Override
        public int getCount() {
            return rowItem.size();
        }

        @Override
        public Object getItem(int position) {

            return rowItem.get(position);

        }

        @Override
        public long getItemId(int position) {

            return rowItem.indexOf(getItem(position));

        }

        public class ViewHolder {
            TextView tvItemName;
            CheckBox check;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {


            holder = new ViewHolder();
            final PaymentItemModel itm = rowItem.get(position);
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (view == null) {

                listView = new View(context);
                listView = layoutInflater.inflate(R.layout.payment_method_listitem,
                        parent, false);

                holder.tvItemName = (TextView) listView
                        .findViewById(R.id.txt_payName);
                holder.check = (CheckBox) listView.findViewById(R.id.checkbox);
                listView.setTag(holder);

            } else {
                listView = (View) view;
                holder = (ViewHolder) listView.getTag();

            }

            holder.tvItemName.setText(itm.getItems());

            holder.check.setText("hhhh");

            if (position == selectedPosition) {
                holder.check.setChecked(true);
            } else holder.check.setChecked(false);

            holder.check.setOnClickListener(onStateChangedListener(holder.check, position));



//            holder.check.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//
//                    PaymentItemModel pp = rowItem.get(position);
//                    getName = pp.getItems();
//                    for(int i=0;i<checkState.length;i++)
//                    {
//                        if(i==position)
//                        {
//                            checkState[i]=true;
//                            Toast.makeText(context, "True"+getName, Toast.LENGTH_SHORT).show();
//                        }
//                        else
//                        {
//                            checkState[i]=false;
//                            Toast.makeText(context, "False"+getName, Toast.LENGTH_SHORT).show();
//                        }
//                   }
//
//
//
//                    notifyDataSetChanged();
//
//                }
//            });
            return listView;
        }

        private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        selectedPosition = position;
                        PaymentItemModel pp = rowItem.get(position);
                        getName = pp.getItems();
                        getPayId = pp.getPay_id();
                        //Toast.makeText(context, ""+getName+":"+getPayId, Toast.LENGTH_SHORT).show();
                    } else {
                        selectedPosition = -1;
                    }
                    notifyDataSetChanged();
                }
            };
        }
    }




    public void callgGetAllPaymentMethods(){

        if (!CommonUtilities.isOnline(PaymentMethodActivity.this)) {
            Toast.makeText(PaymentMethodActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

//        final ProgressDialog pd = new ProgressDialog(PaymentMethodActivity.this);
//        pd.setTitle("Please Wait");
//        pd.setCancelable(false);
//        pd.show();

        final CustomProgressDialogue pd= new CustomProgressDialogue(PaymentMethodActivity.this);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "common/getAllPaymentMethods";

        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                parseResponsePayment(response);
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

                        parseResponsePayment(response.toString());

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
                header.put("Content-Type", "application/json");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(PaymentMethodActivity.this));
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
        VolleySingleton.getInstance(PaymentMethodActivity.this).addToRequestQueue(sr);
    }

    public void parseResponsePayment(String response){
        Log.e("CO Payment Response", "response " + response);
        try{
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            JSONArray jarr = job.getJSONArray("data");
            if (data == true) {
                for(int i=0;i<jarr.length();i++){

                JSONObject obj = jarr.getJSONObject(i);
                    String payment_methodID = obj.getString("payment_methodID");
                    String name = obj.getString("name");
                    String status = obj.getString("status");

                    PaymentItemModel pim = new PaymentItemModel(payment_methodID,name);
                    rowItem.add(pim);

                }}
            list.setAdapter(adapter);





//            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                }
//            });


        }catch (Exception e){}
    }


    public void onExit(String str){

    }

    @Override
    public void finish() {
        Intent in = new Intent();
        in.putExtra("PayItem",getName);
        in.putExtra("PayItemID",getPayId);
        setResult(RESULT_OK,in);
        super.finish();
    }
}
