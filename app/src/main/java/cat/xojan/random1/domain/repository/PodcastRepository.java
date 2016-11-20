package cat.xojan.random1.domain.repository;

import java.io.IOException;
import java.util.List;

import cat.xojan.random1.domain.entities.Podcast;

public interface PodcastRepository {

    List<Podcast> getLatestPodcasts(int numPodcasts) throws IOException;

    List<Podcast> getLatestPodcasts(int numPodcasts, String program) throws IOException;

    List<Podcast> getLatestSections(int numPodcasts, String section) throws IOException;
}
