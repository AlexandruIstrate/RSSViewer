package ro.internals.rssviewer.feed;

import android.graphics.Bitmap;

public class FeedEntry {

    private String title;
    private String description;
    private String link;
    private Bitmap icon;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public FeedEntry(String title, String description, String link, Bitmap icon) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "FeedEntry{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
