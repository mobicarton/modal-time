package mobi.carton.modaltime.maze;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import mobi.carton.modaltime.R;


public class MazeAdapter extends BaseAdapter {


    private final Context mContext;


    public MazeAdapter(Context context) {
        this.mContext = context;
    }


    @Override
    public int getCount() {
        return 16;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.item_maze, parent, false);
        }

        final TextView textViewNumber = (TextView)convertView.findViewById(R.id.textViewNumber);
        textViewNumber.setText(String.valueOf(position + 1));

        return convertView;
    }
}