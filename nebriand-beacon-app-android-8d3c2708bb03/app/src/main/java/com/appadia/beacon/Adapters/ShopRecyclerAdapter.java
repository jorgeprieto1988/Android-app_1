package com.appadia.beacon.Adapters;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.appadia.beacon.Models.DealModel;
import com.appadia.beacon.Models.ShopModel;
import com.appadia.beacon.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Jorge on 31/03/2016.
 */

//Adapter. Loads the data of the Shops inside the row view
public class ShopRecyclerAdapter extends RecyclerView
    .Adapter<ShopRecyclerAdapter
            .MyViewHolder> {

        protected ImageLoader imageLoader = ImageLoader.getInstance();

        DisplayImageOptions options;
        Context context;
        int width,height;



    private static OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    //Load the data of the shop into the row view
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater inflater= LayoutInflater.from(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display mDisplay = wm.getDefaultDisplay();
        Point size = new Point();
        mDisplay.getSize(size);
        width = size.y;
        height = width * 5 / 15;

        View shopView= inflater.inflate(R.layout.shop_listing_row, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(shopView);

        return viewHolder;

    }

    //Load the data of the shop into the row view
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        try {
            ShopModel shopModel = mShops.get(position);

            TextView textname = holder.tvname;
            textname.setText(shopModel.getShop_name());
            TextView textfloor = holder.tvfloor;
            textfloor.setText(shopModel.getFloor());
            //Also implement the image
           // holder.tvimage.getLayoutParams().height = height;
            // holder.tvimage.getLayoutParams().width = width;
            ImageView image = holder.tvimage;
            Uri url = Uri.parse(shopModel.getImages());

            if(shopModel.getImages()!="") {
                imageLoader.displayImage(shopModel.getImages(), image);
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return mShops.size();
    }


    //Sets all the objects of the row view
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tvname;
        public TextView tvfloor;
        public ImageView tvimage;


        public MyViewHolder(final View itemView) {
            super(itemView);
            tvname=(TextView) itemView.findViewById(R.id.nameshop);
            tvfloor=(TextView) itemView.findViewById(R.id.floor);
            tvimage=(ImageView) itemView.findViewById(R.id.imageshop);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null)
                        listener.onItemClick(itemView, getLayoutPosition());
                }
            });


        }


    }

    private List<ShopModel> mShops;

    public ShopRecyclerAdapter(List<ShopModel> shops){
        mShops=shops;
    }


    }
