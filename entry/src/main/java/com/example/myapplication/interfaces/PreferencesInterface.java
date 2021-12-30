package com.example.myapplication.interfaces;

import com.example.myapplication.User;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import ohos.data.DatabaseHelper;
import ohos.data.preferences.Preferences;
public class PreferencesInterface {

    private Context context;
    private Preferences prefs;

    public PreferencesInterface(Context context)  {
        this.context = context;
        DatabaseHelper databaseHelper = new DatabaseHelper(context );
        String fileName = "MyPrefs";
        this.prefs = databaseHelper.getPreferences(fileName);

    }

    public void saveUserData(User user) {

        prefs.putString("user_name", user.getName());
        prefs.putString("user_surname", user.getSurname());
        prefs.putInt("user_age", user.getAge());
        prefs.putFloat("user_height", user.getHeight());
        prefs.putBoolean("user_married", user.isMarried());
        prefs.flushSync();
        new ToastDialog(context)
                .setText(user.getUserInfo())
                .show();
    }

    public User getUserData() {
        String userName = prefs.getString("user_name", "");
        String userSurname = prefs.getString("user_surname", "");
        int userAge = prefs.getInt("user_age", 0);
        float userHeight = prefs.getFloat("user_height", 0.0f);
        boolean userMarried = prefs.getBoolean("user_married", false);

        return new User (userName, userSurname, userAge, userHeight, userMarried);
    }
}
