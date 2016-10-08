package cat.xojan.random1.domain.model.rss;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class Channel {
    /*@Element
    String description;*/

    @ElementList(entry="item", inline=true)
    private List<FeedItem> items;

    public List<FeedItem> getItems() {
        return items;
    }
}
