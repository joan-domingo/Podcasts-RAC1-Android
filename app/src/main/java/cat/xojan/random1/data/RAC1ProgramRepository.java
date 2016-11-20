package cat.xojan.random1.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.domain.entities.racfeeds.ItemProgram;
import cat.xojan.random1.domain.entities.racfeeds.ItemSection;
import cat.xojan.random1.domain.entities.racfeeds.RacFeeds;
import cat.xojan.random1.domain.repository.ProgramRepository;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;

public class RAC1ProgramRepository implements ProgramRepository {

    private static final String RAC1_URL = "http://racfeeds.rac1.org/";

    private final Retrofit mRetrofit;

    private interface RAC1FeedService {
        @GET("podcastsRAC1.xml")
        Call<RacFeeds> listSections();
    }

    public RAC1ProgramRepository() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(RAC1_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
    }

    @Override
    public List<Program> getProgramList() throws IOException {
        RAC1FeedService service = mRetrofit.create(RAC1FeedService.class);
        Call<RacFeeds> feed = service.listSections();

        Response<RacFeeds> response = feed.execute();
        RacFeeds racFeeds = response.body();
        return racFeedsToProgramList(racFeeds);
    }

    private List<Program> racFeedsToProgramList(RacFeeds racFeeds) {
        List<Program> programs = new ArrayList<>();

        for (ItemProgram itemProgram : racFeeds.getItems()) {
            if (itemProgram.getSections().size() > 0) {
                String programParam = itemProgram.getSections().get(0).getParam();
                programs.add(new Program(itemProgram.getTitle(), programParam,
                        itemSectionToSection(itemProgram.getSections(), programParam)));
            }
        }
        return programs;
    }

    private List<Section> itemSectionToSection(List<ItemSection> items, String programParam) {
        List<Section> sections = new ArrayList<>();

        for (ItemSection item : items) {
            sections.add(new Section(item.getTitle(), item.getParam(), programParam));
        }
        return sections;
    }
}
