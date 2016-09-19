package cat.xojan.random1.data;

import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.domain.entity.Program;
import cat.xojan.random1.domain.repository.ProgramRepository;

public class StaticProgramRepository implements ProgramRepository {

    @Override
    public List<Program> getProgramList() {
        List<Program> programs = new ArrayList<>(19);

        programs.add(Program.create("EL MON A RAC1", "el_mon_a_rac1"));
        programs.add(Program.create("LA COMPETENCIA", "la_competencia"));
        programs.add(Program.create("LA SEGONA HORA", "la_segona_hora"));
        programs.add(Program.create("14 15", "14_15"));
        programs.add(Program.create("PRIMER TOC", "primer_toc"));
        programs.add(Program.create("TOT ES POSSIBLE", "tot_es_possible"));
        programs.add(Program.create("VERSIO RAC1", "versio_rac1"));
        programs.add(Program.create("ISLANDIA", "islandia"));
        programs.add(Program.create("NO HO SE", "no_ho_se"));
        programs.add(Program.create("TU DIRAS", "tu_diras"));
        programs.add(Program.create("LA PRIMERA PEDRA", "la_primera_pedra"));
        programs.add(Program.create("VIA LLIURE", "via_lliure"));
        programs.add(Program.create("AMB MOLT DE GUST", "amb_molt_de_gust"));
        programs.add(Program.create("SUPERDIUMENGE", "superdiumenge"));
        programs.add(Program.create("EL BARÇA JUGA A RAC1", "el_barca_juga_a_rac1"));
        programs.add(Program.create("L'ESPANYOL JUGA A RAC1", "espanyol"));
        programs.add(Program.create("ULTRAESPORTS", "ultraesports"));
        programs.add(Program.create("MISTERIS", "misteris"));
        programs.add(Program.create("NO HI SOM PER FESTES", "no_hi_som_per_festes"));
        programs.add(Program.create("RAC1NCENTRAT", "rac1ncentrat"));

        return programs;
    }
}
