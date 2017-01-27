package mobi.carton.modaltime.training;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.controlwear.library.android.ControlWearApi;
import mobi.carton.csr.ContinuousSpeechRecognition;
import mobi.carton.library.CartonActivity;
import mobi.carton.library.CartonSdk;
import mobi.carton.library.HeadRecognition;
import mobi.carton.modaltime.MainActivity;
import mobi.carton.modaltime.NeedleView;
import mobi.carton.modaltime.Pref;
import mobi.carton.modaltime.R;
import mobi.carton.modaltime.TouchView;

public class TrainingActivity extends CartonActivity
        implements
        TouchView.OnFingerTouchGestureListener,
        HeadRecognition.OnHeadGestureListener,
        HeadRecognition.OnHeadTrackingListener,
        ContinuousSpeechRecognition.OnTextListener,
        ControlWearApi.OnControlSwipeListener {


    public static final String EXTRA_INTERACTION = "extra_interaction";

    private static final String EXTRA_DIRECTION = "extra_direction";


    private int mDirection;

    private TextView mTextViewCount;
    private int mCount;


    private String mInteraction;


    private NeedleView mNeedleView;
    private TextView mTextViewTracking;
    private HeadRecognition mHeadRecognition;


    private ContinuousSpeechRecognition mContinuousSpeechRecognition;
    private ControlWearApi mControlWearApi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        mCount = 3;
        mTextViewCount = (TextView) findViewById(R.id.textViewCount);
        mTextViewCount.setText(Integer.toString(mCount));

        /*
        DIRECTION INIT
         */

        Intent intent = getIntent();
        mDirection = intent.getIntExtra(TrainingActivity.EXTRA_DIRECTION, 0);

        String direction = "";
        switch (mDirection) {
            case CartonSdk.RIGHT:
                direction = "right";
                break;
            case CartonSdk.LEFT:
                direction = "left";
                break;
            case CartonSdk.UP:
                direction = "up";
                break;
            case CartonSdk.DOWN:
                direction = "down";
                break;
        }

        TextView textView = (TextView) findViewById(R.id.textViewTraining);

        /*
        INTERACTION INIT
         */

        mInteraction = intent.getStringExtra(TrainingActivity.EXTRA_INTERACTION);
        int resourceId = getResources().getIdentifier("training_" + mInteraction + "_" + direction, "string", getPackageName());
        textView.setText(getString(resourceId));

        TouchView touchView = (TouchView) findViewById(R.id.touchView);
        mHeadRecognition = new HeadRecognition(this);

        FrameLayout frameLayoutHelp = (FrameLayout) findViewById(R.id.relativeLayoutHelp);

        mContinuousSpeechRecognition = new ContinuousSpeechRecognition(this);
        mControlWearApi = new ControlWearApi(this);

        switch (mInteraction) {
            case "finger":
                touchView.setOnFingerTouchGestureListener(this);
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(R.drawable.finger_interaction);
                frameLayoutHelp.addView(imageView);
                break;
            case "head":
                LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rootIncludedView;

                if (mDirection < 2) { // which means left/right (horizontal/tilting)
                    rootIncludedView = layoutInflater.inflate(R.layout.include_head_horizontal, frameLayoutHelp, false);
                } else {
                    rootIncludedView = layoutInflater.inflate(R.layout.include_head_vertical, frameLayoutHelp, false);
                }
                frameLayoutHelp.addView(rootIncludedView);

                mTextViewTracking = (TextView) rootIncludedView.findViewById(R.id.textViewTracking);
                mNeedleView = (NeedleView) rootIncludedView.findViewById(R.id.needleView);

                mHeadRecognition.setOnHeadGestureListener(this);
                mHeadRecognition.setOnHeadTrackingListener(this);
                break;
            case "voice":
                mContinuousSpeechRecognition.setOnTextListener(this);
                break;
            case "watch":
                mControlWearApi.setOnControlSwipeListener(this);
                mControlWearApi.startWearApp(ControlWearApi.MODE_PAD);
                break;
            case "all":
                // finger
                touchView.setOnFingerTouchGestureListener(this);
                // head
                mHeadRecognition.setOnHeadGestureListener(this);
                // voice
                mContinuousSpeechRecognition.setOnTextListener(this);
                // watch
                mControlWearApi.setOnControlSwipeListener(this);
                mControlWearApi.startWearApp(ControlWearApi.MODE_PAD);
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mControlWearApi.connect();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mHeadRecognition.start();
        mContinuousSpeechRecognition.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mHeadRecognition.stop();
        mContinuousSpeechRecognition.stop();
        mControlWearApi.connect();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContinuousSpeechRecognition.destroy();
    }


    private void checkDirection(int direction) {
        if (mDirection == direction) {
            mCount -= 1;

            if (mCount == 0) { // if user did 3 times the action
                if (mDirection >= 3) {
                    if (Pref.getCurrentInteraction(getApplicationContext()) == 4) { // should be 4 with voice and smart watch
                        Pref.incrementCurrentActivity(getApplicationContext(), 1); // if user did with all kind of interaction we increment current activity
                        Pref.setCurrentInteraction(getApplicationContext(), 1); // and we reset to 0 interaction
                    } else {
                        Pref.incrementCurrentInteraction(getApplicationContext(), 1); // if user just did it in all direction we increment interaction
                    }
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra(MainActivity.EXTRA_NOCONFIG, true);
                    startActivity(intent);
                } else { // if not of the above, we just increment the direction
                    Intent intent = new Intent(this, TrainingActivity.class);
                    intent.putExtra(EXTRA_DIRECTION, mDirection + 1);
                    intent.putExtra(EXTRA_INTERACTION, mInteraction);
                    startActivity(intent);
                }
                finish();
            } else {
                mTextViewCount.setText(Integer.toString(mCount));
            }
        }
    }


    /*
    IMPLEMENTS
     */


    // TouchView.OnFingerTouchGestureListener
    @Override
    public void onSwipe(int direction) {
        checkDirection(direction);
    }


    // HeadRecognition.OnHeadGestureListener
    @Override
    public void onTilt(int direction) {
        checkDirection(direction);
    }


    // HeadRecognition.OnHeadGestureListener
    @Override
    public void onNod(int direction) {
        checkDirection(direction);
    }


    @Override
    public void onShake() {

    }


    // HeadRecognition.OnHeadTrackingListener
    @Override
    public void onDirectionChanged(int azimuth, int pitch, int roll) {
        if (mDirection < 2) {
            mNeedleView.setAngle(pitch * -1 + 90);
            mTextViewTracking.setText(String.format("%d", (int) Math.sqrt(pitch * pitch)));
        } else {
            mNeedleView.setAngle(roll + 90);
            mTextViewTracking.setText(String.format("%d", (int) Math.sqrt(roll * roll)));
        }
    }


    // ContinuousSpeechRecognition.OnTextListener
    @Override
    public void onTextMatched(ArrayList<String> matchedText) {
        Log.d("onTextMatched", matchedText.toString());
        for (String s : matchedText) {
            switch (s) {
                case "right":
                    checkDirection(CartonSdk.RIGHT);
                    return;
                case "left":
                    checkDirection(CartonSdk.LEFT);
                    return;
                case "down":
                    checkDirection(CartonSdk.DOWN);
                    return;
                case "up":
                    checkDirection(CartonSdk.DOWN);
                    return;
            }
        }
    }


    // ContinuousSpeechRecognition.OnTextListener
    @Override
    public void onError(int error) {

    }


    // ControlWearApi.OnControlSwipeListener
    @Override
    public void OnSwipeLeft() {
        checkDirection(CartonSdk.LEFT);
    }


    // ControlWearApi.OnControlSwipeListener
    @Override
    public void OnSwipeRight() {
        checkDirection(CartonSdk.RIGHT);
    }


    // ControlWearApi.OnControlSwipeListener
    @Override
    public void OnSwipeUp() {
        checkDirection(CartonSdk.UP);
    }


    // ControlWearApi.OnControlSwipeListener
    @Override
    public void OnSwipeDown() {
        checkDirection(CartonSdk.DOWN);
    }
}