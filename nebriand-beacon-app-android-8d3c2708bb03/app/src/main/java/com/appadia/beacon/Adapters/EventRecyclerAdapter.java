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
import com.appadia.beacon.Models.EventModel;
import com.appadia.beacon.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by nnadmin on 25/2/16.
 */

//Adapter. Loads the data of the Events inside the row view
public class EventRecyclerAdapter extends RecyclerView
        .Adapter<EventRecyclerAdapter
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


    //Load the data of the event into the row view
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

        View dealView= inflater.inflate(R.layout.news_listing_row, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(dealView);

        return viewHolder;

    }

    //Load the data of the event into the row view
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        try {
            EventModel eventModel = mEvents.get(position);
            TextView texttitulo = holder.tvtitle;
            texttitulo.setText(eventModel.getEvent_title());
            TextView textdates = holder.tvdates;
            textdates.setText("From " + eventModel.getStart_day() + " to " + eventModel.getEnd_day());
            ImageView image = holder.tvimage;
            Uri url = Uri.parse(eventModel.getImages());
            imageLoader.displayImage(eventModel.getImages(), image);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    //Sets all the objects of the row view
    public static  class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvtitle;

        public TextView tvdates;
        public ImageView tvimage;


        public MyViewHolder(final View itemView) {
            super(itemView);
            tvtitle=(TextView) itemView.findViewById(R.id.titleevent);

            tvdates=(TextView) itemView.findViewById(R.id.datesevent);
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

    private List<EventModel> mEvents;

    public EventRecyclerAdapter(List<EventModel> event){
        mEvents =event;
    }

}
