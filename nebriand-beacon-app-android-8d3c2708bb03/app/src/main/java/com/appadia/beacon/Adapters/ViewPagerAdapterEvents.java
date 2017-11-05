package com.appadia.beacon.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.appadia.beacon.Fragments.DealsFavourite;
import com.appadia.beacon.Fragments.Mall;
import com.appadia.beacon.Fragments.MallEvents;

/**
 * Created by nnadmin on 25/2/16.
 */

//Pager Adapter. Loads the fragment Events.
public class ViewPagerAdapterEvents extends FragmentStatePagerAdapter {

    CharSequence Titles[]= new String[]{"ALL SHOPS", "MY SHOPS"}; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapterDeals is created
    int NumbOfTabs;

    public ViewPagerAdapterEvents(FragmentManager fm) {
        super(fm);

        this.NumbOfTabs = 1;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MallEvents event=new MallEvents();
                return  event;
            case 1:
                DealsFavourite dealfav=new DealsFavourite();
                return dealfav;
            case 2:

                //return  new Tab1();
            case 3:
                //return   new Tab2();
        }
        return null;
    }



    @Override
    public int getCount() {
        return NumbOfTabs;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return Titles[position];
    }
}
