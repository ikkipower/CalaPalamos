/**
 * 
 */
package com.example.calapalamos;




import org.json.JSONException;
import org.json.JSONObject;


import com.example.calapalamos.library.MiTabListener;
import com.example.calapalamos.library.OpenWeather;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;


/**
 * @author sergio
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
	private OpenWeather op;
	

	public OpenWeather getOp() {
		return op;
	}


	public void setOp(OpenWeather op) {
		this.op = op;
	}

	
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
	 * Generacion de la ACtivty, donde se crea la action bar, el modo de funcionamiento
	 * y los tabs (Fragments) que vamos a necesitar
	 * Cogemos los datos recibidos de la FoscaMainActivity y establecemos el estado de la playa
	 * que configurara tantos el tab State como el Change
	 * 
	 * @author sergio
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * @throws JSONException
	 * 
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);

		setAuthToken(getIntent().getStringExtra("AuthToken"));
        setInitCond(getIntent().getBooleanExtra("init_cond", true));
		//Obtenemos la actionbar asociada a la Activity
	    ActionBar abar = getActionBar();
	    abar.show();
	 
	    //Seleccionamos el modo de navegacion por tabs
	    abar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	   
	    //Creamos las tabs
	    ActionBar.Tab tab1 = abar.newTab().setText("State");
	    ActionBar.Tab tab2 = abar.newTab().setText("Change");
	    ActionBar.Tab tab3 = abar.newTab().setText("Weather");
	   
	    //Creamos los fragments asociados a las tabs
	    Fragment tab1frag = new Tab1State();
	    Fragment tab2frag = new Tab2Opt();
	    Fragment tab3frag = new Tab3Weather();
	    
	    //Asociamos los listener a los tabs
	    tab1.setTabListener(new MiTabListener(tab1frag));
	    tab2.setTabListener(new MiTabListener(tab2frag));
	    tab3.setTabListener(new MiTabListener(tab3frag));
	    
	    //Agregamos los tabs a la action bar
	    abar.addTab(tab1);
	    abar.addTab(tab3);
	    abar.addTab(tab2);
	    
        
        
		try {
			JSONObject jstate = new JSONObject(getIntent().getStringExtra("state"));
			setState(jstate.get("state").toString());
			
			//cogemos el estado del Intent y guardamos los parametros en las variables locales de la activty
			
			if(getState().equals("open"))
			{
				setDirty(jstate.getString("dirtiness"));			
				setHappy(jstate.getString("happiness"));
				setKids(jstate.getString("kids"));
		     	setFlag(jstate.getString("flag"));
			}

			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	    

	}
	
	
    /**
 	  * Creacion de las diferentes opciones de menu en la navigation bar.
 	  * Asociado al menu options
 	  * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
      * 
 	  */ 
	
    public boolean onCreateOptionsMenu(Menu menu) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.options, menu);
                return true;
    }

}
