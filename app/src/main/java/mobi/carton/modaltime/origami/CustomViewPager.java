package mobi.carton.modaltime.origami;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import mobi.carton.library.CartonViewPager;


public class CustomViewPager extends CartonViewPager {


    private boolean isPagingEnabled = true;


    public CustomViewPager(Context context) {
        super(context);
    }


    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }


    /*
    SETTERS
     */


    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
}
