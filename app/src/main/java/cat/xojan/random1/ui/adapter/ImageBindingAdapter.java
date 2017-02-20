package cat.xojan.random1.ui.adapter;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Animatable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;

import cat.xojan.random1.R;
import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.ui.view.CircleTransform;

public class ImageBindingAdapter {

    @BindingAdapter({"podcastIcon"})
    public static void setPodcastIcon(ImageView imageView, Podcast.State state) {
        switch (state) {
            case LOADED:
                imageView.setImageResource(R.drawable.ic_arrow_down);
                break;
            case DOWNLOADING:
                imageView.setImageResource(R.drawable.animated_arrow);
                if (imageView.getDrawable() instanceof Animatable) {
                    ((Animatable) imageView.getDrawable()).start();
                }
                break;
            case DOWNLOADED:
                imageView.setImageResource(R.drawable.ic_delete);
                break;
        }
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        Picasso.with(imageView.getContext())
                .load(url + "?w=" + getWeekOfTheYear())
                .placeholder(R.drawable.default_rac1)
                .into(imageView);
    }

    @BindingAdapter({"smallImageUrl"})
    public static void loadSmallImage(ImageView imageView, String url) {
        Picasso.with(imageView.getContext())
                .load(url + "?w=" + getWeekOfTheYear())
                .resize(200, 200)
                .transform(new CircleTransform())
                .placeholder(R.drawable.default_rac1)
                .into(imageView);
    }

    private static int getWeekOfTheYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
}
