package com.appadia.beacon;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.app.FragmentTransaction;
//import android.app.FragmentManager;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appadia.beacon.Activities.DealDetailed;
import com.appadia.beacon.Activities.Mall_Amenities;
import com.appadia.beacon.Activities.ShopDetailed;
import com.appadia.beacon.Adapters.ViewPagerAdapterDeals;
import com.appadia.beacon.Adapters.ViewPagerAdapterEvents;
import com.appadia.beacon.Adapters.ViewPagerAdapterMall;
import com.appadia.beacon.Adapters.ViewPagerAdapterShops;
import com.appadia.beacon.Fragments.Deals;
import com.appadia.beacon.Fragments.Mall;
import com.appadia.beacon.Fragments.MallEvents;
import com.appadia.beacon.Fragments.Shops;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import butterknife.Bind;
import butterknife.ButterKnife;


//Main Activity, Loads the Deal fragment by default
public class MainActivity extends AppCompatActivity implements Deals.OnItemSelectedListener , Shops.OnItemSelectedListenerShop, Mall.OnMallItemSelectedListener{


    CharSequence Titles[] = {"ALL DEALS", "MY DEALS","ALL SHOPS","MY SHOPS"};
    int Numboftabs = 2;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolBar)
    Toolbar toolBar;
    @Bind(R.id.toollinear)
    LinearLayout toollinear;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.containerViewone)
    LinearLayout containerViewone;
    @Bind(R.id.containerViewtwo)
    LinearLayout containerViewtwo;
    @Bind(R.id.containerthree)
    LinearLayout containerthree;
    @Bind(R.id.containerviewfour)
    LinearLayout containerviewfour;
    @Bind(R.id.MainCointainer)
    FrameLayout MainCointainer;
    @Bind(R.id.navigation_view)
    NavigationView navigationView;
    @Bind(R.id.drawer)
    DrawerLayout drawer;

    ViewPagerAdapterDeals viewpagerad;
    ViewPagerAdapterShops viewpagershop;
    ViewPagerAdapterMall viewpagermall;
    ViewPagerAdapterEvents viewpagerevent;
    DisplayImageOptions options;
    Deals fragment;
    CustomTabLayout customtab;
    //  private DrawerLayout drawerLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        customtab=new CustomTabLayout(this);
        tabLayout.addTab(tabLayout.newTab().setText("ALL DEALS"));
       tabLayout.addTab(tabLayout.newTab().setText("MY DEALS"));

        //fragment = (Deals) getSupportFragmentManager().findFragmentById(R.id.Deals);


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(getResources().getColor(R.color.tabunselect), getResources().getColor(R.color.black));
        //Options for the imageloader, it defaults with the empy_deals picture
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.empty_deals)
                .showImageForEmptyUri(R.drawable.empty_deals)
                .showImageOnFail(R.drawable.empty_deals)
                .showImageOnLoading(R.drawable.empty_deals)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration config=new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);





        //Initializing NavigationView
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawer.closeDrawers();

                //Checks which item in the menu has been clicked and starts its fragments
                switch (menuItem.getItemId()) {
                    case R.id.Deals:
                        toolbarTitle.setText("Deals");
                        tabLayout.setVisibility(View.VISIBLE);
                        tabLayout.getTabAt(0).setText(Titles[0]);
                        tabLayout.getTabAt(1).setText(Titles[1]);
                        viewpagerad = new ViewPagerAdapterDeals(getSupportFragmentManager(), Numboftabs);
                        pager.setAdapter(viewpagerad);
                        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.red));

                        break;

                    case R.id.Shops:
                        toolbarTitle.setText("Shops");
                        tabLayout.setVisibility(View.VISIBLE);
                        tabLayout.getTabAt(0).setText(Titles[2]);
                        tabLayout.getTabAt(1).setText(Titles[3]);
                        viewpagershop = new ViewPagerAdapterShops(getSupportFragmentManager(), Numboftabs);
                        pager.setAdapter(viewpagershop);
                        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.red));

                        break;

                    case R.id.The_Mall:

                        Mall themall= new Mall();
                       // getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.containerViewone)).commit();
                       //PagerAdapter aux= pager.getAdapter();
                        toolbarTitle.setText("Mall");
                        tabLayout.setVisibility(View.GONE);
                        viewpagermall= new ViewPagerAdapterMall(getSupportFragmentManager());
                        pager.setAdapter(viewpagermall);

                        break;

                    case R.id.News_Events:

                        MallEvents events= new MallEvents();
                        toolbarTitle.setText("News,Events & Safety");
                        tabLayout.setVisibility(View.GONE);
                        viewpagerevent= new ViewPagerAdapterEvents(getSupportFragmentManager());
                        pager.setAdapter(viewpagerevent);

                }
                return false;
            }
        });
        // Initializing Drawer Layout and ActionBarToggle
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolBar, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        viewpagerad = new ViewPagerAdapterDeals(getSupportFragmentManager(), Numboftabs);
       // pager.setOffscreenPageLimit(Numboftabs);
        pager.setAdapter(viewpagerad);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.red));


        //customtab.setTabsFromPagerAdapter(viewpagerad);

        /*LinearLayout ll = (LinearLayout) tabLayout.getChildAt(0);
        TabWidget tw = (TabWidget) ll.getChildAt(0);
        RelativeLayout rllf = (RelativeLayout) tw.getChildAt(0);
        TextView lf = (TextView) rllf.getChildAt(1);
        lf.setTextSize(21);*/


        //Starts when I chane the tab inside the tab menu
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {



              //  FragmentStatePagerAdapter fragm = (FragmentStatePagerAdapter) pager.getAdapter();

               // Deals deal1= (Deals) fragm.instantiateItem(pager, 0);
               // Deals deal3= (Deals) fragm.instantiateItem(pager,1);
              //  String actualfragment= "android:switcher:" + get
               // pager.
                pager.setCurrentItem(tab.getPosition(),true);




            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    //Takes arguments when I click in a Deal and it starts its activity
    @Override
       public void onRssItemSelected(String link) {

        try {
            // if (fragment != null && fragment.isInLayout()) {
            //   Toast.makeText(this.getApplicationContext(), link + " from fragment!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this, DealDetailed.class);
            i.putExtra("id", link);
            startActivity(i);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        // }
    }

    //Takes arguments when I click in a Shop and it starts its activity
    @Override
    public void onItemSelectedShop(String link) {

        try {
            // if (fragment != null && fragment.isInLayout()) {
            //   Toast.makeText(this.getApplicationContext(), link + " from fragment!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this, ShopDetailed.class);
            i.putExtra("shopid", link);
            startActivity(i);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        // }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAmenitiesItemSelected(int mallid) {

        try {
            // if (fragment != null && fragment.isInLayout()) {
            //   Toast.makeText(this.getApplicationContext(), link + " from fragment!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this, Mall_Amenities.class);
            i.putExtra("mallid", mallid);
            startActivity(i);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public class CustomTabLayout extends TabLayout {
        public CustomTabLayout(Context context) {
            super(context);
        }

        public CustomTabLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        public void setTabsFromPagerAdapter(@NonNull PagerAdapter adapter) {
            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Bold.ttf");

            this.removeAllTabs();

            ViewGroup slidingTabStrip = (ViewGroup) getChildAt(0);

            for (int i = 0, count = adapter.getCount(); i < count; i++) {
                Tab tab = this.newTab();
                this.addTab(tab.setText(adapter.getPageTitle(i)));
                AppCompatTextView view = (AppCompatTextView) ((ViewGroup)slidingTabStrip.getChildAt(i)).getChildAt(1);
                view.setTypeface(typeface);
            }
        }
    }





}
