package wjhj.orbital.sportsmatchfindingapp.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import wjhj.orbital.sportsmatchfindingapp.R;

public class MyTextView extends AppCompatTextView {

    Drawable leftDrawable;
    TypedArray a;

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyTextView, 0, 0);
        setCompoundDrawablesWithIntrinsicBounds(
                a.getDrawable(R.styleable.MyTextView_leftIcon),
                null,
                null,
                null);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    public void setError(CharSequence error) {
        if (error == "") {
            leftDrawable = getCompoundDrawables()[0];
            setCompoundDrawablesWithIntrinsicBounds(
                    a.getDrawable(R.styleable.MyTextView_leftIcon),
                    null,
                    ContextCompat.getDrawable(getContext(), R.drawable.ic_error_outline_red_24dp),
                    null);
        } else if (error == null && leftDrawable != null) {
            setCompoundDrawablesWithIntrinsicBounds(
                    a.getDrawable(R.styleable.MyTextView_leftIcon),
                    null,
                    null,
                    null);
        } else {
            super.setError(error);
        }
    }
}
