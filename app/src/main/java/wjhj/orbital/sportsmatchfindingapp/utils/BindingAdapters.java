package wjhj.orbital.sportsmatchfindingapp.utils;

import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputLayout;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;


public class BindingAdapters {

    @BindingAdapter("android:bindingSrc")
    public static void setImageResource(ImageView view, int resource) {
        view.setImageResource(resource);
    }

//    @BindingAdapter("android:bindingSrcWhiteOverlay")
//    public static void setImageResourceWhite(ImageView view, int resource) {
//        Drawable drawable = DrawableCompat.wrap(view.getContext().getResources().getDrawable(resource));
//        DrawableCompat.setTint(drawable, Color.WHITE);
//        view.setImageDrawable(drawable);
//    }

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

    @BindingAdapter("android:setTextDate")
    public static void setTextDate(TextView view, LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EE, d/L");
        view.setText(formatter.format(date));
    }

    @BindingAdapter({"android:setTextTime", "android:duration"})
    public static void setTextTime(TextView view, LocalTime time, Duration duration) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:ma");
        String startTime = formatter.format(time);
        String endTime = formatter.format(time.plus(duration));
        view.setText(startTime + " to " + endTime);
    }
}
