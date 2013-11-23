package com.example.calapalamos;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.calapalamos.library.HttpAsync;
import com.example.calapalamos.library.OpenWeather;
import com.example.calapalamos.library.HttpAsync.OnAsyncResult;

import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
    	
		Green_check = (RadioButton)v.findViewById(R.id.cFlagGreen);
		Yellow_check = (RadioButton)v.findViewById(R.id.cFlagYellow);
		Red_check = (RadioButton)v.findViewById(R.id.cFlagRed);
		btnFlag = (Button)v.findViewById(R.id.btnFlag);
		btnLimpiar = (Button)v.findViewById(R.id.btnClean);
		btnLanzar = (Button)v.findViewById(R.id.btnNivea);
    	btnState=(Button)v.findViewById(R.id.btnState);
    	
    	
    	if(activity.getState().equals("open")){
			setEnable();
		}else{
			setDisable();
		}	
	    
	
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
    	
    	btnLanzar.setOnClickListener(new View.OnClickListener(){
		      @Override
		      public void onClick(View v) {
		        sendChange(v,2);
		      };	
		
	    });

    	btnLimpiar.setOnClickListener(new View.OnClickListener(){
		      @Override
		      public void onClick(View v) {
		        sendChange(v,3);
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
             	 jSend.put("state", activity.getState()); //coger el estado 
                 asyncTask = new HttpAsync(getActivity(),Constants.CHANGE_STATE_OPT);
                 asyncTask.setOnResultListener(asynResult);  
                 asyncTask.execute(jSend); 
             }
             else{
            	 if(p == 1)
            	 {
            		 
            		 
            		 JSONObject jFlag = new JSONObject();
            		 Log.d("senData",activity.getFlag());
            		 jSend.put("Auth",Auth);
            		 if(Green_check.isChecked()==true && !activity.getFlag().equals("0"))
            		 {
            			 jFlag.put("flag", "0"); 
            			 jSend.put("flag_j", jFlag); //coger el estado 
                		 Log.d("JSEN",jSend.toString());
                		 asyncTask = new HttpAsync(getActivity(),Constants.CHANGE_STATE_FLAG);
                		 asyncTask.setOnResultListener(asynResult);  
                         asyncTask.execute(jSend); 
            		 }else{
            			 if(Yellow_check.isChecked()==true && !activity.getFlag().equals("1"))
            			 {
            				 jFlag.put("flag", "1");
            				 jSend.put("flag_j", jFlag); //coger el estado 
                    		 Log.d("JSEN",jSend.toString());
                    		 asyncTask = new HttpAsync(getActivity(),Constants.CHANGE_STATE_FLAG);
                    		 asyncTask.setOnResultListener(asynResult);  
                             asyncTask.execute(jSend); 
            			 }else{
            				 if(Red_check.isChecked()==true && !activity.getFlag().equals("2"))
            				 {
            					 jFlag.put("flag", "2");
            					 jSend.put("flag_j", jFlag); //coger el estado 
                        		 Log.d("JSEN",jSend.toString());
                        		 asyncTask = new HttpAsync(getActivity(),Constants.CHANGE_STATE_FLAG);
                        		 asyncTask.setOnResultListener(asynResult);  
                                 asyncTask.execute(jSend); 
            				 }else{
            					 Toast.makeText(activity.getBaseContext(), "Esta bandera ya ondea en la playa", Toast.LENGTH_LONG).show(); 
            				 }
            				 
            			 }
            		 }
            	 
            		 
            		 
            		 
            	 }else{ //opt 2 y 3
            		 jSend.put("Auth",Auth);
            		 Log.d("JSEN",jSend.toString());
            		 if(p == 2){
            			 asyncTask = new HttpAsync(getActivity(),Constants.THROW_OPT); 
            		 }else{
            			 asyncTask = new HttpAsync(getActivity(),Constants.CLEAN_OPT);
            		 }
            		 asyncTask.setOnResultListener(asynResult);  
                     asyncTask.execute(jSend); 
            	 }
             }
        	 
             
        	 
         }  catch (JSONException e) {
				// TODO Auto-generated catch block
      	   
      	   Log.e("Error JSON Login",null);
			   e.printStackTrace();
          }
	}
	
	OnAsyncResult asynResult = new OnAsyncResult() {  

			@Override
			public void onResult(final boolean resultCode, final OpenWeather weather, JSONObject j)  {

			}

			@Override
			public void onStateResult(boolean resultCode, int i, JSONObject j) {
				// TODO Auto-generated method stub
				OptionsActivity activity = (OptionsActivity) getActivity();
				try {
					if(resultCode == true && i == 4){
						activity.setState(j.getString("state"));
						if(activity.getState().equals("open")){
							activity.setState(j.getString("state"));
							activity.setFlag(j.getString("flag"));
							activity.setDirty(j.getString("dirtiness"));
							activity.setHappy(j.getString("happiness"));
							Log.d("Estado Cambiado",activity.getState());
							
							setEnable();
							
						}else if(j.getString("state").equals("closed")){
							activity.setState(j.getString("state"));
							Log.d("Estado Cambiado clo",activity.getState());
							
							setDisable();
						
						}
					}else if(resultCode == true && i == 3){
						
						activity.setFlag(j.getString("flag"));
	    				Log.d("Flag Cambiado",j.getString("flag"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
};
	   

	   
	   private void setDisable(){
			btnLanzar.setEnabled(false);
			btnLimpiar.setEnabled(true);
			Green_check.setEnabled(false);
			Yellow_check.setEnabled(false);
			Red_check.setEnabled(false);
			btnFlag.setEnabled(false);
	   }
	   
	   private void setEnable(){
			btnLanzar.setEnabled(true);
			btnLimpiar.setEnabled(false);
			btnFlag.setEnabled(true);
			Green_check.setEnabled(true);
			Yellow_check.setEnabled(true);
			Red_check.setEnabled(true);
			if(activity.getFlag().equals("1"))
    		    Yellow_check.setChecked(true);
	     	else if(activity.getFlag().equals("0"))
   			Green_check.setChecked(true);
		    else
			    Red_check.setChecked(true);
	   }
	   
}
