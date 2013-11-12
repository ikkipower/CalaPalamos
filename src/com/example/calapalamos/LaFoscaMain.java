package com.example.calapalamos;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.calapalamos.library.Constants;
import com.example.calapalamos.library.HttpAsync;
import com.example.calapalamos.library.HttpAsync.OnAsyncResult;
import com.example.calapalamos.library.User;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
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
		if(isConnected())
		{
			Toast.makeText(this, "Resultado ok ", Toast.LENGTH_SHORT).show();			
		}
		else
		{
			Toast.makeText(this, "Resultado no oj", Toast.LENGTH_SHORT).show();
		}
		
		
		// add click listener to Button "POST"
        btnLogin.setOnClickListener(this);
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
                      //Log.e("Asynctask bafter","Jreg");
                      //Toast.makeText(this, "Resultado ", Toast.LENGTH_SHORT).show();
    		        //new HttpAsyncGet(LaFoscaMain.this,Constants.LOG_IN_OPT).execute(jReg);
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
            
            Toast.makeText(this, "Resultado cancelado", Toast.LENGTH_SHORT)
                    .show();
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
			runOnUiThread(new Runnable() {
				public void run() {
					if(resultCode==false)
					{
						AlertDialog.Builder builder = new AlertDialog.Builder(LaFoscaMain.this);
				    	 builder.setTitle("Login Failed!").setMessage("Try Again!").setCancelable(false)
			                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
			                    public void onClick(DialogInterface dialog, int id) {
			                         dialog.cancel();
			                    }
			             });
			             AlertDialog alert = builder.create();
			             alert.show();
					}else{	
						Log.d("ASYNCRESULT",message+"->"+resultCode);
						user.setAuthToken(message);
						
			            JSONObject jReg = new JSONObject();
			            JSONObject jUser = new JSONObject();
			            try {
			            	jUser.put("username",user.getName());
			            	jUser.put("authenticationToken",user.getAuthToken());
					        jReg.put("user", jUser);
					        HttpAsync aStateTask = new HttpAsync(LaFoscaMain.this,Constants.GSTATE_OPT);  
		                    aStateTask.setOnResultListener(asynResult);
					        aStateTask.execute(jReg);
			            } catch (JSONException e) {
							// TODO Auto-generated catch block
			            	   
			            	   Log.e("Error JSON Register",null);
							   e.printStackTrace();
			            }
						
						
						
						
	                      
	                    
						
						/*httpput.setHeader("Authorization", "Token token="
                                + authenticationToken);*/
					}
				}
			});
			
		}
		
		@Override
		public void onStateResult(boolean resultCode, final JSONObject j) {
		   
			// TODO Auto-generated method stub
/*			runOnUiThread(new Runnable() {
				public void run() { */
 		           try{
                       
                       Intent intent = new Intent(LaFoscaMain.this, OptionsActivity.class);
					   intent.putExtra("username", user.getName());
					   intent.putExtra("AuthToken", user.getAuthToken());
					   intent.putExtra("state", j.toString());
					   startActivity(intent);
		           }catch(Exception e) {
			          Log.d("JSON onStateResult", e.getLocalizedMessage());
		           }
/*				}
			});*/
		
			
			
			// TODO Auto-generated method stub
			/*Intent intent = new Intent(LaFoscaMain.this, OptionsActivity.class);
		intent.putExtra("username", user.getName());
		intent.putExtra("AuthToken", user.getAuthToken());
		startActivity(intent);*/	
		}  
    };
	
	
}


