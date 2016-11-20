package cat.xojan.random1.domain.repository;

import java.io.IOException;
import java.util.List;

import cat.xojan.random1.domain.entities.Program;

public interface ProgramRepository {

    List<Program> getProgramList() throws IOException;
}
