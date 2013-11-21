package com.example.calapalamos;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.calapalamos.library.HttpAsync;
import com.example.calapalamos.library.OpenWeather;
import com.example.calapalamos.library.HttpAsync.OnAsyncResult;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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
        
      /*  if (weather.iconData != null && weather.iconData.length > 0) {
            Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length); 
            imgView.setImageBitmap(img);
         }*/
    }
	
	OnAsyncResult asynResult = new OnAsyncResult() {  

		@Override
		public void onResult(final boolean resultCode, final OpenWeather weather)  {
			Log.d("TAB3!!!",weather.getName());
		}

		@Override
		public void onStateResult(boolean resultCode, int i, JSONObject j) {
			// TODO Auto-generated method stub
	
		
        }
	};
}
