package com.sczn.wearlauncher.yahooweather.http;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * http generic request
 * public api builder
 * Created by archie on 15/11/7.
 */
public abstract class OkHttpRequest {
    protected OkhttpClientManager mOkHttpClientManager = OkhttpClientManager.getInstance();
    protected OkHttpClient mOkHttpClient;
    protected RequestBody requestBody;
    protected Request request;
    protected String url;
    protected String tag;
    protected Map<String, String> params;
    protected Map<String, String> headers;
    protected OkHttpRequest(String url, String tag,
                            Map<String, String> params, Map<String, String> headers)
    {
        mOkHttpClient = mOkHttpClientManager.getmOkHttpClient();
        this.url = url;
        this.tag = tag;
        this.params = params;
        this.headers = headers;
    }

    protected abstract Request buildRequest();

    protected abstract RequestBody buildRequestBody();

    protected void prepareInvoked(ResultCallback callback)
    {
        requestBody = buildRequestBody();
        requestBody = wrapRequestBody(requestBody, callback);
        request = buildRequest();
    }

    public void invokeAsyn(ResultCallback callback)
    {
        prepareInvoked(callback);
        mOkHttpClientManager.execute(request, callback);
    }

    protected RequestBody wrapRequestBody(RequestBody requestBody, final ResultCallback callback)
    {
        return requestBody;
    }

    public <T> T invoke(Class<T> clazz) throws IOException
    {
        Request request = buildRequest();
        return mOkHttpClientManager.execute(request, clazz);
    }

    protected void appendHeaders(Request.Builder builder, Map<String, String> headers)
    {
        if (builder == null)
        {
            throw new IllegalArgumentException("builder can not be empty!");
        }

        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) return;

        for (String key : headers.keySet())
        {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }

    /**
     * request builder to app url/cancel tag/headers/params/upload content(bytes file...)...
     */
    public static class Builder
    {
        private String url;
        private String tag;
        private Map<String, String> headers;
        private Map<String, String> params;
        private int errorResId = -1;
        //for post
        private String content;
        private byte[] bytes;
        private File file;

        public Builder url(String url)
        {
            this.url = url;
            return this;
        }

        //builder cancel tag
        public Builder tag(String tag)
        {
            this.tag = tag;
            return this;
        }

        public Builder params(Map<String, String> params)
        {
            this.params = params;
            return this;
        }

        public Builder addParams(String key, String val)
        {
            if (this.params == null)
            {
                params = new IdentityHashMap<String, String>();
            }
            params.put(key, val);
            return this;
        }

        public Builder headers(Map<String, String> headers)
        {
            this.headers = headers;
            return this;
        }

        public Builder addHeader(String key, String val)
        {
            if (this.headers == null)
            {
                headers = new IdentityHashMap<String, String>();
            }
            headers.put(key, val);
            return this;
        }

        public Builder content(String content)
        {
            this.content = content;
            return this;
        }

        public <T> T get(Class<T> clazz) throws IOException
        {
            OkHttpRequest request = new OkHttpGetRequest(url, tag, params, headers);
            return request.invoke(clazz);
        }

        public OkHttpRequest get(ResultCallback callback)
        {
            OkHttpRequest request = new OkHttpGetRequest(url, tag, params, headers);
            request.invokeAsyn(callback);
            return request;
        }

        public <T> T post(Class<T> clazz) throws IOException
        {
            OkHttpRequest request = new OkHttpPostRequest(url, tag, params, headers, content, bytes, file);
            return request.invoke(clazz);
        }

        public OkHttpRequest post(ResultCallback callback)
        {
            OkHttpRequest request = new OkHttpPostRequest(url, tag, params, headers, content, bytes, file);
            request.invokeAsyn(callback);
            return request;
        }

    }
}
