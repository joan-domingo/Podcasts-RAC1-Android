package cat.xojan.random1.domain.repository;

import java.io.IOException;
import java.util.List;

import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.entities.Program;
import rx.Observable;

public interface ProgramRepository {

    Observable<List<Program>> getProgramListObservable() throws IOException;

    Observable<List<Podcast>> getPodcastByProgram(String programId) throws IOException;

    Observable<List<Podcast>> getPodcastBySection(String programId, String sectionId)
            throws IOException;
}
