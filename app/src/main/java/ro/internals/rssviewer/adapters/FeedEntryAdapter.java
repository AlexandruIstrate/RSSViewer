package ro.internals.rssviewer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ro.internals.rssviewer.R;
import ro.internals.rssviewer.feed.FeedEntry;

public class FeedEntryAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<FeedEntry> feedEntries;

    public FeedEntryAdapter(Context context, ArrayList<FeedEntry> feedEntries) {
        super();
        this.context = context;
        this.feedEntries = feedEntries;
    }

    @Override
    public int getCount() {
        return feedEntries.size();
    }

    @Override
    public FeedEntry getItem(int i) {
        return feedEntries.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_item, viewGroup, false);

        FeedEntry feedEntry = getItem(i);

        TextView titleView = rowView.findViewById(R.id.titleView);
        titleView.setText(feedEntry.getTitle());

        TextView descriptionView = rowView.findViewById(R.id.descriptionView);
        descriptionView.setText(feedEntry.getDescription());

        ImageView imageView = rowView.findViewById(R.id.imageView);
        imageView.setImageBitmap(feedEntry.getIcon());

        return rowView;
    }
}
