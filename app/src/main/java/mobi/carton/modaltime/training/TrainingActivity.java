package mobi.carton.modaltime.training;


import android.content.Intent;
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


    public static final String EXTRA_INTERACTION = "extra_interaction";

    private static final String EXTRA_DIRECTION = "extra_direction";


    private int mDirection;

    private TextView mTextViewCount;
    private int mCount;


    private String mInteraction;


    private HeadRecognition mHeadRecognition;


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
        ImageView imageView = (ImageView) findViewById(R.id.imageViewTraining);
        imageView.setImageResource(R.drawable.finger_interaction);

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

        switch (mInteraction) {
            case "finger":
                touchView.setOnFingerTouchGestureListener(this);
                break;
            case "head":
                mHeadRecognition.setOnHeadGestureListener(this);
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
    }


    private void checkDirection(int direction) {
        if (mDirection == direction) {
            mCount -= 1;

            if (mCount == 0) {
                Intent intent = new Intent(this, TrainingActivity.class);
                intent.putExtra(EXTRA_DIRECTION, mDirection + 1);
                intent.putExtra(EXTRA_INTERACTION, mInteraction);
                startActivity(intent);
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
}