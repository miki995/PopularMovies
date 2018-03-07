package miki.inc.com.popularmovies.ui.base;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import miki.inc.com.popularmovies.R;


public class BaseActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    MaterialDialog materialDialog;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        setupToolbar();

        if (isDisplayHomeAsUpEnabled()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
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
