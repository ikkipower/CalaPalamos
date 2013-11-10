/**
 * 
 */
package com.example.calapalamos;



import com.example.calapalamos.library.MiTabListener;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;


/**
 * @author i2131344
 *
 */

public class OptionsActivity extends Activity {

	private String AuthToken;
	//private ListView optionsLV;
	//private String[] options = new String[]{"Abrir/Cerrar la Playa","Cambiar Bandera","Lanzar Pelotas","Listado de Ni–os"};
	
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

		//Obtenemos una referencia a la actionbar
	    ActionBar abar = getActionBar();
	 
	    //Establecemos el modo de navegaci—n por pesta–as
	    abar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	 
	    //Creamos las pesta–as
	    ActionBar.Tab tab1 = abar.newTab().setText("State");
	    ActionBar.Tab tab2 = abar.newTab().setText("Change");
	 
	    //Creamos los fragments de cada pesta–a
	    Fragment tab1frag = new Tab1State();
	    Fragment tab2frag = new Tab2Opt();
	 
	    //Asociamos los listener a las pesta–as
	    tab1.setTabListener(new MiTabListener(tab1frag));
	    tab2.setTabListener(new MiTabListener(tab2frag));
	 
	    //A–adimos las pesta–as a la action bar
	    abar.addTab(tab1);
	    abar.addTab(tab2);
	}
	
	
}
