package com.example.limetv.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JSONReader {
    public static JSONObject getJson(String url) throws IOException, JSONException {
        try (InputStream inputStream = new URL(url).openStream()) {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            return new JSONObject(read(reader));
        }
    }

    private static String read(Reader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int character;

        while ((character = reader.read()) != -1) {
            stringBuilder.append((char) character);
        }

        return stringBuilder.toString();
    }
}
