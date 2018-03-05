package miki.inc.com.popularmovies.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import java.util.ArrayList;

/**
 * Created by MIKI on 04-03-2018.
 */
public class ObservableScrollView extends ScrollView {
    private ArrayList<Callbacks> mCallbacks = new ArrayList<Callbacks>();

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        for (Callbacks c : mCallbacks) {
            c.onScrollChanged(l - oldl, t - oldt);
        }
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }


    public interface Callbacks {
        void onScrollChanged(int deltaX, int deltaY);
    }

    public void addCallbacks(Callbacks listener) {
        if (!mCallbacks.contains(listener)) {
            mCallbacks.add(listener);
        }
    }
}