package com.example.a100585817.nearbyrestaurants;

import android.os.AsyncTask;
import android.widget.Toast;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class getRestaurants extends AsyncTask<Object, String, String> {

    GoogleMap mMap;
    String url;
    InputStream is;
    BufferedReader br;
    StringBuilder sb;
    String data;

    public getRestaurants(MapsActivity mapsActivity) {
    }

    @Override
    protected String doInBackground(Object... params) {
        mMap = (GoogleMap)params[0];
        url = (String)params[1];

        try{
            URL myurl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)myurl.openConnection();
            httpURLConnection.connect();

            is = httpURLConnection.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));

            String line ="";
            sb = new StringBuilder();

            while((line = br.readLine()) != null){
                sb.append(line);
            }
            data = sb.toString();
        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

        return data;
    }

    @Override
    protected void onPostExecute(String s){

        try {
            JSONObject parentObject = new JSONObject(s);
            JSONArray results = parentObject.getJSONArray("results");

            for(int i = 0; i <results.length(); i++){
                JSONObject jsonObject = results.getJSONObject(i);
                JSONObject locationObj = jsonObject.getJSONObject("geometry").getJSONObject("location");

                String latitude = locationObj.getString("lat");
                String longitude = locationObj.getString("lng");

                JSONObject name = results.getJSONObject(i);
                String restName = name.getString("name");

                LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(restName);
                markerOptions.position(latLng);

                mMap.addMarker(markerOptions);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}

