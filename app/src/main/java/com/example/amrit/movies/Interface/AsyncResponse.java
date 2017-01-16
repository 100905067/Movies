package com.example.amrit.movies.Interface;

import com.example.amrit.movies.Pojos.Movie;

import java.util.List;

/**
 * Created by amrita on 1/11/2017.
 */

public interface AsyncResponse {
    public void processFinish(List<Movie> mList);
    public void processFinish(String key);
}
