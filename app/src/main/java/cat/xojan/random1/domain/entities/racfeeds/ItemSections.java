package cat.xojan.random1.domain.entities.racfeeds;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class ItemSections {

    public ItemSections() {}

    @ElementList(entry="item", inline=true)
    private List<ItemSection> mSections;

    public List<ItemSection> getSections() {
        return mSections;
    }
}
