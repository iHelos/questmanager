package duality.questmanager.rest;

import android.util.Log;

import java.io.BufferedReader;
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
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build();

        client = new OkHttpClient.Builder()
                .connectionSpecs(Collections.singletonList(spec))
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public RESTAnswer start(final String title, final String text, final String price, final String reciever, final String year, final String month, final String day, final String token) throws IOException {
        Response temp = post(METHOD_URL, makeJson(title, text, price, reciever, year, month, day), token);
        RESTAnswer result = new RESTAnswer(temp.code());
        result.setMessage(temp.body().string());
        Log.d("MyBackend", result.getMessage());
        return result;
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
        return response;
    }

    String makeJson(final String title, final String text, final String price, final String reciever, final String year, final String month, final String day)
    {
        return '{' +
                "\"title\": \"" + title + "\"," +
                "\"text\": \"" + text + "\"," +
                "\"price\": \"" + price + "\"," +
                "\"reciever\": \"" + reciever + "\"," +
                "\"year\": \"" + year + "\"," +
                "\"month\": \"" + month + "\"," +
                "\"day\": \"" + day + "\"" +
                "}";
    }



    private static String inputStreamToString(final InputStream is) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while((line = bufferedReader.readLine()) != null){
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}
