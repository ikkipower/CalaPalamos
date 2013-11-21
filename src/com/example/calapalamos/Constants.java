package com.example.calapalamos;

import android.app.Application;

/**
 * @author i2131344
 *
 */

public class Constants extends Application{
	
	//options variables
	public final static String REG_OPT = "1";
	public final static String LOG_IN_OPT = "2";
	public final static String GSTATE_OPT = "3";
	public final static String STATE_OPT = "4";
	public final static String CHANGE_STATE_OPT = "5";
	public final static String CHANGE_STATE_FLAG = "6";
	public final static String THROW_OPT = "7";
	public final static String CLEAN_OPT = "8";
	public final static String WEATHER_OPT = "9";
	
	//url sufix
	public final static String SUFIX_REGISTER = "/users";
	public final static String SUFIX_LOGIN = "/user";
	public final static String SUFIX_GET_STATE = "/state";
	public final static String SUFIX_PUT_OPEN = "/open";
	public final static String SUFIX_PUT_CLOSE = "/close";
	public final static String SUFIX_PUT_FLAG = "/flag";
	public final static String SUFIX_POST_BALLS = "/nivea-rain";
	public final static String SUFIX_POST_CLEAN = "/clean";
	
	
	public final static String url = "http://lafosca-beach.herokuapp.com/api/v1";
	public final static String weather_url = "http://api.openweathermap.org/data/2.5/weather?lat=41.8571344&lon=3.1433845&lang=es&units=metric";
	public final static String weather_img_url = "http://openweathermap.org/img/w/";
	

	//url http messages
	public final static String CREATED_201= "HTTP/1.1 201 Created";
	public final static String OK_200 = "HTTP/1.1 200 OK";
	public final static String FLAG_OK = "HTTP/1.1 204 No Content";
	public final static String LOG_IN_FAILED = "HTTP/1.1 401 Unauthorized";
	
}