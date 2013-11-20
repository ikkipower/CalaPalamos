package com.example.calapalamos;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.calapalamos.library.HttpAsync;
import com.example.calapalamos.library.HttpAsync.OnAsyncResult;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
	Button btnKids;
	OptionsActivity activity;
    
	
	@Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        
		View v = inflater.inflate(R.layout.activity_tab1_state, container, false);
       
		
		
		activity = (OptionsActivity) getActivity();
		eState=(TextView)v.findViewById(R.id.ETState);
		eFlag=(TextView)v.findViewById(R.id.ETFlag);
		eDirty=(TextView)v.findViewById(R.id.ETDirty);
		eHappy=(TextView)v.findViewById(R.id.ETHappy);
		tFlag=(TextView)v.findViewById(R.id.TFlag);
		tDirty=(TextView)v.findViewById(R.id.TDirty);
		tHappy=(TextView)v.findViewById(R.id.THappy);
		btnKids=(Button)v.findViewById(R.id.BKids);
		
		Log.d("TAB1","tab1");
		JSONObject jSend = new JSONObject();
		JSONObject jUser = new JSONObject();
		try {

        	jUser.put("authenticationToken",activity.getAuthToken());
	        jSend.put("user", jUser);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
        if(activity.getInitCond()==false)
        {
        	HttpAsync aStateTask = new HttpAsync(v.getContext(),Constants.GSTATE_OPT);  
        	aStateTask.setOnResultListener(asynResult);
            aStateTask.execute(jSend);
        }else{
        	activity.setInitCond(false);
        	
        }
        
    	btnKids.setOnClickListener(new View.OnClickListener(){
		      @Override
		      public void onClick(View v) {
	        	   Intent intent = new Intent(v.getContext(), KidsListActivity.class);
				   intent.putExtra("Kids", activity.getKids());  
				   startActivity(intent);
		      };	
		
	    });
		
	    //recogemos los datos pasados desde LaFoscaMain
	    
       
        //Log.d("TAB1","2");
		
		
		setElState();
    	
		//Report that this fragment would like to participate in populating the options menu by receiving a call to
		//setHasOptionsMenu(true);
    	
    	return v;
        
    }
    
	public void setElState(){
		activity = (OptionsActivity) getActivity();
		State = activity.getState();
		//Log.d("TAB1","3");
		eState.setText(State);		

		//Log.d("TAB1","4");
		if(State.equals("closed"))
		{
			tFlag.setEnabled(false);
			tDirty.setEnabled(false);
			tHappy.setEnabled(false);
			eFlag.setEnabled(false);
			eDirty.setEnabled(false);
			eHappy.setEnabled(false);
			btnKids.setEnabled(false);
			Log.d("CLoSED","CLOSED");
			
		}else{
			tFlag.setEnabled(true);
			tDirty.setEnabled(true);
			tHappy.setEnabled(true);
			eFlag.setEnabled(true);
			eDirty.setEnabled(true);
			eHappy.setEnabled(true);
			btnKids.setEnabled(true);				

            /*JSONArray ka= new JSONArray(activity.getKids());		
			List<JSONObject> ja = sortKidsObjects(ka);*/
			
			if(activity.getFlag().equals("1"))
			    eFlag.setText("Yellow");
			else if(activity.getFlag().equals("0"))
				eFlag.setText("Green");
			else
				eFlag.setText("Red");
			eDirty.setText(activity.getDirty());
			eHappy.setText(activity.getHappy());
			
			
			//Log.d("SortJson",ja.toString());
		}
	}
	
	/**
	 * 
	 * @param array
	 * @return
	 */


	OnAsyncResult asynResult = new OnAsyncResult() {  

		@Override
		public void onResult(final boolean resultCode, final String message) {
			// TODO Auto-generated method stub
			Log.d("onResult TAB1",message);			
		}

		@Override
		public void onStateResult(boolean resultCode, JSONObject j) {
			// TODO Auto-generated method stub
			try {
				activity = (OptionsActivity) getActivity();
				
				
				activity.setState(j.getString("state").toString());
				if(activity.getState().equals("open")){
					activity.setFlag(j.getString("flag").toString());
					activity.setDirty(j.getString("dirtiness").toString());
					activity.setHappy(j.getString("happiness").toString());
				}
				
				Log.d("onStateResult TAB1",j.getString("state").toString());
                setElState();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
   };
   
  /* @Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.tab1_state, menu);
       super.onCreateOptionsMenu(menu,inflater);
   }*/
}
