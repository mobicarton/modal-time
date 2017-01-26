package mobi.carton.modaltime.maze;


import android.os.Bundle;
import android.widget.GridView;

import mobi.carton.library.CartonActivity;
import mobi.carton.library.CartonSdk;
import mobi.carton.library.HeadRecognition;
import mobi.carton.modaltime.R;
import mobi.carton.modaltime.TouchView;

public class MazeActivity extends CartonActivity
        implements
        HeadRecognition.OnHeadGestureListener,
        TouchView.OnFingerTouchGestureListener {


    private MazeAdapter mAdapter;
    private int[] mMazeArray;
    private int currentPosition;


    private HeadRecognition mHeadRecognition;


    private void initMazeArray() {
        for (int i = 0; i < 16; i++) {
            mMazeArray[i] = MazeAdapter.STATE_NEUTRAL;
        }

        mMazeArray[10] = MazeAdapter.STATE_START;
        mMazeArray[15] = MazeAdapter.STATE_END;
        mMazeArray[7] = MazeAdapter.STATE_POINT_BEFORE;
        mMazeArray[0] = MazeAdapter.STATE_POINT_BEFORE;
        mMazeArray[13] = MazeAdapter.STATE_POINT_BEFORE;

        currentPosition = 10;
        mMazeArray[currentPosition] += 1;
    }


    private void setPosition(int newPosition) {
        mMazeArray[currentPosition] -= 1;
        mMazeArray[newPosition] += 1;
        currentPosition = newPosition;
        mAdapter.notifyDataSetChanged();
    }


    private void goRight() {
        if ((currentPosition+1) % 4 > 0) {
            setPosition(currentPosition + 1);
        }
    }


    private void goLeft() {
        if (currentPosition % 4 > 0) {
            setPosition(currentPosition - 1);
        }
    }


    private void goUp() {
        if (currentPosition > 3) {
            setPosition(currentPosition - 4);
        }
    }


    private void goDown() {
        if (currentPosition < 12) {
            setPosition(currentPosition + 4);
        }
    }


    /*
    LIFECYCLE
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze);

        GridView gridViewMaze = (GridView) findViewById(R.id.gridViewMaze);

        /*
        Maze init
         */
        mMazeArray = new int[16];
        initMazeArray();
        mAdapter = new MazeAdapter(this, mMazeArray);
        gridViewMaze.setAdapter(mAdapter);

        /*
        Interaction
         */
        mHeadRecognition = new HeadRecognition(this);
        mHeadRecognition.setOnHeadGestureListener(this);

        TouchView touchView = (TouchView) findViewById(R.id.touchView);
        touchView.setOnFingerTouchGestureListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mHeadRecognition.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mHeadRecognition.stop();
    }


    /*
    IMPLEMENTS
     */


    // HeadRecognition.OnHeadGestureListener
    @Override
    public void onTilt(int direction) {
        switch (direction) {
            case HeadRecognition.TILT_LEFT:
                goLeft();
                break;
            case HeadRecognition.TILT_RIGHT:
                goRight();
                break;
        }
    }


    // HeadRecognition.OnHeadGestureListener
    @Override
    public void onNod(int direction) {
        switch (direction) {
            case HeadRecognition.NOD_DOWN:
                goDown();
                break;
            case HeadRecognition.NOD_UP:
                goUp();
                break;
        }
    }


    // HeadRecognition.OnHeadGestureListener
    @Override
    public void onShake() {

    }


    // TouchView.OnFingerTouchGestureListener
    @Override
    public void onSwipe(int direction) {
        switch (direction) {
            case CartonSdk.RIGHT:
                goRight();
                break;
            case CartonSdk.LEFT:
                goLeft();
                break;
            case CartonSdk.UP:
                goUp();
                break;
            case CartonSdk.DOWN:
                goDown();
                break;
            default:
                break;
        }
    }
}
