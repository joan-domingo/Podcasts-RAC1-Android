package cat.xojan.random1.domain.model.racfeeds;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "podcasts", strict = false)
public class RacFeeds {

    @ElementList(entry="item", inline=true)
    private List<ItemProgram> mItems;

    public List<ItemProgram> getItems() {
        return mItems;
    }
}
