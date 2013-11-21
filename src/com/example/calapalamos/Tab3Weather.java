package com.example.calapalamos;

import org.json.JSONObject;

import com.example.calapalamos.library.HttpAsync;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Tab3Weather extends Fragment{ //implements OnClickListener{
    

	@Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
		

    	View v = inflater.inflate(R.layout.activity_tab3_weather, container, false);
    	
    	HttpAsync aStateTask = new HttpAsync(v.getContext(),Constants.WEATHER_OPT);  
    	//aStateTask.setOnResultListener(asynResult);
        aStateTask.execute(new JSONObject());
    	
        return v;
    }
}
