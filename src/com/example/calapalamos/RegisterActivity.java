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
    
	
	/** 
	  * Asociamos la Activity con el layout activity_register y asociamos sus elementos.  
      * Activamos las opciones OnclicListener en los botones.
      * 
      * @author sergio
	  * @see android.app.Activity#onCreate(android.os.Bundle)
	  * 
	  */
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
	
	/** 
	  * @see android.app.Activity#onResume()
	  */
	
	/**
	  * Funcion que reinicia los dos editview del layout
	  * Esto se realiza en la funcion onResume que se ejecuta cada vez que 
	  * la activity ha de interactuar con el usuario
	  * @author sergio
	  * @see android.app.Activity#onResume()
	  * @return null
	  */
	
	@Override
	public void onResume(){
		super.onResume();
		passwdReg.setText("");
		nameReg.setText("");
		passwdReg.setHint("Password");
		nameReg.setHint("UserName");
	}

	/**
	  * Override de la funcion onClick. 
	  * Esto funcion se ejecuta cada vez que un elemento de la activity es clicado
	  * Switch mediante id que permite ejeutar el codigo por separada de los diferentes
	  * metodos que tienen asociados la funcion setOnClickListener.
	  * 
      * @see android.view.View.OnClickListener#onClick(android.view.View)  
      */
	
    @Override
    public void onClick(View view) {
    	Intent i = getIntent();
        switch(view.getId()){
        
        case R.id.btnReg:
        	//comprobamos que los dos campos no esten vacio y devolvemos el resultado 
        	//a la FoscaMain mediante setresult
        	if(nameReg.getText().length()!=0 && passwdReg.getText().length()!=0) {

		      i.putExtra("REGISTER1", nameReg.getText().toString());
		      i.putExtra("REGISTER2", passwdReg.getText().toString());
		      setResult(RESULT_OK, i);
				     
              //cerramos la activity
              finish();
               
        	}else{
        		Toast.makeText(getBaseContext(), "Error: Try Again ", Toast.LENGTH_LONG).show();

        	}  
               break;
                    
        case R.id.btnCancel:
        	//cerrar la activity y devolver resultado a LaFoscaMain.
        	
            setResult(RESULT_CANCELED, i);
            finish();
          	
            break;
        }
 
    }

}	