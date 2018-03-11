package miki.inc.com.popularmovies.ui.movies_details;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import miki.inc.com.popularmovies.ui.base.BaseActivity;
import miki.inc.com.popularmovies.network.database.MoviesContract;
import miki.inc.com.popularmovies.network.database.MoviesOpenHelper;
import miki.inc.com.popularmovies.event.FavoriteChangeEvent;
import miki.inc.com.popularmovies.event.MovieVideosEvent;
import miki.inc.com.popularmovies.network.model.MovieVideos;
import miki.inc.com.popularmovies.network.model.Movies;
import miki.inc.com.popularmovies.network.utils.GenreHelper;
import miki.inc.com.popularmovies.network.utils.LocalStoreUtil;
import miki.inc.com.popularmovies.network.utils.ShareUtils;
import miki.inc.com.popularmovies.network.utils.Utils;
import miki.inc.com.popularmovies.network.utils.ViewUtils;
import miki.inc.com.popularmovies.ui.widgets.AppBarStateChangeListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MoviesDetailsActivity extends BaseActivity {

    private Movies movies;
    private SimpleDraweeView mHeaderImage, mMoviePosterImage;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private TextView mMovieTitle, mMovieGenre;
    private FloatingActionButton mFavoriteButton;

    private ViewPager mViewPager;
    private MoviesDetailsAdapter mMoviesDetailsAdapter;
    private TabLayout tabLayout;
    private boolean isFavoriteChanged = false;
    private MovieVideos movieVideos;


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
        setContentView(miki.inc.com.popularmovies.R.layout.activity_movies_details);

        getIntentData();

        initViews();
        inflateData();

    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            movies =  intent.getExtras().getParcelable(Movies.TAG_MOVIES);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        appBarLayout =  findViewById(miki.inc.com.popularmovies.R.id.appbarLayout);
        collapsingToolbarLayout =  findViewById(miki.inc.com.popularmovies.R.id.collapsingToolbar);
        mFavoriteButton =  findViewById(miki.inc.com.popularmovies.R.id.favButton);
        mHeaderImage =  findViewById(miki.inc.com.popularmovies.R.id.headerImage);
        mMoviePosterImage =  findViewById(miki.inc.com.popularmovies.R.id.moviePosterImage);
        mMovieTitle =  findViewById(miki.inc.com.popularmovies.R.id.movieTitle);
        mMovieGenre =  findViewById(miki.inc.com.popularmovies.R.id.movieGenre);

        setupTabLayout();
    }

    private void setupTabLayout() {
        mMoviesDetailsAdapter = new MoviesDetailsAdapter(getSupportFragmentManager(), movies);
        mViewPager =  findViewById(miki.inc.com.popularmovies.R.id.viewpager);
        tabLayout =  findViewById(miki.inc.com.popularmovies.R.id.tabLayout);

        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mMoviesDetailsAdapter);

        tabLayout.setTabTextColors(getResources().getColor(miki.inc.com.popularmovies.R.color.colorGrey100), getResources().getColor(miki.inc.com.popularmovies.R.color.primaryText));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(miki.inc.com.popularmovies.R.color.primaryText));
        tabLayout.setSelectedTabIndicatorHeight(Utils.dpToPx(2, this));
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void inflateData() {

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onExpanded(AppBarLayout appBarLayout) {
                setActivityTitle("");
                mFavoriteButton.show();
            }

            @Override
            public void onCollapsed(AppBarLayout appBarLayout) {
                setActivityTitle(movies.getTitle());
                mFavoriteButton.hide();
            }

            @Override
            public void onIdle(AppBarLayout appBarLayout) {
            }
        });

        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        Uri uriHeader = Uri.parse("http://image.tmdb.org/t/p/w500/" + movies.getBackdrop_path());
        Uri uriPoster = Uri.parse("http://image.tmdb.org/t/p/w185/" + movies.getPoster_path());

        mHeaderImage.setImageURI(uriHeader);
        mMoviePosterImage.setImageURI(uriPoster);

        mMovieTitle.setText(movies.getTitle());
        mMovieGenre.setText(GenreHelper.getGenreNamesList(movies.getGenre_ids()).trim());
        mFavoriteButton.setSelected(movies.isFavorite());

        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (movies.isFavorite()) {
                    LocalStoreUtil.removeFromFavorites(MoviesDetailsActivity.this, movies.getId());
                    getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movies.getId())).build(), null, null);

                    ViewUtils.showToast(getResources().getString(miki.inc.com.popularmovies.R.string.removed_favorite), MoviesDetailsActivity.this);
                    movies.setFavorite(false);

                } else {
                    LocalStoreUtil.addToFavorites(MoviesDetailsActivity.this, movies.getId());
                    ContentValues values = MoviesOpenHelper.getMovieContentValues(movies);
                    getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, values);

                    ViewUtils.showToast(getResources().getString(miki.inc.com.popularmovies.R.string.added_favorite), MoviesDetailsActivity.this);
                    movies.setFavorite(true);
                }

                isFavoriteChanged = true;
                mFavoriteButton.setSelected(movies.isFavorite());

                EventBus.getDefault().post(new FavoriteChangeEvent(isFavoriteChanged));

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(miki.inc.com.popularmovies.R.menu.menu_details, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case miki.inc.com.popularmovies.R.id.action_share:
                onClickShare();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onClickShare() {

        if (movieVideos != null && !movieVideos.getKey().isEmpty()) {

            String message = movies.getOriginal_title() + "\nhttps://www.youtube.com/watch?v=" + movieVideos.getKey();
            ShareUtils.shareCustom(message, this);
        } else {
            Toast.makeText(this, "There is no video link to be shared", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final MovieVideosEvent event) {

        movieVideos = event.getMovieVideos();

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        if (movies != null)
            savedInstanceState.putParcelable(Movies.TAG_MOVIES, movies);

        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Movies.TAG_MOVIES)) {
                movies =  savedInstanceState.getParcelable(Movies.TAG_MOVIES);
            }
        }

        super.onRestoreInstanceState(savedInstanceState);
    }


}
