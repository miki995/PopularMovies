package miki.inc.com.popularmovies.ui.movies_details;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import miki.inc.com.popularmovies.network.model.MovieReviews;
import miki.inc.com.popularmovies.network.utils.Utils;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    private Context context;
    private List<MovieReviews> mFeedList;

    public ReviewsAdapter(List<MovieReviews> feedList) {
        this.mFeedList = feedList;
    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = View.inflate(parent.getContext(), miki.inc.com.popularmovies.R.layout.item_review, null);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        final MovieReviews movieReviews = mFeedList.get(position);

        holder.mReviewUser.setText(movieReviews.getAuthor());
        holder.mReviewContent.setText(movieReviews.getContent());
    }

    @Override
    public int getItemCount() {
        return (mFeedList!=null? mFeedList.size():0);
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {

        private TextView mReviewUser, mReviewContent;
        private ImageView mAvatar;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            mReviewUser =  itemView.findViewById(miki.inc.com.popularmovies.R.id.reviewUser);
            mReviewContent =  itemView.findViewById(miki.inc.com.popularmovies.R.id.reviewContent);
            mAvatar =  itemView.findViewById(miki.inc.com.popularmovies.R.id.reviewAvatar);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ViewCompat.setElevation(mAvatar, Utils.dpToPx(5, context));
                ViewCompat.setTranslationZ(mAvatar, Utils.dpToPx(5, context));

            } else {
                mAvatar.bringToFront();
            }
        }
    }

}
