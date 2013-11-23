package com.example.calapalamos.library;


import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

/**
  * Clase que permite guardar el estado le’do de OpenWeather
  * 
  * @author sergio
  *  
  */


public class OpenWeather {
	
	private double coordLong;
	private double coordLat;
	private String weatherMain; 
	private String weatherDescp;
	private Bitmap icon;
	private double temp,temp_max,temp_min;
	private double humidity;
	private double pressure;
	private double windSpeed;
	private String name;
	

	public OpenWeather(){
		this.coordLong=0.0;
		this.coordLat=0.0;
        this.weatherMain="";
        this.weatherDescp="";
		this.temp=0.0;
		this.temp_max=0.0;
		this.temp_min=0.0;
		this.humidity=0.0;
		this.pressure=0.0;
		this.windSpeed=0.0;
		this.name="";		
	}

	public OpenWeather(JSONObject j){
		try {
			this.coordLong = j.getJSONObject("coord").getDouble("lon");
			this.coordLat = j.getJSONObject("coord").getDouble("lat");
            this.weatherMain = j.getJSONArray("weather").getJSONObject(0).getString("main");
            this.weatherDescp = j.getJSONArray("weather").getJSONObject(0).getString("description");
			this.temp=j.getJSONObject("main").getDouble("temp");
			this.temp_max=j.getJSONObject("main").getDouble("temp_max");
			this.temp_min=j.getJSONObject("main").getDouble("temp_min");
			this.humidity=j.getJSONObject("main").getDouble("humidity");
			this.pressure=j.getJSONObject("main").getDouble("pressure");
			this.windSpeed=j.getJSONObject("wind").getDouble("speed");
			this.name=j.getString("name");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	
	public double getCoordLong() {
		return coordLong;
	}
	public void setCoordLong(double coordLong) {
		this.coordLong = coordLong;
	}
	public double getCoordLat() {
		return coordLat;
	}
	public void setCoordLat(double coordLat) {
		this.coordLat = coordLat;
	}
	public String getWeatherMain() {
		return weatherMain;
	}
	public void setWeatherMain(String weatherMain) {
		this.weatherMain = weatherMain;
	}
	public String getWeatherDescp() {
		return weatherDescp;
	}
	public void setWeatherDescp(String weatherDescp) {
		this.weatherDescp = weatherDescp;
	}
	public Bitmap getIcon() {
		return icon;
	}
	public void setIcon(Bitmap icon) {
		this.icon = icon;
	}
	public double getTemp() {
		return temp;
	}
	public void setTemp(double temp) {
		this.temp = temp;
	}
	public double getTemp_max() {
		return temp_max;
	}
	public void setTemp_max(double temp_max) {
		this.temp_max = temp_max;
	}
	public double getTemp_min() {
		return temp_min;
	}
	public void setTemp_min(double temp_min) {
		this.temp_min = temp_min;
	}
	public double getHumidity() {
		return humidity;
	}
	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}
	public double getPressure() {
		return pressure;
	}
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}
	public double getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}

