package com.appadia.beacon.Adapters;

import android.content.Context;
import android.graphics.Point;
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
import com.appadia.beacon.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by nnadmin on 25/2/16.
 */

//Adapter. Loads the data of the deals inside the row view
public class DealReclycerAdapter extends RecyclerView
        .Adapter<DealReclycerAdapter
        .MyViewHolder> {


    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    Context context;
    int width,height;
    // Define listener member variable
    private static OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    //Loads the row view with its specification
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

        View dealView= inflater.inflate(R.layout.deal_listing_row, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(dealView);

        return viewHolder;

    }

    //Load the data of the deal into the row view
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        try {
            DealModel dealModel = mDeals.get(position);

            TextView texttitulo = holder.tvtitle;
            texttitulo.setText(dealModel.getDeal_title());
            TextView textcategory = holder.tvcategory;
            textcategory.setText(dealModel.getCategory());
            TextView textdates = holder.tvdates;
            textdates.setText("From " + dealModel.getStart_day() + " to " + dealModel.getEnd_day());
            //Also implement the image
            holder.tvimage.getLayoutParams().height = height;
           // holder.tvimage.getLayoutParams().width = width;
            ImageView image = holder.tvimage;
            Uri url = Uri.parse(dealModel.getImages());
            imageLoader.displayImage(dealModel.getImages(), image);
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mDeals.size();
    }

    //Sets all the objects of the row view
    public static  class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvtitle;
        public TextView tvcategory;
        public TextView tvdates;
        public ImageView tvimage;


        public MyViewHolder(final View itemView) {
            super(itemView);
            tvtitle=(TextView) itemView.findViewById(R.id.title);
            tvcategory=(TextView) itemView.findViewById(R.id.category);
            tvdates=(TextView) itemView.findViewById(R.id.dates);
            tvimage=(ImageView) itemView.findViewById(R.id.iv_newimage);

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

    private List<DealModel> mDeals;

    public DealReclycerAdapter(List<DealModel> deals){
        mDeals=deals;
    }







}
