package mobi.carton.modaltime;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.Locale;

public class CentiChronometer extends android.support.v7.widget.AppCompatTextView
        implements
        Runnable {


    private static final int LOOP_CENTISECOND = 10; // 10 milliseconds

    private float count = 0f;

    private boolean running = false;

    private Thread mThread = new Thread(this);


    public CentiChronometer(Context context) {
        super(context);
        mThread = new Thread(this);
    }


    public CentiChronometer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mThread = new Thread(this);
    }


    public CentiChronometer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mThread = new Thread(this);
    }


    public void reset() {
        count = 0f;
        setText(String.format(Locale.ENGLISH, "%05.2f", count));
    }


    public void start() {
        running = true;
        mThread.start();
    }


    public void stop() {
        running = false;
        mThread.interrupt();
    }


    @Override // Runnable
    public void run() {
        while (!Thread.interrupted()) {
            post(new Runnable() {
                public void run() {
                    if (running) {
                        count += 0.01;
                        setText(String.format(Locale.ENGLISH, "%05.2f", count));
                    }
                }
            });

            try {
                Thread.sleep(LOOP_CENTISECOND);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
