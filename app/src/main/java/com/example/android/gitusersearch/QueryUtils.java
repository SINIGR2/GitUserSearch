package com.example.android.gitusersearch;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    public QueryUtils(){

    }

    public static ArrayList<User> extractFeatureFromJson(String userJSON) {

        if (TextUtils.isEmpty(userJSON)) {
            return null;
        }

        ArrayList<User> users = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(userJSON);
            JSONArray usersArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject currentUser = usersArray.getJSONObject(i);

                String login = currentUser.getString("login");
                String avatar_url = currentUser.getString("avatar_url");

                User user = new User(login, avatar_url);
                users.add(user);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the users JSON results", e);
        }

        return users;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
            }
        } catch (IOException e) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<User> fetchUserData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) { }

        List<User> users = extractFeatureFromJson(jsonResponse);

        return users;
    }
}
