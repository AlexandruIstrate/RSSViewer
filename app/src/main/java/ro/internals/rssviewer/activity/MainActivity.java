package ro.internals.rssviewer.activity;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import ro.internals.rssviewer.Caller;
import ro.internals.rssviewer.R;
import ro.internals.rssviewer.RSSFeedFragment;
import ro.internals.rssviewer.RSSTask;
import ro.internals.rssviewer.RSSTaskStatus;
import ro.internals.rssviewer.adapters.ErrorAdapter;
import ro.internals.rssviewer.adapters.FeedEntryAdapter;
import ro.internals.rssviewer.adapters.LoadingAdapter;
import ro.internals.rssviewer.feed.Feeds;
import ro.internals.rssviewer.feed.RSSFeed;

public class MainActivity extends AppCompatActivity implements Caller {

    private static final String TAG = "MainActivity";

    private ListView list;
    private BaseAdapter listAdapter;

    private RSSFeed rssFeed;
    private RSSTask rssTask;

    private int currentFeedId = Feeds.UNDEFINED;
    private static final String FEED_ID_KEY = "FeedID";

    private RSSTaskStatus taskStatus;
    private Exception exception;

    private static final String RETAINED_FEED_FRAGMENT = "RetainedFeedFragment";
    private RSSFeedFragment feedFragment;

    public static final String INTENT_URL = "IntentURL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
            this.currentFeedId = Feeds.TOP_STORIES;
        else
            this.currentFeedId = savedInstanceState.getInt(FEED_ID_KEY);

        // Get GUI elements
        list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (rssFeed != null)
                    if (taskStatus == RSSTaskStatus.DOWNLOADED)
                        viewFeed(rssFeed.getEntries().get(i).getLink());
            }
        });

        FragmentManager fragmentManager = getFragmentManager();
        feedFragment = (RSSFeedFragment) fragmentManager.findFragmentByTag(RETAINED_FEED_FRAGMENT);

        if (feedFragment == null && savedInstanceState == null) {
            feedFragment = new RSSFeedFragment();
            fragmentManager.beginTransaction().add(feedFragment, RETAINED_FEED_FRAGMENT).commit();

            downloadData();
        } else {
            this.rssFeed = feedFragment.getRssFeed();
            this.taskStatus = feedFragment.getTaskStatus();
            this.exception = feedFragment.getException();

            if (taskStatus == RSSTaskStatus.DOWNLOADED)
                displayListEntries();
            else if (taskStatus == RSSTaskStatus.ERROR)
                displayError(feedFragment.getException().getMessage());
            else if (taskStatus == RSSTaskStatus.DOWNLOADING)
                displayLoading();
        }
    }

    private void displayListEntries() {
        if (rssFeed != null) {
            this.taskStatus = RSSTaskStatus.DOWNLOADED;

            listAdapter = new FeedEntryAdapter(this, rssFeed.getEntries());
            list.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
        }
    }

    private void displayError(String message) {
        this.taskStatus = RSSTaskStatus.ERROR;

        listAdapter = new ErrorAdapter(this, message);
        list.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    private void displayLoading() {
        this.taskStatus = RSSTaskStatus.DOWNLOADING;

        listAdapter = new LoadingAdapter(this);
        list.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    private void downloadData() {
        displayLoading();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        rssTask = new RSSTask(this);
        rssTask.execute(Feeds.getFeedURL(currentFeedId));
    }

    private void purgeOldData() {
        this.rssFeed = null;
        this.taskStatus = null;
        this.exception = null;
    }

    @Override
    public void onBackgroundTaskCompleted(RSSFeed rssFeed) {
        purgeOldData();

        Exception rssTaskException = rssTask.getException();

        if (rssTaskException == null) {
            this.taskStatus = RSSTaskStatus.DOWNLOADED;
            this.rssFeed = rssFeed;
            feedFragment.setRssFeed(rssFeed);

            displayListEntries();
        } else {
            this.taskStatus = RSSTaskStatus.ERROR;
            this.exception = rssTaskException;

            displayError(rssTaskException.getMessage());
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        RSSFeedFragment feedFragment = (RSSFeedFragment) getFragmentManager().findFragmentByTag(RETAINED_FEED_FRAGMENT);
        feedFragment.setRssFeed(rssFeed);
        feedFragment.setTaskStatus(taskStatus);
        feedFragment.setException(exception);

        outState.putInt(FEED_ID_KEY, currentFeedId);
    }

    private void viewFeed(String url) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(INTENT_URL, url);
        startActivity(intent);
    }

    private void viewFeedList() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Feeds");
        builder.setSingleChoiceItems(Feeds.FEED_NAMES, currentFeedId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (currentFeedId != i) {
                    currentFeedId = i;
                    downloadData();
                }

                dialogInterface.dismiss();

                invalidateOptionsMenu();
            }
        });
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.cultureMenu).setVisible(false);
        menu.findItem(R.id.photoMenu).setVisible(false);
        menu.findItem(R.id.scienceMenu).setVisible(false);
        menu.findItem(R.id.securityMenu).setVisible(false);
        menu.findItem(R.id.transportationMenu).setVisible(false);

        switch (currentFeedId) {
            case Feeds.TOP_STORIES:
                menu.findItem(R.id.topStoriesMenu).setChecked(true);
                break;

            case Feeds.BUSINESS:
                menu.findItem(R.id.businessMenu).setChecked(true);
                break;

            case Feeds.GEAR:
                menu.findItem(R.id.gearMenu).setChecked(true);
                break;

            case Feeds.CULTURE:
                menu.findItem(R.id.cultureMenu).setVisible(true).setChecked(true);
                break;

            case Feeds.PHOTO:
                menu.findItem(R.id.photoMenu).setVisible(true).setChecked(true);
                break;

            case Feeds.SCIENCE:
                menu.findItem(R.id.scienceMenu).setVisible(true).setChecked(true);
                break;

            case Feeds.SECURITY:
                menu.findItem(R.id.securityMenu).setVisible(true).setChecked(true);
                break;

            case Feeds.TRANSPORTATION:
                menu.findItem(R.id.transportationMenu).setVisible(true).setChecked(true);
                break;

            case Feeds.UNDEFINED:

            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refreshMenu:
                Log.d(TAG, "onOptionsItemSelected: Refreshing...");
                downloadData();
                return true;

            case R.id.topStoriesMenu:
                item.setChecked(true);
                currentFeedId = Feeds.TOP_STORIES;

                downloadData();
                return true;

            case R.id.businessMenu:
                item.setChecked(true);
                currentFeedId = Feeds.BUSINESS;

                downloadData();
                return true;

            case R.id.gearMenu:
                item.setChecked(true);
                currentFeedId = Feeds.GEAR;

                downloadData();
                return true;

            case R.id.cultureMenu:
                item.setChecked(true);
                currentFeedId = Feeds.CULTURE;

                downloadData();
                return true;

            case R.id.photoMenu:
                item.setChecked(true);
                currentFeedId = Feeds.PHOTO;

                downloadData();
                return true;

            case R.id.scienceMenu:
                item.setChecked(true);
                currentFeedId = Feeds.SCIENCE;

                downloadData();
                return true;

            case R.id.securityMenu:
                item.setChecked(true);
                currentFeedId = Feeds.SECURITY;

                downloadData();
                return true;

            case R.id.transportationMenu:
                item.setChecked(true);
                currentFeedId = Feeds.TRANSPORTATION;

                downloadData();
                return true;

            case R.id.moreMenu:
                viewFeedList();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
