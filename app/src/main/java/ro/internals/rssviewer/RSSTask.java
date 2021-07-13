package ro.internals.rssviewer;

import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import ro.internals.rssviewer.feed.RSSFeed;
import ro.internals.rssviewer.feed.RSSParser;

public class RSSTask extends AsyncTask<String, Void, RSSFeed> {

    private static final String TAG = "RSSTask";

    private Caller caller;

    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public RSSTask(Caller caller) {
        this.caller = caller;
    }

    @Override
    protected RSSFeed doInBackground(String... strings) {
        RSSParser rssParser = new RSSParser(strings[0]);
        RSSFeed rssFeed = null;

        try {
            rssFeed = rssParser.readFeed();
        } catch (SAXException e) {
            Log.e(TAG, "readFeed: A SAX Exception has occurred: ", e);
            this.exception = e;
        } catch (ParserConfigurationException e) {
            Log.e(TAG, "readFeed: A Parser Configuration Exception has occurred: ", e);
            this.exception = e;
        } catch (IOException e) {
            Log.e(TAG, "readFeed: An IO Exception has occurred: ", e);
            this.exception = e;
        }

        return rssFeed;
    }

    @Override
    protected void onPostExecute(RSSFeed RSSFeed) {
        caller.onBackgroundTaskCompleted(RSSFeed);
    }
}
