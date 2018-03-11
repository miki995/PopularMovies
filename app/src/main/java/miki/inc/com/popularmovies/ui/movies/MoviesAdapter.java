package miki.inc.com.popularmovies.ui.movies;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import miki.inc.com.popularmovies.network.model.Movies;
import miki.inc.com.popularmovies.network.utils.GenreHelper;
import miki.inc.com.popularmovies.network.utils.LocalStoreUtil;

import java.util.List;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    public interface Callbacks {
        public void onMovieClick(Movies movies);

        public void onFavoriteClick(Movies movies);
    }

    private Callbacks mCallbacks;
    private Context context;
    private List<Movies> mFeedList;

    public MoviesAdapter(List<Movies> feedList) {
        this.mFeedList = feedList;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = View.inflate(parent.getContext(), miki.inc.com.popularmovies.R.layout.item_movie, null);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        final Movies movies = mFeedList.get(position);

        final MovieViewHolder movieViewHolder = (MovieViewHolder) holder;

        movieViewHolder.mMovieName.setText(movies.getOriginal_title());
        movieViewHolder.mMovieGenre.setText(GenreHelper.getGenreNamesList(movies.getGenre_ids()).trim());

        Uri uri = Uri.parse("http://image.tmdb.org/t/p/w185/" + movies.getPoster_path());
        movieViewHolder.mMovieImage.setImageURI(uri);



        if (LocalStoreUtil.hasInFavorites(context, movies.getId())) {
            movieViewHolder.mFavoriteButton.setSelected(true);
            movies.setFavorite(true);
        } else {
            movieViewHolder.mFavoriteButton.setSelected(false);
            movies.setFavorite(false);
        }

        movieViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCallbacks != null) {
                    mCallbacks.onMovieClick(movies);
                }
            }
        });

        movieViewHolder.mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCallbacks != null) {
                    movieViewHolder.mFavoriteButton.setSelected(!movies.isFavorite());
                    mCallbacks.onFavoriteClick(movies);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (mFeedList != null ? mFeedList.size() : 0);
    }

    public void setCallbacks(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        private TextView mMovieName, mMovieGenre;
        private SimpleDraweeView mMovieImage;
        private ImageButton mFavoriteButton;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mMovieName =  itemView.findViewById(miki.inc.com.popularmovies.R.id.movieTextView);
            mMovieGenre =  itemView.findViewById(miki.inc.com.popularmovies.R.id.movieGenre);
            mMovieImage =  itemView.findViewById(miki.inc.com.popularmovies.R.id.movieImage);
            mFavoriteButton =  itemView.findViewById(miki.inc.com.popularmovies.R.id.movie_item_btn_favorite);
        }
    }

}
