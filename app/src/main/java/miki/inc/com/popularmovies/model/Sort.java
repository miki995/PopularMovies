package miki.inc.com.popularmovies.model;

/**
 * Created by MIKI on 05-03-2018.
 */
public enum Sort {

    POPULAR("popular"),
    TOP_RATED("top_rated");

    private final String value;

    Sort(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
