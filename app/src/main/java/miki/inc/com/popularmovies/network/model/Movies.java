package miki.inc.com.popularmovies.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by MIKI on 03-03-2018.
 */
public class Movies implements Parcelable {

    public static final String TAG_MOVIES = "movies";

    private int id;
    private boolean adult;
    private String poster_path;
    private String overview;
    private String title;
    private List<Integer> genre_ids;
    private String backdrop_path;
    private String popularity;
    private boolean video;
    private String release_date;
    private String orig_title;
    private String orig_language;
    private String vote_average;
    private int vote_count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public boolean getIsAdult() {
        return adult;
    }


    public List<Integer> getGenre_ids() {
        return genre_ids;
    }


    public String getRelease_date() {
        return release_date;
    }


    public String getOrig_title() {
        return orig_title;
    }

    public String getOrig_language() {
        return orig_language;
    }

    public String getTitle() {
        return title;
    }

    public String getPopularity() {
        return popularity;
    }

    public boolean getIsVideo() {
        return video;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVote_average() {
        return vote_average;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(orig_title);
        dest.writeString(orig_language);
        dest.writeString(title);
        dest.writeString(backdrop_path);
        dest.writeString(popularity);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeString(vote_average);
        dest.writeInt(vote_count);

    }

    public static final Creator CREATOR = new Creator() {

        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        public Movies[] newArray(int size) {
            return new Movies[size];
        }

    };

    public Movies(Parcel in) {
        id = in.readInt();
        adult = in.readByte() != 0;
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
        orig_title = in.readString();
        orig_language = in.readString();
        title = in.readString();
        backdrop_path = in.readString();
        popularity = in.readString();
        video = in.readByte() != 0;
        vote_average = in.readString();
        vote_count = in.readInt();
    }
}
