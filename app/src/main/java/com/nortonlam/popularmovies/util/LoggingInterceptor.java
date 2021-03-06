package com.nortonlam.popularmovies.util;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created date: 10/7/15.
 *
 * Class to allow logging of Retrofit 2 calls so we can see actual URL being called.
 * Copied from http://stackoverflow.com/questions/32514410/logging-with-retrofit-2
 */
public class LoggingInterceptor implements Interceptor {
    private String _tag;

    public LoggingInterceptor(String tag) {
        _tag = tag;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Log.d(_tag, String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        Log.d(_tag, String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        return response;
    }
}
