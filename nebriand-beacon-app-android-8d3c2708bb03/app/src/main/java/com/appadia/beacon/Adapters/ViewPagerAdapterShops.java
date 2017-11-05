package com.appadia.beacon.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.appadia.beacon.Fragments.Deals;
import com.appadia.beacon.Fragments.DealsFavourite;
import com.appadia.beacon.Fragments.Shops;

/**
 * Created by nnadmin on 25/2/16.
 */

//Pager Adapter. Loads the fragment Shops.
public class ViewPagerAdapterShops extends FragmentStatePagerAdapter {

    CharSequence Titles[]= new String[]{"ALL SHOPS", "MY SHOPS"}; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapterDeals is created
    int NumbOfTabs;

    public ViewPagerAdapterShops(FragmentManager fm, int mNumbOfTabsumb) {
        super(fm);

        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Shops shop=new Shops();
                return  shop;
            case 1:
                Shops shop2=new Shops();
                return  shop2;
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
