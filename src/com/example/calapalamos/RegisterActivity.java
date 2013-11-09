package com.example.calapalamos;

import com.example.calapalamos.library.User;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
            
		      n.setName(nameReg.getText().toString());
		      n.setPasswd(passwdReg.getText().toString());
		      i.putExtra("REGISTER1", n.getName());
		      i.putExtra("REGISTER2", n.getPasswd());
		      setResult(RESULT_OK, i);
				     

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