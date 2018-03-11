package miki.inc.com.popularmovies.ui.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import miki.inc.com.popularmovies.ui.base.BaseActivity;
import miki.inc.com.popularmovies.ui.movies_details.MoviesDetailsActivity;
import miki.inc.com.popularmovies.event.MovieSelectedEvent;
import miki.inc.com.popularmovies.network.model.Movies;
import miki.inc.com.popularmovies.network.model.Sort;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HomeActivity extends BaseActivity {

    private final String TAG_SORT = "sort";
    private Sort mSort = Sort.POPULAR;

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

        showMoviesFragment();

        if (findViewById(miki.inc.com.popularmovies.R.id.homeDetailsFragment) != null) {
            mTwoPane = true;
        }

    }

    private void showMoviesFragment() {
        replaceFragment(MoviesFragment.newInstance(mSort));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case miki.inc.com.popularmovies.R.id.sort_by_popularity:
                item.setChecked(!item.isChecked());
                onSortChanged(Sort.POPULAR);
                showMoviesFragment();
                break;

            case miki.inc.com.popularmovies.R.id.sort_by_rating:
                item.setChecked(!item.isChecked());
                onSortChanged(Sort.TOP_RATED);
                showMoviesFragment();
                break;

            case miki.inc.com.popularmovies.R.id.sort_by_favorite:
                item.setChecked(!item.isChecked());
                showFavoriteMoviesFragment();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onSortChanged(Sort sort) {
        mSort = sort;
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
