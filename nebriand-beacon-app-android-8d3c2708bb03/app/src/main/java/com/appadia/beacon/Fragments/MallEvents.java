package com.appadia.beacon.Fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appadia.beacon.Adapters.DealReclycerAdapter;
import com.appadia.beacon.Adapters.EventRecyclerAdapter;
import com.appadia.beacon.InternetCheck;
import com.appadia.beacon.Models.DealModel;
import com.appadia.beacon.Models.EventModel;
import com.appadia.beacon.R;
import com.appadia.beacon.SQL.MySQLiteHelper;
import com.appadia.beacon.SQL.ServiceRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

//import android.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MallEvents#newInstance} factory method to
 * create an instance of this fragment.
 */

//Fragments. Shows the events of the mall in a listview
public  class MallEvents extends Fragment{
    View v;
    DealReclycerAdapter Dealadapter;
    boolean flag = false;
    JSONObject json = null;
    public Context mContext;
    private RecyclerView.LayoutManager mLayoutManager;

     ArrayList<EventModel> eventlist;

    private AsyncTask<Void, Integer, Void> _ListTask;
    @Bind(R.id.Recycler_News)
    RecyclerView Recycler_new;
    RecyclerView rvevents;
    private DisplayImageOptions options;




    public MallEvents() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    //Creates a new instance of the mallevents
    public static MallEvents newInstance() {
        MallEvents fragment = new MallEvents();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Creates and load the image loader, with the empty events as the default image
        ImageLoader image=ImageLoader.getInstance();
        image.destroy();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.empty_events)
                .showImageForEmptyUri(R.drawable.empty_events)
                .showImageOnFail(R.drawable.empty_events)
                .showImageOnLoading(R.drawable.empty_events)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration config2=new ImageLoaderConfiguration.Builder(this.getActivity()).defaultDisplayImageOptions(options).build();
        ImageLoader.getInstance().init(config2);

        //RecyclerView rvdeals= (RecyclerView) getView().findViewById(R.id.Reclycer_Deal);

        //Initialize deals




    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {

            //to reload all the data, delete after test
           // _ListTask = new AsyncData().execute();
            v = inflater.inflate(R.layout.fragment_news, container, false);
            final MySQLiteHelper db = new MySQLiteHelper(getActivity());
            rvevents = (RecyclerView) v.findViewById(R.id.Recycler_News);


                eventlist = db.tbl_getallevents();


                //Used temporaly to force to get the data anytime I start the app
                if (InternetCheck.isInternetOn1(getActivity()) && eventlist.isEmpty()) {
                    initData();
                }


            EventRecyclerAdapter eventadapater = new EventRecyclerAdapter(eventlist);
            rvevents.setAdapter(eventadapater);

            mLayoutManager=new LinearLayoutManager(mContext);

            rvevents.setLayoutManager(mLayoutManager);
            mContext=this.getActivity();





            //rvdeals.setLayoutManager(new LinearLayoutManager(this.getContext()));

           // ButterKnife.bind(v);
           //Declaration(v);
            //   init();

        }
        catch(NullPointerException e)
            {
                e.printStackTrace();
             //   _ListTask = new AsyncData().execute();
            }
        return v;
    }


    //Gets the data of the events from the API
    private void initData(){
        try{
            MySQLiteHelper db=new MySQLiteHelper(getActivity());
            if(InternetCheck.isInternetOn1(getActivity()) && flag==false){
                ServiceRequest sr= new ServiceRequest();
                json=sr.GetEventsfromMall(db.getUid(),db.getToken());
                if(json.optString("success").contains("true")){
                    db.tbl_events_delete();
                }
                db.tbl_saveevents(json);
            }
            eventlist= db.tbl_getallevents();
            db.close();
        }catch (Exception e){
            Log.e("Exception", "Popular json webservice" + e.toString());
        }
    }







    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
/*
    @Override
    public void onConnected(Bundle connectionHint) {
        try{
            _ListTask=new AsyncData().execute();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }*/
}
