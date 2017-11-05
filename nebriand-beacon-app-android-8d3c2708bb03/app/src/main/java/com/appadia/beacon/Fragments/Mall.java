package com.appadia.beacon.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appadia.beacon.Models.MallModel;
import com.appadia.beacon.Models.SocialModel;
import com.appadia.beacon.R;
import com.appadia.beacon.SQL.MySQLiteHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by Jorge on 12/04/2016.
 */

//Fragment. Show the mall information (By default it will show the information of the mall with id 1)
public class Mall extends Fragment {



    View v;
    ImageLoader image;

    @Bind(R.id.iv_iconmall)
    ImageView iconmall;
    @Bind(R.id.iv_newimage)
    ImageView mallimage;
    @Bind(R.id.iv_fbmall)
    ImageView fb;
    @Bind(R.id.iv_twmall)
    ImageView tw;
    @Bind(R.id.iv_instmall)
    ImageView inst;
    @Bind(R.id.iv_fourmall)
    ImageView four;
    @Bind(R.id.iv_youmall)
    ImageView you;

    @Bind(R.id.tv_mallname)
    TextView mallname;
    @Bind(R.id.tv_malladdress)
    TextView malladdress;
    @Bind(R.id.tv_mallopeninghours)
    TextView mallopeninghours;
    @Bind(R.id.tv_mallhistory)
    TextView mallhistory;
    @Bind(R.id.tv_seemallmap)
    TextView mall_map;
    @Bind(R.id.tv_mallamenities)
    TextView amenities;
    @Bind(R.id.tv_mallamenitiesall)
    TextView allamenitiesmall;
    @Bind(R.id.tv_parking)
    TextView parking;
    @Bind(R.id.tv_mallcinema)
    TextView cinema;
    @Bind(R.id.tv_mallweb)
    TextView web;
    @Bind(R.id.tv_mallemail)
    TextView email;


    private OnMallItemSelectedListener listener;

    public interface OnMallItemSelectedListener{
        public void onAmenitiesItemSelected(int mallid);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMallItemSelectedListener) {
            listener = (OnMallItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflates the mall into the layout
            v = inflater.inflate(R.layout.fragment_mall, container, false);
        //ButterKnife.bind(v);
        try {
            MySQLiteHelper db = new MySQLiteHelper(getActivity());
            if(db.getMallbyId(1)!=null) {
                MallModel mall = db.getMallbyId(1);
                //deal=db.getDealbyId(getIntent().getStringExtra("id"));

                image = ImageLoader.getInstance();
                final int mallid=mall.getMallid();
                mallimage=(ImageView) v.findViewById(R.id.iv_newimage);

                image.displayImage(mall.getImages(),mallimage);
                mallname=(TextView) v.findViewById(R.id.tv_mallname);
                       mallname.setText(mall.getMall_name());
                malladdress=(TextView) v.findViewById(R.id.tv_malladdress);
                        malladdress.setText(mall.getAddress());
                mallhistory=(TextView) v.findViewById(R.id.tv_mallhistory);
                        mallhistory.setText(mall.getDescription());
                amenities=(TextView) v.findViewById(R.id.tv_mallamenities);
                        amenities.setText(mall.getAmenitiesshort());
                parking=(TextView) v.findViewById(R.id.tv_parking);
                        parking.setText(mall.getParking());
                cinema=(TextView) v.findViewById(R.id.tv_mallcinema);
                        cinema.setText(mall.getCinema());
                web=(TextView) v.findViewById(R.id.tv_mallweb);
                        web.setText(mall.getWebsite());
                allamenitiesmall=(TextView) v.findViewById(R.id.tv_mallamenitiesall);
                email=(TextView) v.findViewById(R.id.tv_mallemail);
                         email.setText(mall.getEmail());

                final String mallurl = mall.getWebsite();
                web.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(mallurl));
                        startActivity(intent);
                    }
                });

                allamenitiesmall.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                            listener.onAmenitiesItemSelected(mallid);
                    }
                });

                fb=(ImageView) v.findViewById(R.id.iv_fbmall);
                tw=(ImageView) v.findViewById(R.id.iv_twmall);
                inst=(ImageView) v.findViewById(R.id.iv_instmall);
                four=(ImageView) v.findViewById(R.id.iv_fourmall);
                you=(ImageView) v.findViewById(R.id.iv_youmall);

                ArrayList<SocialModel> social = new ArrayList<SocialModel>();
                social = db.tbl_getsocialbymall(mall.getMallid());

                //Inflates the social media links

                if (!social.isEmpty()) {
                    for (SocialModel media : social) {
                        switch (media.getSm_name()) {
                            case "facebook":
                                OpenLink(fb, media.getSm_link());
                                break;
                            case "twitter":
                                OpenLink(tw, media.getSm_link());
                                break;
                            case "instagram":
                                OpenLink(inst, media.getSm_link());
                                break;
                            case "foursquare":
                                OpenLink(four, media.getSm_link());
                                break;
                            case "youtube":
                                OpenLink(you, media.getSm_link());
                                break;
                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         image=ImageLoader.getInstance();
        image.destroy();

        //I will have to create a class so I can create multiple imageloader instances if I have to show by default an image of shops and deals
        ImageLoaderConfiguration config=new ImageLoaderConfiguration.Builder(getContext()).build();
        ImageLoader.getInstance().init(config);


    }


    //Procedure to show and  open the social media link when clicking the icon
    public void OpenLink(ImageView i_social,final String mallurl){
        i_social.setVisibility(View.VISIBLE);

        i_social.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(mallurl));
                startActivity(intent);
            }
        });

    }

}
