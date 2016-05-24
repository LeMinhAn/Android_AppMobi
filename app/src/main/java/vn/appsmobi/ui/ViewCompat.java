package vn.appsmobi.ui;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by tobrother on 04/01/2016.
 */
public final class ViewCompat extends Animation {

    // ===========================================================
    // Static method
    // ===========================================================

    public static ViewCompat wrap(View view) {
        Animation wrapper = view.getAnimation();
        if (wrapper instanceof ViewCompat) {
            return (ViewCompat) wrapper;
        } else {
            return new ViewCompat(view);
        }
    }

    // ===========================================================
    // Private variables
    // ===========================================================

    private final View mView;

    private float mTranslationY;

    // ===========================================================
    // Constructor
    // ===========================================================

    private ViewCompat(View view) {
        setDuration(0);
        setFillAfter(true);
        view.setAnimation(this);
        mView = view;
    }

    public void setTranslationY(float translationY) {
        mTranslationY = translationY;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        if (mView != null) {
            t.getMatrix().postTranslate(0, mTranslationY);
        }
    }
}