package com.example.calapalamos;

import android.app.Application;
import android.content.Context;

/**
 * @author i2131344
 *
 */

public class Constants extends Application{
	
	
	public final static String REG_OPT = "POST";
	public final static String LOG_IN_OPT = "LOGIN";
	public final static String GSTATE_OPT = "GSTATE";
	public final static String SUFIX_REGISTER = "/users";
	public final static String SUFIX_LOGIN = "/user";
	public final static String SUFIX_GET_STATE = "/state";
	public final static String SUFIX_PUT_OPEN = "/open";
	public final static String SUFIX_PUT_CLOSE = "/close";
	public final static String url = "http://lafosca-beach.herokuapp.com/api/v1";
	public final static String ERROR_LOGIN = "HTTP Basic: Access denied.";
	public final static String STATE_OPT = "POST";
	public final static String CHANGE_STATE_OPT = "PUT";
	public final static String OPEN_CLOSE_OK = "HTTP/1.1 200 OK";
	
    /**
     * Keeps a reference of the application context
     */
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();

    }

    /**
     * Returns the application context
     *
     * @return application context
     */
    public static Context getContext() {
        return sContext;
    }
	
}