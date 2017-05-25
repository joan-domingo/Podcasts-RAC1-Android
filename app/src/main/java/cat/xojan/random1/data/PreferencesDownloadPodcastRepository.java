package cat.xojan.random1.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.repository.DownloadPodcastRepository;

public class PreferencesDownloadPodcastRepository implements DownloadPodcastRepository {

    private static final String DOWNLOAD_PODCASTS = "dowload_podcasts_repo";
    private static final String DOWNLOADING_PODCASTS = "downloading_podcasts";
    private static final String DOWNLOADED_PODCASTS = "downloaded_podcasts";

    private final Gson mGson;
    private SharedPreferences mPreferences;

    public PreferencesDownloadPodcastRepository(Context context) {
        mPreferences = context.getSharedPreferences(DOWNLOAD_PODCASTS, Context.MODE_PRIVATE);
        mGson = new Gson();
    }

    @Override
    public boolean addDownloadingPodcast(Podcast podcast) {
        Set<Podcast> podcasts = getDownloadingPodcasts();
        podcast.setState(Podcast.State.DOWNLOADING);
        podcasts.add(podcast);
        return mPreferences.edit()
                .putString(DOWNLOADING_PODCASTS, setToJson(podcasts))
                .commit();
    }

    @Override
    public boolean deleteDownloadingPodcast(Podcast podcast) {
        Set<Podcast> podcasts = getDownloadingPodcasts();
        podcasts.remove(podcast);
        return mPreferences.edit()
                .putString(DOWNLOADING_PODCASTS, setToJson(podcasts))
                .commit();
    }

    @Override
    public void setPodcastAsDownloaded(String audioId, String filePath) {
        Podcast podcast = getDownloadingPodcast(audioId);
        if (podcast != null && deleteDownloadingPodcast(podcast)) {
            podcast.setFilePath(filePath);
            podcast.setState(Podcast.State.DOWNLOADED);
            addDownloadedPodcast(podcast);
        }
    }

    public Set<Podcast> getDownloadingPodcasts() {
        String jsonPodcasts = mPreferences.getString(DOWNLOADING_PODCASTS, "");
        return jsonToSet(jsonPodcasts);
    }

    public Set<Podcast> getDownloadedPodcasts() {
        String jsonPodcasts = mPreferences.getString(DOWNLOADED_PODCASTS, "");
        return jsonToSet(jsonPodcasts);
    }

    @Override
    public void deleteDownloadedPodcast(Podcast podcast) {
        Set<Podcast> podcasts = getDownloadedPodcasts();
        podcasts.remove(podcast);
        mPreferences.edit()
                .putString(DOWNLOADED_PODCASTS, setToJson(podcasts))
                .apply();
    }

    @Override @Nullable
    public String getDownloadedPodcastTitle(String audioId) {
        Set<Podcast> podcasts = getDownloadedPodcasts();
        for (Podcast podcast : podcasts) {
            if (podcast.getAudioId().equals(audioId)) {
                return podcast.getTitle();
            }
        }
        return null;
    }

    private void addDownloadedPodcast(Podcast podcast) {
        Set<Podcast> podcasts = getDownloadedPodcasts();
        podcasts.add(podcast);
        mPreferences.edit()
                .putString(DOWNLOADED_PODCASTS, setToJson(podcasts))
                .apply();
    }

    @Nullable
    private Podcast getDownloadingPodcast(String audioId) {
        Podcast downloadedPodcast = null;

        for (Podcast podcast : getDownloadingPodcasts()) {
            if (podcast.getAudioId().equals(audioId)) {
                downloadedPodcast = podcast;
                break;
            }
        }
        return downloadedPodcast;
    }

    private Set<Podcast> jsonToSet(String json) {
        TypeToken<Set<Podcast>> token = new TypeToken<Set<Podcast>>() {};
        Set<Podcast> podcasts = mGson.fromJson(json, token.getType());
        return podcasts == null ? new HashSet<Podcast>() : podcasts;
    }

    private String setToJson(Set<Podcast> podcasts) {
        return mGson.toJson(podcasts);
    }
}
