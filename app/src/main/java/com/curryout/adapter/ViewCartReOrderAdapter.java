package com.curryout.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.curryout.R;
import com.curryout.model.ShowCartFoodItemModel;
import com.curryout.model.ViewReorderModel;

import java.util.ArrayList;

public class ViewCartReOrderAdapter extends RecyclerView.Adapter<ViewCartReOrderAdapter.ViewHolder>
{

        Context context;
        static ArrayList<ViewReorderModel> alist;
        static int position;

        public ViewCartReOrderAdapter(Context context,ArrayList<ViewReorderModel> alist){
            this.context=context;
            this.alist=alist;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
            View listItem=layoutInflater.inflate(R.layout.product_inflate_in_cart,parent,false);
            ViewHolder viewHolder=new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder,int position){
            ViewReorderModel fm=alist.get(position);
            holder.txtListProduct.setText(fm.getProduct_name());
            holder.txtListQty.setText(fm.getOrdered_item_quantity());
            holder.txtListPrice.setText(fm.getOrdered_item_price());
            holder.linearListener1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent in=new Intent();
                //in.putExtra("T",fm);

            }
        });
            holder.setIsRecyclable(false);

        }

    @Override
    public int getItemCount(){
        return alist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

    public TextView txtListProduct, txtListQty, txtListPrice;
    public LinearLayout linearListener1;

    public ViewHolder(final View itemView) {
        super(itemView);
        this.txtListProduct = (TextView) itemView.findViewById(R.id.txtListProduct);
        this.txtListQty = (TextView) itemView.findViewById(R.id.txtListQty);
        this.txtListPrice = (TextView) itemView.findViewById(R.id.txtListPrice);
        this.linearListener1 = (LinearLayout) itemView.findViewById(R.id.linearListener1);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get position
                int pos = getAdapterPosition();

                // check if item still exists
                if (pos != RecyclerView.NO_POSITION) {
                    ViewReorderModel fm = alist.get(pos);
                    Toast.makeText(v.getContext(), "You clicked " + fm.getProduct_name(), Toast.LENGTH_SHORT).show();
                    ViewCartReOrderAdapter.getPosition(pos);
                }
            }
        });
    }

}

    public static void getPosition(int pos) {
        position = pos;
    }

    public static ViewReorderModel sendPostion() {
        ViewReorderModel fm = alist.get(position);
        return fm;
    }
}







