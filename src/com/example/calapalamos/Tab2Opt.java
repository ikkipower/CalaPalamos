package com.example.calapalamos;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.calapalamos.library.HttpAsync;
import com.example.calapalamos.library.HttpAsync.OnAsyncResult;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost.OnTabChangeListener;

public class Tab2Opt extends Fragment{ //implements OnClickListener{
    
	RadioGroup radioFlagGroup;
	RadioButton Green_check, Yellow_check, Red_check;
	Button btnState, btnFlag, btnLanzar, btnLimpiar;
	String Auth;
	Context Cont;
	OptionsActivity activity;
	
	@Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
		
		activity = (OptionsActivity) getActivity();
    	View v = inflater.inflate(R.layout.activity_tab2_opt, container, false);
    	Log.d("Tab2state",""+v.getContext());
    	
        Auth = activity.getAuthToken();
    	
    	Log.d("Tab2Opt",Auth);
    	
    	btnState=(Button)v.findViewById(R.id.btnState);
    	
    	
    	
    	setEnableDisable(v);
    	
    	btnState.setOnClickListener(new View.OnClickListener(){
    		      @Override
    		      public void onClick(View v) {
    		        sendChange(v,0);
    		      };	
    		
    	});
    	
    	btnFlag.setOnClickListener(new View.OnClickListener(){
		      @Override
		      public void onClick(View v) {
		        sendChange(v,1);
		      };	
		
	    });
    	
    	

        return v;
    }

	
	public void sendChange(View v,int p){
		
		JSONObject jSend = new JSONObject();
		HttpAsync asyncTask;
		 
         try {
             if(p == 0)
             {
                 jSend.put("Auth",Auth);
             	 //OptionsActivity activity = (OptionsActivity) getActivity();
             	 jSend.put("state", activity.getState()); //coger el estado 
                 asyncTask = new HttpAsync(getActivity(),Constants.CHANGE_STATE_OPT);
             }
             else{
            	 JSONObject jFlag = new JSONObject();
            	 if(Green_check.isChecked()==true)
            	 {
            		 jFlag.put("flag", "0"); 
            	 }else{
            		 if(Yellow_check.isChecked()==true)
            		 {
            			 jFlag.put("flag", "1");
            		 }else{
            			 jFlag.put("flag", "2");
            		 }
            	 }
            	 
    			 jSend.put("Auth",Auth);
            	 //OptionsActivity activity = (OptionsActivity) getActivity();
            	 jSend.put("flag_j", jFlag); //coger el estado 
            	 Log.d("JSEN",jSend.toString());
            	 asyncTask = new HttpAsync(getActivity(),Constants.CHANGE_STATE_FLAG);
             }
        	 
             asyncTask.setOnResultListener(asynResult);  
             asyncTask.execute(jSend); 
        	 
         }  catch (JSONException e) {
				// TODO Auto-generated catch block
      	   
      	   Log.e("Error JSON Login",null);
			   e.printStackTrace();
          }
	}
	
	OnAsyncResult asynResult = new OnAsyncResult() {  

			@Override
			public void onResult(final boolean resultCode, final String message) {
				// TODO Auto-generated method stub
				Log.d("onResult TAB2",message);
				if(resultCode==true){
					OptionsActivity activity = (OptionsActivity) getActivity();
					
                        if(message.equals("open")){
                        	activity.setState(message);
    						Log.d("Estado Cambiado",message);
                            btnLimpiar.setEnabled(false);
    						btnLanzar.setEnabled(true);
    						btnFlag.setEnabled(true);
    						Green_check.setEnabled(true);
    						Yellow_check.setEnabled(true);
    						Red_check.setEnabled(true);
    						if(activity.getFlag().equals("0"))
    		            	 {
    		            		 Green_check.setChecked(true); 
    		            	 }else{
    		            		 if(activity.getFlag().equals("1"))
    		            		 {
    		            			 Yellow_check.setChecked(true);
    		            		 }else{
    		            			 Red_check.setChecked(true);
    		            		 }
    		            	 }
                        }else if(message.equals("closed")){
                        	activity.setState(message);
    						Log.d("Estado Cambiado clo",message);
    						Green_check.setEnabled(false);
    						Yellow_check.setEnabled(false);
    						Red_check.setEnabled(false);
    						btnLimpiar.setEnabled(true);
    						btnLanzar.setEnabled(false);
    						
    						btnFlag.setEnabled(false);
                        }else{
                        	activity.setFlag(message);
    						Log.d("Flag Cambiado",message);
                        }

				}else{
					Log.d("NO Cambiado",message);
				}
				
			}

			@Override
			public void onStateResult(boolean resultCode, JSONObject j) {
				// TODO Auto-generated method stub
				
			}
			
	   };
	   
	   public void setEnableDisable(View v){
			Green_check = (RadioButton)v.findViewById(R.id.cFlagGreen);
			Yellow_check = (RadioButton)v.findViewById(R.id.cFlagYellow);
			Red_check = (RadioButton)v.findViewById(R.id.cFlagRed);
			btnFlag = (Button)v.findViewById(R.id.btnFlag);
			btnLimpiar = (Button)v.findViewById(R.id.btnClean);
			btnLanzar = (Button)v.findViewById(R.id.btnNivea);
			activity = (OptionsActivity) getActivity();
			
			if(activity.getState().equals("open")){
				btnLimpiar.setEnabled(false);
				if(activity.getFlag().equals("1"))
	     		    Yellow_check.setChecked(true);
		     	else if(activity.getFlag().equals("0"))
	    			Green_check.setChecked(true);
			    else
				    Red_check.setChecked(true);
			}else{
				
				btnLanzar.setEnabled(false);
				btnLimpiar.setEnabled(true);
				Green_check.setEnabled(false);
				Yellow_check.setEnabled(false);
				Red_check.setEnabled(false);
				btnFlag.setEnabled(false);
			}
						
			
	   }
	   
}
