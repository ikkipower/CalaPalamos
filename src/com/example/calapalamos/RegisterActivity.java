package com.example.calapalamos;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
	public void onResume(){
		super.onResume();
		passwdReg.setText("");
		nameReg.setText("");
		passwdReg.setHint("Password");
		nameReg.setHint("UserName");
	}

    @Override
    public void onClick(View view) {
    	Intent i = getIntent();
        switch(view.getId()){
        
        case R.id.btnReg:
        	if(nameReg.getText().length()!=0 && passwdReg.getText().length()!=0) {

		      i.putExtra("REGISTER1", nameReg.getText().toString());
		      i.putExtra("REGISTER2", passwdReg.getText().toString());
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

}	