package cat.xojan.random1.domain.entities.rss;

import android.support.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class Channel {

    @Element(required = false)
    private String title;

    @ElementList(entry="item", inline=true)
    private List<FeedItem> items;

    public List<FeedItem> getItems() {
        return items;
    }

    @Nullable
    public String getTitle() {
        if (title != null) {
            return title.split("-")[0];
        }
        return null;
    }
}
