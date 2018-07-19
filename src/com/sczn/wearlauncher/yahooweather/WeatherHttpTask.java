package com.sczn.wearlauncher.yahooweather;

import android.util.Log;

import com.google.gson.Gson;
import com.sczn.wearlauncher.util.MxyLog;
import com.sczn.wearlauncher.yahooweather.http.OkHttpRequest;
import com.sczn.wearlauncher.yahooweather.http.ResultCallback;
import com.sczn.wearlauncher.yahooweather.model.Atmosphere;
import com.sczn.wearlauncher.yahooweather.model.LocationInfo;
import com.sczn.wearlauncher.yahooweather.model.Weather;
import com.squareup.okhttp.Request;

import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * weather requests
 * Created by mxy on 2016/3/12.
 */
public class WeatherHttpTask {
    private static WeatherHttpTask weatherInstance;

    public static WeatherHttpTask getInstance() {
        if (weatherInstance == null) {
            weatherInstance = new WeatherHttpTask();
        }

        return weatherInstance;
    }

    public void getWeatherInfo(String city, final WeatherCallBack weatherCallBack) {
        try {
            String BaseUrl = "http://query.yahooapis.com/v1/public/yql";
            String yql_query = "select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"" + city + ", ak\")";
            String yql_query_url = BaseUrl + "?q=" + URLEncoder.encode(yql_query, "UTF-8") + "&format=json";
            new OkHttpRequest.Builder().url(yql_query_url).get(new ResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {
                	weatherCallBack.onError(e.getMessage());
                }

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject result = new JSONObject(response);
                        JSONObject query = result.getJSONObject("query");
                        JSONObject results = query.getJSONObject("results");
                        JSONObject channel = results.getJSONObject("channel");
                        JSONObject location = channel.getJSONObject("location");
                        JSONObject item = channel.getJSONObject("item");
                        JSONObject atmosphere = channel.getJSONObject("atmosphere");
                        Gson gson = new Gson();
                        Weather weather = gson.fromJson(item.toString(), Weather.class);
                        LocationInfo cityInfo = gson.fromJson(location.toString(), LocationInfo.class);
                        Atmosphere atmosphereInfo = gson.fromJson(atmosphere.toString(), Atmosphere.class);
                        weatherCallBack.onSuccess(weather, cityInfo, atmosphereInfo);
                    }catch (Exception e) {
                    	MxyLog.d("weather", e.getMessage());
                    }
                }
            });
        }catch (Exception e) {
        	MxyLog.d("weather", "url error");
        }

    }

    public interface WeatherCallBack {
        void onSuccess(Weather weather, LocationInfo cityInfo,Atmosphere atmosphere);
        void onError(String msg);
    }
}
