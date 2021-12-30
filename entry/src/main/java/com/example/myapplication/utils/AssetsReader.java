package com.example.myapplication.utils;

import ohos.app.Context;
import ohos.global.configuration.Configuration;

import java.io.IOException;
import java.io.InputStream;

public class AssetsReader {

    public static String readFileAsText(Context context, String fileName) {
        String text;
        InputStream stream;
        try {
                stream = context.getResourceManager().getRawFileEntry("entry/resources/rawfile/"+fileName).openRawFile();
                int size = stream.available();
                byte[] buffer = new byte[size];
                stream.read(buffer);
                stream.close();
                text = new String(buffer);
                return text;

        } catch ( IOException e) {
            throw new RuntimeException(e);
        }
    }
}
