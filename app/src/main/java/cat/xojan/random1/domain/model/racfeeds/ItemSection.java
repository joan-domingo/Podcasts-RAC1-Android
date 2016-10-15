package cat.xojan.random1.domain.model.racfeeds;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class ItemSection {

    public ItemSection() {}

    @Element(name = "titulo")
    private String mTitle;

    @Element(name = "url")
    private String mUrl;

    public String getTitle() {
        return mTitle;
    }

    public String getParam() {
        if (mUrl.contains("param")) {
            return mUrl.split("param=")[1];
        } else {
            return mUrl.split("cat=")[1].split("&")[0];
        }
    }
}
