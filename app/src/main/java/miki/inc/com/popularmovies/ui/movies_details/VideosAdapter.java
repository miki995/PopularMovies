package miki.inc.com.popularmovies.ui.movies_details;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import miki.inc.com.popularmovies.network.model.MovieVideos;

import java.util.List;


public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosViewHolder> {

    public interface Callbacks {
        public void onVideoClick(MovieVideos movieVideos);
    }

    private Callbacks mCallbacks;
    private Context context;
    private List<MovieVideos> mFeedList;

    public VideosAdapter(List<MovieVideos> feedList) {
        this.mFeedList = feedList;
    }

    @Override
    public VideosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = View.inflate(parent.getContext(), miki.inc.com.popularmovies.R.layout.item_video, null);
        return new VideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideosViewHolder holder, int position) {
        final MovieVideos movieVideos = mFeedList.get(position);

        Uri uriVideo = Uri.parse("http://img.youtube.com/vi/" + movieVideos.getKey() + "/0.jpg");

        holder.mVideoContainer.setImageURI(uriVideo);

        holder.mVideoTitle.setText(movieVideos.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCallbacks != null) {
                    mCallbacks.onVideoClick(movieVideos);
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

    public class VideosViewHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView mVideoContainer;
        private TextView mVideoTitle;

        public VideosViewHolder(View itemView) {
            super(itemView);
            mVideoContainer =  itemView.findViewById(miki.inc.com.popularmovies.R.id.videoThumb);
            mVideoTitle =  itemView.findViewById(miki.inc.com.popularmovies.R.id.videoTitle);
        }
    }

}
