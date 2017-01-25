package mobi.carton.modaltime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ConfigActivity extends AppCompatActivity
        implements
        View.OnClickListener {


    public static final String EXTRA_CONFIG_DONE = "extra_config_done";


    private EditText editTextName;
    private EditText editTextVoice;
    private EditText editTextHead;
    private EditText editTextFinger;
    private EditText editTextWatch;


    /**
     * Check the value of the EditText in param, must be between 1 and 4
     * @param editText EditText to check value
     * @return return True if value between 1 and 4
     */
    private boolean checkValue(EditText editText) {
        if (editText.length() == 0)
            return false;
        int value = Integer.parseInt(editText.getText().toString());
        return value == 1 || value == 2 || value == 3 || value == 4;
    }


    private boolean checkValues() {
        if (!checkValue(editTextVoice))
            return false;
        if (!checkValue(editTextHead))
            return false;
        if (!checkValue(editTextFinger))
            return false;
        if (!checkValue(editTextWatch))
            return false;

        return true;
    }


    /**
     * Make sure any values from the EditText are unique
     * @return return True if unique
     */
    private boolean checkUnique() {
        if (editTextVoice.getText().toString().equals(editTextHead.getText().toString()))
            return false;
        if (editTextVoice.getText().toString().equals(editTextFinger.getText().toString()))
            return false;
        if (editTextVoice.getText().toString().equals(editTextWatch.getText().toString()))
            return false;

        if (editTextHead.getText().toString().equals(editTextFinger.getText().toString()))
            return false;
        if (editTextHead.getText().toString().equals(editTextWatch.getText().toString()))
            return false;

        if (editTextFinger.getText().toString().equals(editTextWatch.getText().toString()))
            return false;

        return true;
    }


    /*
    LIFECYCLE
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        Button buttonValidate = (Button) findViewById(R.id.buttonValidate);
        buttonValidate.setOnClickListener(this);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextVoice = (EditText) findViewById(R.id.editTextVoice);
        editTextHead = (EditText) findViewById(R.id.editTextHead);
        editTextFinger = (EditText) findViewById(R.id.editTextFinger);
        editTextWatch = (EditText) findViewById(R.id.editTextWatch);
    }


    /*
    IMPLEMENTS
     */


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonValidate:
                if (!checkValues()) {
                    Toast.makeText(this, getString(R.string.textErrorValues), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!checkUnique()) {
                    Toast.makeText(this, getString(R.string.textErrorUnique), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (editTextName.length()<3) {
                    Toast.makeText(this, getString(R.string.textErrorName), Toast.LENGTH_SHORT).show();
                    return;
                }

                Pref.setUserName(getApplicationContext(), editTextName.getText().toString());
                Pref.setOrderVoice(getApplicationContext(), Integer.parseInt(editTextVoice.getText().toString()));
                Pref.setOrderHead(getApplicationContext(), Integer.parseInt(editTextHead.getText().toString()));
                Pref.setOrderFinger(getApplicationContext(), Integer.parseInt(editTextFinger.getText().toString()));
                Pref.setOrderWatch(getApplicationContext(), Integer.parseInt(editTextWatch.getText().toString()));

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(EXTRA_CONFIG_DONE, true);
                startActivity(intent);
                finish();
                break;
        }
    }
}
