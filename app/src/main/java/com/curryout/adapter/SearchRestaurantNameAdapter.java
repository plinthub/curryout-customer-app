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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.curryout.R;
import com.curryout.SingleItemRestuActivity;
import com.curryout.model.SearchFoodItemModel;
import com.curryout.model.SearchRestaurantNameModel;
import com.curryout.model.ShowCartFoodItemModel;

import java.util.ArrayList;
import java.util.Collection;

public class SearchRestaurantNameAdapter extends RecyclerView.Adapter<SearchRestaurantNameAdapter.ViewHolder> {

    static Context context;
    static ArrayList<SearchRestaurantNameModel> alist;
    static SearchRestaurantNameModel fm;
    public SearchRestaurantNameAdapter(Context context, ArrayList<SearchRestaurantNameModel> alist) {
        this.context = context;
        this.alist = alist;
    }

    @NonNull
    @Override
    public SearchRestaurantNameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.search_restaurantsname_layout, parent, false);
        SearchRestaurantNameAdapter.ViewHolder viewHolder = new SearchRestaurantNameAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    public void clearApplications() {
        int size = alist.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                alist.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void addApplications(ArrayList<SearchRestaurantNameModel> applications) {
        applications.addAll(applications);
        this.notifyItemRangeInserted(0, applications.size() - 1);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRestaurantNameAdapter.ViewHolder holder, int position) {
        fm = alist.get(position);
        holder.txt_RestaurantsName.setText(fm.getRestName());
        holder.txt_FoodType.setText(fm.getResfoodType());
        holder.txt_FoodTime.setText(fm.getFoodTime());
        holder.txt_SearchRestAddress.setText(fm.getFoodAddress());
        holder.txt_SearchRestOffers.setText(fm.getFoodOffers());

        Glide.with(context).load(Uri.parse(fm.getImgRes()))
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img_Restaurant);
        holder.setIsRecyclable(false);

//        holder.img_Restaurant.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return alist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_RestaurantsName,txt_FoodType,txt_FoodTime,txt_SearchRestAddress,txt_SearchRestOffers;
        public ImageView img_Restaurant;
        public ViewHolder(final View itemView) {
            super(itemView);
            this.img_Restaurant = (ImageView) itemView.findViewById(R.id.img_Restaurant);
            this.txt_RestaurantsName = (TextView) itemView.findViewById(R.id.txt_RestaurantsName);
            this.txt_FoodType = (TextView) itemView.findViewById(R.id.txt_FoodType);
            this.txt_FoodTime = (TextView) itemView.findViewById(R.id.txt_FoodTime);
            this.txt_SearchRestAddress = (TextView) itemView.findViewById(R.id.txt_SearchRestAddress);
            this.txt_SearchRestOffers = (TextView) itemView.findViewById(R.id.txt_SearchRestOffers);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    // check if item still exists
                    if(pos != RecyclerView.NO_POSITION){
                        fm = alist.get(pos);
                        String rid = fm.getRid();
                        Log.e("RID in Adapter",rid);
                        Intent in = new Intent(context, SingleItemRestuActivity.class);
                        in.putExtra("RID",rid);
                        context.startActivity(in);
                    }

                }
            });
        }

    }
}
