package com.appadia.beacon.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
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
import java.util.zip.Inflater;

/**
 * Created by Jorge on 08/04/2016.
 */
//Fragment, to Show the deals related to a shop (It has to be finished)
public class DealsbyId extends Fragment {
    View v;
    RecyclerView rvdeals;
    ArrayList<DealModel> dealslist;
    private RecyclerView.LayoutManager mLayoutManager;
    public Context mContext;
    boolean flag = false;
    JSONObject json = null;
    private DisplayImageOptions options;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Get back arguments
       // int SomeInt = getArguments().getInt("someInt", 0);
       // String someTitle = getArguments().getString("someTitle", "");


    }

    //Creates a new Instance of the fragment, with some parameters (It has to be changed to receive the id of the deal)
    public static DealsbyId newInstance(int someInt, String someTitle) {
        DealsbyId fragmentDemo = new DealsbyId();
        Bundle args = new Bundle();
        args.putInt("someInt", someInt);
        args.putString("someTitle", someTitle);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


            //to reload all the data, delete after test
            // _ListTask = new AsyncData().execute();
            v = inflater.inflate(R.layout.fragment_deals, container, false);
            final MySQLiteHelper db = new MySQLiteHelper(getActivity());
         rvdeals = (RecyclerView) v.findViewById(R.id.Reclycer_Deal);
         dealslist = db.tbl_getalldeals();

        if(InternetCheck.isInternetOn1(getActivity()) && dealslist.isEmpty()){
            initData();
        }

        DealReclycerAdapter dealadapater = new DealReclycerAdapter(dealslist);
        rvdeals.setAdapter(dealadapater);

        mLayoutManager=new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
       // mLayoutManager.setMeasuredDimension();
        rvdeals.setLayoutManager(mLayoutManager);
        mContext=this.getActivity();

            return v;

    }

    //Loads the data of the deals (It has to be changed, and return only the deals of the shop given by parameter)
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



}
