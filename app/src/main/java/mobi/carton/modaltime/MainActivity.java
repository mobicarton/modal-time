package mobi.carton.modaltime;

import android.content.Intent;
import android.os.Bundle;

import mobi.carton.library.CartonActivity;
import mobi.carton.library.CartonPrefs;
import mobi.carton.modaltime.maze.MazeActivity;
import mobi.carton.modaltime.origami.OrigamiActivity;
import mobi.carton.modaltime.training.TrainingActivity;

public class MainActivity extends CartonActivity {


    public static final String EXTRA_NOCONFIG = "extra_noconfig";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CartonPrefs.setWithoutCarton(getApplicationContext(), true);

        Intent intentExtra = getIntent();
        boolean noConfig = intentExtra.getBooleanExtra(MainActivity.EXTRA_NOCONFIG, false);

        if (!noConfig) {
            Intent intentConfig = new Intent(this, ConfigActivity.class);
            startActivity(intentConfig);
            finish();
        } else {
            // define which activity to launch ..
            int activity = Pref.getCurrentActivity(getApplicationContext());
            Intent intent;
            switch (activity) {
                default:
                case 1:
                    intent = new Intent(this, TrainingActivity.class);
                    break;
                case 2:
                    intent = new Intent(this, MazeActivity.class);
                    break;
                case 3:
                    intent = new Intent(this, OrigamiActivity.class);
                    break;
            }

            // .. with which interaction
            int order = Pref.getCurrentInteraction(getApplicationContext());

            if (Pref.getCurrentInteraction(getApplicationContext()) == Pref.getOrderFinger(getApplicationContext())) {
                intent.putExtra(TrainingActivity.EXTRA_INTERACTION, "finger");
            }

            if (order == Pref.getOrderHead(getApplicationContext())) {
                intent.putExtra(TrainingActivity.EXTRA_INTERACTION, "head");
            }

            if (order == Pref.getOrderWatch(getApplicationContext())) {
                intent.putExtra(TrainingActivity.EXTRA_INTERACTION, "watch");
            }

            if (order == Pref.getOrderVoice(getApplicationContext())) {
                intent.putExtra(TrainingActivity.EXTRA_INTERACTION, "voice");
            }

            // launch the activity
            startActivity(intent);
            finish();
        }
    }
}