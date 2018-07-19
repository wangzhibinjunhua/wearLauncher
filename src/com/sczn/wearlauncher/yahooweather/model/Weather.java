package com.sczn.wearlauncher.yahooweather.model;

import java.io.Serializable;
import java.util.List;

/**
 * weather model
 * Created by mxy on 2017/3/12.
 */
public class Weather{
    private TempCondition condition;
    private String description;
    private String title;
    private List<Forecast> forecast;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Forecast> getForecast() {
        return forecast;
    }

    public void setForecast(List<Forecast> forecast) {
        this.forecast = forecast;
    }

    public TempCondition getCondition() {
        return condition;
    }

    public void setCondition(TempCondition condition) {
        this.condition = condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class TempCondition{
        private String code;
        private String date;
        private String temp;
        private String text;

        public String getCode(){return code;}
        public void setCode(String code) {this.code = code;}

        public String getDate() {return date;}
        public void setDate(String date) {this.date = date;}

        public String getTemp() {return temp;}
        public void setTemp(String temp) {this.temp = temp;}

        public String getText() {return text;}
        public void setText(String text) {this.text = text;}

    }

    public static class Forecast{
        private String code;
        private String date;
        private String day;
        private String high;
        private String low;
        private String text;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public String getLow() {
            return low;
        }

        public void setLow(String low) {
            this.low = low;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

}
