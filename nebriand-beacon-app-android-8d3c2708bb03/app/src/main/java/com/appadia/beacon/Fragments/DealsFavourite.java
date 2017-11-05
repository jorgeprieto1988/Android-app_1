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
import com.appadia.beacon.InternetCheck;
import com.appadia.beacon.Models.DealModel;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DealsFavourite#newInstance} factory method to
 * create an instance of this fragment.
 */

//Fragment to show the favourited deals (This class is not implemented, use it only if you have problems loading the saved deals from teh Fragment Deals)
public  class DealsFavourite extends Fragment{
    View v;
    DealReclycerAdapter Dealadapter;
    boolean flag = false;
    JSONObject json = null;
    public Context mContext;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<DealModel> dealslist;
    private AsyncTask<Void, Integer, Void> _ListTask;
    @Bind(R.id.Reclycer_Deal)
    RecyclerView ReclycerDeal;
    RecyclerView rvdeals;
    private DisplayImageOptions options;



    public DealsFavourite() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DealsFavourite newInstance() {
        DealsFavourite fragment = new DealsFavourite();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageLoader image=ImageLoader.getInstance();
        image.destroy();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.empty_deals)
                .showImageForEmptyUri(R.drawable.empty_deals)
                .showImageOnFail(R.drawable.empty_deals)
                .showImageOnLoading(R.drawable.empty_deals)
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
            v = inflater.inflate(R.layout.fragment_deals, container, false);
            final MySQLiteHelper db = new MySQLiteHelper(getActivity());
            rvdeals = (RecyclerView) v.findViewById(R.id.Reclycer_Deal);
            dealslist = db.tbl_getallsavedeals();


            //Used temporaly to force to get the data anytime I start the app
            if(InternetCheck.isInternetOn1(getActivity()) && dealslist.isEmpty()){
                initData();
            }

            if (dealslist.isEmpty()) {
              //  initData();
               // dealslist = db.tbl_getalldeals();
            }
            DealReclycerAdapter dealadapater = new DealReclycerAdapter(dealslist);
            rvdeals.setAdapter(dealadapater);

            mLayoutManager=new LinearLayoutManager(mContext);

            rvdeals.setLayoutManager(mLayoutManager);
            mContext=this.getActivity();


            dealadapater.setOnItemClickListener(new DealReclycerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                   // String name = dealslist.get(position).getDeal_title();
                    int iddealnum=dealslist.get(position).getDealid();
                    String iddealtext= String.valueOf(iddealnum);
                    //Toast.makeText(mContext, name + " was clicked!", Toast.LENGTH_SHORT).show();
                    onSomeClick(v,iddealtext);
                }
            });


            //rvdeals.setLayoutManager(new LinearLayoutManager(this.getContext()));

           // ButterKnife.bind(v);
           //Declaration(v);
            //   init();

        }
        catch(NullPointerException e)
            {
                e.printStackTrace();

            }
        return v;
    }

    private void initData(){
        try{
            MySQLiteHelper db=new MySQLiteHelper(getActivity());

            dealslist= db.tbl_getallsavedeals();
            db.close();
        }catch (Exception e){
            Log.e("Exception", "Popular json webservice" + e.toString());
        }
    }

    public void onSomeClick(View v,String id) {
        listener.onRssItemSelected(id);
    }
    private OnItemSelectedListener listener;
    public interface OnItemSelectedListener {
        // This can be any number of events to be sent to the activity
        public void onRssItemSelected(String link);
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


}
