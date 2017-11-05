package com.appadia.beacon.SQL;

import android.app.Activity;
import android.content.ContentValues;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ServiceRequest {
	 // Declared the variables, arrays and server details
	 private JSONParser jsonParser= new JSONParser();
	private String uid;
	private String token;
	private String devicetype = "android";
	private String apptype = "mall";
	private String umsid = "1";
	private String mallid= "1";
	private ContentValues postParams= new ContentValues();
	private ContentValues getParams= new ContentValues();
	private static String prefix = "http://beacon.realy.co/api/";
	public static String RegisterResponse, RequestResponse;


	// constructor
	// DO NOT MODIFY
	public ServiceRequest() {

	}

	// DO NOT MODIFY
	 // set the user id
	public void setUid(String p1) {
		if (p1.length() > 0)
			uid = p1;
	}

	public String getPrefix() {
		return prefix;
	}

	 // get the user id
	public String getUid() {
		return uid;
	}

	 // set the token id
	public void setToken(String p2) {
		if (p2.length() > 0)
			token = p2;
	}

	 // get the token id
	public String getToken() {
		return token;
	}

	 // Success details method
	// DO NOT MODIFY
	public boolean isOk(JSONObject request) {
		try {
			return (request.getBoolean("success") && !request
					.getBoolean("error"));
		} catch (JSONException e) {
			return false;
		}
	}

	// Send the request to server and register the device
	public JSONObject token_request(String device, Activity activity,String gsmToken) {
		JSONObject json = null;
		String url = prefix + "register.php";
		postParams.clear();
		getParams.clear();
		// ADD POST PARAMETERS
		postParams.put("request", "token_request");
		postParams.put("device_id", device);
		postParams.put("device_type",devicetype);
		postParams.put("app_type",apptype);
		postParams.put("ums_id",umsid);
		postParams.put("parse_id",gsmToken);
		// This function will format the URL automatically for you and will
		// return the response as a JSONObject
		json = jsonParser.getJSONFromUrl(url, getParams, postParams);

		RequestResponse = json.toString();
		MySQLiteHelper db = new MySQLiteHelper(activity);
		if (json.optString("success").contains("true")) {
			db.tbl_registerStatus_insert("true", "true");
		} else {
			db.tbl_registerStatus_insert("false", "false");
		}


		try {
			if (isOk(json)) {
				JSONObject jsondata = null;
				jsondata=json.getJSONObject("data");

				// setting the uid and token values. Then will be read from
				// Splashscreen:registerDevice() to write them on db
				uid = jsondata.getString("uid");
				token = jsondata.getString("token");
				json=jsondata;
				// System.out.println("Uid: " + uid + " Token : " + token);
			} else {
				uid = null;
				token = null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}



		return json;
	}

	// Using this method to get all Deals from a mall.
	public JSONObject GetDealsfromMall(String uid, String token) {
		JSONObject json = null;
		//Maybe it is better to pass the json string as a parameter
		String jsonString="{\"mall_id\":"+ mallid +", \"type\":\"deal\"}";
		String url = prefix + "deals.php";
		postParams.clear();
		getParams.clear();
		// ADD POST PARAMETERS
		postParams.put("uid", uid);
		postParams.put("token", token);
		postParams.put("json", jsonString);
		// This function will format the URL automatically for you and will
		// return the response as a JSONObject
		json = jsonParser.getJSONFromUrl(url, getParams, postParams);
		return json;
	}

	// Using this method to get all Events from a mall.
	public JSONObject GetEventsfromMall(String uid, String token) {
		JSONObject json = null;
		//Maybe it is better to pass the json string as a parameter
		String jsonString="{\"mall_id\":"+ mallid +" }";
		String url = prefix + "deals.php";
		postParams.clear();
		getParams.clear();
		// ADD POST PARAMETERS
		postParams.put("uid", uid);
		postParams.put("token", token);
		postParams.put("json", jsonString);
		// This function will format the URL automatically for you and will
		// return the response as a JSONObject
		json = jsonParser.getJSONFromUrl(url, getParams, postParams);
		return json;
	}

	// Using this method to get all shops from a mall.
	public JSONObject GetShopsfromMall(String uid, String token) {
		JSONObject json = null;
		//Maybe it is better to pass the json string as a parameter
		String jsonString="{\"mall_id\":"+ mallid +"}";
		String url = prefix + "shops.php";
		postParams.clear();
		getParams.clear();
		// ADD POST PARAMETERS
		postParams.put("uid", uid);
		postParams.put("token", token);
		postParams.put("json", jsonString);
		// This function will format the URL automatically for you and will
		// return the response as a JSONObject
		json = jsonParser.getJSONFromUrl(url, getParams, postParams);
		return json;
	}

	// Using this method to get a mall.Returns the first mall by default
	public JSONObject GetMall(String uid, String token) {
		JSONObject json = null;
		//Maybe it is better to pass the json string as a parameter
		String jsonString="{\"id\":"+ mallid +"}";
		String url = prefix + "malls.php";
		postParams.clear();
		getParams.clear();
		// ADD POST PARAMETERS
		postParams.put("uid", uid);
		postParams.put("token", token);
		postParams.put("json", jsonString);
		// This function will format the URL automatically for you and will
		// return the response as a JSONObject
		json = jsonParser.getJSONFromUrl(url, getParams, postParams);
		return json;
	}

	// Using this method to get one shops from a mall by its id.
	public JSONObject GetShopfromMallbyId(String uid, String token, String shopid) {
		JSONObject json = null;
		//Maybe it is better to pass the json string as a parameter
		String jsonString="{\"mall_id\":"+ mallid +",\"id\":" + shopid +"}";
		String url = prefix + "shops.php";
		postParams.clear();
		getParams.clear();
		// ADD POST PARAMETERS
		postParams.put("uid", uid);
		postParams.put("token", token);
		postParams.put("json", jsonString);
		// This function will format the URL automatically for you and will
		// return the response as a JSONObject
		json = jsonParser.getJSONFromUrl(url, getParams, postParams);
		return json;
	}

	// Save deal in the api.
	public JSONObject SaveDeal(String uid, String token, String dealid) {
		JSONObject json = null;
		//Maybe it is better to pass the json string as a parameter
		String jsonString="{\"action\":\"save-deal\",\"deal_id\":" + dealid +",\"fav_action\":\"add\"}";

		String url = prefix + "consumer.php";
		postParams.clear();
		getParams.clear();
		// ADD POST PARAMETERS
		postParams.put("uid", uid);
		postParams.put("token", token);
		postParams.put("json", jsonString);
		// This function will format the URL automatically for you and will
		// return the response as a JSONObject
		json = jsonParser.getJSONFromUrl(url, getParams, postParams);
		return json;
	}

	// Get my saved deals.
	public JSONObject getMyDeals(String uid, String token) {
		JSONObject json = null;
		//Maybe it is better to pass the json string as a parameter
		String jsonString="{\"action\":\"get-my-deals\"}";

		String url = prefix + "consumer.php";
		postParams.clear();
		getParams.clear();
		// ADD POST PARAMETERS
		postParams.put("uid", uid);
		postParams.put("token", token);
		postParams.put("json", jsonString);
		// This function will format the URL automatically for you and will
		// return the response as a JSONObject
		json = jsonParser.getJSONFromUrl(url, getParams, postParams);
		return json;
	}



	/*
	 * Send the request to server and get the preference details like language,
	 * specializations etc
	 */




}
