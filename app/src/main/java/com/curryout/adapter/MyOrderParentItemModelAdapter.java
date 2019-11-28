package com.curryout.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.curryout.R;
import com.curryout.model.MyOrderParentItemModel;

import java.util.ArrayList;

public class MyOrderParentItemModelAdapter  extends BaseAdapter {
    ArrayList<MyOrderParentItemModel> alist;
    Context context;;

    public MyOrderParentItemModelAdapter(ArrayList<MyOrderParentItemModel> alist, Context context) {
        this.alist = alist;
        this.context = context;
    }

    @Override
    public int getCount() {
        Log.e("OrderSize",alist.size()+"");
        return alist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyOrderParentItemModel orderParentItemModel = alist.get(position);
        LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myv = inf.inflate(R.layout.my_order_item_layout,null);
        LinearLayout ln_Item = myv.findViewById(R.id.ln_Item);
        TextView txtItem = myv.findViewById(R.id.txtitem);
        TextView txtQty = myv.findViewById(R.id.txtQty);
        txtItem.setText(orderParentItemModel.getProduct_name());
        //txtItem.setText("Hello World");
        Log.e("orderParentItemModel",orderParentItemModel.getProduct_name());
        txtQty.setText(orderParentItemModel.getOrdered_item_quantity());
        Log.e("orderParentItemModelQty",orderParentItemModel.getOrdered_item_quantity());

        txtItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Item Click", Toast.LENGTH_SHORT).show();
            }
        });
        return myv;
    }
}
