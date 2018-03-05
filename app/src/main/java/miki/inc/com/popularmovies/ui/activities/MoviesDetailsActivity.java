package miki.inc.com.popularmovies.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import miki.inc.com.popularmovies.R;
import miki.inc.com.popularmovies.model.Movie;


public class MoviesDetailsActivity extends BaseActivity {

    private Movie movie;
    private SimpleDraweeView mHeaderImage, mMoviePosterImage;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView mMovieTitle, mMovieReleaseDate, mMovieRating, mMovieOverview;
    private RatingBar mRatingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);

        getIntentData();
        initViews();
        inflateData();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            movie = intent.getExtras().getParcelable(Movie.TAG_MOVIES);
            setActivityTitle(movie.getTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        mHeaderImage = findViewById(R.id.headerImage);
        mMoviePosterImage = findViewById(R.id.moviePosterImage);
        mMovieTitle = findViewById(R.id.movieTitle);
        mMovieReleaseDate = findViewById(R.id.movieReleaseDate);
        mMovieRating = findViewById(R.id.movieRating);
        mMovieOverview = findViewById(R.id.movieOverview);
        mRatingBar = findViewById(R.id.ratingBar);
    }

    private void inflateData() {
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        Uri uriHeader = Uri.parse("http://image.tmdb.org/t/p/w500/" + movie.getBackdrop_path());
        Uri uriPoster = Uri.parse("http://image.tmdb.org/t/p/w185/" + movie.getPoster_path());
        mHeaderImage.setImageURI(uriHeader);
        mMoviePosterImage.setImageURI(uriPoster);

        mMovieTitle.setText(movie.getTitle());
        mMovieReleaseDate.setText(movie.getRelease_date());

        float rating = (float) Math.round(Double.parseDouble(movie.getVote_average()) * 10) / 10;

        mMovieRating.setText(rating + "/10");
        mRatingBar.setRating(rating);
        mMovieOverview.setText(movie.getOverview());

    }

}
