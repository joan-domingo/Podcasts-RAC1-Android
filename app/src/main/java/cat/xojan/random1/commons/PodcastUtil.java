package cat.xojan.random1.commons;

import java.util.List;

import cat.xojan.random1.domain.entities.Podcast;

public class PodcastUtil {

    public static void updateDownloadedPodcasts(List<Podcast> loadedPodcasts,
                                                List<Podcast> downloadedPodcasts) {
        for (Podcast podcast : loadedPodcasts) {
            podcast.setFilePath(null);
            podcast.setState(Podcast.State.LOADED);
        }

        for (Podcast download : downloadedPodcasts) {
            int index = loadedPodcasts.indexOf(download);
            if (index >= 0) {
                Podcast podcast = loadedPodcasts.get(index);
                podcast.setFilePath(download.getFilePath());
                podcast.setState(download.getState());
            }
        }
    }
}
