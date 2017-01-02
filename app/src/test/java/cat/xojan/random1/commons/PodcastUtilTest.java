package cat.xojan.random1.commons;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.domain.entities.Podcast;

import static org.junit.Assert.assertEquals;

public class PodcastUtilTest {

    @Test
    public void test_something() {
        // Given a list of loaded and downloaded podcasts
        List<Podcast> loaded = getLoadedPodcasts();
        List<Podcast> downloaded = getDownloadedPodcasts();

        // When we want to sync them
        PodcastUtil.updateDownloadedPodcasts(loaded, downloaded);

        // Then the the loaded podcasts list is updated
        assertEquals(loaded.get(0).getState(),  Podcast.State.DOWNLOADED);
        assertEquals(loaded.get(1).getState(),  Podcast.State.DOWNLOADED);
        assertEquals(loaded.get(2).getState(),  Podcast.State.LOADED);
    }

    private List<Podcast> getLoadedPodcasts() {
        List<Podcast> podcasts = new ArrayList<>();
        podcasts.add(new Podcast("path1", "program1", "title1"));
        podcasts.add(new Podcast("path2", "program2", "title2"));
        podcasts.add(new Podcast("path3", "program3", "title3"));
        return podcasts;
    }

    private List<Podcast> getDownloadedPodcasts() {
        List<Podcast> podcasts = new ArrayList<>();

        Podcast podcast1 = new Podcast("path1", "program1", "title1");
        podcast1.setState(Podcast.State.DOWNLOADED);
        podcasts.add(podcast1);

        Podcast podcast2 = new Podcast("path2", "program2", "title2");
        podcast2.setState(Podcast.State.DOWNLOADED);
        podcasts.add(podcast2);

        return podcasts;
    }
}
