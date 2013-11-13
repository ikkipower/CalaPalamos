package com.example.calapalamos;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class Tab1State extends Fragment {
    
	TextView eState;
	TextView eFlag;
	TextView eHappy;
	TextView eDirty;
	
	
	@Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        
		View v = inflater.inflate(R.layout.activity_tab1_state, container, false);
        
		eState=(TextView)v.findViewById(R.id.ETState);
		eFlag=(TextView)v.findViewById(R.id.ETFlag);
		eDirty=(TextView)v.findViewById(R.id.ETDirty);
		eHappy=(TextView)v.findViewById(R.id.ETHappy);
		
    	Log.d("Tab1State",getArguments().getString("state"));
	    //recogemos los datos pasados desde LaFoscaMain
	    try {
			JSONObject obj = new JSONObject(getArguments().getString("state"));
			List<JSONObject> ja = sortKidsObjects(obj.getJSONArray("kids"));
			eState.setText(obj.getString("state"));
			eFlag.setText(obj.getString("flag"));
			eDirty.setText(obj.getString("dirtiness"));
			eHappy.setText(obj.getString("happiness"));
			
			Log.d("STATE",obj.getString("state"));
			Log.d("SortJson",ja.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("TabState",e.toString());
		}
    	
		
    	
    	return v;
        
    }
    
	/**
	 * 
	 * @param array
	 * @return
	 */
	public static List<JSONObject> sortKidsObjects(JSONArray array) {
	    List<JSONObject> jsons = new ArrayList<JSONObject>();
	    for (int i = 0; i < array.length(); i++) {
	        try {
				jsons.add(array.getJSONObject(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    Collections.sort(jsons, new Comparator<JSONObject>() {
	        @Override
	        public int compare(JSONObject lhs, JSONObject rhs) {
	            Integer lid = 0;
	            Integer rid = 0;
				try {
					lid = lhs.getInt("age");
					rid = rhs.getInt("age");
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
	            return rid.compareTo(lid);
	        }
	    });
	    return jsons;
	}

}
