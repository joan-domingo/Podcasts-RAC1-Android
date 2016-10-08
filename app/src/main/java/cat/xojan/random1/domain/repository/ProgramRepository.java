package cat.xojan.random1.domain.repository;

import java.util.List;

import cat.xojan.random1.domain.model.Program;

public interface ProgramRepository {
    List<Program> getProgramList();
}
