package mobi.carton.modaltime.origami;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mobi.carton.csr.ContinuousSpeechRecognition;
import mobi.carton.library.CartonActivity;
import mobi.carton.library.CartonViewPager;
import mobi.carton.library.HeadRecognition;
import mobi.carton.modaltime.CustomPagerAdapter;
import mobi.carton.modaltime.R;

public class OrigamiActivity extends CartonActivity
        implements
        HeadRecognition.OnHeadGestureListener,
        ViewPager.OnPageChangeListener,
        CartonViewPager.OnScrollListener,
        ContinuousSpeechRecognition.OnTextListener {



    private CartonViewPager mViewPager;
    private int mNbSteps;
    private TextView mTextViewStepPosition;
    private ImageView mImageViewStepPosition;


    private HeadRecognition mHeadRecognition;
    private ContinuousSpeechRecognition mContinuousSpeechRecognition;


    private void actionDirection(int direction) {
        switch (direction) {
            case HeadRecognition.NOD_DOWN:
                if (mViewPager.getCurrentItem() == mNbSteps)
                    onBackPressed();
                break;
        }
    }


    /*
    LIFECYCLE
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_origami);

        String name = "Tulip";
        mNbSteps = 23;

        TextView textViewName = (TextView) findViewById(R.id.textView_origamiName);
        textViewName.setText(name);

        List<Fragment> fragments = new ArrayList<>();

        int resourceId;
        for (int i = 1; i <= mNbSteps; i++) {
            resourceId = getResources().getIdentifier(name.toLowerCase().concat("_step_").concat(Integer.toString(i)), "drawable", getPackageName());
            fragments.add(OrigamiFragment.newInstance(i, resourceId));
        }

        fragments.add(OrigamiFragment.newInstance(0, getResources().getIdentifier(name.toLowerCase().concat("_finished"), "drawable", getPackageName())));

        CustomPagerAdapter pagerAdapter = new CustomPagerAdapter(super.getSupportFragmentManager(), fragments);

        mTextViewStepPosition = (TextView) findViewById(R.id.textView_stepPosition);
        mTextViewStepPosition.setText("1");
        mImageViewStepPosition = (ImageView) findViewById(R.id.imageView_stepPosition);

        mViewPager = (CartonViewPager) findViewById(R.id.viewPager_OrigamiSteps);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        final float scale = getResources().getDisplayMetrics().density;
        mViewPager.setPageMargin((int) -(160 * scale + 0.5f));
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOnScrollListener(this);

        mHeadRecognition = new HeadRecognition(this);
        mHeadRecognition.setOnHeadGestureListener(this);

        mContinuousSpeechRecognition = new ContinuousSpeechRecognition(this);
        mContinuousSpeechRecognition.setOnTextListener(this);
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
                mViewPager.nextPage();
                break;
            case HeadRecognition.TILT_LEFT:
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
        if (position < mNbSteps) {
            mTextViewStepPosition.setText(String.format("%d", position+1));
            mImageViewStepPosition.setImageDrawable(null);
        } else {
            mTextViewStepPosition.setText("");
            mImageViewStepPosition.setImageResource(R.drawable.ic_action_accept);
        }
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
                case "next":
                    onTilt(HeadRecognition.TILT_RIGHT);
                    return;
                case "previous":
                    onTilt(HeadRecognition.TILT_LEFT);
                    return;
                case "cancel":
                    actionDirection(HeadRecognition.NOD_UP);
                    return;
                case "ok":
                    actionDirection(HeadRecognition.NOD_DOWN);
                    return;
            }
        }
    }


    // ContinuousSpeechRecognition.OnTextListener
    @Override
    public void onError(int error) {

    }
}