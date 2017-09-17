package com.example.anant.moviesdb.Utilities;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by anant on 16/9/17.
 */

public class MoviesList {

    public MoviesList(){
    }

    public String mJSONResults;

    public URL buildURL(String URL){

        URL url = null;
        Uri builtUri = Uri.parse(URL).buildUpon().
                appendQueryParameter("api_key", Constants.API_KEY).
                appendQueryParameter("language", "en-US").
                build();
        try {
            url =new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;

    }

    public String getJSONResponse(String url) throws IOException {

        mJSONResults = null;

        HttpURLConnection urlConnection = (HttpURLConnection) buildURL(url).openConnection();

        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            if (scanner.hasNext()) {
                mJSONResults = scanner.next();
                return mJSONResults;
            } else return null;
        }
        finally {
            urlConnection.disconnect();
        }
    }

    public ArrayList<String> parseJSONImage() throws JSONException {

        ArrayList<String> mylist = new ArrayList<String>();

        JSONObject jsonObject = new JSONObject(mJSONResults);
        JSONArray array = jsonObject.getJSONArray("results");
        for(int i = 0; i<array.length();i++){
            JSONObject path = array.getJSONObject(i);
            mylist.add(path.getString("poster_path"));
        }
        return mylist;
    }
}
