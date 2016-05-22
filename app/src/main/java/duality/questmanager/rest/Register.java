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
 * Created by olegermakov on 18.04.16.
 */
public class Register{
    private final static String METHOD_URL = "https://api.questmanager.ru/registerDevice/";
    public OkHttpClient client;

    public Register() {
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

    public RESTAnswer processText(final String reg_id, final String dev_id, final String email) throws IOException {

        Response result = post(METHOD_URL, makeJson(reg_id, dev_id, email));
        Log.d("MyBackend", reg_id);
        Log.d("MyBackend", dev_id);
        RESTAnswer answer = new RESTAnswer(result.code());
        answer.setMessage(result.body().string());
        Log.d("MyBackend", answer.getMessage());
        return answer;
    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    Response post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    String makeJson(final String reg_id, final String dev_id, final String email)
    {
        return '{' +
                "\"email\": \"" + email + "\"," +
                "\"reg_id\":\"" + reg_id +"\","+
                "\"dev_id\":\"" + dev_id +"\""+
                "}";
    }
}