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

public class Tab2Opt extends Fragment { //implements OnClickListener{
    
	Button btnState;
	String Auth;
	Context Cont;
	OptionsActivity activity;
	
	@Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
		
		activity = (OptionsActivity) getActivity();
    	View v = inflater.inflate(R.layout.activity_tab2_opt, container, false);
    	Log.d("Tab2state",""+v.getContext());
    	
    	btnState=(Button)v.findViewById(R.id.btnState);
    	if(activity.getState().equals("open")){
    		btnState.setText("closed");	
    	}else{
    		btnState.setText("open");
    	}
    	
    	btnState.setOnClickListener(new View.OnClickListener(){
    		      @Override
    		      public void onClick(View v) {
    		        sendChangeState(v);
    		      };	
    		
    	});
    	
    	
    	Auth = activity.getAuthToken();
    	
    	Log.d("Tab2Opt",Auth);

        return v;
    }

	public void sendChangeState(View v){
		
		JSONObject jAuth = new JSONObject();
    	
		 
         try {
       
        	 jAuth.put("Auth",Auth);
        	 //OptionsActivity activity = (OptionsActivity) getActivity();
        	 jAuth.put("state", activity.getState()); //coger el estado 
        	 HttpAsync asyncTask = new HttpAsync(getActivity(),Constants.CHANGE_STATE_OPT);  
             asyncTask.setOnResultListener(asynResult);  
             asyncTask.execute(jAuth);
             
        	 
         }  catch (JSONException e) {
				// TODO Auto-generated catch block
      	   
      	   Log.e("Error JSON Login",null);
			   e.printStackTrace();
          }
		
		
		/*HttpAsync asyncTask = new HttpAsync(LaFoscaMain.this,Constants.LOG_IN_OPT);  
        asyncTask.setOnResultListener(asynResult);  
        asyncTask.execute(jReg);*/
	}


	
	   OnAsyncResult asynResult = new OnAsyncResult() {  

			@Override
			public void onResult(final boolean resultCode, final String message) {
				// TODO Auto-generated method stub
				Log.d("onResult TAB2",message);
				if(resultCode==true){
					OptionsActivity activity = (OptionsActivity) getActivity();
					activity.setState(message);

					Log.d("Estado Cambiado",message);
				}else{
					Log.d("Estado NO Cambiado",message);
				}
				
			}

			@Override
			public void onStateResult(boolean resultCode, JSONObject j) {
				// TODO Auto-generated method stub
				
			}
			
	   };
}
