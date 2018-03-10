package miki.inc.com.popularmovies.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import miki.inc.com.popularmovies.R;
import miki.inc.com.popularmovies.network.model.Movies;
import miki.inc.com.popularmovies.ui.base.BaseActivity;


public class MoviesDetailsActivity extends BaseActivity {

    private Movies movies;
    @BindView(R.id.headerImage) SimpleDraweeView mHeaderImage;
    @BindView(R.id.moviePosterImage) SimpleDraweeView mMoviePosterImage;
    @BindView(R.id.collapsingToolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.movieTitle) TextView mMovieTitle;
    @BindView(R.id.movieReleaseDate) TextView mMovieReleaseDate;
    @BindView(R.id.movieRating) TextView mMovieRating;
    @BindView(R.id.movieOverview) TextView mMovieOverview;
    @BindView(R.id.ratingBar) RatingBar mRatingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);

        getIntentData();
        ButterKnife.bind(this);
        inflateData();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            movies = intent.getExtras().getParcelable(Movies.TAG_MOVIES);
            setActivityTitle(movies.getTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void inflateData() {
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        Uri uriHeader = Uri.parse("http://image.tmdb.org/t/p/w500/" + movies.getBackdrop_path());
        Uri uriPoster = Uri.parse("http://image.tmdb.org/t/p/w185/" + movies.getPoster_path());
        mHeaderImage.setImageURI(uriHeader);
        mMoviePosterImage.setImageURI(uriPoster);

        mMovieTitle.setText(movies.getTitle());
        mMovieReleaseDate.setText(movies.getRelease_date());

        float rating = (float) Math.round(Double.parseDouble(movies.getVote_average()) * 10) / 10;
        String sumRating = rating + "/10";

        mMovieRating.setText(sumRating);
        mRatingBar.setRating(rating);
        mMovieOverview.setText(movies.getOverview());

    }

}
