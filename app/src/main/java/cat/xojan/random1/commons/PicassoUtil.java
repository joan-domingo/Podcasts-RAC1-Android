package cat.xojan.random1.commons;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.Calendar;

import cat.xojan.random1.R;
import cat.xojan.random1.ui.view.CircleTransform;

/**
 * We use Picasso to load the images from an url. Picasso loads these images and caches them.
 * The problem is that these image might change and Picasso will keep showing the old image
 * because the url never changed. This workaround refreshes the images every week.
 */
public class PicassoUtil {

    public static void loadImage(Context ctx, String imageUrl, ImageView imageView,
                                 boolean isReduced) {
        RequestCreator requestCreator = Picasso.with(ctx)
                .load(imageUrl + "?w=" + getWeekOfTheYear());
                //.load(imageResource);
        if (isReduced) {
            requestCreator.resize(200, 200)
            .transform(new CircleTransform());
        }
        requestCreator.placeholder(R.drawable.default_rac1)
                .into(imageView);
    }

    // Not used since the "offline version" was introduced
    private static int getWeekOfTheYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
}
