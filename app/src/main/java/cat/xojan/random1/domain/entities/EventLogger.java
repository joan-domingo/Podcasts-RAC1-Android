package cat.xojan.random1.domain.entities;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;

public class EventLogger {

    private Answers mAnswers;

    public EventLogger() {
    }

    public EventLogger(Answers answers) {
        mAnswers = answers;
    }

    public void logDownloadedPodcast(String title) {
        if (mAnswers != null) {
            mAnswers.logCustom(new CustomEvent("Downloads").putCustomAttribute("title", title));
        }

    }

    public void logPlayedPodcast(Podcast podcast) {
        if (mAnswers != null) {
            mAnswers.logContentView(new ContentViewEvent().putContentName(podcast.getTitle()));
        }
    }

    public void logExportedPodcast(final String podcastTitle) {
        if (mAnswers != null) {
            mAnswers.logCustom(new CustomEvent("Exported").putCustomAttribute("title", podcastTitle));
        }
    }

    public void logExportedPodcastAction() {
        if (mAnswers != null) {
            mAnswers.logCustom(new CustomEvent("ExportAction"));
        }
    }
}
