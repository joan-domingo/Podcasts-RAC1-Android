package cat.xojan.random1.commons;

import cat.xojan.random1.R;

public class ImageUtil {

    public static int getPodcastImageDrawable(String category) {
        switch (category) {
            case "EL MON A RAC1":
                return R.drawable.el_mon;
            case "LA COMPETENCIA":
                return R.drawable.la_competencia;
            case "LA SEGONA HORA":
                return R.drawable.la_segona_hora;
            case "14 15":
                return R.drawable.catorze_quinze;
            case "PRIMER TOC":
                return R.drawable.primer_toc;
            case "TOT ES POSSIBLE":
                return R.drawable.tot_es_possible;
            case "VERSIO RAC1":
                return R.drawable.versio;
            case "ISLANDIA":
                return R.drawable.islandia;
            case "NO HO SE":
                return R.drawable.no_ho_se;
            case "TU DIRAS":
                return R.drawable.tu_diras;
            case "LA PRIMERA PEDRA":
            case "LA PRIMERA PEDRA Vermut 3 0":
                return R.drawable.la_primera_pedra;
            case "VIA LLIURE":
                return R.drawable.via_lliure;
            case "EL BARÇA JUGA A RAC1":
            case "EL BARCA JUGA A RAC1":
                return R.drawable.el_barca_juga_a_rac1;
            case "RAC1NCENTRAT":
                return R.drawable.rac1ncentrat;
            case "NO HI SOM PER FESTES":
                return R.drawable.no_hi_som_per_festes;
            case "AMB MOLT DE GUST":
                return R.drawable.amb_molt_de_gust;
            case "LESPANYOL JUGA A RAC1":
            case "L'ESPANYOL JUGA A RAC1":
            case "L ESPANYOL JUGA A RAC1":
                return R.drawable.lespanyol_juga_a_rac1;
            case "ULTRAESPORTS":
                return R.drawable.ultraesports;
            case "MISTERIS":
            case "SUPERDIUMENGE":
            default:
                return R.drawable.default_rac1;
        }
    }

    public static String getPodcastImageUrl(String category) {
        switch (category) {
            case "EL MON A RAC1":
                return "http://www.rac1.cat/audioteca/rsc/img/person-small/el-mon.png";
            case "LA COMPETENCIA":
                return "http://www.rac1.cat/audioteca/rsc/img/person-small/la-competencia.png";
            case "LA SEGONA HORA":
                return "http://www.rac1.cat/audioteca/rsc/img/person-small/la-segona-hora.png";
            case "14 15":
                return "http://www.rac1.cat/audioteca/rsc/img/person-small/14-15.png";
            case "PRIMER TOC":
                return "http://www.rac1.cat/audioteca/rsc/img/person-small/primer-toc.png";
            case "TOT ES POSSIBLE":
                return "http://www.rac1.cat/audioteca/rsc/img/person-small/tot-es-possible.png";
            case "VERSIO RAC1":
                return "http://www.rac1.cat/audioteca/rsc/img/person-small/versio.png";
            case "ISLANDIA":
                return "http://www.rac1.cat/audioteca/rsc/img/person-small/islandia.png";
            case "NO HO SE":
                return "http://www.rac1.cat/audioteca/rsc/img/person-small/no-ho-se.png";
            case "TU DIRAS":
                return "http://www.rac1.cat/audioteca/rsc/img/person-small/tu-diras.png";
            case "LA PRIMERA PEDRA":
            case "LA PRIMERA PEDRA Vermut 3 0":
                return "http://www.rac1.cat/audioteca/rsc/img/person-small/la-primera-pedra.png";
            case "VIA LLIURE":
                return "http://www.rac1.cat/audioteca/rsc/img/person-small/via-lliure.png";
            case "EL BARÇA JUGA A RAC1":
            case "EL BARCA JUGA A RAC1":
                return "http://www.rac1.cat/audioteca/rsc/img/person-small/el-barca-juga-a-rac1.png";
            case "RAC1NCENTRAT":
                return "http://www.rac1.cat/audioteca/rsc/img/person-small/rac1ncentrat.png";
            case "NO HI SOM PER FESTES":
                return "http://www.rac1.cat/audioteca/rsc/img/person-small/no-hi-som-per-festes.png";
            case "AMB MOLT DE GUST":
                return "http://www.rac1.cat/audioteca/rsc/img/person-small/amb-molt-de-gust.png";
            case "LESPANYOL JUGA A RAC1":
            case "L'ESPANYOL JUGA A RAC1":
            case "L ESPANYOL JUGA A RAC1":
                return "http://www.rac1.cat/audioteca/rsc/img/person-small/lespanyol-juga-a-rac1.png";
            case "ULTRAESPORTS":
                return "http://www.rac1.cat/audioteca/rsc/img/person-small/ultraesports.png";
            case "MISTERIS":
            case "SUPERDIUMENGE":
            default:
                return "http://www.rac1.cat/audioteca/rsc/img/person-small/default.png";
        }
    }
}
