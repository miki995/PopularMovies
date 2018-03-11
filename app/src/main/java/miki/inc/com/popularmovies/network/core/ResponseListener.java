package miki.inc.com.popularmovies.network.core;

import com.android.volley.Response;

public interface ResponseListener<T> extends Response.Listener<T>, Response.ErrorListener{
}
