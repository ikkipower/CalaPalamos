package com.example.calapalamos;
 
import org.json.JSONObject;

import com.example.calapalamos.library.HttpAsync;
import com.example.calapalamos.library.OpenWeather;
import com.example.calapalamos.library.HttpAsync.OnAsyncResult;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Tab3Weather extends Fragment{ //implements OnClickListener{
    

	private TextView city, wCond, temp, temp_max, temp_min; 
	private TextView press, hum, wind;
	private ImageView ico;
	private OptionsActivity activity;
	@Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
		
		activity = (OptionsActivity) getActivity();
    	View v = inflater.inflate(R.layout.activity_tab3_weather, container, false);
    	
    	city = (TextView)v.findViewById(R.id.cityText);
    	wCond = (TextView)v.findViewById(R.id.condDescr);
    	temp = (TextView)v.findViewById(R.id.temp);
    	temp_max = (TextView)v.findViewById(R.id.tempMax);
    	temp_min = (TextView)v.findViewById(R.id.tempMin);
    	press = (TextView)v.findViewById(R.id.press);
    	hum = (TextView)v.findViewById(R.id.hum);
    	wind = (TextView)v.findViewById(R.id.windSpeed);
    	ico = (ImageView)v.findViewById(R.id.condIcon);
    	
    	HttpAsync aStateTask = new HttpAsync(v.getContext(),Constants.WEATHER_OPT);  
    	aStateTask.setOnResultListener(asynResult);
        aStateTask.execute(new JSONObject());
    	
        return v;
        

    }
	
	OnAsyncResult asynResult = new OnAsyncResult() {  

		@Override
		public void onResult(final boolean resultCode, final OpenWeather weather)  {
			Log.d("TAB3!!!",weather.getName());

			activity = (OptionsActivity) getActivity();
			activity.setOp(weather);
			
			city.setText(activity.getOp().getName()+", Coord("+activity.getOp().getCoordLong()+","+activity.getOp().getCoordLat()+")");
			wCond.setText(activity.getOp().getWeatherMain()+": "+activity.getOp().getWeatherDescp());
			temp.setText("Tª: "+activity.getOp().getTemp()+"º");
			temp_max.setText("Tmax: "+activity.getOp().getTemp_max()+"º");
			temp_min.setText("Tmin: "+activity.getOp().getTemp_min()+"º");
			press.setText(activity.getOp().getPressure()+" hpa");
			hum.setText(activity.getOp().getHumidity()+"%");
			wind.setText(activity.getOp().getWindSpeed()+"m/s");
			if (activity.getOp().getIcon() != null && activity.getOp().getIcon().length > 0) {
		            Bitmap img = BitmapFactory.decodeByteArray(activity.getOp().getIcon(), 0, activity.getOp().getIcon().length); 
		            ico.setImageBitmap(img);
			}
		}

		@Override
		public void onStateResult(boolean resultCode, int i, JSONObject j) {
			// TODO Auto-generated method stub
	
		
        }
	};
}
