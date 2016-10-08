package cat.xojan.random1.domain.model.rss;

import android.text.TextUtils;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Represents a podcast item.
 */
@Root(strict = false)
public class FeedItem {
    @Element
    private String description;
    @Element
    private String link;
    @Element(required = false)
    private String category;

    public String getDescription() {
        return description.replace(":", "");
    }

    public String getLink() {
        return link;
    }

    public String getCategory() {
        return TextUtils.isEmpty(category) ? "" : category;
    }
}
