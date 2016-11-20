package cat.xojan.random1.domain.entities.racfeeds;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(strict = false)
public class ItemProgram {

    public ItemProgram() {}

    @Element(name = "titulo")
    private String mTitle;

    @Element(name="secciones", required = false)
    private ItemSections mSections;

    public String getTitle() {
        return mTitle;
    }

    public List<ItemSection> getSections() {
        if (mSections == null) {
            return new ArrayList<ItemSection>();
        }
        return mSections.getSections();
    }
}
