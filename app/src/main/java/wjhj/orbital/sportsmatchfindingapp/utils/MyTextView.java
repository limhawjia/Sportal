package wjhj.orbital.sportsmatchfindingapp.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import wjhj.orbital.sportsmatchfindingapp.R;

public class MyTextView extends AppCompatTextView {

    Drawable leftDrawable;

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    public void setError(CharSequence error) {
        if (error == "") {
            leftDrawable = getCompoundDrawables()[0];
            setCompoundDrawablesWithIntrinsicBounds(
                    leftDrawable,
                    null,
                    ContextCompat.getDrawable(getContext(), R.drawable.ic_error_outline_red_24dp),
                    null);
        } else if (error == null && leftDrawable != null) {
            setCompoundDrawablesWithIntrinsicBounds(
                    leftDrawable,
                    null,
                    null,
                    null);
        } else {
            super.setError(error);
        }
    }
}
