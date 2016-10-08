package cat.xojan.random1.domain.model.rss;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Rss feed parent class.
 */
@Root(name = "rss", strict = false)
public class RssFeed {
    @Element
    private Channel channel;

    public Channel getChannel() {
        return channel;
    }
}
