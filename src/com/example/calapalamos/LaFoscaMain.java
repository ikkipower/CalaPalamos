package com.example.calapalamos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LaFoscaMain extends Activity implements OnClickListener {
 
	private final static int REGISTER = 0;
	static final String EXTRA_MESSAGE = null;
	EditText nameLogin;
    EditText passwdLogin;
    Button btnLogin;
    TextView regLink;
	private static String url = "http://lafosca-beach.herokuapp.com/api/v1";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		nameLogin=(EditText)findViewById(R.id.nameLogin);
		passwdLogin=(EditText)findViewById(R.id.passwdLogin);
		btnLogin= (Button)findViewById(R.id.btnLogin);
		regLink = (TextView)findViewById(R.id.link_to_register);
		// add click listener to Button "POST"
        btnLogin.setOnClickListener(this);
	}

    @Override
    public void onClick(View view) {
 
        switch(view.getId()){
            case R.id.btnLogin:
      
              //      Toast.makeText(getBaseContext(), userReg.getText()+" Enter some data! "+passwdReg.getText(), Toast.LENGTH_LONG).show();
            	    
                    break;
                    
            case R.id.link_to_register:
                Intent intent = new Intent(this, RegisterActivity.class);
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
            
            String user = data.getExtras().getString("REGISTER1");
            String passwd = data.getExtras().getString("REGISTER2");
            Toast.makeText(this, "Resultado "+user+ " "+passwd, Toast.LENGTH_SHORT).show();
            // Y tratamos el resultado en funci—n de si se lanz— para rellenar el
            // nombre o el apellido.
            switch (requestCode) {
            case REGISTER:
                //etNombre.setText(resultado);
                break;

            }
        }
    }    
    

}
