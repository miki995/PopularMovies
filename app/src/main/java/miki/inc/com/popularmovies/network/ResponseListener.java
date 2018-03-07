package miki.inc.com.popularmovies.network;

import com.android.volley.Response;

/**
 * Created by MIKI on 05-03-2018.
 */
public interface ResponseListener<T> extends Response.Listener<T>, Response.ErrorListener{
}
