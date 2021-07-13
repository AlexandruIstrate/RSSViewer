package ro.internals.rssviewer.feed;

import java.util.HashMap;

public class Feeds {

    public static final String[] FEED_NAMES = new String[] {"Top Stories", "Business", "Culture", "Gear", "Science", "Security", "Transportation", "Photo"};

    private static HashMap<Integer, String> rssFeeds = new HashMap<>();

    public static final int UNDEFINED = -1;
    public static final int TOP_STORIES = 0;
    public static final int BUSINESS = 1;
    public static final int CULTURE = 2;
    public static final int GEAR = 3;
    public static final int SCIENCE = 4;
    public static final int SECURITY = 5;
    public static final int TRANSPORTATION = 6;
    public static final int PHOTO = 7;

    public static HashMap<Integer, String> getRssFeeds() {
        return rssFeeds;
    }

    public static String getFeedURL(int id) {
        return rssFeeds.get(id);
    }

    public static String getFeedName(int id) {
        switch (id) {
            case TOP_STORIES:
                return "Top Stories";

            case BUSINESS:
                return "Business";

            case CULTURE:
                return "Culture";

            case GEAR:
                return "Gear";

            case SCIENCE:
                return "Science";

            case SECURITY:
                return "Security";

            case TRANSPORTATION:
                return "Transportation";

            case PHOTO:
                return "Photo";

            case UNDEFINED:
            default:
                return "Undefined";
        }
    }

    static {
        rssFeeds.put(TOP_STORIES, "https://www.wired.com/feed/rss");
        rssFeeds.put(BUSINESS, "https://www.wired.com/feed/category/business/latest/rss");
        rssFeeds.put(CULTURE, "https://www.wired.com/feed/category/culture/latest/rss");
        rssFeeds.put(GEAR, "https://www.wired.com/feed/category/gear/latest/rss");
        rssFeeds.put(SCIENCE, "https://www.wired.com/feed/category/science/latest/rss");
        rssFeeds.put(SECURITY, "https://www.wired.com/feed/category/security/latest/rss");
        rssFeeds.put(TRANSPORTATION, "https://www.wired.com/feed/category/transportation/latest/rss");
        rssFeeds.put(PHOTO, "https://www.wired.com/feed/category/photo/latest/rss");
    }
}
