package com.example.popularmovies;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class NetRequest {

    public static List<String> imgCache = new ArrayList<>();
    public static List<String> descCache = new ArrayList<>();
    public static List<String> backdropCache = new ArrayList<>();
    public static List<String> titleCache = new ArrayList<>();
    public static List<String> voteCache = new ArrayList<>();
    public static List<String> releaseCache = new ArrayList<>();
    public static List<String> idCache = new ArrayList<>();
    // TODO insert own API key
    private static final  String API_KEY = "3223ceb92ba1e255b9954e7d2c25ee55";

    public static String request(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setConnectTimeout(5000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder(inputStream.available());
                for (String stream; (stream = reader.readLine()) != null; ) {
                    builder.append(stream).append("\n");
                }
                reader.close();
                urlConnection.disconnect();
                return builder.toString();
            }
        } catch (UnknownHostException u) {
            u.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }
        return null;
    }

    public static URL urlCrafter(String host) {
        Uri uri = Uri.parse(host).buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", "en-US")
                .appendQueryParameter("page", "1")
                .build();

        String uriString = uri.toString();
        try {
            return new URL(uriString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static URL YTUrl(String host) {
        Uri uri = Uri.parse(host).buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", "en-US")
                .build();

        String uriString = uri.toString();
        try {
            return new URL(uriString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void json(String streamBuilder) {
        try {
            if (streamBuilder != null) {
                JSONObject parse = new JSONObject(streamBuilder);
                JSONArray jArray = parse.getJSONArray("results");
                for (int j = 0; j < jArray.length(); j++) {
                    JSONObject jObj = jArray.getJSONObject(j);
                    // (Re)populate caches;
                    imgCache.add(jObj.optString("poster_path"));
                    descCache.add(jObj.optString("overview"));
                    backdropCache.add(jObj.optString("backdrop_path"));
                    titleCache.add(jObj.optString("title"));
                    releaseCache.add(jObj.optString("release_date"));
                    voteCache.add(jObj.optString("vote_average"));
                    idCache.add(jObj.optString("id"));
                }
            }
        }catch (JSONException j) {
            j.printStackTrace();
        }
    }

    public static String getYTLink(String streamBuilder, String key)  {
        try {
            if (streamBuilder != null) {
                JSONObject parse = new JSONObject(streamBuilder);
                JSONArray jArray = parse.getJSONArray("results");
                for (int j = 0; j < jArray.length(); j++) {
                    JSONObject jObj = jArray.getJSONObject(j);
                    return jObj.optString(key);
                }
            }
        } catch (JSONException j) {
            j.printStackTrace();
        }
        return null;
    }
}

