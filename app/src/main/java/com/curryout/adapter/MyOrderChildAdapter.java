package com.curryout.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.curryout.R;
import com.curryout.model.MyOrderParentItemModel;
import com.curryout.model.ShowCartFoodItemModel;

import java.util.ArrayList;

public class MyOrderChildAdapter extends RecyclerView.Adapter<MyOrderChildAdapter.ViewHolder> {

    static ArrayList<MyOrderParentItemModel> alist;
    Context context;;
    static int position;
    public MyOrderChildAdapter(ArrayList<MyOrderParentItemModel> alist, Context context) {
        this.alist = alist;
        this.context = context;
    }
    @NonNull
    @Override
    public MyOrderChildAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.my_order_item_layout, parent, false);
        MyOrderChildAdapter.ViewHolder viewHolder = new MyOrderChildAdapter.ViewHolder(listItem);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyOrderChildAdapter.ViewHolder holder, int position) {
        MyOrderParentItemModel fm = alist.get(position);
        holder.txtItem.setText(fm.getProduct_name());
        holder.txtQty.setText(fm.getOrdered_item_quantity());
    }

    @Override
    public int getItemCount() {
        Log.e("MyOrderChild",alist.size()+"");
        return alist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtItem, txtQty;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.txtItem = (TextView) itemView.findViewById(R.id.txtitem);
            this.txtQty = (TextView) itemView.findViewById(R.id.txtQty);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get position
                    int pos = getAdapterPosition();

                    // check if item still exists
                    if (pos != RecyclerView.NO_POSITION) {

                    }
                }
            });
        }
    }
}
