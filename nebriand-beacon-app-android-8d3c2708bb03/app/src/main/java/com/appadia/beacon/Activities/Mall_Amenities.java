package com.appadia.beacon.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.appadia.beacon.Models.DealModel;
import com.appadia.beacon.Models.MallModel;
import com.appadia.beacon.R;
import com.appadia.beacon.SQL.MySQLiteHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jorge on 26/04/2016.
 */
public class Mall_Amenities extends AppCompatActivity {


    @Bind(R.id.tv_allamenities)
    TextView mallamenities;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_amenities);
        ButterKnife.bind(this);

        final MySQLiteHelper db = new MySQLiteHelper(this.getApplicationContext());
        MallModel mall= new MallModel();
        //Show all the amenities into the textview
        //String mallid=getIntent().getStringExtra("mallid");
       // mall=db.getMallbyId(Integer.parseInt(mallid));
       // mallamenities.setText(mall.getAmenitieslong());


    }

    }
