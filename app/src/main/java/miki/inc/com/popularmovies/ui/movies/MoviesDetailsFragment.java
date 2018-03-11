package miki.inc.com.popularmovies.ui.movies;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import miki.inc.com.popularmovies.ui.movies_details.MoviesDetailsAdapter;
import miki.inc.com.popularmovies.network.database.MoviesContract;
import miki.inc.com.popularmovies.network.database.MoviesOpenHelper;
import miki.inc.com.popularmovies.event.FavoriteChangeEvent;
import miki.inc.com.popularmovies.network.model.Movies;
import miki.inc.com.popularmovies.network.utils.GenreHelper;
import miki.inc.com.popularmovies.network.utils.LocalStoreUtil;
import miki.inc.com.popularmovies.network.utils.Utils;
import miki.inc.com.popularmovies.network.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;

public class MoviesDetailsFragment extends BaseMovieFragment {

    private static final String TAG_MOVIES = "movies";

    private Movies movies;
    private SimpleDraweeView mHeaderImage, mMoviePosterImage;
    private TextView mMovieTitle, mMovieGenre;
    private FloatingActionButton mFavoriteButton;

    private ViewPager mViewPager;
    private MoviesDetailsAdapter mMoviesDetailsAdapter;
    private TabLayout tabLayout;
    private boolean isFavoriteChanged = false;


    public static MoviesDetailsFragment newInstance(@NonNull Movies movies) {
        Bundle args = new Bundle();
        args.putParcelable(TAG_MOVIES, movies);

        MoviesDetailsFragment fragment = new MoviesDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(miki.inc.com.popularmovies.R.layout.fragment_movie_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movies =  getArguments().getParcelable(TAG_MOVIES);

        mFavoriteButton =  view.findViewById(miki.inc.com.popularmovies.R.id.favButton);
        mHeaderImage =  view.findViewById(miki.inc.com.popularmovies.R.id.headerImage);
        mMoviePosterImage =  view.findViewById(miki.inc.com.popularmovies.R.id.moviePosterImage);
        mMovieTitle =  view.findViewById(miki.inc.com.popularmovies.R.id.movieTitle);
        mMovieGenre =  view.findViewById(miki.inc.com.popularmovies.R.id.movieGenre);

        mMoviesDetailsAdapter = new MoviesDetailsAdapter(getChildFragmentManager(), movies);
        mViewPager =  view.findViewById(miki.inc.com.popularmovies.R.id.viewpager);
        tabLayout =  view.findViewById(miki.inc.com.popularmovies.R.id.tabLayout);

        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mMoviesDetailsAdapter);

        tabLayout.setTabTextColors(getResources().getColor(miki.inc.com.popularmovies.R.color.colorGrey100), getResources().getColor(miki.inc.com.popularmovies.R.color.primaryText));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(miki.inc.com.popularmovies.R.color.primaryText));
        tabLayout.setSelectedTabIndicatorHeight(Utils.dpToPx(2, getActivity()));
        tabLayout.setupWithViewPager(mViewPager);

        inflateData();
    }


    private void inflateData() {
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

                if (movies.isFavorite()) { // Already added is removed
                    LocalStoreUtil.removeFromFavorites(getActivity(), movies.getId());
                    getActivity().getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movies.getId())).build(), null, null);

                    ViewUtils.showToast(getResources().getString(miki.inc.com.popularmovies.R.string.removed_favorite), getActivity());
                    movies.setFavorite(false);

                } else {
                    LocalStoreUtil.addToFavorites(getActivity(), movies.getId());
                    ContentValues values = MoviesOpenHelper.getMovieContentValues(movies);
                    getActivity().getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, values);

                    ViewUtils.showToast(getResources().getString(miki.inc.com.popularmovies.R.string.added_favorite), getActivity());
                    movies.setFavorite(true);
                }

                isFavoriteChanged = true;
                mFavoriteButton.setSelected(movies.isFavorite());

                EventBus.getDefault().post(new FavoriteChangeEvent(isFavoriteChanged));

            }
        });

    }


}
