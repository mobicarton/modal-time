package mobi.carton.modaltime.maze;


import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Locale;

import io.github.controlwear.library.android.ControlWearApi;
import mobi.carton.library.CartonActivity;
import mobi.carton.library.CartonSdk;
import mobi.carton.library.HeadRecognition;
import mobi.carton.modaltime.CentiChronometer;
import mobi.carton.modaltime.LogSaved;
import mobi.carton.modaltime.MainActivity;
import mobi.carton.modaltime.Pref;
import mobi.carton.modaltime.R;
import mobi.carton.modaltime.TouchView;
import mobi.carton.modaltime.training.TrainingActivity;

public class MazeActivity extends CartonActivity
        implements
        HeadRecognition.OnHeadGestureListener,
        TouchView.OnFingerTouchGestureListener,
        ControlWearApi.OnControlSwipeListener {


    private MazeAdapter mAdapter;
    private int[] mMazeArray;
    private int currentPosition;


    private CentiChronometer mChronometer;
    private TextView mTextViewTrial;
    private float mTrialTiming;


    private HeadRecognition mHeadRecognition;


    private ControlWearApi mControlWearApi;


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


    private boolean checkMazeEnd() {
        for (int i = 0; i < 16; i++) {
            if (mMazeArray[i] == MazeAdapter.STATE_POINT_BEFORE)
                return false;
        }
        return true;
    }


    private void setPosition(int newPosition) {
        mMazeArray[currentPosition] -= 1;

        if (mMazeArray[currentPosition] == MazeAdapter.STATE_START) {
            mChronometer.start();
        }

        if (mMazeArray[newPosition] == MazeAdapter.STATE_END) {
            if (checkMazeEnd()) {
                mChronometer.stop();
                LogSaved.LogWithHeader(getApplicationContext(), "finish Maze in : " + String.format(Locale.ENGLISH, "%05.2f", mChronometer.getCount()));
                if (mTrialTiming == 0) {
                    mTrialTiming = mChronometer.getCount();
                    mTextViewTrial.setText(String.format(Locale.ENGLISH, "%05.2f", mTrialTiming));
                    mChronometer.reset();
                    initMazeArray();
                    mAdapter.notifyDataSetChanged();
                    return;
                } else {
                    if (Pref.getCurrentInteraction(getApplicationContext()) == 4) { // should be 4 with voice and smart watch
                        Pref.incrementCurrentActivity(getApplicationContext(), 1); // if user did with all kind of interaction we increment current activity
                        LogSaved.LogWithHeader(getApplicationContext(), "start Origami");
                        Pref.setCurrentInteraction(getApplicationContext(), 1); // and we reset to 0 interaction
                    } else {
                        Pref.incrementCurrentInteraction(getApplicationContext(), 1); // if user just did it in all direction we increment interaction
                    }
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra(MainActivity.EXTRA_NOCONFIG, true);
                    startActivity(intent);
                    finish();
                }
            }
        }

        mMazeArray[newPosition] += 1;

        if (mMazeArray[newPosition] == MazeAdapter.STATE_START + 1) {
            mChronometer.stop();
            mChronometer.reset();
            initMazeArray();
        }

        if (mMazeArray[currentPosition] == MazeAdapter.STATE_POINT_BEFORE) {
            mMazeArray[currentPosition] = MazeAdapter.STATE_POINT_AFTER;
        }


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
        Timing init
         */
        mChronometer = (CentiChronometer) findViewById(R.id.chronometer);
        mTrialTiming = 0f;
        mTextViewTrial = (TextView) findViewById(R.id.textViewTrialTiming);

        /*
        Interaction
         */
        String interaction = getIntent().getStringExtra(TrainingActivity.EXTRA_INTERACTION);

        int resourceId = getResources().getIdentifier("maze_" + interaction, "string", getPackageName());
        TextView textViewInteraction = (TextView) findViewById(R.id.textViewInteraction);
        textViewInteraction.setText(getString(resourceId));

        mHeadRecognition = new HeadRecognition(this);
        TouchView touchView = (TouchView) findViewById(R.id.touchView);
        mControlWearApi = new ControlWearApi(this);

        switch (interaction) {
            case "finger":
                touchView.setOnFingerTouchGestureListener(this);
                break;
            case "head":
                mHeadRecognition.setOnHeadGestureListener(this);
                break;
            case "watch":
                mControlWearApi.setOnControlSwipeListener(this);
                mControlWearApi.startWearApp(ControlWearApi.MODE_PAD);
                break;
            case "voice":
                break;
            case "all":
                touchView.setOnFingerTouchGestureListener(this);
                mHeadRecognition.setOnHeadGestureListener(this);
                mControlWearApi.setOnControlSwipeListener(this);
                mControlWearApi.startWearApp(ControlWearApi.MODE_PAD);
                break;
        }
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
        mControlWearApi.disconnect();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mControlWearApi.connect();
    }


    /*
    IMPLEMENTS
     */


    // HeadRecognition.OnHeadGestureListener
    @Override
    public void onTilt(int direction) {
        switch (direction) {
            case HeadRecognition.TILT_LEFT:
                LogSaved.LogWithHeaderProcess(getApplicationContext(), "head", CartonSdk.LEFT);
                goLeft();
                break;
            case HeadRecognition.TILT_RIGHT:
                LogSaved.LogWithHeaderProcess(getApplicationContext(), "head", CartonSdk.RIGHT);
                goRight();
                break;
        }
    }


    // HeadRecognition.OnHeadGestureListener
    @Override
    public void onNod(int direction) {
        switch (direction) {
            case HeadRecognition.NOD_DOWN:
                LogSaved.LogWithHeaderProcess(getApplicationContext(), "head", CartonSdk.DOWN);
                goDown();
                break;
            case HeadRecognition.NOD_UP:
                LogSaved.LogWithHeaderProcess(getApplicationContext(), "head", CartonSdk.UP);
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
                LogSaved.LogWithHeaderProcess(getApplicationContext(), "finger", CartonSdk.RIGHT);
                goRight();
                break;
            case CartonSdk.LEFT:
                LogSaved.LogWithHeaderProcess(getApplicationContext(), "finger", CartonSdk.LEFT);
                goLeft();
                break;
            case CartonSdk.UP:
                LogSaved.LogWithHeaderProcess(getApplicationContext(), "finger", CartonSdk.UP);
                goUp();
                break;
            case CartonSdk.DOWN:
                LogSaved.LogWithHeaderProcess(getApplicationContext(), "finger", CartonSdk.DOWN);
                goDown();
                break;
            default:
                break;
        }
    }


    // ControlWearApi.OnControlSwipeListener
    @Override
    public void OnSwipeLeft() {
        LogSaved.LogWithHeaderProcess(getApplicationContext(), "watch", CartonSdk.LEFT);
        goLeft();
    }


    // ControlWearApi.OnControlSwipeListener
    @Override
    public void OnSwipeRight() {
        LogSaved.LogWithHeaderProcess(getApplicationContext(), "watch", CartonSdk.RIGHT);
        goRight();
    }


    // ControlWearApi.OnControlSwipeListener
    @Override
    public void OnSwipeUp() {
        LogSaved.LogWithHeaderProcess(getApplicationContext(), "watch", CartonSdk.UP);
        goUp();
    }


    // ControlWearApi.OnControlSwipeListener
    @Override
    public void OnSwipeDown() {
        LogSaved.LogWithHeaderProcess(getApplicationContext(), "watch", CartonSdk.DOWN);
        goDown();
    }
}
