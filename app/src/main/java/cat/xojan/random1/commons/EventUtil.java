package cat.xojan.random1.commons;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;

import cat.xojan.random1.BuildConfig;
import cat.xojan.random1.domain.entities.Podcast;

public class EventUtil {

    public static void logPlayedPodcast(Podcast podcast) {
        if (!BuildConfig.DEBUG) {
            Answers.getInstance().logContentView(new ContentViewEvent()
                    .putContentName(podcast.getTitle()));
        }
    }

    public static void logDownloadedPodcast(String title) {
        if (!BuildConfig.DEBUG) {
            Answers.getInstance().logCustom(new CustomEvent("Downloads")
                    .putCustomAttribute("title", title));
        }
    }
}
