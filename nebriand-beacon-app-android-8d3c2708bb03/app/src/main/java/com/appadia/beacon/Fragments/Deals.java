package com.appadia.beacon.Fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
 * Use the {@link Deals#newInstance} factory method to
 * create an instance of this fragment.
 */

//Fragment Deals, Used to load a listview of the Deals stored, also it should be use to show the saved deals
public class Deals extends Fragment{
    View v;
    DealReclycerAdapter Dealadapter;
    boolean flag = false;
    JSONObject json = null;
    public Context mContext;
    private RecyclerView.LayoutManager mLayoutManager;

     ArrayList<DealModel> dealslist;
    ArrayList<DealModel> dealsmylist;
    private AsyncTask<Void, Integer, Void> _ListTask;
    @Bind(R.id.Reclycer_Deal)
    RecyclerView ReclycerDeal;
    RecyclerView rvdeals;
    private DisplayImageOptions options;


    private OnFragmentInteractionListener mListener;

    public Deals() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    //Creates a new instance of the Deal with parameters, the value should be true or false, if you want to show the save deals or all deals
    public static Deals newInstance(String value) {
        Deals fragment = new Deals();
        Bundle args= new Bundle();
        args.putString("frommydeals",value);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Loads the image loader with the empty deal as default image
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

            if(getArguments().getString("frommydeals").equals("true")){

                dealslist=db.tbl_getallsavedeals();
            }
            else {
                dealslist = db.tbl_getalldeals();


                //Used temporaly to force to get the data anytime I start the app
                if (InternetCheck.isInternetOn1(getActivity()) && dealslist.isEmpty()) {
                    initData();
                }
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
             //   _ListTask = new AsyncData().execute();
            }
        return v;
    }


    //Loads the deals from the api
    private void initData(){
        try{
            MySQLiteHelper db=new MySQLiteHelper(getActivity());
            if(InternetCheck.isInternetOn1(getActivity()) && flag==false){
                ServiceRequest sr= new ServiceRequest();
                json=sr.GetDealsfromMall(db.getUid(),db.getToken());
                if(json.optString("success").contains("true")){
                    db.tbl_deals_delete();
                }
                db.tbl_savedeals(json);
            }
            dealslist= db.tbl_getalldeals();
            db.close();
        }catch (Exception e){
            Log.e("Exception", "Popular json webservice" + e.toString());
        }
    }

    private void init() {
        try {
            final MySQLiteHelper db = new MySQLiteHelper(getActivity());
            RecyclerView rvdeals= (RecyclerView) getActivity().findViewById(R.id.Reclycer_Deal);
            dealslist = db.tbl_getalldeals();
            DealReclycerAdapter dealadapater = new DealReclycerAdapter(dealslist);


            if (!dealslist.isEmpty()) {
               // DealReclycerAdapter dealapater = new DealReclycerAdapter(dealslist);
                rvdeals.setAdapter(dealadapater);

                rvdeals.setLayoutManager(new LinearLayoutManager(this.getContext()));
            } else {
                if (!InternetCheck.isInternetOn1(getActivity())) {

                }
            }
        }catch(NullPointerException e)
        {
            e.printStackTrace();
           // _ListTask = new AsyncData().execute();
        }
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }

    private void Declaration(View v) {
       // ReclycerDeal.setHasFixedSize(true);
      //  ReclycerDeal.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

    // TODO: Rename method, update argument and hook method into UI event
    private OnItemSelectedListener listener;

    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {
        // This can be any number of events to be sent to the activity
        public void onRssItemSelected(String link);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }


    //It will send the id of the deals to the main activity, so it will load the DealDetailed Activity
    public void onSomeClick(View v,String id) {
        listener.onRssItemSelected(id);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
