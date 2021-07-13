package ro.internals.rssviewer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import ro.internals.rssviewer.feed.RSSFeed;

public class RSSFeedFragment extends Fragment {

    private RSSFeed rssFeed;

    private RSSTaskStatus taskStatus;
    private Exception exception;

    public RSSFeed getRssFeed() {
        return rssFeed;
    }

    public void setRssFeed(RSSFeed rssFeed) {
        this.rssFeed = rssFeed;
    }

    public RSSTaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(RSSTaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
