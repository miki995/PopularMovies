package miki.inc.com.popularmovies.network;

import com.google.gson.reflect.TypeToken;

import miki.inc.com.popularmovies.BuildConfig;
import miki.inc.com.popularmovies.network.core.BaseService;
import miki.inc.com.popularmovies.network.core.ResponseListener;
import miki.inc.com.popularmovies.network.responses.MovieReviewResponse;
import miki.inc.com.popularmovies.network.responses.MovieVideoResponse;
import miki.inc.com.popularmovies.network.responses.MoviesResponse;

import java.util.HashMap;
import java.util.Map;


public class PopularMoviesService extends BaseService {

     public void getMostPopularMovies(int currentPage, ResponseListener<MoviesResponse> listener) {
        Map<String, String> params = new HashMap<>();
         params.put("api_key", BuildConfig.MOVIEDB_API);
         params.put("page", String.valueOf(currentPage));
        executePostRequest(ApiEndpoints.GET_MOVIES_POPULAR, null, params, new TypeToken<MoviesResponse>() {
        }, listener);
    }

    public void getTopRatedMovies(int currentPage, ResponseListener<MoviesResponse> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("api_key", BuildConfig.MOVIEDB_API);
        params.put("page", String.valueOf(currentPage));
        executePostRequest(ApiEndpoints.GET_MOVIES_TOP_RATED, null, params, new TypeToken<MoviesResponse>() {
        }, listener);
    }

    public void getMovieVideos(String movie_id, ResponseListener<MovieVideoResponse> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("api_key", BuildConfig.MOVIEDB_API);
        executeGetRequest(ApiEndpoints.GET_MOVIE_VIDEOS+movie_id+"/videos", null, params, new TypeToken<MovieVideoResponse>() {
        }, listener);
    }

    public void getMovieReviews(String movie_id, ResponseListener<MovieReviewResponse> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("api_key", BuildConfig.MOVIEDB_API);
        executeGetRequest(ApiEndpoints.GET_MOVIE_VIDEOS+movie_id+"/reviews", null, params, new TypeToken<MovieReviewResponse>() {
        }, listener);
    }

}
