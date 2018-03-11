package miki.inc.com.popularmovies.ui.movies_details;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import miki.inc.com.popularmovies.network.model.Movies;


public class OverviewFragment extends Fragment {

    private TextView mMovieReleaseDate, mMovieRating, mMovieOverview;
    private RatingBar mRatingBar;
    private Movies movies;

    public static OverviewFragment newInstance(Movies movies){
        if (movies == null) {
            throw new IllegalArgumentException("The Movies Data can not be null");
        }
        Bundle args = new Bundle();
        args.putParcelable(Movies.TAG_MOVIES, movies);

        OverviewFragment fragment = new OverviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(miki.inc.com.popularmovies.R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movies = getArguments().getParcelable(Movies.TAG_MOVIES);

        mMovieReleaseDate =  view.findViewById(miki.inc.com.popularmovies.R.id.movieReleaseDate);
        mMovieRating =  view.findViewById(miki.inc.com.popularmovies.R.id.movieRating);
        mMovieOverview =  view.findViewById(miki.inc.com.popularmovies.R.id.movieOverview);
        mRatingBar =  view.findViewById(miki.inc.com.popularmovies.R.id.ratingBar);

        inflateData();
    }


    private void inflateData() {
        mMovieReleaseDate.setText(movies.getRelease_date());

        float rating = (float)Math.round(Double.parseDouble(movies.getVote_average()) * 10) / 10;

        mMovieRating.setText(rating+"/10");
        mRatingBar.setRating(rating);
        mMovieOverview.setText(movies.getOverview());
    }
}
