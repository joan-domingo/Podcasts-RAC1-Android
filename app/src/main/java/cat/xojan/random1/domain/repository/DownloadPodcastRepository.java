package cat.xojan.random1.domain.repository;

import java.util.Set;

import cat.xojan.random1.domain.entities.Podcast;

public interface DownloadPodcastRepository {

    boolean addDownloadingPodcast(Podcast podcast);

    boolean deleteDownloadingPodcast(Podcast podcast);

    void setPodcastAsDownloaded(String audioId, String filePath);

    Set<Podcast> getDownloadingPodcasts();

    Set<Podcast> getDownloadedPodcasts();

    void deleteDownloadedPodcast(Podcast podcast);
}
