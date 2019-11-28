package com.curryout.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.curryout.R;
import com.curryout.SingleFoodDetailsActivity;
import com.curryout.SingleItemRestuActivity;
import com.curryout.model.SearchFoodItemModel;
import com.curryout.model.ShowCartFoodItemModel;

import java.util.ArrayList;

public class SearchFootItemName extends RecyclerView.Adapter<SearchFootItemName.ViewHolder>{

    static Context context;
    static ArrayList<SearchFoodItemModel> alist;
    static int position;
    static SearchFoodItemModel fm;
    public SearchFootItemName(Context context, ArrayList<SearchFoodItemModel> alist) {
        this.context = context;
        this.alist = alist;
    }

    @NonNull
    @Override
    public SearchFootItemName.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.search_fooditem_layout, parent, false);
        SearchFootItemName.ViewHolder viewHolder = new SearchFootItemName.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchFootItemName.ViewHolder holder, int position) {
        fm = alist.get(position);
        try {
            holder.txtSearchFoodName.setText(fm.getFoodname());
            holder.txtSearchFoodTime.setText(fm.getFoodtime());
            holder.txtSearchFoodPrice.setText(fm.getFoodprice());
            holder.txtSearchFoodRestName.setText(fm.getFoodrestName());
            Glide.with(context).load(Uri.parse(fm.getStringImg()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.itemImage);
            Glide.with(context).resumeRequests();
            holder.setIsRecyclable(false);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return alist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtSearchFoodName,txtSearchFoodTime,txtSearchFoodPrice,txtSearchFoodRestName;

        public ImageView itemImage;
        public ViewHolder(final View itemView) {
            super(itemView);
            this.itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
            this.txtSearchFoodName = (TextView) itemView.findViewById(R.id.txtSearchFoodName);
            this.txtSearchFoodTime = (TextView) itemView.findViewById(R.id.txtSearchFoodTime);
            this.txtSearchFoodPrice = (TextView) itemView.findViewById(R.id.txtSearchFoodPrice);
            this.txtSearchFoodRestName = (TextView) itemView.findViewById(R.id.txtSearchFoodRestName);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    // check if item still exists
                    if(pos != RecyclerView.NO_POSITION){
                        fm = alist.get(pos);
                        String pid = fm.getPid();
                        Log.e("PID in Adapter",pid);
                        Intent in = new Intent(context, SingleFoodDetailsActivity.class);
                        in.putExtra("PID",pid);
                        in.putExtra("PRICE",fm.getFoodprice());
                        context.startActivity(in);
                    }
                }
            });
        }

    }
}
