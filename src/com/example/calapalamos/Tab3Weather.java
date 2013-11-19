package com.example.calapalamos;

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

    	
        return v;
    }
}
