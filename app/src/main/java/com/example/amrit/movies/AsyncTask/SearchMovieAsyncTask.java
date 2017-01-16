package com.example.amrit.movies.AsyncTask;

import android.net.Uri;
import android.os.AsyncTask;

import com.example.amrit.movies.BuildConfig;
import com.example.amrit.movies.Interface.AsyncResponse;
import com.example.amrit.movies.Pojos.Movie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by amrita on 1/11/2017.
 */

public class SearchMovieAsyncTask extends AsyncTask<Void, Void, String> {
    public AsyncResponse mResponse;
    @Override
    protected String doInBackground(Void... voids) {

        String movie_base_url = "https://api.themoviedb.org/3/movie";
        String APPID_PARAM = "api_key";

        String searchCriteria = "now_playing";
        String appid = BuildConfig.MOVIE_DB_API_KEY;

        HttpURLConnection urlConnection;
        BufferedReader bufferedReader;

        try {
            Uri uri = Uri.parse(movie_base_url).buildUpon()
                        .appendPath(searchCriteria)
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
            JSONObject movieJson= new JSONObject(movieJsonStr);
            JSONArray moviesJsonArr = movieJson.getJSONArray("results");
            return moviesJsonArr.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if(s == null)
            return;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        List<Movie> moviesList = new ArrayList<Movie>();
        moviesList = Arrays.asList(gson.fromJson(s, Movie[].class));

        mResponse.processFinish(moviesList);
    }
}
