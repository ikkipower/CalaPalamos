package com.example.calapalamos;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.calapalamos.library.HttpAsync;
import com.example.calapalamos.library.HttpAsync.OnAsyncResult;
import com.example.calapalamos.library.User;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * @author i2131344
 *
 */
public class LaFoscaMain extends Activity implements OnClickListener {
 
	private final static int REGISTER = 0;
	static final String EXTRA_MESSAGE = null;
	EditText nameLogin;
    EditText passwdLogin;
    Button btnLogin;
    TextView regLink;
    
    //save username and token
    //private String username;
    //private String authToken;
    private User user; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		nameLogin=(EditText)findViewById(R.id.nameLogin);
		passwdLogin=(EditText)findViewById(R.id.passwdLogin);
		btnLogin= (Button)findViewById(R.id.btnLogin);
		regLink = (TextView)findViewById(R.id.link_to_register);
		user = new User();
		//passwdLogin.setHint("Password");
		//nameLogin.setHint("UserName");
		if(isConnected())
		{
			Toast.makeText(this, "Resultado ok ", Toast.LENGTH_SHORT).show();			
		}
		else
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(LaFoscaMain.this);
	    	 builder.setTitle("SIN CONEXION").setMessage("Este APP necesita conexion a Internet").setCancelable(false)
                   .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                   }
            });
            AlertDialog alert = builder.create();
            alert.show();
		}
		
		// add click listener to Button "POST"
        btnLogin.setOnClickListener(this);
	}

	@Override
	public void onResume(){
		super.onResume();
		passwdLogin.setText("");
		nameLogin.setText("");
		passwdLogin.setHint("Password");
		nameLogin.setHint("UserName");
	}
	
    @Override
    public void onClick(View view) {
 
        switch(view.getId()){
            case R.id.btnLogin:
        		nameLogin=(EditText)findViewById(R.id.nameLogin);
        		passwdLogin=(EditText)findViewById(R.id.passwdLogin);
        		btnLogin= (Button)findViewById(R.id.btnLogin);
        		
                JSONObject jReg = new JSONObject();
                JSONObject jUser = new JSONObject();
                try {
                	user.setName(nameLogin.getText().toString());
                	jUser.put("username",user.getName());
                	jUser.put("password",passwdLogin.getText().toString());
    		        jReg.put("user", jUser);
    		        
    		        HttpAsync asyncTask = new HttpAsync(LaFoscaMain.this,Constants.LOG_IN_OPT);  
                    asyncTask.setOnResultListener(asynResult);  
                    asyncTask.execute(jReg);
                    
                }  catch (JSONException e) {
    				// TODO Auto-generated catch block
             	   
             	   Log.e("Error JSON Login",null);
 				   e.printStackTrace();
             }
        			
             break;
                    
            case R.id.link_to_register:
                
                Intent intent = new Intent(this, RegisterActivity.class);
                //inicio Activity Register esperando un resultado
                startActivityForResult(intent,REGISTER);
                
            	
            break;
        }
 
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_CANCELED) {
            
            Toast.makeText(this, "Resultado cancelado", Toast.LENGTH_SHORT).show();
        } else {
            
            JSONObject jReg = new JSONObject();
            JSONObject jUser = new JSONObject();
            try {
            	jUser.put("username",data.getExtras().getString("REGISTER1"));
            	jUser.put("password",data.getExtras().getString("REGISTER2"));
		        jReg.put("user", jUser);
		        new HttpAsync(LaFoscaMain.this,Constants.REG_OPT).execute(jReg);
            } catch (JSONException e) {
				// TODO Auto-generated catch block
            	   
            	   Log.e("Error JSON Register",null);
				   e.printStackTrace();
            }
		    
            

        }
    }       
    
    public boolean isConnected(){
    	ConnectivityManager con = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = con.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) 
            return true;
        else
            return false;
    }
    
    OnAsyncResult asynResult = new OnAsyncResult() {  

		@Override
		public void onResult(final boolean resultCode, final String message) {
			// TODO Auto-generated method stub		
		}
		
		@Override
		public void onStateResult(boolean resultCode, final JSONObject j) {
		   
			// TODO Auto-generated method stub
/*			runOnUiThread(new Runnable() {
				public void run() { */
 		           try{
 		        	   user.setAuthToken(j.getString("AuthToken"));
                       Intent intent = new Intent(LaFoscaMain.this, OptionsActivity.class);
					   intent.putExtra("init_cond", true);
                       intent.putExtra("username", user.getName());
					   intent.putExtra("AuthToken", user.getAuthToken());
					   //Log.d("INTENT",j.getJSONObject("jresult").toString());
					   intent.putExtra("state", j.getJSONObject("jresult").toString());
					   startActivity(intent);
		           }catch(Exception e) {
			          Log.d("JSON onStateResult", e.getLocalizedMessage());
		           }
/*				}
			});*/
		

		}  
    };
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.la_fosca_main, menu);
		return true;
	}

	  @Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.action_about:
	    	
           AlertDialog.Builder dialog = new AlertDialog.Builder(LaFoscaMain.this)  
	    	.setTitle("About Us")  
	    	.setMessage("Lafosca\n" +
	    		     	"Doctor Trueta 113, Barcelona\n" +
                        "email: xxx@lafosca.cat")  
	    	.setIcon(R.drawable.ic_launcher)  
	    	.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id) {
                	  dialog.cancel();
                  }
	    	});
	    	AlertDialog aboutDialog = dialog.create();
	    	aboutDialog.show();
	      break;
	    default:
	      break;
	    }
	    return true;  
	  }
}


