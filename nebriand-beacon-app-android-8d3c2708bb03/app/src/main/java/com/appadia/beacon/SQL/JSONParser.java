package com.appadia.beacon.SQL;

import android.content.ContentValues;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class JSONParser {
 
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    static String rc="";
    URI uri;
    private static final String NAME_VALUE_SEPARATOR = "=";
    // constructor
    public JSONParser() { 
    }
 
    public JSONObject getJSONFromUrl(String url, ContentValues getParams, ContentValues postParams) {
     	BufferedReader reader2=null;
        StringBuilder stringBuilder;

        // Making HTTP request
        try {
            URL link = new URL(url);
            HttpURLConnection urlConnection=(HttpURLConnection)link.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);
            OutputStream out= new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(out, "UTF-8"));
           // writer.write(getQuery(postParams));
            writer.write(format(postParams,'&',"UTF-8"));
            writer.flush();
            writer.close();
            out.close();

            urlConnection.connect();

            InputStream in =new BufferedInputStream(urlConnection.getInputStream());
            readStream(in);

 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
       catch (IOException e) {
            e.printStackTrace();          
        }
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }      // return JSON String
        return jObj;
    }



    public static String format(
            final ContentValues values,
            final char parameterSeparator,
            final String charset) {
        final StringBuilder result = new StringBuilder();
        for (final Map.Entry<String, Object> entry : values.valueSet()) {
            final String encodedName = entry.getKey();
            final Object value = entry.getValue();
            final String encodedValue = value.toString();
            if (result.length() > 0) {
                result.append(parameterSeparator);
            }
            result.append(encodedName);
            if (encodedValue != null) {
                result.append(NAME_VALUE_SEPARATOR);
                result.append(encodedValue);
            }
        }
        return result.toString();
    }

    private String getQuery(ContentValues postParams) {
        StringBuilder result = new StringBuilder();
        boolean first = true;


                result.append("&");

            result.append(postParams.toString());


        return result.toString();
    }

    private void readStream(InputStream in) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    in, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            json = sb.toString();
        }
            catch(IOException e){
                e.printStackTrace();
            }




    }

    private void writeStream(OutputStream out, ContentValues postParams) {
        String output=postParams.toString();
        try {
            out.write(output.getBytes());
            out.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }


    //Retrieve Google Data
    
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException,
			JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

   
}

