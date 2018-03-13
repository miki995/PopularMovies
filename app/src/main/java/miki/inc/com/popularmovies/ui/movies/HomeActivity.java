package miki.inc.com.popularmovies.ui.movies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.drawee.view.SimpleDraweeView;

import miki.inc.com.popularmovies.R;
import miki.inc.com.popularmovies.ui.base.BaseActivity;
import miki.inc.com.popularmovies.ui.movies_details.MoviesDetailsActivity;
import miki.inc.com.popularmovies.event.MovieSelectedEvent;
import miki.inc.com.popularmovies.network.model.Movies;
import miki.inc.com.popularmovies.network.model.Sort;
import miki.inc.com.popularmovies.ui.movies_details.MoviesDetailsFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends BaseActivity {

    private final String TAG_SORT = "sort";
    private Sort mSort;
    public static final String PREFERENCES = "MoviePreferences";
    SharedPreferences sharedpreferences;

    private boolean mTwoPane;

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(miki.inc.com.popularmovies.R.layout.activity_home);

        sharedpreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        mSort = getPreference();

        if (null == savedInstanceState) {
            showCorrespondingFragment(mSort);
            if (findViewById(miki.inc.com.popularmovies.R.id.homeDetailsFragment) != null) {
                mTwoPane = true;
            }
        }
    }

    private void showCorrespondingFragment(Sort sort) {
        if (Sort.FAVORITE == sort) {
            showFavoriteMoviesFragment();
        } else {
            showMoviesFragment(sort);
        }
    }


    private void setPreferences(Sort sortCriteria) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Sort.PREFERENCE_KEY.toString(), sortCriteria.toString());
        editor.apply();
    }

    private Sort getPreference() {
        String enumString = sharedpreferences.getString(Sort.PREFERENCE_KEY.toString(), Sort.POPULAR.toString());
        switch (enumString) {
            case "popular":
                return Sort.POPULAR;
            case "top_rated":
                return Sort.TOP_RATED;
            case "favorite":
                return Sort.FAVORITE;
            default:
                return Sort.POPULAR;
        }
    }

    private void showMoviesFragment(Sort sort) {
        replaceFragment(MoviesFragment.newInstance(sort));
    }

    private void showFavoriteMoviesFragment() {
        replaceFragment(new FavouriteMoviesFragment());
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(miki.inc.com.popularmovies.R.id.homeFragment, fragment)
                .commit();
    }

    private void replaceDetailsFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(miki.inc.com.popularmovies.R.id.homeDetailsFragment, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(miki.inc.com.popularmovies.R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (mSort) {
            case POPULAR:
                menu.findItem(miki.inc.com.popularmovies.R.id.sort_by_popularity).setChecked(true);
                break;
            case TOP_RATED:
                menu.findItem(miki.inc.com.popularmovies.R.id.sort_by_rating).setChecked(true);
                break;
            case FAVORITE:
                menu.findItem(miki.inc.com.popularmovies.R.id.sort_by_favorite).setChecked(true);
                break;
        }
        return true;
    }

    private void initializeEmptyFragment() {
        List<Integer> emptylist = new ArrayList<>();
        emptylist.add(18);
        Movies movies = new Movies(1, false, "", "",
                "1999-10-15",
                emptylist,
                "Please choose movie", "", "Please Choose a movie",
                "", "",
                false, "1", 1);

        if (mTwoPane) {
            MoviesDetailsFragment fragment = MoviesDetailsFragment.newInstance(movies);
            replaceDetailsFragment(fragment);

            Uri uri = Uri.parse("http://media.comicbook.com/files/img/default-movie.png");

            SimpleDraweeView mHeaderImage = findViewById(R.id.headerImage);
            mHeaderImage.setImageURI(uri);
            SimpleDraweeView mMoviePosterImage = findViewById(R.id.moviePosterImage);
            mMoviePosterImage.setImageURI(uri);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case miki.inc.com.popularmovies.R.id.sort_by_popularity:
                item.setChecked(!item.isChecked());
                setPreferences(Sort.POPULAR);
                showCorrespondingFragment(Sort.POPULAR);
                initializeEmptyFragment();
                break;

            case miki.inc.com.popularmovies.R.id.sort_by_rating:
                item.setChecked(!item.isChecked());
                setPreferences(Sort.TOP_RATED);
                showCorrespondingFragment(Sort.TOP_RATED);
                initializeEmptyFragment();
                break;

            case miki.inc.com.popularmovies.R.id.sort_by_favorite:
                item.setChecked(!item.isChecked());
                setPreferences(Sort.FAVORITE);
                showCorrespondingFragment(Sort.FAVORITE);
                initializeEmptyFragment();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        if (mSort != null)
            savedInstanceState.putSerializable(TAG_SORT, mSort);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(TAG_SORT)) {
                mSort = (Sort) savedInstanceState.getSerializable(TAG_SORT);
            }
        }

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final MovieSelectedEvent event) {

        Movies movies = event.getSelectedMovie();

        if (movies != null) {

            if (mTwoPane) {
                MoviesDetailsFragment fragment = MoviesDetailsFragment.newInstance(movies);
                replaceDetailsFragment(fragment);
            } else {
                Intent intent = new Intent(this, MoviesDetailsActivity.class);
                intent.putExtra(Movies.TAG_MOVIES, movies);
                startActivity(intent);
            }
        }

    }
}
