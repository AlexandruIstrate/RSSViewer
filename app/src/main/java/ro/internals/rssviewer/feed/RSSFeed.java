package ro.internals.rssviewer.feed;

import java.util.ArrayList;

public class RSSFeed {

    private String title;
    private String link;
    private String description;
    private String language;
    private String copyright;

    private int feedType;

    private ArrayList<FeedEntry> entries;

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public String getCopyright() {
        return copyright;
    }

    public ArrayList<FeedEntry> getEntries() {
        return entries;
    }

    public RSSFeed(String title, String link, String description, String language, String copyright, ArrayList<FeedEntry> entries) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.language = language;
        this.copyright = copyright;
        this.feedType = Feeds.UNDEFINED;

        this.entries = entries;
    }

    public RSSFeed(String title, String link, String description, String language, String copyright, ArrayList<FeedEntry> entries, int feedType) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.language = language;
        this.copyright = copyright;
        this.feedType = feedType;

        this.entries = entries;
    }
}
