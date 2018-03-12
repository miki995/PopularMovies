package miki.inc.com.popularmovies.network.model;

public enum Sort {

    POPULAR("popular"),
    TOP_RATED("top_rated"),
    FAVORITE("favorite"),
    PREFERENCE_KEY("preference_key"),;

    private final String value;

    Sort(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return value;
    }
}
