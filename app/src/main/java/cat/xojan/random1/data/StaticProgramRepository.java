package cat.xojan.random1.data;

import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.domain.model.Program;
import cat.xojan.random1.domain.repository.ProgramRepository;

public class StaticProgramRepository implements ProgramRepository {

    @Override
    public List<Program> getProgramList() {
        List<Program> programs = new ArrayList<>(19);

        programs.add(new Program("EL MON A RAC1", "el_mon_a_rac1"));
        programs.add(new Program("LA COMPETENCIA", "la_competencia"));
        programs.add(new Program("LA SEGONA HORA", "la_segona_hora"));
        programs.add(new Program("14 15", "14_15"));
        programs.add(new Program("PRIMER TOC", "primer_toc"));
        programs.add(new Program("TOT ES POSSIBLE", "tot_es_possible"));
        programs.add(new Program("VERSIO RAC1", "versio_rac1"));
        programs.add(new Program("ISLANDIA", "islandia"));
        programs.add(new Program("NO HO SE", "no_ho_se"));
        programs.add(new Program("TU DIRAS", "tu_diras"));
        programs.add(new Program("LA PRIMERA PEDRA", "la_primera_pedra"));
        programs.add(new Program("VIA LLIURE", "via_lliure"));
        programs.add(new Program("AMB MOLT DE GUST", "amb_molt_de_gust"));
        programs.add(new Program("SUPERDIUMENGE", "superdiumenge"));
        programs.add(new Program("EL BARÇA JUGA A RAC1", "el_barca_juga_a_rac1"));
        programs.add(new Program("L'ESPANYOL JUGA A RAC1", "espanyol"));
        programs.add(new Program("ULTRAESPORTS", "ultraesports"));
        programs.add(new Program("MISTERIS", "misteris"));
        programs.add(new Program("NO HI SOM PER FESTES", "no_hi_som_per_festes"));
        programs.add(new Program("RAC1NCENTRAT", "rac1ncentrat"));

        return programs;
    }
}
