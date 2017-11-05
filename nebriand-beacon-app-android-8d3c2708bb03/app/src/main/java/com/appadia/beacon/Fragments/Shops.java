package com.appadia.beacon.Fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.appadia.beacon.Adapters.DealReclycerAdapter;
import com.appadia.beacon.Adapters.ShopRecyclerAdapter;
import com.appadia.beacon.InternetCheck;
import com.appadia.beacon.Models.DealModel;
import com.appadia.beacon.Models.ShopModel;
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
 * Created by Jorge on 30/03/2016.
 */

//Fragment. Shows all the shops in a gridview
public class Shops extends Fragment {
    View v;
    ShopRecyclerAdapter Shopadapter;
    boolean flag = false;
    JSONObject json = null;
    public Context mContext;
    private RecyclerView.LayoutManager mLayoutManager;
    private GridLayoutManager mGridViewManager;

    ArrayList<ShopModel> shoplist;
    private AsyncTask<Void, Integer, Void> _ListTask;
    @Bind(R.id.Reclycer_Deal)
    RecyclerView ReclycerShop;
    RecyclerView rvshops;
    private DisplayImageOptions options;

    private OnFragmentInteractionListener mListener;

    public Shops() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    //New instance of the shop
    public static Shops newInstance() {
        Shops fragment = new Shops();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Creates and loads the image loader configuration, with the empty_shops as the default image
        ImageLoader image=ImageLoader.getInstance();
        image.destroy();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.empty_shops)
                .showImageForEmptyUri(R.drawable.empty_shops)
                .showImageOnFail(R.drawable.empty_shops)
                .showImageOnLoading(R.drawable.empty_shops)
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
            v = inflater.inflate(R.layout.fragment_shops, container, false);
            final MySQLiteHelper db = new MySQLiteHelper(getActivity());
            rvshops = (RecyclerView) v.findViewById(R.id.Reclycer_Shop);
            shoplist = db.tbl_getallshops();


            //Used temporaly to force to get the data anytime I start the app
            if(InternetCheck.isInternetOn1(getActivity()) && shoplist.isEmpty()){
                initData();
            }

            if (shoplist.isEmpty()) {
                //  initData();
                // dealslist = db.tbl_getalldeals();
            }
            ShopRecyclerAdapter shopadapater = new ShopRecyclerAdapter(shoplist);
            rvshops.setAdapter(shopadapater);

            mLayoutManager=new LinearLayoutManager(mContext);
            mGridViewManager=new GridLayoutManager(this.mContext,3);
            rvshops.setLayoutManager(mGridViewManager);
            //rvshops.setLayoutManager(mLayoutManager);
            mContext=this.getActivity();



            shopadapater.setOnItemClickListener(new ShopRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    // String name = dealslist.get(position).getDeal_title();
                    int idshopnum=shoplist.get(position).getShopid();
                    String idshoptext= String.valueOf(idshopnum);
                    //Toast.makeText(mContext, name + " was clicked!", Toast.LENGTH_SHORT).show();
                    onSomeClickshop(v, idshoptext);
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
            _ListTask = new AsyncData().execute();
        }
        return v;
    }


    //Saves and loads the shops from the API
    private void initData(){
        try{
            MySQLiteHelper db=new MySQLiteHelper(getActivity());
            if(InternetCheck.isInternetOn1(getActivity()) && flag==false){
                ServiceRequest sr= new ServiceRequest();
                json=sr.GetShopsfromMall(db.getUid(), db.getToken());
                if(json.optString("success").contains("true")){
                    db.tbl_shops_delete();
                }
                db.tbl_saveshops(json);
            }
            shoplist= db.tbl_getallshops();
            db.close();
        }catch (Exception e){
            Log.e("Exception", "Popular json webservice" + e.toString());
        }
    }

    private void init() {
        try {
            final MySQLiteHelper db = new MySQLiteHelper(getActivity());
            RecyclerView rvshops= (RecyclerView) getActivity().findViewById(R.id.Reclycer_Shop);
            shoplist = db.tbl_getallshops();
            ShopRecyclerAdapter shopadapater = new ShopRecyclerAdapter(shoplist);


            if (!shoplist.isEmpty()) {
                // DealReclycerAdapter dealapater = new DealReclycerAdapter(dealslist);
                rvshops.setAdapter(shopadapater);

                rvshops.setLayoutManager(new LinearLayoutManager(this.getContext()));
            } else {
                if (!InternetCheck.isInternetOn1(getActivity())) {

                }
            }
        }catch(NullPointerException e)
        {
            e.printStackTrace();
            _ListTask = new AsyncData().execute();
        }
    }

    public class AsyncData extends AsyncTask<Void,Integer,Void>{
        protected Void doInBackground(Void... params){
            synchronized (this){
                try{
                    MySQLiteHelper db=new MySQLiteHelper(getActivity());
                    if(InternetCheck.isInternetOn1(getActivity()) && flag==false){
                        ServiceRequest sr= new ServiceRequest();
                        json=sr.GetShopsfromMall(db.getUid(),db.getToken());
                        if(json.optString("success").contains("true")){
                            db.tbl_shops_delete();
                        }
                        db.tbl_saveshops(json);
                    }
                     shoplist= db.tbl_getallshops();
                }catch (Exception e){
                    Log.e("Exception", "Popular json webservice" + e.toString());
                }
            }
            return null;
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnItemSelectedListenerShop) {
            listener = (OnItemSelectedListenerShop) context;
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

    private OnItemSelectedListenerShop listener;

    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListenerShop {
        // This can be any number of events to be sent to the activity
        public void onItemSelectedShop(String link);
    }

    public void onSomeClickshop(View v,String id) {
        listener.onItemSelectedShop(id);
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
