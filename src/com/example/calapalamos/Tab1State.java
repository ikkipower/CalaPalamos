package com.example.calapalamos;



import org.json.JSONException;
import org.json.JSONObject;

import com.example.calapalamos.library.HttpAsync;
import com.example.calapalamos.library.OpenWeather;
import com.example.calapalamos.library.HttpAsync.OnAsyncResult;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	
	/** 
	  * 
	  * @author sergio
	  * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	  * @see com.example.calapalamos.KidsListActivity
	  * @throws JSONException
	  */
	@Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        
		View v = inflater.inflate(R.layout.activity_tab1_state, container, false);
       
		
		//cogemos la activity parent (en este caso optionsActivity)
		activity = (OptionsActivity) getActivity();
		
		//mostramos la navigation bar
		ActionBar abar = activity.getActionBar();
		abar.show();
		
		//Asociamos los elementos del layout con el fragment
		
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
			e1.printStackTrace();
		}

		
		//NO lo ejecutamos la primera vez poque ya tenemos los datos cogidos al crear
		//la optionsActivity.
		
        if(activity.getInitCond()==false)
        {
        	//llamada y ejecuci—n del HttpAsync para coger los datos del estado de la playa.
        	HttpAsync aStateTask = new HttpAsync(v.getContext(),Constants.GSTATE_OPT);  
        	aStateTask.setOnResultListener(asynResult);
            aStateTask.execute(jSend);
        }else{
        	activity.setInitCond(false);
        	
        }
        
        //Listener del boton que nos llama a la activity KidsListActivity.
    	btnKids.setOnClickListener(new View.OnClickListener(){
		      @Override
		      public void onClick(View v) {
	        	   Intent intent = new Intent(v.getContext(), KidsListActivity.class);
				   intent.putExtra("Kids", activity.getKids());  
				   startActivity(intent);
		      };	
		
	    });
		
		
		//update de los diferentes elementos dependiendo de si la playa esta abierta o cerrada
		setElState();
    	
		//Permite al fragment participar en las opciones del menu recibiendo 
		//una llamada de onCreateOptionsMenu y otros metodos relacionados
		setHasOptionsMenu(true);
    	
    	return v;
        
    }
    
	/**
	  * Funcion que pone Activa/Desactiva los diferentes elementos del Tab1 dependiendo
	  * del estado de la playa
	  * 
	  * @author sergio
	  *  
	  */
	public void setElState(){
		activity = (OptionsActivity) getActivity();
		State = activity.getState();
		eState.setText(State);		

		if(State.equals("closed"))
		{
			tFlag.setEnabled(false);
			tDirty.setEnabled(false);
			tHappy.setEnabled(false);
			eFlag.setEnabled(false);
			eDirty.setEnabled(false);
			eHappy.setEnabled(false);
			btnKids.setEnabled(false);
			
		}else{
			tFlag.setEnabled(true);
			tDirty.setEnabled(true);
			tHappy.setEnabled(true);
			eFlag.setEnabled(true);
			eDirty.setEnabled(true);
			eHappy.setEnabled(true);
			btnKids.setEnabled(true);				
			
			if(activity.getFlag().equals("1"))
			    eFlag.setText("Yellow");
			else if(activity.getFlag().equals("0"))
				eFlag.setText("Green");
			else
				eFlag.setText("Red");
			eDirty.setText(activity.getDirty());
			eHappy.setText(activity.getHappy());
		}
	}
	



	/**
	  * Generacion de la Interface con la funcion HttpAsync. 
	  * Dos funciones onResult y onStateResult.
	  * En este caso unicamente utilizaremos la funcion onStateResult.
	  * 
	  * @author sergio
	  * 
	  */   
	OnAsyncResult asynResult = new OnAsyncResult() {  

		@Override
		public void onResult(final boolean resultCode, final OpenWeather weather, JSONObject j)  {
			// TODO Auto-generated method stub
				
		}

		/**
		  *Recogemos los datos recibidos de la funcion HttpAsync para actualizar
		  *el estado de los diferentes elementos del tab1 
		  *@author sergio
		  * 
		  */
		
		@Override
		public void onStateResult(boolean resultCode, int i, JSONObject j) {
			// TODO Auto-generated method stub
			try {
				activity = (OptionsActivity) getActivity();
				
				
				activity.setState(j.getString("state").toString());
				if(activity.getState().equals("open")){
					activity.setFlag(j.getString("flag").toString());
					activity.setDirty(j.getString("dirtiness").toString());
					activity.setHappy(j.getString("happiness").toString());
				}
				
                setElState();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
   };
   
   /* (non-Javadoc)
 * @see android.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
 */
@Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
      inflater.inflate(R.menu.tab1_state, menu);
   }
   

/**
  * Definicion de la funcionalidad de los diferentes elementos del menu.
  * En esta activity unicamente tenemos un boton, que sirve para hacer un 
  * update de los datos
  * 
  * @autor sergio
  * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
  * @throw JSONException
  */

@Override
   public boolean onOptionsItemSelected(MenuItem item) {
	   Log.d("options","menu");
       // Take appropriate action for each action item click
       switch (item.getItemId()) {
       case R.id.action_refresh:
    	   // refresh
           JSONObject jSend = new JSONObject();
           JSONObject jUser = new JSONObject();
           try {

        	   jUser.put("authenticationToken",activity.getAuthToken());
        	   jSend.put("user", jUser);
           } catch (JSONException e1) {
        	   // TODO Auto-generated catch block
   			e1.printStackTrace();
           }
           // load the data from server
           HttpAsync aStateTask = new HttpAsync(activity.getBaseContext(),Constants.GSTATE_OPT);  
           aStateTask.setOnResultListener(asynResult);
           aStateTask.execute(jSend);
           return true;
       default:
           return super.onOptionsItemSelected(item);
       }
   }
   
}
