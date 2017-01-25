package mobi.carton.modaltime.training;


import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import mobi.carton.library.CartonActivity;
import mobi.carton.library.CartonSdk;
import mobi.carton.library.HeadRecognition;
import mobi.carton.modaltime.R;
import mobi.carton.modaltime.TouchView;

public class TrainingActivity extends CartonActivity
        implements
        TouchView.OnFingerTouchGestureListener,
        HeadRecognition.OnHeadGestureListener {


    private int mDirection;

    private TextView mTextViewCount;
    private int mCount;


    private HeadRecognition mHeadRecognition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        ImageView imageView = (ImageView) findViewById(R.id.imageViewTraining);
        TextView textView = (TextView) findViewById(R.id.textViewTraining);

        imageView.setImageResource(R.drawable.finger_interaction);
        textView.setText(getString(R.string.training_finger_right));
        mDirection = CartonSdk.RIGHT;

        mCount = 3;
        mTextViewCount = (TextView) findViewById(R.id.textViewCount);
        mTextViewCount.setText(Integer.toString(mCount));

        TouchView touchView = (TouchView) findViewById(R.id.touchView);
        touchView.setOnFingerTouchGestureListener(this);

        mHeadRecognition = new HeadRecognition(this);
        mHeadRecognition.setOnHeadGestureListener(this);
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


    private void checkDirection(int direction) {
        if (mDirection == direction) {
            mCount -= 1;

            if (mCount == 0) {

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
}