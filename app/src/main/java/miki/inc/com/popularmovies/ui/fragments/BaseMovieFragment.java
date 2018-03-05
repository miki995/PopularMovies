package miki.inc.com.popularmovies.ui.fragments;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;

import miki.inc.com.popularmovies.ui.activities.HomeActivity;
import miki.inc.com.popularmovies.utils.NetworkUtils;

/**
 * Created by MIKI on 03-03-2018.
 */
public class BaseMovieFragment extends Fragment {



    public void showProgressDialog() {
        ((HomeActivity) getActivity()).showProgressDialog();
    }

    public void hideProgressDialog() {
        ((HomeActivity) getActivity()).hideProgressDialog();
    }

    public boolean isInternetAvailable() {
        return NetworkUtils.isNetworkConnected(getActivity());
    }

    public void showSnackBar(String value) {
        ((HomeActivity) getActivity()).showSnackBar(value);
    }

    public void showSnackBar(int value) {
        ((HomeActivity) getActivity()).showSnackBar(value);
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return ((HomeActivity) getActivity()).getCoordinatorLayout();
    }

}
