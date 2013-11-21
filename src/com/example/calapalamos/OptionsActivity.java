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
    private boolean init_cond;
	
	public boolean getInitCond(){
		return this.init_cond;
	}
	
	public void setInitCond(boolean c){
		this.init_cond = c;
	}
	
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
        setInitCond(getIntent().getBooleanExtra("init_cond", true));

		
		//Obtenemos una referencia a la actionbar
	    ActionBar abar = getActionBar();
	   
	 
	    //Establecemos el modo de navegacion por tabs
	    abar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	   
	    //Creamos las tabs
	    ActionBar.Tab tab1 = abar.newTab().setText("State");
	    ActionBar.Tab tab2 = abar.newTab().setText("Change");
	    ActionBar.Tab tab3 = abar.newTab().setText("Weather");
	   
	    //Creamos los fragments de cada tabs
	    Fragment tab1frag = new Tab1State();
	    Fragment tab2frag = new Tab2Opt();
	    Fragment tab3frag = new Tab3Weather();
	    
	    //Asociamos los listener a los tabs
	    tab1.setTabListener(new MiTabListener(tab1frag));
	    tab2.setTabListener(new MiTabListener(tab2frag));
	    tab3.setTabListener(new MiTabListener(tab3frag));
	    //Añadimos las tabs a la action bar
	    abar.addTab(tab1);
	    abar.addTab(tab3);
	    abar.addTab(tab2);
	    
        
        
		try {
			JSONObject jstate = new JSONObject(getIntent().getStringExtra("state"));
			setState(jstate.get("state").toString());
			if(getState().equals("open"))
			{
				setDirty(jstate.getString("dirtiness"));			
				setHappy(jstate.getString("happiness"));
				setKids(jstate.getString("kids"));
				//JSONArray js = new JSONArray(getKids());
				//Log.d("optKids", js.getJSONObject(2).toString());
		     	setFlag(jstate.getString("flag"));
		     	//TODO add kids string
			}

			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		

	    

	}
	

}
