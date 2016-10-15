package cat.xojan.random1.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.domain.model.Podcast;
import cat.xojan.random1.domain.model.rss.FeedItem;
import cat.xojan.random1.domain.model.rss.RssFeed;
import cat.xojan.random1.domain.repository.PodcastRepository;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RAC1PodcastRepository implements PodcastRepository {

    private static final String RAC1_URL = "http://www.racalacarta.com/";

    private Retrofit mRetrofit;

    private interface RAC1Service {
        @GET("wp-feeder.php")
        Call<RssFeed> listPodcasts(@Query("limit") String numPodcasts);

        @GET("wp-feeder.php")
        Call<RssFeed> listPodcastsByProgram(@Query("limit") String numPodcasts,
                                            @Query("param") String program);

        @GET("wp-feeder.php")
        Call<RssFeed> listPodcastsBySection(@Query("limit") String numPodcasts,
                                            @Query("cat") String section,
                                            @Query("cp") int cp);
    }

    public RAC1PodcastRepository() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(RAC1_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
    }

    @Override
    public List<Podcast> getLatestPodcasts(int numPodcasts) throws IOException {
        RAC1Service service = mRetrofit.create(RAC1Service.class);
        Call<RssFeed> feed = service.listPodcasts(String.valueOf(numPodcasts));

        Response<RssFeed> response = feed.execute();
        RssFeed rssFeed = response.body();

        return rssFeedToPodcasts(rssFeed);
    }

    @Override
    public List<Podcast> getLatestPodcasts(int numPodcasts, String program) throws IOException {
        RAC1Service service = mRetrofit.create(RAC1Service.class);
        Call<RssFeed> feed = service.listPodcastsByProgram(String.valueOf(numPodcasts), program);

        Response<RssFeed> response = feed.execute();
        RssFeed rssFeed = response.body();

        return rssFeedToPodcasts(rssFeed);
    }

    @Override
    public List<Podcast> getLatestSections(int numPodcasts, String section) throws IOException {
        RAC1Service service = mRetrofit.create(RAC1Service.class);
        Call<RssFeed> feed = service.listPodcastsBySection(String.valueOf(numPodcasts), section, 6);

        Response<RssFeed> response = feed.execute();
        RssFeed rssFeed = response.body();

        return rssFeedToPodcasts(rssFeed);
    }

    private List<Podcast> rssFeedToPodcasts(RssFeed rssFeed) {
        List<Podcast> podcasts = new ArrayList<>(rssFeed.getChannel().getItems().size());
        String programTitle = rssFeed.getChannel().getTitle();
        for (FeedItem item : rssFeed.getChannel().getItems()) {
            podcasts.add(new Podcast(item.getDescription(), item.getLink(), item.getCategory(),
                    programTitle));
        }
        return podcasts;
    }
}
