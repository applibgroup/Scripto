package com.example.mylibrary;

import java.util.logging.Logger;

import static com.example.mylibrary.ScriptoSettings.LogLevel.*;


public class ScriptoLogUtils {

    private static final String LOG_TAG = "ScriptoLog";

    public static void logMessage(String message) {
        ScriptoSettings.LogLevel level  = ScriptoSettings.getLogLevel();
        if (level == FULL || level == INFO) {
            Logger.getLogger(LOG_TAG,message);

        }
    }

    public static void logError(Throwable t) {
        logError(String.format("Message: %s. Cause: %s", "none", t.getMessage()));
    }

    public static void logError(Throwable t, String message) {
        logError(String.format("Message: %s. Cause: %s", message, t.getMessage()));
    }

    public static void logError(String message) {
        ScriptoSettings.LogLevel level  = ScriptoSettings.getLogLevel();
        if (level == FULL || level == ERROR) {
            Logger.getLogger(LOG_TAG,message);
        }
    }

}
