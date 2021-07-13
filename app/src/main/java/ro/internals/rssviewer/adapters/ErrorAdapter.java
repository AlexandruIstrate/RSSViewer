package ro.internals.rssviewer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ro.internals.rssviewer.R;

public class ErrorAdapter extends BaseAdapter {

    private Context context;

    private String errorMessage;

    public ErrorAdapter(Context context, String errorMessage) {
        this.context = context;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View mainView = inflater.inflate(R.layout.error_item, viewGroup, false);

        TextView errorView = mainView.findViewById(R.id.errorView);
        errorView.setText(errorMessage);

        return mainView;
    }
}
