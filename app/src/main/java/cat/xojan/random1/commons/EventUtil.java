package cat.xojan.random1.commons;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import cat.xojan.random1.BuildConfig;

public class EventUtil {

    public static void logDownloadedPodcast(String title) {
        if (!BuildConfig.DEBUG) {
            Answers.getInstance().logCustom(new CustomEvent("Downloads")
                    .putCustomAttribute("title", title));
        }
    }
}
