package cat.xojan.random1.domain.entity.rss;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Represents a podcast item.
 */
@Root(strict = false)
public class FeedItem {
    @Element
    private String title;
    @Element
    private String description;
    @Element
    private String link;
    @Element
    private String category;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getCategory() {
        return category;
    }
}
