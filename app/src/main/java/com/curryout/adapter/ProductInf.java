package com.curryout.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.curryout.MainActivity;
import com.curryout.R;
import com.curryout.model.ShowCartFoodItemModel;

import java.util.ArrayList;
import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


public class ProductInf extends RecyclerView.Adapter<ProductInf.ViewHolder> {
    Context context;
    static ArrayList<ShowCartFoodItemModel> alist;
    static int position;
    public ProductInf(Context context, ArrayList<ShowCartFoodItemModel> alist) {
        this.context = context;
        this.alist = alist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.product_inflate_in_cart, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShowCartFoodItemModel fm = alist.get(position);
        holder.txtListProduct.setText(fm.getName());
        holder.txtListQty.setText(fm.getQuantity());
        holder.txtListPrice.setText(fm.getPrice());
        holder.linearListener1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in  = new Intent();
                //in.putExtra("T",fm);

            }
        });
        holder.setIsRecyclable(false);

    }

    @Override
    public int getItemCount() {
        return alist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtListProduct,txtListQty,txtListPrice;
        public LinearLayout linearListener1;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.txtListProduct = (TextView) itemView.findViewById(R.id.txtListProduct);
            this.txtListQty = (TextView) itemView.findViewById(R.id.txtListQty);
            this.txtListPrice = (TextView) itemView.findViewById(R.id.txtListPrice);
            this.linearListener1=(LinearLayout)itemView.findViewById(R.id.linearListener1);


            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // get position
                    int pos = getAdapterPosition();

                    // check if item still exists
                    if(pos != RecyclerView.NO_POSITION){
                        ShowCartFoodItemModel fm = alist.get(pos);
                        Toast.makeText(v.getContext(), "You clicked " + fm.getName(), Toast.LENGTH_SHORT).show();
                        ProductInf.getPosition(pos);
                    }
                }
            });
        }
    }
    public static void getPosition(int pos){
        position = pos;
    }
    public static ShowCartFoodItemModel sendPostion(){
        ShowCartFoodItemModel fm = alist.get(position);
        return fm;
    }
}

/*public class ProductInf extends BaseAdapter {

    Context context;
    ArrayList<ShowCartFoodItemModel> alist;

    public ProductInf(Context context, ArrayList<ShowCartFoodItemModel> alist) {
        this.context = context;
        this.alist = alist;
    }

    @Override
    public int getCount() {
        return alist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShowCartFoodItemModel fm = alist.get(position);
        LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myv = inf.inflate(R.layout.product_inflate_in_cart,null);
        TextView txtListProduct = (TextView) myv.findViewById(R.id.txtListProduct);
        TextView txtListQty = (TextView) myv.findViewById(R.id.txtListQty);
        TextView txtListPrice = (TextView) myv.findViewById(R.id.txtListPrice);
        txtListProduct.setText(fm.getName());
        txtListQty.setText(fm.getQuantity());
        txtListPrice.setText(fm.getPrice());
        return myv;
    }
}*/
