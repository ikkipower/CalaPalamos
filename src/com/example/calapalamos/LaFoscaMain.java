package com.example.calapalamos;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.calapalamos.library.Constants;
import com.example.calapalamos.library.HttpAsyncGet;
import com.example.calapalamos.library.HttpAsyncPost;
import com.example.calapalamos.library.User;

import android.os.Bundle;
import android.app.Activity;
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

public class LaFoscaMain extends Activity implements OnClickListener {
 
	private final static int REGISTER = 0;
	static final String EXTRA_MESSAGE = null;
	EditText nameLogin;
    EditText passwdLogin;
    Button btnLogin;
    TextView regLink;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		nameLogin=(EditText)findViewById(R.id.nameLogin);
		passwdLogin=(EditText)findViewById(R.id.passwdLogin);
		btnLogin= (Button)findViewById(R.id.btnLogin);
		regLink = (TextView)findViewById(R.id.link_to_register);
		
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
                	jUser.put("username",nameLogin.getText());
                	jUser.put("password",passwdLogin.getText());
    		        jReg.put("user", jUser);
    		        Log.d("JSON JSON",jReg.getJSONObject("user").getString("username").toString());
    		        Log.d("CALLED ASYNCGET",""+new HttpAsyncGet(LaFoscaMain.this,Constants.LOG_IN_OPT).execute(jReg));
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
            
        	User nuser = new User();
            nuser.setName(data.getExtras().getString("REGISTER1"));
            nuser.setPasswd(data.getExtras().getString("REGISTER2"));
            Toast.makeText(this, "Resultado "+nuser.getName()+ " "+nuser.getPasswd(), Toast.LENGTH_SHORT).show();
            JSONObject jReg = new JSONObject();
            JSONObject jUser = new JSONObject();
            try {
            	jUser.put("username",nuser.getName());
            	jUser.put("password",nuser.getPasswd());
		        jReg.put("user", jUser);
		        new HttpAsyncPost(LaFoscaMain.this,Constants.POST_OPT).execute(jReg);
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
    


	
	
}


