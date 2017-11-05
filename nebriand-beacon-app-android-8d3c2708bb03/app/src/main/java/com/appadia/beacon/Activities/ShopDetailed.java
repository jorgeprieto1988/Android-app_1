package com.appadia.beacon.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appadia.beacon.Adapters.ViewPagerAdapterDeals;
import com.appadia.beacon.Fragments.Deals;
import com.appadia.beacon.Fragments.DealsbyId;
import com.appadia.beacon.Models.DealModel;
import com.appadia.beacon.Models.ShopModel;
import com.appadia.beacon.Models.SocialModel;
import com.appadia.beacon.R;
import com.appadia.beacon.SQL.MySQLiteHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jorge on 01/04/2016.
 */

//Class to show all the detailes of a shop when you have cliked in it from the fragment shop
public class ShopDetailed extends AppCompatActivity {

    View v;
    @Bind(R.id.iv_shopinshop)
    ImageView shopimage;
    @Bind(R.id.tv_shopnameL_DealInfo)
    TextView shopname;
    @Bind(R.id.tv_shopdescriptioninshop)
    TextView shopdescription;
    @Bind(R.id.tv_shoptitleinshop)
    TextView titleshop;
    @Bind(R.id.tv_shophistory)
    TextView shopabout;
    @Bind(R.id.tv_webshopinshop)
    TextView shopwebsite;
    @Bind(R.id.tv_opening_hours)
    TextView open_hours;
    @Bind(R.id.iv_fb)
    ImageView facebook;
    @Bind(R.id.iv_tw)
    ImageView twitter;
    @Bind(R.id.iv_inst)
    ImageView instagram;
    @Bind(R.id.iv_four)
    ImageView foursquare;
    @Bind(R.id.iv_you)
    ImageView youtube;
    @Bind(R.id.iv_back)
    ImageView goback;



    ViewPagerAdapterDeals viewpagerad;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopdetailed);
        ButterKnife.bind(this);


        //Creates and loads the configuration of the ImageLoader
        ImageLoader image=ImageLoader.getInstance();
        image.destroy();

        //I will have to create a class so I can create multiple imageloader instances if I have to show by default an image of shops and deals
        ImageLoaderConfiguration config=new ImageLoaderConfiguration.Builder(this.getApplicationContext()).build();
        ImageLoader.getInstance().init(config);


        //Loads the data of the shop from the database and it show it into the layout
        final MySQLiteHelper db = new MySQLiteHelper(this.getApplicationContext());
        DealModel deal;
        //deal=db.getDealbyId(getIntent().getStringExtra("id"));
        String shopid=getIntent().getStringExtra("shopid");
        ShopModel shop=db.getShopbyId(getIntent().getStringExtra("shopid"));
        image=ImageLoader.getInstance();

        image.displayImage(shop.getImages(), shopimage);
        shopname.setText(shop.getShop_name());
        shopdescription.setText(shop.getCategory());
        titleshop.setText("Shop");
        open_hours.setText(shop.getOpen_hours());
        shopabout.setText(shop.getDescription());
        shopwebsite.setText(shop.getWebsite());
        final String shopurl=shop.getWebsite();

        //Listener, when you click on the website it will direct you to your brownser with that website
        shopwebsite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(shopurl));
                startActivity(intent);
            }
        });

        //Listener, attached with the back icon
        goback.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();

            }
        });

        try {

            //Loads the deals related with the shop (It has not been finished yet)
                DealsbyId dealsbyshop= new DealsbyId();


                getSupportFragmentManager().beginTransaction().add(R.id.F_dealsinshop,dealsbyshop).commit();


             //   Deals dealsbyshop= Deals.newInstance();
               //  viewpagerad = new ViewPagerAdapterDeals(getSupportFragmentManager(),1);
               // pager.setAdapter(viewpagerad);
                //pager.setAdapter(dealsbyshop);



        }
        catch(Exception e){
            e.printStackTrace();
        }

        //Loads the social media related with the shop, and when you click, it will take you to that site (Call to OpenLink)
        ArrayList<SocialModel> social= new ArrayList<SocialModel>();
        social=db.tbl_getsocialbyshop(getIntent().getStringExtra("shopid"));

        if (!social.isEmpty()){
            for( SocialModel media : social){
                switch (media.getSm_name()){
                    case "facebook":
                            OpenLink(facebook,media.getSm_link());
                        break;
                    case "twitter":
                        OpenLink(twitter,media.getSm_link());
                        break;
                    case "instagram":
                        OpenLink(instagram,media.getSm_link());
                        break;
                    case "foursquare":
                        OpenLink(foursquare,media.getSm_link());
                        break;
                    case "youtube":
                        OpenLink(youtube,media.getSm_link());
                        break;
                }
            }
        }
        //RecyclerView rvdeals= (RecyclerView) getView().findViewById(R.id.Reclycer_Deal);

        //Initialize deals


    }


    //Procedure to show and  open the social media link when clicking the icon
    public void OpenLink(ImageView i_social,final String shopurl){
        i_social.setVisibility(View.VISIBLE);

        i_social.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(shopurl));
                startActivity(intent);
            }
        });

    }







}
