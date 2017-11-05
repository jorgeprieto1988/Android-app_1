package com.appadia.beacon;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

//import com.appadia.beacon.IApiMethods;
import com.appadia.beacon.Models.DealModel;
import com.appadia.beacon.SQL.MySQLiteHelper;
import com.appadia.beacon.SQL.ServiceRequest;
import com.appadia.beacon.app.Config;
import com.appadia.beacon.gcm.GcmIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.util.ArrayList;

//This is the first class to be executed
public class SplashScreen extends FragmentActivity {
	SQLiteDatabase newDB;
	String parseid;
	String status = "Parse registration not fired!";
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	//NotificationStatus notificationStatus;
	String gcmtoken;
	private String TAG = SplashScreen.class.getSimpleName();
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private BroadcastReceiver mRegistrationBroadcastReceiver;
	private Handler mHandler= new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setTheme(android.R.style.Theme_Holo_Light_NoActionBar);
		setContentView(R.layout.splash_screen);

		//Common.setBadge(this, 0);

		// Check this condition to avoid loading the app duplicated when open the push notification in notification bar
		// Condition = 1 -> matched when open the push notification
		// Condition = 0 -> normally app open


		//Check if the device has a google token and if it doesn't, gets a new one.
		final MySQLiteHelper db = new MySQLiteHelper(SplashScreen.this);
		db.getWritableDatabase();
		String tokencheck=db.getgcmToken();
		if(db.getgcmToken()==null) {


			mRegistrationBroadcastReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					// checking for type intent filter
					if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
						// gcm successfully registered
						// now subscribe to `global` topic to receive app wide notifications
						//String token = intent.getStringExtra("token");
						gcmtoken = intent.getStringExtra("token");
						db.setgcmToken(gcmtoken);
						Toast.makeText(getApplicationContext(), "GCM registration token: " + gcmtoken, Toast.LENGTH_LONG).show();

					} else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
						// gcm registration id is stored in our server's MySQL

						Toast.makeText(getApplicationContext(), "GCM registration token is stored in server!", Toast.LENGTH_LONG).show();

					} else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
						// new push notification is received

						Toast.makeText(getApplicationContext(), "Push notification is received!", Toast.LENGTH_LONG).show();
					}
				}
			};

			if (checkPlayServices()) {
				registerGCM();
			}


		}


		//Starts the init() process waiting 2 seconds for it (so it has time to register the token in google)
		if(getSharedPreferences("notif", Context.MODE_PRIVATE).getInt("flag", 0) == 1){
			SharedPreferences settings = getSharedPreferences("notif", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("flag", 0);
			editor.commit();
			finish();
		}else{
			try{
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						Init();
					}
				},2000);

			}catch (Exception e){
				e.printStackTrace();
			}
		}


	}

	//Procedure to register the gcm token
	private void registerGCM() {
		Intent intent = new Intent(this, GcmIntentService.class);
		intent.putExtra("key", "register");
		startService(intent);
	}

	//Chck if the google play services are available
	private boolean checkPlayServices() {
		GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
		int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (apiAvailability.isUserResolvableError(resultCode)) {
				apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
						.show();
			} else {
				Log.i(TAG, "This device is not supported. Google Play Services not installed!");
				Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
				finish();
			}
			return false;
		}
		return true;
	}



	//Checks the internet conexion and if it doesn't have any problem register the device in the API and starts the main activity
	private void Init(){

					try {
						MySQLiteHelper db = new MySQLiteHelper(SplashScreen.this);
						db.getWritableDatabase();
						if (InternetCheck.isInternetOn1(SplashScreen.this)) {
							// if we have not got any id, we must to register the device to get one
							if (db.getUid() == null) {
								registerDevice(db);
							}

							intent_to_mainactivity();

						} else {
							try {
								if ((db.getUid() == null)) {
									Builder builder = new Builder(SplashScreen.this);
									builder.setCancelable(true);
									builder.setTitle("No Internet Connection.");
									builder.setMessage(" Currently your device is not connected with internet. Please connect it and try again later");
									builder.setInverseBackgroundForced(true);
									builder.setPositiveButton("Ok",
											new OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {

													dialog.dismiss();
													System.exit(0);
													android.os.Process.killProcess(android.os.Process.myPid());
												}
											});

									AlertDialog alert = builder.create();
									alert.show();
								}
								else{
									intent_to_mainactivity();
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

	}


	//Starts the main activity
	private void intent_to_mainactivity(){
		//Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
		Intent i = new Intent(SplashScreen.this, MainActivity.class);
		i.putExtra("dest", "SplashScreen");
		initData();
		startActivity(i);

		finish();
	}


	//Load all the data anytime I start the app
	private void initData(){

		JSONObject jsondeals,jsonshops,jsonmall,jsonevents;
		ServiceRequest srdeals= new ServiceRequest();
		ServiceRequest srshops= new ServiceRequest();
		ServiceRequest srmall= new ServiceRequest();
		ServiceRequest srevents= new ServiceRequest();
		try{
			MySQLiteHelper db=new MySQLiteHelper(this.getApplicationContext());
			if(InternetCheck.isInternetOn1(this.getApplicationContext())) {

				jsonmall=srmall.GetMall(db.getUid(),db.getToken());
				if(jsonmall.optString("success").contains("true")) {
					db.tbl_mall_delete();
					db.tbl_savemall(jsonmall);
				}


				jsonshops=srshops.GetShopsfromMall(db.getUid(), db.getToken());
				if (jsonshops.optString("success").contains("true")){
					db.tbl_shops_delete();
					db.tbl_saveshops(jsonshops);
				}

				jsondeals=srdeals.GetDealsfromMall(db.getUid(), db.getToken());
				if(jsondeals.optString("success").contains("true")){
					db.tbl_deals_delete();
					db.tbl_savedeals(jsondeals);
				}

				jsonevents=srevents.GetEventsfromMall(db.getUid(),db.getToken());
				if(jsonevents.optString("success").contains("true")){
					db.tbl_events_delete();
					db.tbl_saveevents(jsonevents);
				}



			}

			db.close();
		}catch (Exception e){
			Log.e("Exception", "Popular json webservice" + e.toString());
		}
	}
	

	
	 // Check the db table field is existing or not
	private boolean field_exists(String p_query) {
		Cursor mCursor = newDB.rawQuery(p_query, null);
		if ((mCursor != null) && (mCursor.moveToFirst())) {
			mCursor.close();
			return true;
		}
		mCursor.close();
		return false;
	}

	 // Register the device to Beacon server
	private void registerDevice(MySQLiteHelper db){
		String device = Secure.getString(SplashScreen.this.getContentResolver(), Secure.ANDROID_ID);
		System.out.println("device id:" + device);
    	ServiceRequest srv = new ServiceRequest();
		String gtoken=db.getgcmToken();
		String gtoken2= gcmtoken;
		srv.token_request(device, SplashScreen.this,gtoken);
		//db.setgcmToken(gcmtoken);
    	String uid = srv.getUid();
    	String token = srv.getToken();
    	if(uid != null && token != null){
			db.setUid(uid);
			db.setToken(token);
    	}
	}

    @Override
	public void onResume() {				
		super.onResume();

		// register GCM registration complete receiver
		LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
				new IntentFilter(Config.REGISTRATION_COMPLETE));

		// register new push message receiver
		// by doing this, the activity will be notified each time a new message arrives
		LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
				new IntentFilter(Config.PUSH_NOTIFICATION));

	}

	@Override
	protected void onPause() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
		super.onPause();
	}
    
    @Override
	public void onDestroy() {
		super.onDestroy();			
	}	
}
