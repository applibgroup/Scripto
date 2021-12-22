package com.example.myapplication.utils;

import ohos.app.Context;

import java.io.IOException;
import java.io.InputStream;

public class AssetsReader {

    public static String readFileAsText(Context context, String fileName) {
        String text;
        InputStream stream;
        try {
            stream = context.getClassloader().getResourceAsStream(fileName);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            text = new String(buffer);
            return text;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
