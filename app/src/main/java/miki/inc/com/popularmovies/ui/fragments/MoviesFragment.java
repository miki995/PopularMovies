package miki.inc.com.popularmovies.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;

import miki.inc.com.popularmovies.model.Movie;
import miki.inc.com.popularmovies.model.Sort;
import miki.inc.com.popularmovies.services.PopularMoviesService;
import miki.inc.com.popularmovies.R;
import miki.inc.com.popularmovies.core.ResponseListener;
import miki.inc.com.popularmovies.ui.activities.MoviesDetailsActivity;
import miki.inc.com.popularmovies.utils.MoviesResponse;
import miki.inc.com.popularmovies.ui.adapters.MoviesAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends BaseMovieFragment implements ResponseListener<MoviesResponse>, MoviesAdapter.Callbacks{

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private MoviesAdapter moviesAdapter;
    private List<Movie> mMovies = new ArrayList<>();

    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        int columnCount = getResources().getInteger(R.integer.movies_columns);

        gridLayoutManager = new GridLayoutManager(getActivity(), columnCount);
        recyclerView.setLayoutManager(gridLayoutManager);

        initAdapter(mMovies);
    }

    private void initAdapter(List<Movie> movies) {
        moviesAdapter = new MoviesAdapter(movies);
        moviesAdapter.setCallbacks(this);
        recyclerView.setAdapter(moviesAdapter);
    }

    public void getMoviesData(final Sort sort) {
        if(isInternetAvailable()) {

            if(sort == Sort.POPULAR) {
                new PopularMoviesService().getMostPopularMovies(this);
            } else {
                new PopularMoviesService().getTopRatedMovies(this);
            }

            showProgressDialog();
        } else {
            Snackbar snackbar = Snackbar
                    .make(getCoordinatorLayout(), R.string.no_internet, Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getMoviesData(sort);
                        }
                    });
            snackbar.setActionTextColor(Color.RED);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("error", "->" + error);
        hideProgressDialog();
    }

    @Override
    public void onResponse(MoviesResponse response) {
        //Log.e("response", "->" + response.getPage());

        hideProgressDialog();
        if(response==null || response.getResults().isEmpty()) {
            return;
        }

        initAdapter(response.getResults());
    }

    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(getActivity(), MoviesDetailsActivity.class);
        intent.putExtra(Movie.TAG_MOVIES, movie);
        startActivity(intent);
    }
}