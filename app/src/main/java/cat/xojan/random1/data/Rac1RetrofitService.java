package cat.xojan.random1.data;

import cat.xojan.random1.domain.entities.PodcastData;
import cat.xojan.random1.domain.entities.ProgramData;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Rac1RetrofitService {
    @GET("v1/programs")
    Call<ProgramData> getProgramData();

    @GET("v1/sessions/{programId}")
    Observable<PodcastData> getPodcastData(@Path("programId") String programId);

    @GET("v1/sessions/{programId}/{sectionId}")
    Observable<PodcastData> getPodcastData(@Path("programId") String programId,
                                     @Path("sectionId") String sectionId);
}
