package mobi.carton.modaltime;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Pref {


    /**
     * Name of the user (String)
     */
    private static final String PREF_USER_NAME = "pref_user_name";


    /**
     * Order of Voice Control interaction
     */
    private static final String PREF_ORDER_VOICE = "pref_order_voice";


    /**
     * Order of Head Gesture interaction
     */
    private static final String PREF_ORDER_HEAD = "pref_order_head";


    /**
     * Order of Finger Touch Gesture interaction
     */
    private static final String PREF_ORDER_FINGER = "pref_order_finger";


    /**
     * Order of SmartWatch interaction
     */
    private static final String PREF_ORDER_WATCH = "pref_order_watch";


    private static final String PREF_CURRENT_ACTIVITY = "pref_current_activity";
    private static final String PREF_CURRENT_INTERACTION = "pref_current_interaction";


    /**
     * Set the user's name (String)
     * @param context Context to be used to edit the {@link android.content.SharedPreferences}.
     * @param userName new value to save
     */
    public static void setUserName(final Context context, final String userName) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(PREF_USER_NAME, userName).apply();
    }


    /**
     * Return the user's name as a String
     * @param context Context to be used to lookup the {@link android.content.SharedPreferences}.
     * @return return the user's name
     */
    public static String getUserName(final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(PREF_USER_NAME, "default");
    }


    /**
     * Set the voice's order (int)
     * @param context Context to be used to edit the {@link android.content.SharedPreferences}.
     * @param order new value to save
     */
    public static void setOrderVoice(final Context context, final int order) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putInt(PREF_ORDER_VOICE, order).apply();
    }


    /**
     * Return the voice's order as an int
     * @param context Context to be used to lookup the {@link android.content.SharedPreferences}.
     * @return return the voice's order
     */
    public static int getOrderVoice(final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(PREF_ORDER_VOICE, 4);
    }


    /**
     * Set the head's order (int)
     * @param context Context to be used to edit the {@link android.content.SharedPreferences}.
     * @param order new value to save
     */
    public static void setOrderHead(final Context context, final int order) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putInt(PREF_ORDER_HEAD, order).apply();
    }


    /**
     * Return the head's order as an int
     * @param context Context to be used to lookup the {@link android.content.SharedPreferences}.
     * @return return the head's order
     */
    public static int getOrderHead(final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(PREF_ORDER_HEAD, 2);
    }


    /**
     * Set the finger's order (int)
     * @param context Context to be used to edit the {@link android.content.SharedPreferences}.
     * @param order new value to save
     */
    public static void setOrderFinger(final Context context, final int order) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putInt(PREF_ORDER_FINGER, order).apply();
    }


    /**
     * Return the finger's order as an int
     * @param context Context to be used to lookup the {@link android.content.SharedPreferences}.
     * @return return the finger's order
     */
    public static int getOrderFinger(final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(PREF_ORDER_FINGER, 1);
    }


    /**
     * Set the watch's order (int)
     * @param context Context to be used to edit the {@link android.content.SharedPreferences}.
     * @param order new value to save
     */
    public static void setOrderWatch(final Context context, final int order) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putInt(PREF_ORDER_WATCH, order).apply();
    }


    /**
     * Return the watch's order as an int
     * @param context Context to be used to lookup the {@link android.content.SharedPreferences}.
     * @return return the watch's order
     */
    public static int getOrderWatch(final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(PREF_ORDER_WATCH, 3);
    }


    public static void setCurrentInteraction(final Context context, final int interaction) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putInt(PREF_CURRENT_INTERACTION, interaction).apply();
    }


    public static void incrementCurrentInteraction(final Context context, final int increment) {
        int currentInteraction = getCurrentInteraction(context);
        currentInteraction += increment;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putInt(PREF_CURRENT_INTERACTION, currentInteraction).apply();
    }


    public static int getCurrentInteraction(final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(PREF_CURRENT_INTERACTION, 1);
    }


    public static void setCurrentActivity(final Context context, final int interaction) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putInt(PREF_CURRENT_ACTIVITY, interaction).apply();
    }


    public static void incrementCurrentActivity(final Context context, final int increment) {
        int currentActivity = getCurrentActivity(context);
        currentActivity += increment;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putInt(PREF_CURRENT_ACTIVITY, currentActivity).apply();
    }


    public static int getCurrentActivity(final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(PREF_CURRENT_ACTIVITY, 1);
    }
}
