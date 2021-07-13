package ro.internals.rssviewer;

import ro.internals.rssviewer.feed.RSSFeed;

public interface Caller {

    void onBackgroundTaskCompleted(RSSFeed RSSFeed);
}
