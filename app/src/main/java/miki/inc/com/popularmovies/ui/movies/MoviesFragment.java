package miki.inc.com.popularmovies.ui.movies;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import miki.inc.com.popularmovies.network.PopularMoviesService;
import miki.inc.com.popularmovies.network.core.ResponseListener;
import miki.inc.com.popularmovies.network.database.MoviesContract;
import miki.inc.com.popularmovies.network.database.MoviesOpenHelper;
import miki.inc.com.popularmovies.network.responses.MoviesResponse;
import miki.inc.com.popularmovies.event.FavoriteChangeEvent;
import miki.inc.com.popularmovies.event.MovieSelectedEvent;
import miki.inc.com.popularmovies.network.model.Movies;
import miki.inc.com.popularmovies.network.model.Sort;
import miki.inc.com.popularmovies.network.utils.LocalStoreUtil;
import miki.inc.com.popularmovies.network.utils.Utils;
import miki.inc.com.popularmovies.network.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoviesFragment extends BaseMovieFragment implements ResponseListener<MoviesResponse>, MoviesAdapter.Callbacks {

    private static final String TAG_SORT = "state_sort";

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private MoviesAdapter moviesAdapter;
    private List<Movies> mMovies = new ArrayList<>();
    private Sort mSort;
    private int currentPage, totalPages;

    private Parcelable state;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDetach() {

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        super.onDetach();
    }

    public static MoviesFragment newInstance(@NonNull Sort sort) {
        Bundle args = new Bundle();
        args.putSerializable(TAG_SORT, sort);

        MoviesFragment fragment = new MoviesFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable("POSITION", recyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(miki.inc.com.popularmovies.R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            state = savedInstanceState.getParcelable("POSITION");
        }

        mSort = (Sort) getArguments().getSerializable(TAG_SORT);

        recyclerView = view.findViewById(miki.inc.com.popularmovies.R.id.recyclerView);
        int columnCount = getResources().getInteger(miki.inc.com.popularmovies.R.integer.movies_columns);


        gridLayoutManager = new GridLayoutManager(getActivity(), columnCount);
        int spacing = Utils.dpToPx(5, getActivity());
        boolean includeEdge = false;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(columnCount, spacing, includeEdge));
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addOnScrollListener(new EndlessRecyclerView(gridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {


                if (currentPage < totalPages) {

                    getMoviesData(mSort, currentPage + 1);

                }

            }
        });

        moviesAdapter = new MoviesAdapter(mMovies);
        moviesAdapter.setCallbacks(this);
        recyclerView.setAdapter(moviesAdapter);
        getMoviesData(mSort, 1);
    }


    public void getMoviesData(final Sort sort, final int currentPage) {
        if (isInternetAvailable()) {

            if (sort == Sort.POPULAR) {
                new PopularMoviesService().getMostPopularMovies(currentPage, this);
            } else {
                new PopularMoviesService().getTopRatedMovies(currentPage, this);
            }

            showProgressDialog();
        } else {
            Snackbar snackbar = Snackbar
                    .make(getCoordinatorLayout(), miki.inc.com.popularmovies.R.string.no_internet, Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getMoviesData(mSort, currentPage);
                        }
                    });
            snackbar.setActionTextColor(Color.RED);

            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        hideProgressDialog();
    }

    @Override
    public void onResponse(MoviesResponse response) {

        hideProgressDialog();
        if (response == null || response.getResults().isEmpty()) {
            return;
        }

        currentPage = response.getPage();
        totalPages = response.getTotalPages();

        List<Movies> movies = response.getResults();

        mMovies.addAll(movies);
        moviesAdapter.notifyDataSetChanged();
        if (state != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(state);
        }
    }

    @Override
    public void onMovieClick(Movies movies) {
        EventBus.getDefault().post(new MovieSelectedEvent(movies));
    }

    @Override
    public void onFavoriteClick(Movies movies) {

        if (movies.isFavorite()) {
            LocalStoreUtil.removeFromFavorites(getActivity(), movies.getId());
            ViewUtils.showToast(getResources().getString(miki.inc.com.popularmovies.R.string.removed_favorite), getActivity());

            getActivity().getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movies.getId())).build(), null, null);

        } else {
            LocalStoreUtil.addToFavorites(getActivity(), movies.getId());
            ViewUtils.showToast(getResources().getString(miki.inc.com.popularmovies.R.string.added_favorite), getActivity());

            ContentValues values = MoviesOpenHelper.getMovieContentValues(movies);
            getActivity().getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, values);
        }

        moviesAdapter.notifyDataSetChanged();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final FavoriteChangeEvent event) {

        moviesAdapter.notifyDataSetChanged();

    }
}
