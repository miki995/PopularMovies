package miki.inc.com.popularmovies.network.services;

import com.google.gson.reflect.TypeToken;

import miki.inc.com.popularmovies.BuildConfig;
import miki.inc.com.popularmovies.network.api.ApiEndpoints;
import miki.inc.com.popularmovies.network.BaseService;
import miki.inc.com.popularmovies.network.ResponseListener;
import miki.inc.com.popularmovies.network.utils.MoviesResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MIKI on 05-03-2018.
 */
public class PopularMoviesService extends BaseService {

    public void getMostPopularMovies(ResponseListener<MoviesResponse> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("api_key", BuildConfig.MOVIEDB_API);
        executePostRequest(ApiEndpoints.MOVIES_POPULAR, null, params, new TypeToken<MoviesResponse>() {
        }, listener);
    }

    public void getTopRatedMovies(ResponseListener<MoviesResponse> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("api_key", BuildConfig.MOVIEDB_API);
        executePostRequest(ApiEndpoints.MOVIES_TOP_RATED, null, params, new TypeToken<MoviesResponse>() {
        }, listener);
    }

}
