package com.example.amrit.movies.AsyncTask;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.amrit.movies.BuildConfig;
import com.example.amrit.movies.Interface.AsyncResponse;

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

/**
 * Created by amrita on 1/14/2017.
 */

public class SearchVideoAsyncTask extends AsyncTask<String, Void, String> {
    private static final String LOG_TAG = SearchVideoAsyncTask.class.getSimpleName();

    public AsyncResponse mResponse;
    @Override
    protected String doInBackground(String... params) {
        if(params==null)
            return null;

        String movie_base_url = "https://api.themoviedb.org/3/movie";
        String APPID_PARAM = "api_key";

        String movieId = params[0];
        String appid = BuildConfig.MOVIE_DB_API_KEY;

        HttpURLConnection urlConnection;
        BufferedReader bufferedReader;

        try {
            Uri uri = Uri.parse(movie_base_url).buildUpon()
                    .appendPath(movieId)
                    .appendPath("videos")
                    .appendQueryParameter(APPID_PARAM, appid).build();
            URL url = new URL(uri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer = new StringBuffer(); //mutable and thread safe
            String line;
            while((line = bufferedReader.readLine())!=null){
                stringBuffer.append(line + "\n");
            }

            if(stringBuffer.length() == 0 )
                return null;

            String movieJsonStr = stringBuffer.toString();

            return movieJsonStr;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if(s==null)
            return;
        JSONObject resultJson = null;
        try {
            resultJson = new JSONObject(s);
            JSONArray resultJsonArr = resultJson.getJSONArray("results");
            String key = resultJsonArr.getJSONObject(0).getString("key");
            mResponse.processFinish(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
