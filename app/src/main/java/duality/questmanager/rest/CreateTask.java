package duality.questmanager.rest;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.TlsVersion;

/**
 * Created by olegermakov on 08.05.16.
 */
public class CreateTask {
    private final static String METHOD_URL = "https://api.questmanager.ru/task/create/";
    public OkHttpClient client;

    public CreateTask() {
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .build();

        client = new OkHttpClient.Builder()
                .connectionSpecs(Collections.singletonList(spec))
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    public RESTAnswer start(final String title, final String text, final String price, final String reciever, final String year, final String month, final String day, final String token) throws IOException {
        try {
            Response temp = post(METHOD_URL, makeJson(title, text, price, reciever, year, month, day), token);
            RESTAnswer result = new RESTAnswer(temp.code());
            result.setMessage(temp.body().string());
            Log.d("MyBackend", result.getMessage());
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            RESTAnswer result = new RESTAnswer(400);
            result.setMessage("");
            return result;
        }
    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    Response post(String url, String json, String token) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Token " + token)
                .build();
        Response response = client.newCall(request).execute();
        Log.d("TaskCreate", response.toString());
        return response;
    }

    String makeJson(final String title, final String text, final String price, final String reciever, final String year, final String month, final String day) throws JSONException {
        String jsonStr = "{" +
                "\"title\": \"" + title + "\"," +
                "\"text\": \"" + text + "\"," +
                "\"price\": \"" + price + "\"," +
                "\"reciever\": \"" + reciever + "\"," +
                "\"year\": \"" + year + "\"," +
                "\"month\": \"" + month + "\"," +
                "\"day\": \"" + day + "\"" +
                "}";
        JSONObject json = new JSONObject(jsonStr);
        return json.toString();
    }
}
