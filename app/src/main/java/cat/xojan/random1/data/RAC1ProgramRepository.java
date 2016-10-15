package cat.xojan.random1.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.domain.model.Program;
import cat.xojan.random1.domain.model.Section;
import cat.xojan.random1.domain.model.racfeeds.ItemProgram;
import cat.xojan.random1.domain.model.racfeeds.ItemSection;
import cat.xojan.random1.domain.model.racfeeds.RacFeeds;
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

    /*@Override
    public List<Program> getProgramList() {
        List<Program> hour_by_hour = new ArrayList<>(19);

        hour_by_hour.add(new Program("EL MON A RAC1", "el_mon_a_rac1"));
        hour_by_hour.add(new Program("LA COMPETENCIA", "la_competencia"));
        hour_by_hour.add(new Program("LA SEGONA HORA", "la_segona_hora"));
        hour_by_hour.add(new Program("14 15", "14_15"));
        hour_by_hour.add(new Program("PRIMER TOC", "primer_toc"));
        hour_by_hour.add(new Program("TOT ES POSSIBLE", "tot_es_possible"));
        hour_by_hour.add(new Program("VERSIO RAC1", "versio_rac1"));
        hour_by_hour.add(new Program("ISLANDIA", "islandia"));
        hour_by_hour.add(new Program("NO HO SE", "no_ho_se"));
        hour_by_hour.add(new Program("TU DIRAS", "tu_diras"));
        hour_by_hour.add(new Program("LA PRIMERA PEDRA", "la_primera_pedra"));
        hour_by_hour.add(new Program("VIA LLIURE", "via_lliure"));
        hour_by_hour.add(new Program("AMB MOLT DE GUST", "amb_molt_de_gust"));
        hour_by_hour.add(new Program("SUPERDIUMENGE", "superdiumenge"));
        hour_by_hour.add(new Program("EL BARÇA JUGA A RAC1", "el_barca_juga_a_rac1"));
        hour_by_hour.add(new Program("L'ESPANYOL JUGA A RAC1", "espanyol"));
        hour_by_hour.add(new Program("ULTRAESPORTS", "ultraesports"));
        hour_by_hour.add(new Program("MISTERIS", "misteris"));
        hour_by_hour.add(new Program("NO HI SOM PER FESTES", "no_hi_som_per_festes"));
        hour_by_hour.add(new Program("RAC1NCENTRAT", "rac1ncentrat"));

        return hour_by_hour;
    }*/

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
