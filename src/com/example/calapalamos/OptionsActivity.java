/**
 * 
 */
package com.example.calapalamos;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.calapalamos.library.MiTabListener;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;


/**
 * @author i2131344
 *
 */

public class OptionsActivity extends Activity {

	private String AuthToken;
	private String Name;
	//private ListView optionsLV;
	//private String[] options = new String[]{"Abrir/Cerrar la Playa","Cambiar Bandera","Lanzar Pelotas","Listado de Ni–os"};
	
	public void setAuthToken(String auth){
		this.AuthToken = auth;
	}
	
	public String getAuthToken(){
		return AuthToken;
	}

	public void setName(String n){
		this.Name = n;
	}
	
	public String getName(){
		return Name;
	}
	
	/**
	 * 
	 * 
	 * 
	 */
	
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
	    
	    //recogemos los datos pasados desde LaFoscaMain
	    try {
			JSONObject obj = new JSONObject(getIntent().getStringExtra("state"));
			Log.d("onStateResult",""+obj.getJSONArray("kids"));
			List<JSONObject> ja = sortKidsObjects(obj.getJSONArray("kids"));
			Log.d("SortJson",ja.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("OptionsActivity",e.toString());
		}
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
					Log.d("lhs",""+lid);
					Log.d("rhs",""+rid);
				    
					// Here you could parse string id to integer and then compare.
					Log.d("CompareTo",""+rid.compareTo(lid));
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
