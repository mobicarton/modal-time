package mobi.carton.modaltime.origami;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.controlwear.library.android.ControlWearApi;
import mobi.carton.csr.ContinuousSpeechRecognition;
import mobi.carton.library.CartonActivity;
import mobi.carton.library.CartonSdk;
import mobi.carton.library.CartonViewPager;
import mobi.carton.library.HeadRecognition;
import mobi.carton.modaltime.CustomPagerAdapter;
import mobi.carton.modaltime.LogSaved;
import mobi.carton.modaltime.Pref;
import mobi.carton.modaltime.R;

public class OrigamiActivity extends CartonActivity
        implements
        HeadRecognition.OnHeadGestureListener,
        ViewPager.OnPageChangeListener,
        CartonViewPager.OnScrollListener,
        ContinuousSpeechRecognition.OnTextListener,
        ControlWearApi.OnControlSwipeListener {



    private CustomViewPager mViewPager;
    private int mNbSteps;
    private TextView mTextViewStepPosition;
    private ImageView mImageViewStepPosition;


    private HeadRecognition mHeadRecognition;
    private ContinuousSpeechRecognition mContinuousSpeechRecognition;


    private int mPosition = 0;

    private TextView mTextViewInteraction;
    private TextView mTextViewHelper;


    private ControlWearApi mControlWearApi;


    private void actionDirection(int direction) {
        switch (direction) {
            case HeadRecognition.NOD_DOWN:
                //if (mViewPager.getCurrentItem() == mNbSteps)
                //    onBackPressed();
                break;
            case CartonSdk.LEFT: // inverted
                LogSaved.LogWithHeaderProcess(getApplicationContext(), "finger", CartonSdk.RIGHT);
                break;
            case CartonSdk.RIGHT: // inverted
                LogSaved.LogWithHeaderProcess(getApplicationContext(), "finger", CartonSdk.LEFT);
        }
    }


    /*
    LIFECYCLE
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_origami);

        mNbSteps = 23;

        List<Fragment> fragments = new ArrayList<>();

        int resourceId;
        for (int i = 1; i <= mNbSteps; i++) {
            resourceId = getResources().getIdentifier("tulip_step_".concat(Integer.toString(i)), "drawable", getPackageName());
            fragments.add(OrigamiFragment.newInstance(i, resourceId));
        }

        fragments.add(OrigamiFragment.newInstance(0, getResources().getIdentifier("tulip_finished", "drawable", getPackageName())));

        CustomPagerAdapter pagerAdapter = new CustomPagerAdapter(super.getSupportFragmentManager(), fragments);

        mTextViewStepPosition = (TextView) findViewById(R.id.textView_stepPosition);
        mTextViewStepPosition.setText("1");
        mImageViewStepPosition = (ImageView) findViewById(R.id.imageView_stepPosition);

        mViewPager = (CustomViewPager) findViewById(R.id.viewPager_OrigamiSteps);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        final float scale = getResources().getDisplayMetrics().density;
        mViewPager.setPageMargin((int) -(160 * scale + 0.5f));
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOnScrollListener(this);

        mTextViewHelper = (TextView) findViewById(R.id.textViewHelp);
        mTextViewHelper.setText(getString(R.string.origami_help_1));

        /*
        Interaction
         */

        mHeadRecognition = new HeadRecognition(this);


        mContinuousSpeechRecognition = new ContinuousSpeechRecognition(this);
        mContinuousSpeechRecognition.setOnTextListener(this);

        mTextViewInteraction = (TextView) findViewById(R.id.textViewInteraction);

        mControlWearApi = new ControlWearApi(this);

        setInteraction();
    }


    private void setInteraction() {
        if (mPosition == (Pref.getOrderFinger(getApplicationContext())-1) * 3) {
            mViewPager.setPagingEnabled(true);
            mHeadRecognition.setOnHeadGestureListener(null);
            mControlWearApi.setOnControlSwipeListener(null);
            mTextViewInteraction.setText(getString(R.string.origami_finger));
            Pref.setCurrentInteraction(getApplicationContext(), Pref.getOrderFinger(getApplicationContext()));
        }

        if (mPosition == (Pref.getOrderHead(getApplicationContext())-1) * 3) {
            mViewPager.setPagingEnabled(false);
            mHeadRecognition.setOnHeadGestureListener(this);
            mControlWearApi.setOnControlSwipeListener(null);
            mTextViewInteraction.setText(getString(R.string.origami_head));
            Pref.setCurrentInteraction(getApplicationContext(), Pref.getOrderHead(getApplicationContext()));
        }

        if (mPosition == (Pref.getOrderWatch(getApplicationContext())-1) * 3) {
            mViewPager.setPagingEnabled(false);
            mHeadRecognition.setOnHeadGestureListener(null);
            mControlWearApi.setOnControlSwipeListener(this);
            mControlWearApi.startWearApp(ControlWearApi.MODE_PAD);
            mTextViewInteraction.setText(getString(R.string.origami_watch));
            Pref.setCurrentInteraction(getApplicationContext(), Pref.getOrderWatch(getApplicationContext()));
        }

        /*
        if (mPosition == (Pref.getOrderVoice(getApplicationContext())-1) * 3) {
            mViewPager.setPagingEnabled(false);
            mHeadRecognition.setOnHeadGestureListener(null);
            mTextViewInteraction.setText(getString(R.string.maze_voice));
        }
        */

        if (mPosition > 8) {
            mViewPager.setPagingEnabled(true);
            mHeadRecognition.setOnHeadGestureListener(this);
            mControlWearApi.setOnControlSwipeListener(this);
            mControlWearApi.startWearApp(ControlWearApi.MODE_PAD);
            mTextViewInteraction.setText(getString(R.string.origami_all_interaction));
            Pref.setCurrentInteraction(getApplicationContext(), 4);
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
        mControlWearApi.disconnect();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContinuousSpeechRecognition.destroy();
    }


    /*
    IMPLEMENTS
     */


    // HeadRecognition.OnHeadGestureListener
    @Override
    public void onTilt(int direction) {
        switch (direction) {
            case HeadRecognition.TILT_RIGHT:
                LogSaved.LogWithHeaderProcess(getApplicationContext(), "head", CartonSdk.RIGHT);
                mViewPager.nextPage();
                break;
            case HeadRecognition.TILT_LEFT:
                LogSaved.LogWithHeaderProcess(getApplicationContext(), "head", CartonSdk.LEFT);
                mViewPager.previousPage();
                break;
        }
    }


    // HeadRecognition.OnHeadGestureListener
    @Override
    public void onNod(int direction) {
        actionDirection(direction);
    }


    // HeadRecognition.OnHeadGestureListener
    @Override
    public void onShake() {

    }


    // ViewPager.OnPageChangeListener
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    // ViewPager.OnPageChangeListener
    @Override
    public void onPageSelected(int position) {
        mPosition = position;
        if (position < mNbSteps) {
            mTextViewStepPosition.setText(String.format("%d", position+1));
            mImageViewStepPosition.setImageDrawable(null);

            int resourceId = getResources().getIdentifier("origami_help_" + Integer.toString(position+1), "string", getPackageName());
            mTextViewHelper.setText(getString(resourceId));

        } else {
            mTextViewStepPosition.setText("");
            mImageViewStepPosition.setImageResource(R.drawable.ic_action_accept);
        }
        setInteraction();
    }


    // ViewPager.OnPageChangeListener
    @Override
    public void onPageScrollStateChanged(int state) {
    }


    // CartonViewPager.OnScrollListener
    @Override
    public void onScroll(int direction) {
        actionDirection(direction);
    }


    // ContinuousSpeechRecognition.OnTextListener
    @Override
    public void onTextMatched(ArrayList<String> matchedText) {
        Log.d("onTextMatched", matchedText.toString());
        for (String s : matchedText) {
            switch (s) {
                case "right":
                    onTilt(HeadRecognition.TILT_RIGHT);
                    return;
                case "left":
                    onTilt(HeadRecognition.TILT_LEFT);
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
        LogSaved.LogWithHeaderProcess(getApplicationContext(), "watch", CartonSdk.LEFT);
        mViewPager.previousPage();
    }


    // ControlWearApi.OnControlSwipeListener
    @Override
    public void OnSwipeRight() {
        LogSaved.LogWithHeaderProcess(getApplicationContext(), "watch", CartonSdk.RIGHT);
        mViewPager.nextPage();
    }


    // ControlWearApi.OnControlSwipeListener
    @Override
    public void OnSwipeUp() {

    }


    // ControlWearApi.OnControlSwipeListener
    @Override
    public void OnSwipeDown() {

    }
}