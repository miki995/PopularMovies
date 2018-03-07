package miki.inc.com.popularmovies.network;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;

import miki.inc.com.popularmovies.ui.base.PopularMoviesApplication;
import miki.inc.com.popularmovies.network.utils.NetworkUtils;

import java.util.Map;

/**
 * Created by MIKI on 02-03-2018.
 */
public class BaseService {

    protected void executePostRequest(String url, Map<String, String> headers, Map<String, String> params, TypeToken typeToken, ResponseListener listener) {
        url = NetworkUtils.getUrl(url, params);
        executeRequest(Request.Method.POST, url, headers, params, typeToken, listener);
    }

    protected void executeRequest(int method, String url, Map<String, String> headers, Map<String, String> params, TypeToken typeToken, ResponseListener listener) {
        BaseRequest request = new BaseRequest(method, url, headers, params, typeToken, listener);
        PopularMoviesApplication.getInstance().addToRequestQueue(request);
    }
}
