package cat.xojan.random1.domain.repository;

import java.io.IOException;
import java.util.List;

import cat.xojan.random1.domain.entity.Podcast;

public interface PodcastRepository {
    List<Podcast> getLatestPodcasts(int numPodcasts) throws IOException;
    List<Podcast> getLatestPodcastsByProgram(int numPodcasts, String program) throws IOException;
}
