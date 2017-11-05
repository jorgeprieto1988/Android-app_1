package com.appadia.beacon.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.appadia.beacon.Fragments.Deals;
import com.appadia.beacon.Fragments.DealsFavourite;
import com.appadia.beacon.Fragments.Shops;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nnadmin on 25/2/16.
 */


//Pager Adapter. Loads the fragment Deals.
public class ViewPagerAdapterDeals extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapterDeals is created
    int NumbOfTabs;
    private ArrayList<Deals> mDeals = new ArrayList<Deals>();



    public ViewPagerAdapterDeals(FragmentManager fm, int mNumbOfTabsumb) {
        super(fm);

        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle=new Bundle();
        switch (position) {
            case 0:
                    Deals deals = Deals.newInstance("false");
                    return deals;
            case 1:
                Deals dealfav = Deals.newInstance("false");
                return dealfav;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }




}
