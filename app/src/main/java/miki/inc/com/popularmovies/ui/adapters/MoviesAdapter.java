package miki.inc.com.popularmovies.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import miki.inc.com.popularmovies.R;
import miki.inc.com.popularmovies.model.Movie;

import java.util.List;


/**
 * Created by MIKI on 03-03-2018.
 */
public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface Callbacks {
        void onMovieClick(Movie movie);
    }

    private Callbacks mCallbacks;
    private Context context;
    private List<Movie> mFeedList;

    public MoviesAdapter(List<Movie> feedList) {
        this.mFeedList = feedList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = View.inflate(parent.getContext(), R.layout.item_movie, null);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MovieViewHolder) {
            final Movie movie = mFeedList.get(position);

            final MovieViewHolder movieViewHolder = (MovieViewHolder) holder;

            movieViewHolder.mMovieName.setText(movie.getOrig_title());
            Uri uri = Uri.parse("http://image.tmdb.org/t/p/w185/" + movie.getPoster_path());
            movieViewHolder.mMovieImage.setImageURI(uri);

            movieViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mCallbacks != null) {
                        mCallbacks.onMovieClick(movie);
                    }
                }
            });
        }

    }

    public void setCallbacks(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    @Override
    public int getItemCount() {
        return (mFeedList != null ? mFeedList.size() : 0);
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder {

        private TextView mMovieName;
        private SimpleDraweeView mMovieImage;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mMovieName = itemView.findViewById(R.id.movieTextView);
            mMovieImage = itemView.findViewById(R.id.movieImage);

        }
    }

}
