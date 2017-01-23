package mobi.carton.modaltime.maze;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mobi.carton.modaltime.R;

import static mobi.carton.modaltime.R.id.textViewNumber;


public class MazeAdapter extends BaseAdapter {


    public static final int STATE_NEUTRAL = 0;
    public static final int STATE_START = 10;
    public static final int STATE_END = 20;
    public static final int STATE_POINT_BEFORE = 30;
    private static final int STATE_POINT_AFTER = 40;


    private final Context mContext;

    private int[] mMazeIntArray;


    public MazeAdapter(Context context, int[] mazeIntArray) {
        this.mContext = context;
        this.mMazeIntArray = mazeIntArray;
    }


    @Override
    public int getCount() {
        return mMazeIntArray.length;
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
        ViewHolder viewHolder;
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.item_maze, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.mBackground = (RelativeLayout) convertView.findViewById(R.id.relativeLayoutBackground);
            viewHolder.mNumber = (TextView) convertView.findViewById(textViewNumber);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mNumber.setText(String.valueOf(position + 1));

        switch (mMazeIntArray[position]) {
            case STATE_START+1:
            case STATE_NEUTRAL+1:
            case STATE_END+1:
            case STATE_POINT_AFTER+1:
            case STATE_POINT_BEFORE+1:
                viewHolder.mBackground.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorMazeActual));
                break;

            case STATE_START:
                viewHolder.mBackground.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorMazeStart));
                break;

            case STATE_END:
                viewHolder.mBackground.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorMazeEnd));
                break;

            case STATE_POINT_BEFORE:
                viewHolder.mBackground.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorMazePointBefore));
                break;

            case STATE_POINT_AFTER:
                viewHolder.mBackground.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorMazePointAfter));
                break;

            case STATE_NEUTRAL:
            default:
                viewHolder.mBackground.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorMazeNeutral));
                break;
        }

        return convertView;
    }


    private static class ViewHolder {
        private RelativeLayout mBackground;
        private TextView mNumber;
    }
}