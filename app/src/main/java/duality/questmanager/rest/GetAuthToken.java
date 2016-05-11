package duality.questmanager.rest;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.TlsVersion;

/**
 * Created by olegermakov on 09.05.16.
 */
public class GetAuthToken {
    private final static String METHOD_URL = "https://api.questmanager.ru/getToken/";
    public OkHttpClient client;

    public GetAuthToken() {
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build();

        client = new OkHttpClient.Builder()
                .connectionSpecs(Collections.singletonList(spec))
                .build();
    }

    public RESTAnswer start(final String reg_id) throws IOException {

        Response result = post(METHOD_URL, makeJson(reg_id));
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

    String makeJson(final String reg_id) {
        return '{' +
                "\"reg_id\":\"" + reg_id + "\"" +
                "}";
    }

    private static String inputStreamToString(final InputStream is) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}
