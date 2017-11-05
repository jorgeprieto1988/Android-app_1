package com.appadia.beacon.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appadia.beacon.Models.DealModel;
import com.appadia.beacon.Models.ShopModel;
import com.appadia.beacon.R;
import com.appadia.beacon.SQL.MySQLiteHelper;
import com.appadia.beacon.SQL.ServiceRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jorge on 01/04/2016.
 */

//Activity with all the detailed data of a deal
public class DealDetailed extends AppCompatActivity {

    View v;
    @Bind(R.id.iv_newimage)
    ImageView dealimage;
    @Bind(R.id.iv_shopimageindeal)
    ImageView shopimage;
    @Bind(R.id.tv_dealtextindeal)
    TextView dealname;
    @Bind(R.id.tv_categories)
    TextView dealcategories;
    @Bind(R.id.tv_descriptionlarge)
    TextView dealdescription;
    @Bind(R.id.tv_datesindeal)
    TextView dealdates;
    @Bind(R.id.tv_shoptitleindeal)
    TextView shoptitle;
    @Bind(R.id.tv_shopindeal)
    TextView shopname;
    @Bind(R.id.tv_categoriesindeal)
    TextView shopcategories;
    @Bind(R.id.tv_floorindeal)
    TextView shopfloor;
    @Bind(R.id.iv_back)
    ImageView goback;
    @Bind(R.id.iv_listfavicon)
    ImageView favourite;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dealdetailed);
        ButterKnife.bind(this);

        ImageLoader image=ImageLoader.getInstance();
        image.destroy();


        //I will have to create a class so I can create multiple imageloader instances if I have to show by default an image of shops and deals
        //Create and load the configuration of the imageloader
        ImageLoaderConfiguration config=new ImageLoaderConfiguration.Builder(this.getApplicationContext()).build();
        ImageLoader.getInstance().init(config);

        final MySQLiteHelper db = new MySQLiteHelper(this.getApplicationContext());
        DealModel deal= new DealModel();
        deal=db.getDealbyId(getIntent().getStringExtra("id"));
       final ShopModel shop=db.getShopbyId(deal.getShops());
        image=ImageLoader.getInstance();


        //Sets all the data into the layout
         dealname.setText(deal.getDeal_title());
        dealcategories.setText(deal.getCategory());
        dealdescription.setText(deal.getDescription());
        dealdates.setText("From " + deal.getStart_day() + " to " + deal.getEnd_day());
        image.displayImage(deal.getImages(), dealimage);
        image.displayImage(shop.getImages(), shopimage);

        shoptitle.setText(shop.getShop_name());
        shopname.setText(shop.getShop_name());
        shopcategories.setText(shop.getCategory());
        shopfloor.setText(shop.getFloor());
        //RecyclerView rvdeals= (RecyclerView) getView().findViewById(R.id.Reclycer_Deal);

        //Initialize deals

        LinearLayout shoplayout=(LinearLayout) findViewById(R.id.check_shop);


        //Listener when clicking the shop attached with the deal
        shoplayout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent i = new Intent(DealDetailed.this, ShopDetailed.class);
                    int shopid=shop.getShopid();
                    i.putExtra("shopid",String.valueOf(shopid));
                    startActivity(i);
                }

        });


        //Listener attached to the back icon
        goback.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();

            }
        });


        //Listener attached to the save icon
        favourite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String imageName= String.valueOf(favourite.getTag());

                if(imageName.equals("ic_save")){
                    ServiceRequest sr= new ServiceRequest();
                    JSONObject jsonsave;
                    jsonsave=sr.SaveDeal(db.getUid(),db.getToken(),getIntent().getStringExtra("id"));
                    if(jsonsave.optString("success").contains("true")) {
                        try {
                            JSONObject savemessage = jsonsave.getJSONObject("data");
                           if( savemessage.optString("msg").contains("success")){
                               Toast.makeText(getApplicationContext(), "Deal saved", Toast.LENGTH_LONG).show();
                               String uri="@drawable/ic_saved";
                               int imageResource= getResources().getIdentifier(uri,null,getPackageName());
                               Drawable res=getResources().getDrawable(imageResource);
                               favourite.setImageDrawable(res);
                           }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }

            }
        });



    }


}
