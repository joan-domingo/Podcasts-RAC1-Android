package cat.xojan.random1.commons;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;

import cat.xojan.random1.BuildConfig;
import cat.xojan.random1.domain.model.Podcast;

public class EventUtil {

    public static void logPlayedPodcast(Podcast podcast) {
        if (!BuildConfig.DEBUG) {
            Answers.getInstance().logContentView(new ContentViewEvent()
                    .putContentName(podcast.getCategory())
                    .putContentType(podcast.getDescription()));
        }
    }

    public static void logDownloaedPodcast(String category, String description) {
        if (!BuildConfig.DEBUG) {
            Answers.getInstance().logCustom(new CustomEvent("Downloads")
                    .putCustomAttribute("Category", category)
                    .putCustomAttribute("Description", description));
        }
    }
}
