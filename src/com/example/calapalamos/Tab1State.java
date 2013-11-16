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
import android.widget.Button;
import android.widget.TextView;

public class Tab1State extends Fragment {
    
	TextView eState;
	String State;
	TextView eFlag;
	TextView eHappy;
	TextView eDirty;
	TextView tFlag;
	TextView tHappy;
	TextView tDirty;
	Button bKids;
	OptionsActivity activity;
	
	@Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        
		View v = inflater.inflate(R.layout.activity_tab1_state, container, false);
       
		eState=(TextView)v.findViewById(R.id.ETState);
		eFlag=(TextView)v.findViewById(R.id.ETFlag);
		eDirty=(TextView)v.findViewById(R.id.ETDirty);
		eHappy=(TextView)v.findViewById(R.id.ETHappy);
		tFlag=(TextView)v.findViewById(R.id.TFlag);
		tDirty=(TextView)v.findViewById(R.id.TDirty);
		tHappy=(TextView)v.findViewById(R.id.THappy);
		bKids=(Button)v.findViewById(R.id.BKids);
		
		  		
	    //recogemos los datos pasados desde LaFoscaMain
	    try {
			
			activity = (OptionsActivity) getActivity();
			State = activity.getState();
			eState.setText(State);		

			
			if(State.equals("closed"))
			{
				tFlag.setEnabled(false);
				tDirty.setEnabled(false);
				tHappy.setEnabled(false);
				bKids.setEnabled(false);
				//eFlag.setActivated(false); //.isActivated()
				Log.d("CLoSED","CLOSED");
				
			}else{
				tFlag.setEnabled(true);
				tDirty.setEnabled(true);
				tHappy.setEnabled(true);
				bKids.setEnabled(true);				

                JSONArray ka= new JSONArray(activity.getKids());		
				List<JSONObject> ja = sortKidsObjects(ka);
				
				if(activity.getHappy().equals("1"))
				    eFlag.setText("Yellow");
				else if(activity.getHappy().equals("0"))
					eFlag.setText("Green");
				else
					eFlag.setText("Red");
				eDirty.setText(activity.getDirty());
				eHappy.setText(activity.getHappy());
				
				
				//Log.d("SortJson",ja.toString());
			}	

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
