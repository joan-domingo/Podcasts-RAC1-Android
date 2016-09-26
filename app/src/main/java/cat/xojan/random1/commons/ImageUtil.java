package cat.xojan.random1.commons;

public class ImageUtil {

    public static String getPodcastImage(String category) {
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
