package miki.inc.com.popularmovies.event;

import miki.inc.com.popularmovies.network.model.MovieVideos;

public class MovieVideosEvent {

    public final MovieVideos movieVideos;

    public MovieVideosEvent(MovieVideos movieVideos) {
        this.movieVideos = movieVideos;
    }

    public MovieVideos getMovieVideos() {
        return movieVideos;
    }

}
