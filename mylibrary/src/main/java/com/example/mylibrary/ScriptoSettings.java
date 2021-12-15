package com.example.mylibrary;

public class ScriptoSettings {

    public enum LogLevel {
        NONE, INFO, ERROR, FULL
    }

    private static LogLevel sLogLevel = LogLevel.NONE;

    public static void setLogLevel(LogLevel loggingEnabled) {
        sLogLevel = loggingEnabled;
    }

    public static LogLevel getLogLevel() {
        return sLogLevel;
    }


}
