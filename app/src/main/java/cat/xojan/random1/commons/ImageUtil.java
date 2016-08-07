package cat.xojan.random1.commons;

public class ImageUtil {

    public static String getPodcastImage(String category) {
        switch (category) {
            case "EL MON A RAC1":
                return "http://www.rac1.org/images/reproductor/el_mon_a_rac1.jpg";
            case "EL BARÇA JUGA A RAC1":
            case "EL BARCA JUGA A RAC1":
                return "http://www.rac1.org/images/reproductor/el_barca_juga_a_rac1.jpg";
            case "LA COMPETENCIA":
                return "http://www.rac1.org/images/reproductor/la_competencia.jpg";
            case "LA NIT A RAC1":
                return "http://www.rac1.org/images/reproductor/la_nit_a_rac1.jpg";
            case "LA PRIMERA PEDRA":
            case "LA PRIMERA PEDRA Vermut 3 0":
                return "http://www.rac1.org/images/reproductor/la_primera_pedra.jpg";
            case "LA SEGONA HORA":
                return "http://www.rac1.org/images/reproductor/la_segona_hora.jpg";
            case "PRIMER TOC":
                return "http://www.rac1.org/images/reproductor/primer_toc.jpg";
            case "RAC1NCENTRAT":
                return "http://www.rac1.org/images/reproductor/rac1ncentrat.jpg";
            case "SUPERDIUMENGE":
                return "http://www.rac1.org/images/reproductor/superdiumenge.jpg";
            case "TOT ES POSSIBLE":
                return "http://www.rac1.org/images/reproductor/tot_es_possible.jpg";
            case "TU DIRAS":
                return "http://www.rac1.org/images/reproductor/tu_diras.jpg";
            case "VERSIO RAC1":
                return "http://www.rac1.org/images/reproductor/versio_rac1.jpg";
            case "VIA LLIURE":
                return "http://www.rac1.org/images/reproductor/via_lliure.jpg";
            /** Altres */
            case "100 METRES":
                return "http://www.rac1.org/images/reproductor/100_metres.jpg";
            case "14 15":
                return "http://www.rac1.org/images/reproductor/14_15.jpg";
            case "NO HI SOM PER FESTES":
                return "http://www.rac1.org/images/reproductor/no_hi_som_per_festes.jpg";
            case "NO HO SE":
                return "http://www.rac1.org/images/reproductor/no_ho_se.jpg";
            case "L'ESPANYOL JUGA A RAC1":
            case "LESPANYOL JUGA A RAC1":
            case "ULTRAESPORTS":
            case "MISTERIS":
            case "AMB MOLT DE GUST":
            default:
                return "http://www.rac1.org/images/reproductor/default.jpg";
        }
    }
}
