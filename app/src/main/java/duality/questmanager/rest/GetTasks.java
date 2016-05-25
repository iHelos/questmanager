package duality.questmanager.rest;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
 * Created by root on 22.05.16.
 */
public class GetTasks {
    private final static String METHOD_URL_INPUT = "https://api.questmanager.ru/task/in/";
    private final static String METHOD_URL_OUTPUT = "https://api.questmanager.ru/task/out/";
    public OkHttpClient client;

    public GetTasks() {
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .build();

        client = new OkHttpClient.Builder()
                .connectionSpecs(Collections.singletonList(spec))
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    public RESTAnswer start(final String hash, final boolean output, final String token) throws IOException {
        try {
            String METHOD_URL = METHOD_URL_INPUT;
            if (output)
                METHOD_URL = METHOD_URL_OUTPUT;

            if (!hash.equals(""))
                METHOD_URL += (hash + "/");

            Response temp = get(METHOD_URL, token);
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

    Response get(String url, String token) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Token " + token)
                .build();
        Response response = client.newCall(request).execute();
        Log.d("TasksGet", response.toString());
        return response;
    }
}
