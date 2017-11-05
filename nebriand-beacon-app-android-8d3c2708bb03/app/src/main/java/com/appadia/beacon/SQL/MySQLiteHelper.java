package com.appadia.beacon.SQL;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.appadia.beacon.Fragments.Mall;
import com.appadia.beacon.Models.DealModel;
import com.appadia.beacon.Models.EventModel;
import com.appadia.beacon.Models.MallModel;
import com.appadia.beacon.Models.ShopModel;
import com.appadia.beacon.Models.SocialModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
import com.realy.models.AutoComplete;
import com.realy.models.CheckFbAuth;
import com.realy.models.Clustering;
import com.realy.models.GetSavedSearches;
import com.realy.models.Location;
import com.realy.models.Param;
import com.realy.models.Params;
import com.realy.models.Photo;
import com.realy.models.Realtor;
import com.realy.models.RealyHome;
import com.realy.models.SearchNearbyAgents;
import com.realy.models.listings.BLB;*/

public class MySQLiteHelper extends SQLiteOpenHelper {

	 // Defined the database version, name and declare the variables
	private static final int DATABASE_VERSION = 15;
	private static final String DB_NAME = "beacon.sqlite";
	private static SQLiteDatabase myDataBase;
	String id, uid,gcmtoken;
	public MySQLiteHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);

	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		// Stored UID and Token
		db.execSQL("CREATE TABLE IF NOT EXISTS tbl_preferences (Id INTEGER NOT NULL PRIMARY KEY, Token TEXT, Uid TEXT,gcmtoken TEXT)");

		
		// My Service Stored timestamp
		db.execSQL("create TABLE IF NOT EXISTS tbl_timeStamp(id Integer not null primary key,fld_timeStamp text,fld_listing_timeStamp text)");

		//Mall from json
		db.execSQL("create TABLE IF NOT EXISTS tbl_mall(id Integer not null primary key,mall_name text, type text, phone_no text, description text, website text, parking text, cinema text,email text,opening_days text, address text,images text,amenitiesshort text,amenitieslong text,amenitiescount Integer )");
				

		// App register in Beacon.co
		db.execSQL("create TABLE IF NOT EXISTS tbl_registerStatus(id Integer not null primary key,fld_register_status text,fld_request_status text)");

		//Deals
		db.execSQL("create TABLE IF NOT EXISTS tbl_deals2(id Integer not null primary key,type varchar, title varchar,start_day datetime,end_day datetime,description varchar,coupons varchar,event_where varchar,active tinyint,ts datetime,last_update datetime)");

		//Deals from json
		db.execSQL("create TABLE IF NOT EXISTS tbl_deals(id Integer not null primary key, type text, deal_title text,start_day text, end_date text,start_time text,end_time text, description text, coupons text, event_where text, active text, category text, shops text,images text, saved_status text )");

		//events from json
		db.execSQL("create TABLE IF NOT EXISTS tbl_events(id Integer not null primary key, type text, event_title text,start_day text, end_date text,start_time text,end_time text, description text, coupons text, event_where text, active text, category text, shops text,images text, saved_status text )");


		//Shops from json
		db.execSQL("create TABLE IF NOT EXISTS tbl_shops(id Integer not null primary key,shop_name text,phone_no text,description text,website text,floor text,type text,email text,category text,social text,open_hours text,images text,floorplan text,beacons text,saved_status text,notify_status text )");

		//Social from json
		db.execSQL("create TABLE IF NOT EXISTS tbl_social(id Integer not null primary key,shopid Integer,mallid Integer,sm_name text, sm_link text )");


		//check fb auth		
		//db.execSQL("CREATE TABLE if not exists tbl_checkfbauth(Id INTEGER NOT NULL PRIMARY KEY, fld_fbid INTEGER, fld_name text, fld_link text, fld_pic_link text,fld_email text)");
		
		db.execSQL("CREATE TABLE if not exists tbl_checkfbauth(Id INTEGER NOT NULL PRIMARY KEY,fld_consumer_id INTEGER,fld_con_user_id INTEGER, fld_fbid INTEGER, fld_name text, fld_link text, fld_pic_link text,fld_email text)");

		}	

	 // Upgrade the database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {			

		onCreate(db);				
	}


		
	 // Store the device registration details
	public void tbl_registerStatus_insert(String _registerStatus,
			String _requestStatus) {
		myDataBase = getWritableDatabase();
		myDataBase.execSQL("insert into tbl_registerStatus(fld_register_status,fld_request_status) values('" + _registerStatus + "','" + _requestStatus + "')");
	}

	 // Select the registration details from table
	public String tbl_registerStatus_select() {
		myDataBase = getWritableDatabase();
		String register_status = null;
		Cursor c = myDataBase.rawQuery(
				"select fld_register_status from tbl_registerStatus", null);
		if (c != null && c.getCount() == 1 && c.moveToFirst()) {
			register_status = c.getString(c
					.getColumnIndex("fld_register_status"));
		}
		myDataBase.close();
		return register_status;
	}

	// Select the token from table
	public String getToken() {
		String token = new String();
		try {
			myDataBase = getWritableDatabase();
			Cursor c = myDataBase.rawQuery("select count(ID)as recount from tbl_preferences", null);
			if (c != null && c.getCount() == 1 && c.moveToFirst()) {
				id = c.getString(c.getColumnIndex("recount"));

			}
			if (!id.equals("0")) {
				c = myDataBase.rawQuery("select Token from tbl_preferences",
						null);
				if (c != null && c.getCount() == 1 && c.moveToFirst()) {
					token = c.getString(c.getColumnIndex("Token"));
				}
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}

	// Store the device token details  
	public void setToken(String s) {
		myDataBase = getWritableDatabase();
		Cursor c = myDataBase.rawQuery(
				"select count(ID)as recount from tbl_preferences", null);
		if (c != null && c.getCount() == 1 && c.moveToFirst()) {
			id = c.getString(c.getColumnIndex("recount"));
		}
		if (id.equals("0")) {
			myDataBase.execSQL("insert into tbl_preferences(Token) values('"
					+ s + "')");
			System.out.println(" inser new uid ");			
		} else {
			System.out.println(" update new uid ");
			myDataBase.execSQL("UPDATE tbl_preferences SET Token='" + s + "'");
		}
	}


	// Select the gcmtoken from table
	public String getgcmToken() {
		String token = new String();
		try {
			myDataBase = getWritableDatabase();
			Cursor c = myDataBase.rawQuery("select count(ID)as recount from tbl_preferences", null);
			if (c != null && c.getCount() == 1 && c.moveToFirst()) {
				id = c.getString(c.getColumnIndex("recount"));

			}
			if (!id.equals("0")) {
				c = myDataBase.rawQuery("select gcmtoken from tbl_preferences",
						null);
				if (c != null && c.getCount() == 1 && c.moveToFirst()) {
					gcmtoken = c.getString(c.getColumnIndex("gcmtoken"));
				}
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gcmtoken;
	}

	public Boolean ExistShopbyId(String shopid){
		Boolean shopexists=false;

		try {
			myDataBase = getWritableDatabase();
			Cursor c = myDataBase.rawQuery("select count(ID)as recount from tbl_deals", null);
			if (c != null && c.getCount() == 1 && c.moveToFirst()) {
				id = c.getString(c.getColumnIndex("recount"));

			}
			if (!id.equals("0")) {
				c = myDataBase.rawQuery("select count(id) from tbl_shops where id= '" + shopid + "'",
						null);
				if (c != null && c.getCount() == 1) {
					shopexists = true;
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}


		return shopexists;
	}


	//Get an event by its ID, Return EventModel
	public EventModel getEventbyId(String eventid) {

		EventModel eventModel= new EventModel();
		try {
			myDataBase = getWritableDatabase();
			Cursor c = myDataBase.rawQuery("select count(ID)as recount from tbl_deals", null);
			if (c != null && c.getCount() == 1 && c.moveToFirst()) {
				id = c.getString(c.getColumnIndex("recount"));

			}
			if (!id.equals("0")) {
				c = myDataBase.rawQuery("select * from tbl_events where id= '" + eventid +"'",
						null);
				if (c != null && c.getCount() == 1 && c.moveToFirst()) {
					eventModel.setEventid(c.getInt(c.getColumnIndex("id")));
					eventModel.setType(c.getString(c.getColumnIndex("type")));
					eventModel.setEvent_title(c.getString(c.getColumnIndex("event_title")));
					eventModel.setStart_day(c.getString(c.getColumnIndex("start_day")));
					eventModel.setEnd_day(c.getString(c.getColumnIndex("end_date")));
					eventModel.setStart_time(c.getString(c.getColumnIndex("start_time")));
					eventModel.setEnd_time(c.getString(c.getColumnIndex("end_time")));
					eventModel.setDescription(c.getString(c.getColumnIndex("description")));
					eventModel.setCoupons(c.getString(c.getColumnIndex("coupons")));
					eventModel.setEvent_where(c.getString(c.getColumnIndex("event_where")));
					eventModel.setActive(c.getString(c.getColumnIndex("active")));
					eventModel.setCategory(c.getString(c.getColumnIndex("category")));
					eventModel.setShops(c.getString(c.getColumnIndex("shops")));
					eventModel.setImages(c.getString(c.getColumnIndex("images")));
					eventModel.setSaved_status(c.getString(c.getColumnIndex("saved_status")));
				}
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventModel;
	}



	//Get a Deal by its ID, return a DealModel
	public DealModel getDealbyId(String dealid) {

		DealModel deal= new DealModel();
		try {
			myDataBase = getWritableDatabase();
			Cursor c = myDataBase.rawQuery("select count(ID)as recount from tbl_deals", null);
			if (c != null && c.getCount() == 1 && c.moveToFirst()) {
				id = c.getString(c.getColumnIndex("recount"));

			}
			if (!id.equals("0")) {
				c = myDataBase.rawQuery("select * from tbl_deals where id= '" + dealid +"'",
						null);
				if (c != null && c.getCount() == 1 && c.moveToFirst()) {
					deal.setDealid(c.getInt(c.getColumnIndex("id")));
					deal.setType(c.getString(c.getColumnIndex("type")));
					deal.setDeal_title(c.getString(c.getColumnIndex("deal_title")));
					deal.setStart_day(c.getString(c.getColumnIndex("start_day")));
					deal.setEnd_day(c.getString(c.getColumnIndex("end_date")));
					deal.setStart_time(c.getString(c.getColumnIndex("start_time")));
					deal.setEnd_time(c.getString(c.getColumnIndex("end_time")));
					deal.setDescription(c.getString(c.getColumnIndex("description")));
					deal.setCoupons(c.getString(c.getColumnIndex("coupons")));
					deal.setEvent_where(c.getString(c.getColumnIndex("event_where")));
					deal.setActive(c.getString(c.getColumnIndex("active")));
					deal.setCategory(c.getString(c.getColumnIndex("category")));
					deal.setShops(c.getString(c.getColumnIndex("shops")));
					deal.setImages(c.getString(c.getColumnIndex("images")));
					deal.setSaved_status(c.getString(c.getColumnIndex("saved_status")));
				}
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deal;
	}


	//Get all the social links attached to a shop (by the id shop) , returns an ArrayList of SocialModel
	public ArrayList<SocialModel> tbl_getsocialbyshop(String shopid){
		ArrayList<SocialModel> sociallist= new ArrayList<SocialModel>();
		try {
			myDataBase = getWritableDatabase();
			Cursor c = myDataBase.rawQuery("select count(ID)as recount from tbl_social", null);
			if (c != null && c.getCount() == 1 && c.moveToFirst()) {
				id = c.getString(c.getColumnIndex("recount"));

			}
			if (!id.equals("0")) {
				c = myDataBase.rawQuery("Select * from tbl_social where shopid= '" + shopid + "'", null);
				if (c != null) {
					if (c.moveToFirst()) {
						do {
							int socialid = Integer.parseInt(c.getString(c.getColumnIndex("id"))); //Control if the id is collected as an integer or not
							int shopidaux = Integer.parseInt(shopid);
							int mallidaux=c.getInt(c.getColumnIndex("mallid"));
							String sm_name = c.getString(c.getColumnIndex("sm_name"));
							String sm_link = c.getString(c.getColumnIndex("sm_link"));

							SocialModel social = new SocialModel(socialid,mallidaux, shopidaux, sm_name, sm_link);
							sociallist.add(social);

						} while (c.moveToNext());

					}
				}
			}
			c.close();
			myDataBase.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return sociallist;

	}

	//Get all the social links attached to a Mall (by the id mall) , returns an ArrayList of SocialModel
	public ArrayList<SocialModel> tbl_getsocialbymall(int mallid){
		ArrayList<SocialModel> sociallist= new ArrayList<SocialModel>();
		try {
			myDataBase = getWritableDatabase();
			Cursor c = myDataBase.rawQuery("select count(ID)as recount from tbl_social", null);
			if (c != null && c.getCount() == 1 && c.moveToFirst()) {
				id = c.getString(c.getColumnIndex("recount"));

			}
			if (!id.equals("0")) {
				c = myDataBase.rawQuery("Select * from tbl_social where mallid= '" + mallid + "'", null);
				if (c != null) {
					if (c.moveToFirst()) {
						do {
							int socialid = Integer.parseInt(c.getString(c.getColumnIndex("id"))); //Control if the id is collected as an integer or not
							int shopidaux = c.getInt(c.getColumnIndex("shopid"));
							int mallidaux=mallid;
							String sm_name = c.getString(c.getColumnIndex("sm_name"));
							String sm_link = c.getString(c.getColumnIndex("sm_link"));

							SocialModel social = new SocialModel(socialid,mallidaux, shopidaux, sm_name, sm_link);
							sociallist.add(social);

						} while (c.moveToNext());

					}
				}
			}
			c.close();
			myDataBase.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return sociallist;

	}


	//Get a mall by its ID, returns a MallModel
	public MallModel getMallbyId(int mallid) {

		MallModel mall= new MallModel();
		try {
			myDataBase = getWritableDatabase();
			Cursor c = myDataBase.rawQuery("select count(ID)as recount from tbl_mall", null);
			if (c != null && c.getCount() == 1 && c.moveToFirst()) {
				id = c.getString(c.getColumnIndex("recount"));

			}
			if (!id.equals("0")) {
				c = myDataBase.rawQuery("select * from tbl_mall where id= '" + mallid +"'",
						null);
				if (c != null && c.getCount() == 1 && c.moveToFirst()) {

					mall.setMallid(Integer.parseInt(c.getString(c.getColumnIndex("id")))); //Control if the id is collected as an integer or not
					mall.setMall_name(c.getString(c.getColumnIndex("mall_name")));
					mall.setType(c.getString(c.getColumnIndex("type")));
					mall.setPhone_no(c.getString(c.getColumnIndex("phone_no")));
					mall.setDescription(c.getString(c.getColumnIndex("description")));
					mall.setWebsite(c.getString(c.getColumnIndex("website")));
					mall.setParking(c.getString(c.getColumnIndex("parking")));
					mall.setCinema(c.getString(c.getColumnIndex("cinema")));
					mall.setEmail(c.getString(c.getColumnIndex("email")));
					mall.setOpening_days(c.getString(c.getColumnIndex("opening_days")));
					mall.setAddress(c.getString(c.getColumnIndex("address")));
					mall.setImages(c.getString(c.getColumnIndex("images")));
					mall.setAmenitiesshort(c.getString(c.getColumnIndex("amenitiesshort")));
					mall.setAmenitieslong(c.getString(c.getColumnIndex("amenitieslong")));
					mall.setAmenities_count(Integer.parseInt(c.getString(c.getColumnIndex("amenitiescount"))));
				}
			}
			c.close();
			myDataBase.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mall;
	}



	//Get a shop by its Id, returns a ShopModel
	public ShopModel getShopbyId(String shopid) {

		ShopModel shop= new ShopModel();
		try {
			myDataBase = getWritableDatabase();
			Cursor c = myDataBase.rawQuery("select count(ID)as recount from tbl_shops", null);
			if (c != null && c.getCount() == 1 && c.moveToFirst()) {
				id = c.getString(c.getColumnIndex("recount"));

			}
			if (!id.equals("0")) {
				c = myDataBase.rawQuery("select * from tbl_shops where id= '" + shopid +"'",
						null);
				if (c != null && c.getCount() == 1 && c.moveToFirst()) {
					shop.setShopid(c.getInt(c.getColumnIndex("id")));
					shop.setShop_name(c.getString(c.getColumnIndex("shop_name")));
					shop.setPhone_no(c.getString(c.getColumnIndex("phone_no")));
					shop.setDescription(c.getString(c.getColumnIndex("description")));
					shop.setWebsite(c.getString(c.getColumnIndex("website")));
					shop.setFloor(c.getString(c.getColumnIndex("floor")));
					shop.setType(c.getString(c.getColumnIndex("type")));
					shop.setEmail(c.getString(c.getColumnIndex("email")));
					shop.setCategory(c.getString(c.getColumnIndex("category")));
					shop.setSocial(c.getString(c.getColumnIndex("social")));
					shop.setOpen_hours(c.getString(c.getColumnIndex("open_hours")));
					shop.setImages(c.getString(c.getColumnIndex("images")));
					shop.setFloorplan(c.getString(c.getColumnIndex("floorplan")));
					shop.setBeacons(c.getString(c.getColumnIndex("beacons")));
					shop.setSaved_status(c.getString(c.getColumnIndex("saved_status")));
					shop.setNotify_status(c.getString(c.getColumnIndex("notify_status")));
				}
			}
			c.close();
			myDataBase.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return shop;
	}

	public MallModel getMallbyId2(String mallid) {

		MallModel mall= new MallModel();
		try {
			myDataBase = getWritableDatabase();
			Cursor c = myDataBase.rawQuery("select count(ID)as recount from tbl_mall", null);
			if (c != null && c.getCount() == 1 && c.moveToFirst()) {
				id = c.getString(c.getColumnIndex("recount"));

			}
			if (!id.equals("0")) {
				c = myDataBase.rawQuery("select * from tbl_mall where id= '" + mallid +"'",
						null);
				if (c != null && c.getCount() == 1 && c.moveToFirst()) {
					mall.setMallid(c.getInt(c.getColumnIndex("id")));
					mall.setMall_name(c.getString(c.getColumnIndex("mall_name")));
					mall.setType(c.getString(c.getColumnIndex("type")));
					mall.setPhone_no(c.getString(c.getColumnIndex("phone_no")));
					mall.setDescription(c.getString(c.getColumnIndex("description")));
					mall.setWebsite(c.getString(c.getColumnIndex("website")));
					mall.setParking(c.getString(c.getColumnIndex("parking")));
					mall.setCinema(c.getString(c.getColumnIndex("cinema")));
					mall.setEmail(c.getString(c.getColumnIndex("email")));
					mall.setOpening_days(c.getString(c.getColumnIndex("opening_days")));
					mall.setAddress(c.getString(c.getColumnIndex("address")));
					mall.setImages(c.getString(c.getColumnIndex("images")));
					mall.setAmenitiesshort(c.getString(c.getColumnIndex("amenitiesshort")));
					mall.setAmenitieslong(c.getString(c.getColumnIndex("amenitieslong")));
					mall.setAmenities_count(c.getInt(c.getColumnIndex("amenities_count")));
				}
			}
			c.close();
			myDataBase.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mall;
	}


	// Store the device token details
	public void setgcmToken(String s) {
		myDataBase = getWritableDatabase();
		Cursor c = myDataBase.rawQuery(
				"select count(ID)as recount from tbl_preferences", null);
		if (c != null && c.getCount() == 1 && c.moveToFirst()) {
			id = c.getString(c.getColumnIndex("recount"));
		}
		if (id.equals("0")) {
			myDataBase.execSQL("insert into tbl_preferences(gcmtoken) values('"
					+ s + "')");
			System.out.println(" inser new uid ");
		} else {
			System.out.println(" update new uid ");
			myDataBase.execSQL("UPDATE tbl_preferences SET gcmtoken='" + s + "'");
		}
	}

	// Select the UserId  
	public String getUid() {
		myDataBase = getWritableDatabase();
		Cursor c = myDataBase.rawQuery("select Uid from tbl_preferences", null);
		if (c != null && c.getCount() == 1 && c.moveToFirst()) {
			uid = c.getString(c.getColumnIndex("Uid"));
		}
		return uid;
	}

	// Store the UserId details  
	public void setUid(String s) {
		myDataBase = getWritableDatabase();
		Cursor c = myDataBase.rawQuery(
				"select count(ID)as recount from tbl_preferences", null);
		if (c != null && c.getCount() == 1 && c.moveToFirst()) {
			id = c.getString(c.getColumnIndex("recount"));
		}
		if (id.equals("0")) {
			System.out.println(" insert new token ");
			myDataBase.execSQL("insert into tbl_preferences(Uid) values('" + s
					+ "')");
		}else{
			System.out.println(" update new token ");
			myDataBase.execSQL("UPDATE tbl_preferences SET Uid='" + s + "'");
		}
	}




		
	// Store the location details  
	public void tbl_permission_insert(String accessLocation) {
		myDataBase = getWritableDatabase();
		myDataBase
				.execSQL("insert into tbl_permission(fld_locationAccess) values('"
						+ accessLocation + "')");
	}



	// Update the location access details  
	public void tbl_permission_update(String accessLocation) {
		myDataBase = getWritableDatabase();
		myDataBase.execSQL("UPDATE tbl_permission SET fld_locationAccess = '"
				+ accessLocation + "'");
	}





	// Select the notifications details
	public String tbl_notifications_readstatus_select(String nid){
		String readstatus = "0";
		Cursor c = myDataBase.rawQuery("select fld_readstatus from tbl_notifications_readstatus where fld_notifid="+ nid +"", null);
		if (c.moveToFirst()) {
			do {
				try {
					readstatus = c.getString(c.getColumnIndex("fld_readstatus"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} while (c.moveToNext());
		}
		c.close();
		return readstatus;
	}

	//Select all the events
	public ArrayList<EventModel> tbl_getallevents(){
		ArrayList<EventModel> eventlist= new ArrayList<EventModel>();
		myDataBase = getWritableDatabase();
		Cursor c= null;
		c=myDataBase.rawQuery("Select * from tbl_events",null);
		if(c != null){
			if(c.moveToFirst()){
				do {
					int eventid = Integer.parseInt(c.getString(c.getColumnIndex("id"))); //Control if the id is collected as an integer or not
					String type = c.getString(c.getColumnIndex("type"));
					String event_title = c.getString(c.getColumnIndex("event_title"));
					String start_day = c.getString(c.getColumnIndex("start_day"));
					String end_day = c.getString(c.getColumnIndex("end_date"));
					String start_time = c.getString(c.getColumnIndex("start_time"));
					String end_time = c.getString(c.getColumnIndex("end_time"));
					String description = c.getString(c.getColumnIndex("description"));
					String coupons = c.getString(c.getColumnIndex("coupons"));
					String event_where = c.getString(c.getColumnIndex("event_where"));
					String active = c.getString(c.getColumnIndex("active"));
					String category = c.getString(c.getColumnIndex("category"));
					String shops = c.getString(c.getColumnIndex("shops"));
					String images = c.getString(c.getColumnIndex("images"));
					String saved_status = c.getString(c.getColumnIndex("saved_status"));
					EventModel event = new EventModel(eventid, type, event_title, start_day, end_day, start_time, end_time, description, coupons, event_where, active, category, shops, images, saved_status);
					eventlist.add(event);
				} while(c.moveToNext());
			}
		}
		c.close();
		myDataBase.close();
		return eventlist;
	}


	//Select all the deals
	public ArrayList<DealModel> tbl_getalldeals(){
		ArrayList<DealModel> dealslist= new ArrayList<DealModel>();
		myDataBase = getWritableDatabase();
		Cursor c= null;
		c=myDataBase.rawQuery("Select * from tbl_deals",null);
		if(c != null){
			if(c.moveToFirst()){
				do {
					int dealid = Integer.parseInt(c.getString(c.getColumnIndex("id"))); //Control if the id is collected as an integer or not
					String type = c.getString(c.getColumnIndex("type"));
					String deal_title = c.getString(c.getColumnIndex("deal_title"));
					String start_day = c.getString(c.getColumnIndex("start_day"));
					String end_day = c.getString(c.getColumnIndex("end_date"));
					String start_time = c.getString(c.getColumnIndex("start_time"));
					String end_time = c.getString(c.getColumnIndex("end_time"));
					String description = c.getString(c.getColumnIndex("description"));
					String coupons = c.getString(c.getColumnIndex("coupons"));
					String event_where = c.getString(c.getColumnIndex("event_where"));
					String active = c.getString(c.getColumnIndex("active"));
					String category = c.getString(c.getColumnIndex("category"));
					String shops = c.getString(c.getColumnIndex("shops"));
					String images = c.getString(c.getColumnIndex("images"));
					String saved_status = c.getString(c.getColumnIndex("saved_status"));
					DealModel deal = new DealModel(dealid, type, deal_title, start_day, end_day, start_time, end_time, description, coupons, event_where, active, category, shops, images, saved_status);
					dealslist.add(deal);
				} while(c.moveToNext());
			}
		}
		c.close();
		myDataBase.close();
		return dealslist;
	}

	//Select all my the deals
	public ArrayList<DealModel> tbl_getallsavedeals(){
		myDataBase=getWritableDatabase();
		ArrayList<DealModel> dealslist= new ArrayList<DealModel>();
		ServiceRequest sr= new ServiceRequest();
		JSONObject mydeals= sr.getMyDeals(getUid(),getToken());
		if(mydeals.optString("success").contains("true")){
			try {
				JSONObject aux = new JSONObject();
				aux = mydeals.getJSONObject("data");
				JSONArray arraymydeals = aux.optJSONArray("deals");
				if (arraymydeals != null) {
					for (int i = 0; i < arraymydeals.length(); i++) {
						String dealid= arraymydeals.optJSONObject(i).optString("id");
						DealModel deal= new DealModel();
						deal=getDealbyId(dealid);
						dealslist.add(deal);
					}
				}
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}


		myDataBase.close();
		return dealslist;
	}


	//Save all the deals from a json object to the database
	public void tbl_savedeals(JSONObject json){
		myDataBase=getWritableDatabase();
		ArrayList<DealModel> dealslist= new ArrayList<DealModel>();
		if(json.optString("success").equals("true")){
			try{
				String timestamp= null;
				try{
					timestamp=json.getString("ts");
				}catch(JSONException e){
					e.printStackTrace();
				}
				//if it doesn't work, obtain first a json by "data" and later do this step
				JSONObject aux= new JSONObject();
				aux=json.getJSONObject("data");
				JSONArray arraydeals= aux.optJSONArray("deals");
				if (arraydeals != null){
					for(int i=0;i< arraydeals.length();i++){
						String dealid= arraydeals.optJSONObject(i).optString("id");
						String type= arraydeals.optJSONObject(i).optString("type");
						String deal_title= arraydeals.optJSONObject(i).optString("deal_title");
						String start_day= arraydeals.optJSONObject(i).optString("start_day");
						String end_day= arraydeals.optJSONObject(i).optString("end_day");
						String start_time= arraydeals.optJSONObject(i).optString("start_time");
						String end_time= arraydeals.optJSONObject(i).optString("end_time");
						String description= arraydeals.optJSONObject(i).optString("description");
						String coupons= arraydeals.optJSONObject(i).optString("coupons");
						String event_where= arraydeals.optJSONObject(i).optString("event_where");
						String active= arraydeals.optJSONObject(i).optString("active");

						//Save the categories of the Deal
						JSONArray categories=new JSONArray();
						JSONObject jsoncategory=new JSONObject();
						categories=arraydeals.optJSONObject(i).getJSONArray("category");

						String category="";// arraydeals.optJSONObject(i).optString("category");
						for(int j=0;j< categories.length();j++){
							category= category + categories.optJSONObject(j).optString("category_name");
						}
						//Save the shop id and check if that shop already exist in your database
						JSONArray shopsarray=new JSONArray();
						JSONObject jsonshop=new JSONObject();
						shopsarray=arraydeals.optJSONObject(i).getJSONArray("shops");
						String shops= shopsarray.optJSONObject(0).optString("id");
						if(!ExistShopbyId(shops)){
							ServiceRequest sr= new ServiceRequest();
							JSONObject shoptosave= sr.GetShopfromMallbyId(getUid(),getToken(),shops);
							tbl_saveshops(shoptosave);
						}
						//Small code to try how to save the images, it would be better to create a class and table with all the images
						JSONArray pics=new JSONArray();
						JSONObject picture= new JSONObject();
						pics=arraydeals.optJSONObject(i).getJSONArray("images");
						String images="";
						if(pics.length()>0) {
							picture = pics.getJSONObject(0);
							images = picture.getString("file");
						}
						String saved_status= arraydeals.optJSONObject(i).optString("saved_status");


						ContentValues values = new ContentValues();
						values.put("id",dealid);
						values.put("type",type);
						values.put("deal_title",deal_title);
						values.put("start_day",start_day);
						values.put("end_date",end_day);
						values.put("start_time",start_time);
						values.put("end_time",end_time);
						values.put("description",description);
						values.put("coupons",coupons);
						values.put("event_where",event_where);
						values.put("active",active);
						values.put("category",category);
						values.put("shops",shops);
						values.put("images",images);
						values.put("saved_status",saved_status);

						myDataBase.insert("tbl_deals",null,values);

						/*
						myDataBase.execSQL("insert into tbl_deals (id,type,deal_title,start_day,end_date,start_time,end_time,description,coupons,event_where,active,category,shops,images,saved_status) values('"
							+ dealid
							+ "','"
							+ type
								+ "','"
								+ deal_title
								+ "','"
								+ start_day
								+ "','"
								+ end_day
								+ "','"
								+ start_time
								+ "','"
								+ end_time
								+ "','"
								+ description
								+ "','"
								+ coupons
								+ "','"
								+ event_where
								+ "','"
								+ active
								+ "','"
								+ category
								+ "','"
								+ shops
								+ "','"
								+ images
								+ "','"
								+ saved_status + "')"); */

					}


				}
			}catch(NullPointerException e){
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	//Save all the events from a json object to the database
	public void tbl_saveevents(JSONObject json){
		myDataBase=getWritableDatabase();
		ArrayList<EventModel> eventlist= new ArrayList<EventModel>();
		if(json.optString("success").equals("true")){
			try{
				String timestamp= null;
				try{
					timestamp=json.getString("ts");
				}catch(JSONException e){
					e.printStackTrace();
				}

				JSONObject aux= new JSONObject();
				aux=json.getJSONObject("data");
				JSONArray arraydeals= aux.optJSONArray("deals");
				if (arraydeals != null){
					for(int i=0;i< arraydeals.length();i++){
						String eventid= arraydeals.optJSONObject(i).optString("id");
						String type= arraydeals.optJSONObject(i).optString("type");
						String event_title= arraydeals.optJSONObject(i).optString("deal_title");
						String start_day= arraydeals.optJSONObject(i).optString("start_day");
						String end_day= arraydeals.optJSONObject(i).optString("end_day");
						String start_time= arraydeals.optJSONObject(i).optString("start_time");
						String end_time= arraydeals.optJSONObject(i).optString("end_time");
						String description= arraydeals.optJSONObject(i).optString("description");
						String coupons= arraydeals.optJSONObject(i).optString("coupons");
						String event_where= arraydeals.optJSONObject(i).optString("event_where");
						String active= arraydeals.optJSONObject(i).optString("active");

						//Save the categories of the Deal
						JSONArray categories=new JSONArray();
						JSONObject jsoncategory=new JSONObject();
						categories=arraydeals.optJSONObject(i).getJSONArray("category");

						String category="";// arraydeals.optJSONObject(i).optString("category");
						for(int j=0;j< categories.length();j++){
							category= category + categories.optJSONObject(j).optString("category_name");
						}
						//Save the shop id and check if that shop already exist in your database
						JSONArray shopsarray=new JSONArray();
						JSONObject jsonshop=new JSONObject();
						shopsarray=arraydeals.optJSONObject(i).getJSONArray("shops");
						String shops= shopsarray.optJSONObject(0).optString("id");
						if(!ExistShopbyId(shops)){
							ServiceRequest sr= new ServiceRequest();
							JSONObject shoptosave= sr.GetShopfromMallbyId(getUid(),getToken(),shops);
							tbl_saveshops(shoptosave);
						}
						//Small code to try how to save the images, it would be better to create a class and table with all the images
						JSONArray pics=new JSONArray();
						JSONObject picture= new JSONObject();
						pics=arraydeals.optJSONObject(i).getJSONArray("images");
						String images="";
						if(pics.length()>0) {
							picture = pics.getJSONObject(0);
							images = picture.getString("file");
						}
						String saved_status= arraydeals.optJSONObject(i).optString("saved_status");


						ContentValues values = new ContentValues();
						values.put("id",eventid);
						values.put("type",type);
						values.put("event_title",event_title);
						values.put("start_day",start_day);
						values.put("end_date",end_day);
						values.put("start_time",start_time);
						values.put("end_time",end_time);
						values.put("description",description);
						values.put("coupons",coupons);
						values.put("event_where",event_where);
						values.put("active",active);
						values.put("category",category);
						values.put("shops",shops);
						values.put("images",images);
						values.put("saved_status",saved_status);

						try {
							myDataBase.insertOrThrow("tbl_events", null, values);
						}
						catch(Exception e){
							e.printStackTrace();
						}



					}


				}
			}catch(NullPointerException e){
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}




	//Save all the shops from a json object to the database
	public ArrayList<ShopModel> tbl_getallshops(){
		ArrayList<ShopModel> shopslist= new ArrayList<ShopModel>();
		myDataBase = getWritableDatabase();
		Cursor c= null;
		c=myDataBase.rawQuery("Select * from tbl_shops",null);
		if(c != null){
			if(c.moveToFirst()){
				do {
					int shopid = Integer.parseInt(c.getString(c.getColumnIndex("id"))); //Control if the id is collected as an integer or not
					String shop_name = c.getString(c.getColumnIndex("shop_name"));
					String phone_no = c.getString(c.getColumnIndex("phone_no"));
					String description = c.getString(c.getColumnIndex("description"));
					String website = c.getString(c.getColumnIndex("website"));
					String floor = c.getString(c.getColumnIndex("floor"));
					String type = c.getString(c.getColumnIndex("type"));
					String email = c.getString(c.getColumnIndex("email"));
					String category = c.getString(c.getColumnIndex("category"));
					String social = c.getString(c.getColumnIndex("social"));
					String open_hours = c.getString(c.getColumnIndex("open_hours"));
					String images = c.getString(c.getColumnIndex("images"));
					String floorplan = c.getString(c.getColumnIndex("floorplan"));
					String beacons = c.getString(c.getColumnIndex("beacons"));
					String saved_status = c.getString(c.getColumnIndex("saved_status"));
					String notify_status = c.getString(c.getColumnIndex("notify_status"));
					ShopModel shop = new ShopModel(shopid,shop_name,phone_no,description,website,floor,type,email,category,social,open_hours,images,floorplan,beacons,saved_status,notify_status);
					shopslist.add(shop);
				} while(c.moveToNext());
			}
		}
		c.close();
		myDataBase.close();
		return shopslist;
	}

	//Select all the malls
	public ArrayList<MallModel> tbl_getallmalls(){
		ArrayList<MallModel> malllist= new ArrayList<MallModel>();
		myDataBase = getWritableDatabase();
		Cursor c= null;
		c=myDataBase.rawQuery("Select * from tbl_mall",null);
		if(c != null){
			if(c.moveToFirst()){
				do {
					int mallid = Integer.parseInt(c.getString(c.getColumnIndex("id"))); //Control if the id is collected as an integer or not
					String mall_name = c.getString(c.getColumnIndex("mall_name"));
					String type = c.getString(c.getColumnIndex("type"));
					String phone_no = c.getString(c.getColumnIndex("phone_no"));
					String description = c.getString(c.getColumnIndex("description"));
					String website = c.getString(c.getColumnIndex("website"));
					String parking = c.getString(c.getColumnIndex("parking"));
					String cinema = c.getString(c.getColumnIndex("cinema"));
					String email = c.getString(c.getColumnIndex("email"));
					String opening_days = c.getString(c.getColumnIndex("opening_days"));


					String address = c.getString(c.getColumnIndex("address"));
					String images = c.getString(c.getColumnIndex("images"));

					String amenitiesshort = c.getString(c.getColumnIndex("amenitiesshort"));
					String amenitieslong = c.getString(c.getColumnIndex("amenitieslong"));
					int amenitiescount= Integer.parseInt(c.getString(c.getColumnIndex("amenitiescount")));

					MallModel mallModel = new MallModel( mallid, amenitiescount,mall_name,type,phone_no,description,website,parking,cinema,email,opening_days,address,images,amenitiesshort,amenitieslong);
					malllist.add(mallModel);
				} while(c.moveToNext());
			}
		}
		c.close();
		myDataBase.close();
		return malllist;
	}

	//Save all the social from a json object to the database, it can be saved by the shop id or the mall id)
	public void tbl_savesocial(JSONArray socials, String shopid,String mallid){
		myDataBase=getWritableDatabase();
		try {
			if (socials.length() > 0) {
				for (int i = 0; i < socials.length(); i++) {
					if(shopid !=null) {
						String socialid = socials.optJSONObject(i).optString("id");
						String shop = shopid;
						String sm_name = socials.optJSONObject(i).optString("sm_name");
						String sm_link = socials.optJSONObject(i).optString("sm_link");


						ContentValues values = new ContentValues();
						values.put("id", socialid);
						values.put("shopid", shop);
						values.put("mallid",0);
						values.put("sm_name", sm_name);
						values.put("sm_link", sm_link);
						myDataBase.insert("tbl_social", null, values);
					}
					else{
						if(mallid !=null) {
							String socialid = socials.optJSONObject(i).optString("id");
							String mall = mallid;
							String sm_name = socials.optJSONObject(i).optString("sm_name");
							String sm_link = socials.optJSONObject(i).optString("sm_link");


							ContentValues values = new ContentValues();
							values.put("id", socialid);
							values.put("shopid", 0);
							values.put("mallid", mallid);
							values.put("sm_name", sm_name);
							values.put("sm_link", sm_link);
							myDataBase.insert("tbl_social", null, values);
						}
					}
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	//	myDataBase.close();

	}


	//Save all the shops from a json object to the database
	public void tbl_saveshops(JSONObject json){
		myDataBase=getWritableDatabase();
		ArrayList<ShopModel> shopslist= new ArrayList<ShopModel>();
		if(json.optString("success").equals("true")){
			try{
				String timestamp= null;
				try{
					timestamp=json.getString("ts");
				}catch(JSONException e){
					e.printStackTrace();
				}
				//if it doesn't work, obtain first a json by "data" and later do this step
				JSONObject aux= new JSONObject();
				aux=json.getJSONObject("data");
				JSONArray arrayshops= aux.optJSONArray("shops");
				if (arrayshops != null){
					for(int i=0;i< arrayshops.length();i++){
						String shopid= arrayshops.optJSONObject(i).optString("id");
						String shop_name= arrayshops.optJSONObject(i).optString("shop_name");
						String phone_no= arrayshops.optJSONObject(i).optString("phone_no");
						//String description="";
						String description= arrayshops.optJSONObject(i).optString("description").toString();
						//description.replace("'","\'");
						//String website="";
						String website= arrayshops.optJSONObject(i).optString("website").toString();
						//website.replace("'","\'");

						String floor= arrayshops.optJSONObject(i).optString("floor");
						String type= arrayshops.optJSONObject(i).optString("type");
						String email= arrayshops.optJSONObject(i).optString("email");
						//Save the categories of the Deal
						JSONArray categories=new JSONArray();
						JSONObject jsoncategory=new JSONObject();
						categories=arrayshops.optJSONObject(i).getJSONArray("category");
						String category="";// arraydeals.optJSONObject(i).optString("category");
						for(int j=0;j< categories.length();j++){
							category= category + " " + categories.optJSONObject(j).optString("category_name");
						}
						//category.replace("'","\'");
						//Save the socials in its own class, although maybe it will be better to create an array of the social class inside the shop instead of saving the id of the shop inside the social class
						String social= "";
						JSONArray socialarray=new JSONArray();
						JSONObject jsonsocial=new JSONObject();
						socialarray=arrayshops.optJSONObject(i).getJSONArray("social");
						tbl_savesocial(socialarray,shopid,null);
						//It will be better to create a class hours who can return an string with the hours formated
						String open_hours= "";
						JSONArray arrayhours=new JSONArray();
						JSONObject hours= new JSONObject();
						arrayhours=arrayshops.optJSONObject(i).getJSONArray("open_hours");

						if(arrayhours.length()>0) {
							for(int j=0;j< arrayhours.length();j++){
								open_hours= open_hours  + arrayhours.optJSONObject(j).optString("day") + " " +  arrayhours.optJSONObject(j).optString("from_time") + " to "  + arrayhours.optJSONObject(j).optString("to_time") + "\n\r" ;
							}
						}

						//Small code to try how to save the images, it would be better to create a class and table with all the images
						JSONArray pics=new JSONArray();
						JSONObject picture= new JSONObject();
						pics=arrayshops.optJSONObject(i).getJSONArray("images");
						String images="";
						if(pics.length()>0) {
							picture = pics.getJSONObject(0);
							images = picture.getString("file");
						}

						String floorplan="";
						String beacons="";

						String saved_status= arrayshops.optJSONObject(i).optString("saved_status");
						String notify_status= arrayshops.optJSONObject(i).optString("notify_status");

						ContentValues values = new ContentValues();
						values.put("id",shopid);
						values.put("shop_name",shop_name);
						values.put("phone_no",phone_no);
						values.put("description",description);
						values.put("website",website);
						values.put("floor",floor);
						values.put("type",type);
						values.put("email",email);
						values.put("category",category);
						values.put("social",social);
						values.put("open_hours",open_hours);
						values.put("images",images);
						values.put("floorplan",floorplan);
						values.put("beacons",beacons);
						values.put("saved_status",saved_status);
						values.put("notify_status",notify_status);
						myDataBase.insert("tbl_shops",null,values);
						/*
						myDataBase.execSQL("insert into tbl_shops (id,shop_name,phone_no,description,website,floor,type,email,category,social,open_hours,images,floorplan,beacons,saved_status,notify_status) values('"
								+ shopid
								+ "','"
								+ shop_name
								+ "','"
								+ phone_no
								+ "','"
								+ description
								+ "','"
								+ website
								+ "','"
								+ floor
								+ "','"
								+ type
								+ "','"
								+ email
								+ "','"
								+ category
								+ "','"
								+ social
								+ "','"
								+ open_hours
								+ "','"
								+ images
								+ "','"
								+ floorplan
								+ "','"
								+ beacons
								+ "','"
								+ saved_status
								+ "','"
								+ notify_status + "')");*/

					}

				}

				//myDataBase.close();
			}catch(NullPointerException e){
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	//Save all the malls from a json object to the database
	public void tbl_savemall(JSONObject json){
		myDataBase=getWritableDatabase();
		ArrayList<MallModel> malllist= new ArrayList<MallModel>();
		if(json.optString("success").equals("true")) {
			try {
				String timestamp = null;
				try {
					timestamp = json.getString("ts");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				//if it doesn't work, obtain first a json by "data" and later do this step
				JSONObject aux = new JSONObject();
				aux = json.getJSONObject("data");
				JSONArray arraymalls = aux.optJSONArray("malls");
				if (arraymalls != null) {
					for (int i = 0; i < arraymalls.length(); i++) {
						String mallid = arraymalls.optJSONObject(i).optString("id");
						String mall_name = arraymalls.optJSONObject(i).optString("mall_name");
						String type = arraymalls.optJSONObject(i).optString("type");
						//String description="";
						String phone_no = arraymalls.optJSONObject(i).optString("phone_no");
						//description.replace("'","\'");
						//String website="";
						String description = arraymalls.optJSONObject(i).optString("description");
						//website.replace("'","\'");

						String website = arraymalls.optJSONObject(i).optString("website");
						String parking = arraymalls.optJSONObject(i).optString("parking");
						String cinema = arraymalls.optJSONObject(i).optString("cinema");
						String email = arraymalls.optJSONObject(i).optString("email");
						String opening_days = arraymalls.optJSONObject(i).optString("opening_days");

						//Save the socials in its own class, although maybe it will be better to create an array of the social class inside the shop instead of saving the id of the shop inside the social class
						String social = "";
						JSONArray socialarray = new JSONArray();
						JSONObject jsonsocial = new JSONObject();
						socialarray = arraymalls.optJSONObject(i).getJSONArray("social");
						tbl_savesocial(socialarray, null, mallid);

						//It will be better to create a class address
						String address = "";
						JSONObject streetjson;
						streetjson = arraymalls.optJSONObject(i).getJSONObject("address");

						if (streetjson.length() > 0) {

							address = streetjson.optString("street") + "," + streetjson.optString("number") + "," + streetjson.optString("city") + "," + streetjson.optString("state") + "," + streetjson.optString("zip");

						}

						//Small code to try how to save the images, it would be better to create a class and table with all the images
						JSONArray pics = new JSONArray();
						JSONObject picture = new JSONObject();
						pics = arraymalls.optJSONObject(i).getJSONArray("images");
						String images = "";
						if (pics.length() > 0) {
							picture = pics.getJSONObject(0);
							images = picture.getString("file");
						}

						String floorplan = "";
						String beacons = "";
						String open_hours = "";
						//Amenities saved as a two strings, one as the short one saving just the 5 first amenities (to show as the InVisioProject and the other one containing all the amenities
						int amenitiescount = Integer.parseInt(arraymalls.optJSONObject(i).optString("amenities_count"));
						String amenitiesshort = "";
						String amenitieslong = "";
						JSONArray arrayamenities = new JSONArray();
						JSONObject amenitie = new JSONObject();
						arrayamenities = arraymalls.optJSONObject(i).getJSONArray("amenities");

						if (arrayamenities.length() > 0) {
							for (int j = 0; j < arrayamenities.length(); j++) {
								if (j <= 4) {
									amenitiesshort = amenitiesshort + arrayamenities.optJSONObject(j).optString("name") + "\n\r";
									amenitieslong = amenitiesshort;
								} else {
									amenitieslong = amenitieslong + arrayamenities.optJSONObject(j).optString("name") + "\n\r";
								}
							}
						}

						ContentValues values = new ContentValues();
						values.put("id", mallid);
						values.put("mall_name", mall_name);
						values.put("type", type);
						values.put("phone_no", phone_no);
						values.put("description", description);
						values.put("website", website);
						values.put("parking", parking);
						values.put("cinema", cinema);
						values.put("email", email);
						values.put("opening_days", opening_days);
						values.put("address", address);
						values.put("images", images);
						values.put("amenitiesshort", amenitiesshort);
						values.put("amenitieslong", amenitieslong);
						values.put("amenitiescount", amenitiescount);
						try {
							myDataBase.insertOrThrow("tbl_mall", null, values);
						}
						catch(Exception e){
							e.printStackTrace();
						}
					}


				}

				//myDataBase.close();
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch(Exception e){
				e.printStackTrace();
			}


		}
	}



	//Delete the deals
	public void tbl_deals_delete(){
		myDataBase=getWritableDatabase();
		myDataBase.execSQL("delete from tbl_deals");
	}

	public void tbl_events_delete(){
		myDataBase=getWritableDatabase();
		myDataBase.execSQL("delete from tbl_events");
	}


	//Delete the shops
	public void tbl_shops_delete(){
		myDataBase=getWritableDatabase();
		myDataBase.execSQL("delete from tbl_shops");
	}

	//Delete the mall
	public void tbl_mall_delete(){
		myDataBase=getWritableDatabase();
		myDataBase.execSQL("delete from tbl_mall");
	}


	 //Delete the fb details
	 public void tbl_checkfbauth_delete() {
		 myDataBase = getWritableDatabase();
		 String deleteLang = "delete from tbl_checkfbauth";
		 myDataBase.execSQL(deleteLang);
	 }
	
	//Insert the checkfbauth data in sqlite table	
	public void tbl_checkfbauth_insert(String fbid, String name, String link, String pic_link, String email) {
		myDataBase = getWritableDatabase();
		myDataBase
				.execSQL("insert into tbl_checkfbauth(fld_fbid,fld_name,fld_link,fld_pic_link,fld_email) values('"
						+ fbid
						+ "','"
						+ name
						+ "','"
						+ link
						+ "','"
						+ pic_link
						+ "','" + email + "')");

	}
	
	 //Select the fb details  	
	public ArrayList<CheckFbAuth> tbl_checkfbauth_select() {
		myDataBase = getWritableDatabase();
		ArrayList<CheckFbAuth> checkfbauth = new ArrayList<CheckFbAuth>();
		Cursor c = myDataBase.rawQuery("select * from tbl_checkfbauth", null);
		if (c.moveToFirst()) {
			do {
				try {
					checkfbauth.add(new CheckFbAuth(c.getString(c
							.getColumnIndex("fld_fbid")), c.getString(c
							.getColumnIndex("fld_name")), c.getString(c
							.getColumnIndex("fld_link")), c.getString(c
							.getColumnIndex("fld_pic_link")), c.getString(c
							.getColumnIndex("fld_email"))));
					//System.out.println("select fbid :" + c.getInt(c.getColumnIndex("fld_fbid")));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} while (c.moveToNext());
		}
		c.close();
		return checkfbauth;
	}
	




	

	// select the agents contact details 
	public ArrayList<String> getSocialNetworks(String _rid) {
		ArrayList<String> _socialnetworks = new ArrayList<String>();
		myDataBase = getWritableDatabase();
		Cursor c = myDataBase.rawQuery(
				"select * from tbl_social_networks where _rid='" + _rid + "'",
				null);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					String _facebook = c.getString(c
							.getColumnIndex("_facebook"));
					String _twitter = c.getString(c.getColumnIndex("_twitter"));
					String _linkedin = c.getString(c
							.getColumnIndex("_linkedin"));
					String _foursquare = c.getString(c
							.getColumnIndex("_foursquare"));
					String _yelp = c.getString(c.getColumnIndex("_yelp"));
					String _trulia = c.getString(c.getColumnIndex("_trulia"));
					String _zillow = c.getString(c.getColumnIndex("_zillow"));
					String _pinterest = c.getString(c.getColumnIndex("_pinterest"));
					_socialnetworks.add(_facebook);
					_socialnetworks.add(_twitter);
					_socialnetworks.add(_linkedin);
					_socialnetworks.add(_foursquare);
					_socialnetworks.add(_yelp);
					_socialnetworks.add(_trulia);
					_socialnetworks.add(_zillow);
					_socialnetworks.add(_pinterest);
				} while (c.moveToNext());

			}
		}
		c.close();
		myDataBase.close();
		return _socialnetworks;
	}
	

	


}
