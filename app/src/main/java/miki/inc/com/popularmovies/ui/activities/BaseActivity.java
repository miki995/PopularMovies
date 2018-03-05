package miki.inc.com.popularmovies.ui.activities;

import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import miki.inc.com.popularmovies.R;


public class BaseActivity extends AppCompatActivity {

    Toolbar toolbar;
    CoordinatorLayout coordinatorLayout;
    MaterialDialog materialDialog;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        injectViews();

        if (isDisplayHomeAsUpEnabled()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void injectViews() {
        toolbar = findViewById(R.id.toolbar);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        setupToolbar();
    }


    protected void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setActivityTitle(int title) {
        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle(title);
    }

    public void setActivityTitle(String title) {
        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null)
            toolbar.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onActionBarHomeIconClicked();
                return true;

            default:
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean isDisplayHomeAsUpEnabled() {
        return false;
    }

    private void onActionBarHomeIconClicked() {
        if (isDisplayHomeAsUpEnabled()) {
            onBackPressed();
        } else {
            finish();
        }
    }


    public void showSnackBar(String value) {
        Snackbar snackbar = Snackbar
                .make(getCoordinatorLayout(), value, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    public void showSnackBar(int value) {
        Snackbar snackbar = Snackbar
                .make(getCoordinatorLayout(), value, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }

    public void showProgressDialog() {
        materialDialog = new MaterialDialog.Builder(this)
                .content("Please Wait")
                .cancelable(false)
                .progress(true, 0)
                .show();
    }

    public void hideProgressDialog() {
        materialDialog.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (materialDialog != null) {
            materialDialog.dismiss();
        }
    }

}
