package com.curryout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yayandroid.parallaxrecyclerview.ParallaxImageView;
import com.yayandroid.parallaxrecyclerview.ParallaxRecyclerView;
import com.yayandroid.parallaxrecyclerview.ParallaxViewHolder;

import java.util.ArrayList;
import com.curryout.model.SearchDataModel;

public class SearchListAdapter extends ParallaxRecyclerView.Adapter<SearchListAdapter.ViewHolder> {
    private ArrayList<SearchDataModel> listdata;
    Context context;
    // RecyclerView recyclerView;
    public SearchListAdapter(ArrayList<SearchDataModel> listdata,Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.search_recylist_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final SearchDataModel searchListData = listdata.get(position);
        holder.txtFoodName.setText(searchListData.getName());
        holder.txtResQnty.setText(searchListData.getRestraunt_count());
        //holder.imageView.setImageResource(searchListData.getImg());
        final String cuisine_id = searchListData.getCuisineID();
        final String rcount = searchListData.getRestraunt_count();

        Glide.with(context).load(Uri.parse(searchListData.getIm()))
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              Toast.makeText(view.getContext(), "click on item: " + searchListData.getFoodName(), Toast.LENGTH_LONG).show();
                if(cuisine_id!=null && (!rcount.contains("0"))){
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new RestaurantListFragment(cuisine_id);
                //myFragment.
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
//                    Intent in = new Intent(context, RestaurantListActivity.class);
//                    in.putExtra("CID",cuisine_id);
//                    context.startActivity(in);
            }
                else{
              Toast.makeText(view.getContext(), "No Restaurants Available", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends ParallaxViewHolder {
        public ParallaxImageView imageView;
        public TextView txtFoodName, txtResQnty;
//        public LinearLayout linearLayout;
        public RelativeLayout relativeLayout;

        @Override
        public int getParallaxImageId() {
            return R.id.imgTitle;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ParallaxImageView) itemView.findViewById(R.id.imgTitle);
            this.txtFoodName = (TextView) itemView.findViewById(R.id.txt_FoodNameSearch);
            this.txtResQnty = (TextView) itemView.findViewById(R.id.txt_restaurantsSearch);
//            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.linearLayout);


        }
    }


}
