package com.example.calapalamos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener {

	EditText nameReg;
    EditText passwdReg;
    Button btnReg;
    Button btnCancel;
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		nameReg=(EditText)findViewById(R.id.nameReg);
		passwdReg=(EditText)findViewById(R.id.passwdReg);
		btnReg= (Button)findViewById(R.id.btnReg);
		btnCancel= (Button)findViewById(R.id.btnCancel);
		btnReg.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
        
	}

    @Override
    public void onClick(View view) {
    	Intent i = getIntent();
        switch(view.getId()){
        
        case R.id.btnReg:
        	if(nameReg.getText().length()!=0 && passwdReg.getText().length()!=0) {
        	   User n = new User();
               /*JSONObject jLogin = new JSONObject();
               JSONObject jUser = new JSONObject();*/
               /*JSONParser jParser = new JSONParser();*/
               //try {
				     /*jUser.put("username",nameReg.getText().toString());
				     jUser.put("password",passwdReg.getText().toString());
				     jLogin.put("user", jUser);*/
				     //Toast.makeText(getBaseContext(), "Data! "+jLogin.getJSONObject("user").getString("username"), Toast.LENGTH_LONG).show();
		             n.setName(nameReg.getText().toString());
		             n.setPasswd(passwdReg.getText().toString());
		             //Log.d("RegisterActivity", "before Jsonparser");
		             //Toast.makeText(getBaseContext(), "Data! "+jParser.jPOST(url,jLogin), Toast.LENGTH_LONG).show()
		             
		             i.putExtra("REGISTER1", nameReg.getText().toString());
		             i.putExtra("REGISTER2", passwdReg.getText().toString());
		             setResult(RESULT_OK, i);
				     
				     
               /*} catch (JSONException e) {
				// TODO Auto-generated catch block
            	   
            	   Log.e("Error JSON Login",null);
				   e.printStackTrace();
               }*/
               

               finish();
               
        	}else{
        		Toast.makeText(getBaseContext(), "Error: Try Again ", Toast.LENGTH_LONG).show();

        	}  
               break;
                    
        case R.id.btnCancel:
            setResult(RESULT_CANCELED, i);
            finish();


            	
            break;
        }
 
    }
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

}	