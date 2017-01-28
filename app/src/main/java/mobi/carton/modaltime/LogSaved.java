package mobi.carton.modaltime;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import mobi.carton.library.CartonSdk;

public class LogSaved {


    private static final String LOG_TAG = LogSaved.class.getSimpleName();


    private static final String DIRECTORY_NAME = "modalTime";
    private static final String FILE_NAME = "log_data_";


    public static void LogWithHeader(Context context, String line) {
        StringBuilder strBToSave = new StringBuilder();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS");
        String currentDateandTime = sdf.format(new Date());

        strBToSave.append(currentDateandTime).append(",");

        String name = Pref.getUserName(context);
        strBToSave.append(name.concat(","));
        int activity = Pref.getCurrentActivity(context);
        switch (activity) {
            default:
            case 1:
                strBToSave.append("training,");
                break;
            case 2:
                strBToSave.append("maze,");
                break;
            case 3:
                strBToSave.append("origami,");
                break;
        }

        int order = Pref.getCurrentInteraction(context);

        if (order == Pref.getOrderFinger(context)) {
            strBToSave.append("finger,");
        }

        if (order == Pref.getOrderHead(context)) {
            strBToSave.append("head,");
        }

        if (order == Pref.getOrderWatch(context)) {
            strBToSave.append("watch,");
        }

        //if (order == Pref.getOrderVoice(getApplicationContext())) {
        //    intent.putExtra(TrainingActivity.EXTRA_INTERACTION, "voice");
        //}

        if (order == 4) { // 5 if voice control
            strBToSave.append("all,");
        }

        strBToSave.append(line);

        Log(name, strBToSave.toString());
    }


    public static void LogWithHeaderProcess(Context context, String interaction, int direction) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(interaction).append(",");
        switch (direction) {
            case CartonSdk.LEFT:
                stringBuilder.append("left");
                break;
            case CartonSdk.RIGHT:
                stringBuilder.append("right");
                break;
            case CartonSdk.UP:
                stringBuilder.append("up");
                break;
            case CartonSdk.DOWN:
                stringBuilder.append("down");
                break;
        }
        LogWithHeader(context, stringBuilder.toString());
    }


    public static void Log(String fileName, String line) {
        try {
            File fileDirectory = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), DIRECTORY_NAME);
            if (!fileDirectory.mkdirs()) {
                Log.e(LOG_TAG, "Directory not created");
            }
            File fileData = new File(fileDirectory, FILE_NAME+fileName+".csv");
            FileWriter writer = new FileWriter(fileData, true);
            writer.append(line.concat("\n"));
            writer.flush();
            writer.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
