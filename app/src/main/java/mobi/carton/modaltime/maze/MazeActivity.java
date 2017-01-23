package mobi.carton.modaltime.maze;


import android.os.Bundle;
import android.widget.GridView;

import mobi.carton.library.CartonActivity;
import mobi.carton.modaltime.R;

public class MazeActivity extends CartonActivity {



    private GridView gridViewMaze;
    private MazeAdapter mAdapter;


    private int[] mMazeArray;
    private int currentPosition;


    private void initMazeArray() {
        for (int i = 0; i < 16; i++) {
            mMazeArray[i] = MazeAdapter.STATE_NEUTRAL;
        }

        mMazeArray[10] = MazeAdapter.STATE_START;
        mMazeArray[15] = MazeAdapter.STATE_END;
        mMazeArray[7] = MazeAdapter.STATE_POINT_BEFORE;
        mMazeArray[0] = MazeAdapter.STATE_POINT_BEFORE;
        mMazeArray[13] = MazeAdapter.STATE_POINT_BEFORE;
    }


    private void setPosition(int newPosition) {
        mMazeArray[currentPosition] -= 1;
        mMazeArray[newPosition] += 1;
        currentPosition = newPosition;
        mAdapter.notifyDataSetChanged();
    }


    /*
    LIFECYCLE
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze);

        gridViewMaze = (GridView) findViewById(R.id.gridViewMaze);

        mMazeArray = new int[16];
        initMazeArray();

        currentPosition = 10;
        mMazeArray[currentPosition] += 1;

        mAdapter = new MazeAdapter(this, mMazeArray);

        gridViewMaze.setAdapter(mAdapter);
    }
}
