package miki.inc.com.popularmovies.ui.movies_details;

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

import butterknife.ButterKnife;
import miki.inc.com.popularmovies.R;
import butterknife.BindView;
import miki.inc.com.popularmovies.ui.movies.BaseMovieFragment;
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

    //Movie details layout contains title, release date, movie poster, vote average, and plot synopsis.

    //Review and Trailers

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
        return inflater.inflate(R.layout.fragment_movie_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movies = (Movies) getArguments().getParcelable(TAG_MOVIES);

        mFavoriteButton = (FloatingActionButton) view.findViewById(R.id.favButton);
        mHeaderImage = (SimpleDraweeView) view.findViewById(R.id.headerImage);
        mMoviePosterImage = (SimpleDraweeView) view.findViewById(R.id.moviePosterImage);
        mMovieTitle = (TextView) view.findViewById(R.id.movieTitle);
        mMovieGenre = (TextView) view.findViewById(R.id.movieGenre);

        mMoviesDetailsAdapter = new MoviesDetailsAdapter(getChildFragmentManager(), movies);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        // Set up the ViewPager with the sections adapter.
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mMoviesDetailsAdapter);

        tabLayout.setTabTextColors(getResources().getColor(R.color.colorGrey100), getResources().getColor(R.color.primaryText));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.primaryText));
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

                    ViewUtils.showToast(getResources().getString(R.string.removed_favorite), getActivity());
                    movies.setFavorite(false);

                } else {
                    LocalStoreUtil.addToFavorites(getActivity(), movies.getId());
                    ContentValues values = MoviesOpenHelper.getMovieContentValues(movies);
                    getActivity().getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, values);

                    ViewUtils.showToast(getResources().getString(R.string.added_favorite), getActivity());
                    movies.setFavorite(true);
                }

                isFavoriteChanged = true;
                mFavoriteButton.setSelected(movies.isFavorite());

                EventBus.getDefault().post(new FavoriteChangeEvent(isFavoriteChanged));

            }
        });

    }


}