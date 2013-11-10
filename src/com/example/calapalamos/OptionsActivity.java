/**
 * 
 */
package com.example.calapalamos;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * @author i2131344
 *
 */

public class OptionsActivity extends Activity {

	private String AuthToken;
	private ListView optionsLV;
	private String[] options = new String[]{"Abrir/Cerrar la Playa","Cambiar Bandera","Lanzar Pelotas","Listado de Ni–os"};
	
	public void setAuthToken(String auth){
		this.AuthToken = auth;
	}
	
	public String getAuthToken(){
		return AuthToken;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);
		
		// Introducimos las opciones del menu en el ListView   
	    optionsLV = (ListView) findViewById( R.id.listView1 );
	    
	    ArrayList<String> optArrList = new ArrayList<String>();  
	    optArrList.addAll( Arrays.asList(options) );  
	    ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, optArrList);
	    
	    optionsLV.setAdapter( listAdapter );
		
		//recogemos el dato del token
	    setAuthToken(getIntent().getExtras().getString("AuthToken"));
	    Log.d("OptionsActivity",getAuthToken());
	}
	
	
}
