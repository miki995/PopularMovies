package miki.inc.com.popularmovies.ui.base;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;

import miki.inc.com.popularmovies.network.OkHttpStack;

/**
 * Created by MIKI on 02-03-2018.
 */
public class PopularMoviesApplication extends Application {

    public static final String TAG = PopularMoviesApplication.class.getSimpleName();

    private static PopularMoviesApplication _instance;
    private RequestQueue mRequestQueue;


    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);

        _instance = this;
    }

    public static PopularMoviesApplication getInstance() {
        return _instance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new OkHttpStack());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
}
