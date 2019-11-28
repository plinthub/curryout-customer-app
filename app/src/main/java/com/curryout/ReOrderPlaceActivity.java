package com.curryout;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.curryout.adapter.ViewCartReOrderAdapter;
import com.curryout.appsharedpreference.AppGlobalConstants;
import com.curryout.appsharedpreference.AppSharedPreference;
import com.curryout.appsharedpreference.CommonUtilities;
import com.curryout.model.ShowCartFoodItemModel;
import com.curryout.model.ViewReorderModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReOrderPlaceActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private GoogleMap mMap;

    ImageView back_icon;
    LinearLayout ln_reorder, ln_report,ln_place_reorder;
    TextView txtAddressOrder, txtItemsFrom,
            txtSubTotalView, txtGrandTotalView, txtPaymentM, txtDeliveryFee;

    EditText edInstructionDelivery,edOrderInstruction;
    RecyclerView myListViewOrder;
    ArrayList<ViewReorderModel> alist;
    ViewCartReOrderAdapter adapter;
    String getOID;
    String user_address_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reorder_place);
        getOID = getIntent().getStringExtra("OrderIDView");
        Log.e("OID",getOID);
        init();
        listener();

        callGetOrderDetailById(getOID);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        displayLocationSettingsRequest(ReOrderPlaceActivity.this);
    }

    private void init() {

        back_icon = (ImageView) findViewById(R.id.back_icon);
        //ln_reorder = (LinearLayout) findViewById(R.id.ln_reorder);
        //ln_report = (LinearLayout) findViewById(R.id.ln_report);
        ln_place_reorder = findViewById(R.id.ln_place_reorder);
        txtAddressOrder = findViewById(R.id.txtAddressOrder);
        edInstructionDelivery = findViewById(R.id.edInstructionDelivery);
        txtItemsFrom = findViewById(R.id.txtItemsFrom);
        edOrderInstruction = findViewById(R.id.edOrderInstruction);
        txtSubTotalView = findViewById(R.id.txtSubTotalView);
        txtGrandTotalView = findViewById(R.id.txtGrandTotalView);
        txtPaymentM = findViewById(R.id.txtPaymentM);
        txtDeliveryFee = findViewById(R.id.txtDeliveryFee);
        myListViewOrder = findViewById(R.id.myListViewOrder);
        myListViewOrder.setHasFixedSize(true);
        myListViewOrder.setLayoutManager(new LinearLayoutManager(ReOrderPlaceActivity.this));
        alist = new ArrayList<>();
    }

    private void listener() {

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ln_place_reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    callPlaceOrderApi();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            });
    }


    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("TAG", "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("TAG", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(ReOrderPlaceActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("TAG", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("TAG", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
//Showing Current Location Marker on Map
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location locations = locationManager.getLastKnownLocation(provider);
        List<String> providerList = locationManager.getAllProviders();
        if (null != locations && null != providerList && providerList.size() > 0) {
            double longitude = locations.getLongitude();
            double latitude = locations.getLatitude();
            Geocoder geocoder = new Geocoder(getApplicationContext(),
                    Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latitude,
                        longitude, 1);
                if (null != listAddresses && listAddresses.size() > 0) {
                    String state = listAddresses.get(0).getAdminArea();
                    String country = listAddresses.get(0).getCountryName();
                    String subLocality = listAddresses.get(0).getSubLocality();
                    markerOptions.title("" + latLng + "," + subLocality + "," + state
                            + "," + country);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                    this);
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission denied",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public void callGetOrderDetailById(final String OrderId) {

        if (!CommonUtilities.isOnline(ReOrderPlaceActivity.this)) {
            Toast.makeText(ReOrderPlaceActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        final CustomProgressDialogue pd = new CustomProgressDialogue(ReOrderPlaceActivity.this);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();
        Log.e("OrderId in Call", OrderId);
        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "order/getOrderDetailsById";

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
                HashMap<String, String> map = new HashMap<>();
                map.put("orderId", OrderId);
                //map.put("orderId", "1");
                return map;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(ReOrderPlaceActivity.this));
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
        VolleySingleton.getInstance(ReOrderPlaceActivity.this).addToRequestQueue(sr);
    }

    public void parseResponse(String response) {
        Log.e("CurryOut View Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            if (data == true) {
                JSONObject obj = job.getJSONObject("data");
                String orderID = obj.getString("orderID");
                user_address_id = obj.getString("user_address_id");
                String customer_id = obj.getString("customer_id");
                String instruction_to_delivery_boy = obj.getString("instruction_to_delivery_boy");
                String instruction_for_order = obj.getString("instruction_for_order");
                String sub_total = obj.getString("sub_total");
                String delivery_fee = obj.getString("delivery_fee");
                String discount = obj.getString("discount");
                String total = obj.getString("total");
                String payment_method_id = obj.getString("payment_method_id");
                String order_datetime = obj.getString("order_datetime");
                String houseno = obj.getString("houseno");
                String street = obj.getString("street");
                String landmark = obj.getString("landmark");
                String city = obj.getString("city");
                String state = obj.getString("state");
                String country = obj.getString("country");
                String postalCode = obj.getString("postalCode");
                String payment_method = obj.getString("payment_method");
                String order_status = obj.getString("order_status");
                String mobile = obj.getString("mobile");
                String customer_name = obj.getString("customer_name");

                JSONArray order_item_details = obj.getJSONArray("order_item_details");
                String restraunt_name="";
                for (int x = 0; x < order_item_details.length(); x++) {
                    JSONObject objj = order_item_details.getJSONObject(x);
                    String ordered_product_id = objj.getString("ordered_product_id");
                    String ordered_item_quantity = objj.getString("ordered_item_quantity");
                    String ordered_item_price = objj.getString("ordered_item_price");
                    String product_name = objj.getString("product_name");
                    String food_type = objj.getString("food_type");
                    String category = objj.getString("category");
                    restraunt_name = objj.getString("restraunt_name");
                    String restraunt_image = objj.getString("restraunt_image");
                    String sub_category = objj.getString("sub_category");
                    alist.add(new ViewReorderModel(ordered_item_quantity, ordered_item_price, product_name,ordered_product_id));
                }
                adapter = new ViewCartReOrderAdapter(ReOrderPlaceActivity.this, alist);
                myListViewOrder.setAdapter(adapter);
                txtAddressOrder.setText(houseno + " " + street + " " + landmark + " " + city + " " + state + " " + country + " " + postalCode);
                edInstructionDelivery.setText(instruction_to_delivery_boy);
                txtItemsFrom.setText(restraunt_name);
                edOrderInstruction.setText(instruction_for_order);
                txtSubTotalView.setText(sub_total);
                txtGrandTotalView.setText(total);
                txtPaymentM.setText(payment_method);
                txtDeliveryFee.setText(delivery_fee);


//                Toast.makeText(YourOrderActivity.this, "User Name Updated Successfully.", Toast.LENGTH_LONG).show();
//                startActivity(new Intent(YourOrderActivity.this, AddressActivity.class));
            } else {
//                Toast.makeText(YourOrderActivity.this, "The Name field is required.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callPlaceOrderApi() throws JSONException {
        if (!CommonUtilities.isOnline(ReOrderPlaceActivity.this)) {
            Toast.makeText(ReOrderPlaceActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        final CustomProgressDialogue pd= new CustomProgressDialogue(ReOrderPlaceActivity.this);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "order/placeOrder";

        JSONObject map = new JSONObject();
        String mRequestBody = "";
        try {
            map.put("user_addressId", user_address_id);
            map.put("instruction_to_delivery_boy", edInstructionDelivery.getText().toString());
            map.put("instruction_for_order", edOrderInstruction.getText().toString());
            map.put("sub_total", txtSubTotalView.getText() + "");
            map.put("delivery_fee", txtDeliveryFee.getText().toString());
            //map.put("delivery_fee", "0");
            //map.put("promo_code", txtPromoCode.getText().toString());
            map.put("promo_code", "Discount0");
            //map.put("discount", txtDeliveryFee.getText().toString());
            map.put("discount", "0");
            map.put("total", txtGrandTotalView.getText().toString());
            //map.put("payment_method", payId);
            map.put("payment_method", "1");



            //ShowCartFoodItemModel scfm = getListData();
            //ShowCartFoodItemModel scfm=new ShowCartFoodItemModel();

            ViewReorderModel vm = new ViewReorderModel();
            JSONArray jarr = new JSONArray();
            JSONObject ob=null;
            Log.e("Alist Size",alist.size()+"");
            for(int i=0;i<alist.size();i++){
                vm = alist.get(i);
                ob = new JSONObject();
                ob.put("product_id",vm.getOrdered_product_id());
                ob.put("quantity" , vm.getOrdered_item_quantity());
                ob.put("price",vm.getOrdered_item_price());
                jarr.put(ob);
                map.put("items",jarr);
            }
            JSONArray items = jarr;
            Log.e("ITEMSArray",items+"");

            Log.e("Map",jarr+"");
            mRequestBody = map.toString();

            Log.e("PlaceOrderData", user_address_id + ":" +
                    edInstructionDelivery.getText().toString() + ":" +
                    edOrderInstruction.getText().toString() + ":" +
                    txtSubTotalView.getText().toString() + ":" +
                    //txtDeliveryFee.getText().toString() + ":" +
                    //txtPromoCode.getText().toString() + ":" +
                    txtGrandTotalView.getText().toString() + ":" +
                    txtPaymentM.getText().toString() + ":" +
                    vm.getOrdered_product_id() + ":" + vm.getOrdered_item_quantity() + vm.getOrdered_item_price()+ map
            );


        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String finalMRequestBody = mRequestBody;
        //Toast.makeText(this, ""+finalMRequestBody, Toast.LENGTH_SHORT).show();
        Log.e("JSONData",map+"");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, map,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pd.dismiss();
                        String res = response.toString();
                        parseResponse2(res);
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(ReOrderPlaceActivity.this));
                return header;
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            public byte[] getBody()  {
                try {
                    return finalMRequestBody == null ? null : finalMRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", finalMRequestBody, "utf-8");
                    return null;
                }
            }

        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                AppGlobalConstants.WEBSERVICE_TIMEOUT_VALUE_IN_MILLIS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        jsonObjReq.setShouldCache(false);
        VolleySingleton.getInstance(ReOrderPlaceActivity.this).addToRequestQueue(jsonObjReq);
    }

    public void parseResponse2(String response){
        Log.e("CO PlaceOrder Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            String message = job.getString("message");
            if (data == true) {
                if (message.equalsIgnoreCase("Order Placed Successfully.")) {
                    Toast.makeText(ReOrderPlaceActivity.this, "Order Placed Successfully.", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(ReOrderPlaceActivity.this,MainActivity.class);
                    startActivity(in);
                    finish();
                }
            }
            if(data==false){
                if (message.equalsIgnoreCase("The Payment Method field is required.<br>\n")) {
                    Toast.makeText(ReOrderPlaceActivity.this, "The Payment Method field is required.", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
