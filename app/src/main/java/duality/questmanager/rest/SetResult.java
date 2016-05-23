package duality.questmanager.rest;

import android.util.Log;

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
 * Created by root on 23.05.16.
 */
public class SetResult {
    private final static String METHOD_URL = "https://api.questmanager.ru/task/result/";
    public OkHttpClient client;

    public SetResult() {
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build();

        client = new OkHttpClient.Builder()
                .connectionSpecs(Collections.singletonList(spec))
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public RESTAnswer start(final String task_id, final String user_result, final String token) throws IOException {

        Response result = post(METHOD_URL, makeJson(task_id, user_result), token);
        RESTAnswer answer = new RESTAnswer(result.code());
        answer.setMessage(result.body().string());

        Log.d("SetResult", answer.getMessage());

        return answer;
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

    String makeJson(final String task_id, final String result)
    {
        return '{' +
                "\"task_id\":\"" + task_id +"\"," +
                "\"result\": \"" + result + "\"" +
                "}";
    }
}
