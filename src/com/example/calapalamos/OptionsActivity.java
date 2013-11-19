/**
 * 
 */
package com.example.calapalamos;




import org.json.JSONException;
import org.json.JSONObject;


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
	private String Name;
	private String mState;
	private String mDirty;
	private String mHappy;
	private String mKids;
	private String mFlag;
	
	
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
	
	public void setState(String s){
		this.mState = s;
	}
	
	public String getState(){
		return mState;
	}	
	
	public void setHappy(String h){
		this.mHappy = h;
	}
	
	public String getHappy(){
		return mHappy;
	}
	
	public void setDirty(String d){
		this.mDirty = d;
	}
	
	public String getDirty(){
		return mDirty;
	}

	public void setKids(String k){
		this.mKids = k;
	}
	
	public String getKids(){
		return mKids;
	}

	public void setFlag(String f){
		this.mFlag = f;
	}
	
	public String getFlag(){
		return mFlag;
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

		setAuthToken(getIntent().getStringExtra("AuthToken"));


		
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
	    
	    
        
        
		try {
			JSONObject jstate = new JSONObject(getIntent().getStringExtra("state"));
			setState(jstate.get("state").toString());
			if(getState().equals("open"))
			{
				setDirty(jstate.getString("dirtiness"));			
				setHappy(jstate.getString("happiness"));
		     	setKids(jstate.getString("kids"));
		     	setFlag(jstate.getString("flag"));
			}

			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	    

	}
	

}
