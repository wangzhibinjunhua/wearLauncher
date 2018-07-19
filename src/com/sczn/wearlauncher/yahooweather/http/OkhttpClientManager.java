package com.sczn.wearlauncher.yahooweather.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;


/**
 * http manager helper class
 * Created by archie on 15/11/7.
 */
public class OkhttpClientManager {
    private static OkhttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;

    private OkhttpClientManager() {
        mOkHttpClient = new OkHttpClient();
        CookieManager manager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
        mOkHttpClient.setCookieHandler(manager);
        mDelivery = new Handler(Looper.getMainLooper());
        Log.d("cookie",manager.getCookieStore().getCookies().toString());
        mGson = new Gson();
    }

    public abstract class MyResultCallback<T> extends ResultCallback<T>
    {

        @Override
        public void onBefore(Request request)
        {
            super.onBefore(request);
        }

        @Override
        public void onAfter()
        {
            super.onAfter();
        }
    }

    private ResultCallback<String> stringResultCallback = new MyResultCallback<String>()//
    {
        @Override
        public void onError(Request request, Exception e)
        {
            Log.e("TAG", "onError , e = " + e.getMessage());
        }

        @Override
        public void onResponse(String response)
        {
            Log.e("TAG", "onResponse , response = " + response);
        }

        @Override
        public void inProgress(float progress)
        {
        }
    };

    public static OkhttpClientManager getInstance()
    {
        if (mInstance == null)
        {
            synchronized (OkhttpClientManager.class)
            {
                if (mInstance == null)
                {
                    mInstance = new OkhttpClientManager();
                }
            }
        }
        return mInstance;
    }

    public void execute(Request request, ResultCallback callback)
    {
        if (callback == null) callback = ResultCallback.DEFAULT_RESULT_CALLBACK;
        final ResultCallback resCallBack = callback;
        resCallBack.onBefore(request);
        mOkHttpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(final Request request, final IOException e)
            {
                sendFailResultCallback(request, e, resCallBack);
            }

            @Override
            public void onResponse(final Response response)
            {
                try
                {
                    final String string = response.body().string();
                    if (resCallBack.mType == String.class)
                    {
                        sendSuccessResultCallback(string, resCallBack);
                    } else
                    {
                        Object o = mGson.fromJson(string, resCallBack.mType);
                        sendSuccessResultCallback(o, resCallBack);
                    }
                } catch (IOException e)
                {
                    sendFailResultCallback(response.request(), e, resCallBack);
                } catch (com.google.gson.JsonParseException e)//Json解析的错误
                {
                    sendFailResultCallback(response.request(), e, resCallBack);
                }

            }
        });
    }

    public <T> T execute(Request request, Class<T> clazz) throws IOException {
        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        String respStr = execute.body().string();
        return new Gson().fromJson(respStr, clazz);
    }

    public void cancelTag(Object tag)
    {
        mOkHttpClient.cancel(tag);
    }

    public void sendFailResultCallback(final Request request, final Exception e, final ResultCallback callback)
    {
        if (callback == null) return;

        mDelivery.post(new Runnable()
        {
            @Override
            public void run()
            {
                callback.onError(request, e);
                callback.onAfter();
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        if (callback == null) return;
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object);
                callback.onAfter();
            }
        });
    }

    public OkHttpClient getmOkHttpClient() {
        return mOkHttpClient;
    }

    public Handler getDelivery() {
        return mDelivery;
    }
}
