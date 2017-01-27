package mobi.carton.modaltime;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import mobi.carton.library.CartonActivity;
import mobi.carton.library.HeadRecognition;


public class CalibrateActivity extends CartonActivity
        implements
        HeadRecognition.OnHeadTrackingListener,
        Animator.AnimatorListener,
        TouchView.OnFingerTapListener {


    private HeadRecognition mHeadRecognition;

    private TextView mTextViewTracking;
    private NeedleView mNeedleView;

    private int mValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calibrate);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbarCalibrate);
        mNeedleView = (NeedleView) findViewById(R.id.needleView);
        mTextViewTracking = (TextView) findViewById(R.id.textViewTracking);

        mHeadRecognition = new HeadRecognition(this);
        mHeadRecognition.setOnHeadTrackingListener(this);

        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(progressBar, "progress", 100, 0);
        objectAnimator.setDuration(5000);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.addListener(this);
        objectAnimator.start();

        TouchView mTouchView = (TouchView) findViewById(R.id.touchView);
        mTouchView.setOnFingerTapListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        mHeadRecognition.start();
    }


    @Override
    public void onPause() {
        super.onPause();
        mHeadRecognition.stop();
    }

    /*
    IMPLEMENTS
     */


    // Animator.AnimatorListener
    @Override
    public void onAnimationStart(Animator animator) {

    }


    // Animator.AnimatorListener
    @Override
    public void onAnimationEnd(Animator animator) {

    }


    // Animator.AnimatorListener
    @Override
    public void onAnimationCancel(Animator animator) {

    }


    // Animator.AnimatorListener
    @Override
    public void onAnimationRepeat(Animator animator) {
        mHeadRecognition.autoCalibrateDeltaNod();
    }


    // HeadRecognition.OnHeadTrackingListener
    @Override
    public void onDirectionChanged(int azimuth, int pitch, int roll) {
        mNeedleView.setAngle(roll + 90);
        mValue = (int) Math.sqrt(roll * roll);
        mTextViewTracking.setText(String.format("%d", mValue));
    }

    // TouchView.OnFingerTapListener
    @Override
    public void onTap() {
        if (mValue < 5 && mValue > -5) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.EXTRA_NOCONFIG, true);
            startActivity(intent);
            finish();
        }
    }
}
