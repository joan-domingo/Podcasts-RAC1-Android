package cat.xojan.random1.data;

import java.io.IOException;
import java.util.List;

import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.entities.PodcastData;
import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.ProgramData;
import cat.xojan.random1.domain.repository.ProgramRepository;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class RemoteProgramRepository implements ProgramRepository {

    private static final String RAC1_URL = "http://www.rac1.cat/audioteca/api/app/";

    private final RAC1FeedService mService;

    private interface RAC1FeedService {
        //@GET("v1/now")
        //@GET("v1/properties")
        //@GET("v1/schedule")
        @GET("v1/programs")
        Call<ProgramData> getProgramData();

        @GET("v1/sessions/{programId}")
        Call<PodcastData> getPodcastData(@Path("programId") String programId);

        @GET("v1/sessions/{programId}/{sectionId}")
        Call<PodcastData> getPodcastData(@Path("programId") String programId,
                                         @Path("sectionId") String sectionId);
    }

    public RemoteProgramRepository() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RAC1_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        mService = retrofit.create(RAC1FeedService.class);
    }

    @Override
    public List<Program> getProgramList() throws IOException {
        return mService.getProgramData().execute().body().getPrograms();
    }

    @Override
    public List<Podcast> getPodcastByProgram(String programId) throws IOException {
        List<Podcast> podcasts = mService.getPodcastData(programId).execute().body().getPodcasts();
        for (Podcast podcast : podcasts) {
            podcast.setProgramId(programId);
        }
        return podcasts;
    }

    @Override
    public List<Podcast> getPodcastBySection(String programId, String sectionId) throws IOException {
        List<Podcast> podcasts = mService.getPodcastData(programId, sectionId)
                .execute().body().getPodcasts();
        for (Podcast podcast : podcasts) {
            podcast.setProgramId(programId);
        }
        return podcasts;
    }
}
