package wjhj.orbital.sportsmatchfindingapp.utils;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class BindingAdapters {

    @BindingAdapter("android:bindingSrc")
    public static void setImageResource(ImageView view, int resource) {
        view.setImageResource(resource);
    }

    @BindingAdapter("android:errorText")
    public static void setErrorMessage(TextInputLayout view, String errorMessage) {
        view.setError(errorMessage);
    }

    @BindingAdapter("android:errorText")
    public static void setErrorMessage(TextView view, String errorMessage) {
        view.setError(errorMessage);
    }

    @BindingAdapter("android:imageUri")
    public static void setImageUri(ImageView view, Uri uri) {
        Glide.with(view).load(uri).into(view);
    }

    @BindingAdapter("android:circularImageUri")
    public static void setCircularImageResources(ImageView view, Uri uri) {
        Glide.with(view)
                .load(uri)
                .apply(RequestOptions.circleCropTransform())
                .into(view);
    }

    @BindingAdapter("android:drawableStartResource")
    public static void setDrawableStart(TextView view, int resource) {
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(resource, 0, 0, 0);
    }
}
