package miki.inc.com.popularmovies.event;

import miki.inc.com.popularmovies.network.model.Movies;


public class MovieSelectedEvent {

    public final Movies movies;

    public MovieSelectedEvent(Movies movies) {
        this.movies = movies;
    }

    public Movies getSelectedMovie() {
        return movies;
    }

}
