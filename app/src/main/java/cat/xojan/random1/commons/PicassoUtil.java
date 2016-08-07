package cat.xojan.random1.commons;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;

/**
 * We use Picasso to load the images from an url. Picasso loads these images and caches them.
 * The problem is that these image might change and Picasso will keep showing the old image
 * because the url never changed. This workaround refreshes the images every week.
 */
public class PicassoUtil {

    public static void loadImage(Context ctx, String url, ImageView imageView) {
        Picasso.with(ctx)
                .load(url + "?week=" + getWeekOfTheYear())
                .into(imageView);
    }

    private static int getWeekOfTheYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
}
