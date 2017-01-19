package mobi.carton.modaltime;

import android.content.Intent;
import android.os.Bundle;

import mobi.carton.library.CartonActivity;

public class MainActivity extends CartonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean configDone = getIntent().getBooleanExtra(ConfigActivity.EXTRA_CONFIG_DONE, false);

        if (!configDone) {
            Intent intent = new Intent(this, ConfigActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
