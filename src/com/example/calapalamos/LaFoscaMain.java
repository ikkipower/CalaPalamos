package com.example.calapalamos;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.calapalamos.library.HttpAsync;
import com.example.calapalamos.library.OpenWeather;
import com.example.calapalamos.library.HttpAsync.OnAsyncResult;

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
import android.widget.Toast;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * @author sergio
 *
 */
public class LaFoscaMain extends Activity implements OnClickListener {
 
	private final static int REGISTER = 0;
	private EditText nameLogin;
    private EditText passwdLogin;
    private Button btnLogin;
    
    private String userName;
    private String userPasswd;
	private String authToken;
    
    
    public String getUserPasswd() {
		return userPasswd;
	}

	public void setUserPasswd(String userPasswd) {
		this.userPasswd = userPasswd;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String username) {
		this.userName = username;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}    
    
	/**
	  * Override del metodo onCreate
	  * Asociacion del layout login con sus componentes a la activity LaFoscaMain
	  * Comprobacion que el dispositivo tiene internet
	  * Llamada a metodo onClickListener del boton y del textView para registrarse
	  *
	  * @author sergio
	  * 
	  */
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		nameLogin=(EditText)findViewById(R.id.nameLogin);
		passwdLogin=(EditText)findViewById(R.id.passwdLogin);
		btnLogin= (Button)findViewById(R.id.btnLogin);

		if(!isConnected()){
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

	/**
	  * Funcion que reinicia los dos editview del layout
	  * Esto se realiza en la funcion onResume que se ejecuta cada vez que 
	  * la activity ha de interactuar con el usuario
	  * @author sergio
	  * @see android.app.Activity#onResume()
	  */
	
	@Override
	public void onResume(){
		super.onResume();
		passwdLogin.setText("");
		nameLogin.setText("");
		passwdLogin.setHint("Password");
		nameLogin.setHint("UserName");
	}
	

	/**
	  * Override de la funcion onClick. 
	  * Esto funcion se ejecuta cada vez que un elemento de la activity es clicado
	  * Switch mediante id que permite ejeutar el codigo por separada de los diferentes
	  * metodos que tienen asociados la funcion setOnClickListener.
	  * 
	  * @author sergio
	  * @param view
	  * @see com.example.calapalamos.library.HttpAsync
	  * @see com.example.calapalamos.library.RegisterActivity
	  * @see android.view.View.OnClickListener#onClick(android.view.View)
	  * @return null
	  * @throws JSONException
	  */
	
    @Override
    public void onClick(View view) {
 
        switch(view.getId()){
        
            //codigo asociado al btnLogin (boton de login)
            case R.id.btnLogin:
            	
            	//asociamos individualmente cada elemento del layout con un elemento de la activity
        		nameLogin=(EditText)findViewById(R.id.nameLogin);
        		passwdLogin=(EditText)findViewById(R.id.passwdLogin);
        		btnLogin= (Button)findViewById(R.id.btnLogin);
        		setUserName(nameLogin.getText().toString());
        		setUserPasswd(passwdLogin.getText().toString());
        		
        		//if que comprueba que tanto en el Login como el password no esten vacios.
        		
        		if(passwdLogin.getText().toString().equals("") || nameLogin.getText().toString().equals(""))
        		{
        			Toast.makeText(LaFoscaMain.this, "Usuario i/o Password vac√≠os", Toast.LENGTH_LONG).show();
        		}else{
                    
        			//else generamos una estructura de JSONObject con otro objecto JSONObject dentro
        			//y llamamos a la funcion HttpAsync.
        			
        			JSONObject jReg = new JSONObject();
                    JSONObject jUser = new JSONObject();
                    try {
                    	
                    	jUser.put("username",getUserName());
                    	jUser.put("password",getUserPasswd());
        		        jReg.put("user", jUser);
        		        
        		        //clase que extendie AsyncTask
        		        HttpAsync asyncTask = new HttpAsync(LaFoscaMain.this,Constants.LOG_IN_OPT);  
                        
        		        //creamos una interficie para el paso de resultados desde HttpAsync a aqui
        		        asyncTask.setOnResultListener(asynResult);  
        		        
        		        //ejecutamos con el paso del JSONObject como parametro
                        asyncTask.execute(jReg);
                        
                    }  catch (JSONException e) {
     				   e.printStackTrace();
                    }
    			
        		}
  
             break;
             //codigo asociado al TextView del registro de nuevo usuario   
            case R.id.link_to_register:
                
            	//Llamada a la activity RegisterActivity.
            	
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent,REGISTER);
                
            	
            break;
        }
 
    }

	/**
	  * Override de la funcion onActivityResult
	  * Esto funcion recibe los datos de la activity RegisterActivity cuando finaliza
	  * Recibe los datos mediante un Intent, y un codigo de resultado.
	  * Si se ha introducido correctamente los datos, llamamos a la funcion HttpAsync y se realiza el registro
	  * Si se ha cancelado el proceso de registro se devuelve un mensaje por pantalla 
	  * @author sergio
	  * @return null
	  * @throws JSONException
	  */
    
    
    
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
		        
            	//contiene los datos en formato JSONObject para realizar el proceso de registro
            	jReg.put("user", jUser);
		        
		        //llamada a la funcion HttpAsync para efectuar el registro.
		        
		        
		        new HttpAsync(LaFoscaMain.this,Constants.REG_OPT).execute(jReg);
            } catch (JSONException e) {
            	   Log.e("Error JSON Register",null);
				   e.printStackTrace();
            }
		    
            

        }
    }       
   
    
	/**
	  * Funcion que chequea si el dispositivo tiene internet o no.
	  * @return true en caso positivo
	  * 
	  */
    
    
    public boolean isConnected(){
    	ConnectivityManager con = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = con.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) 
            return true;
        else
            return false;
    }
    
	/**
	  * Generacion de la Interface con la funcion HttpAsync. 
	  * Dos funciones onResult y onStateResult.
	  * En este caso unicamente utilizaremos la funcion onResult.
	  * 
	  * @author sergio
	  * 
	  */   
    
    OnAsyncResult asynResult = new OnAsyncResult() {  

    /**
   	  * Funcion onResult.
   	  * Recibimos los datos posteriores al Login y ejecutamos la activity OptionsActivity
   	  * 
   	  * 
   	  * @author sergio
   	  * @see  com.example.calapalamos.libraryHttpAsync#logIn
   	  * @params resultCode: sin uso en esta activity
   	  * @params weather: sin uso en esta activity
   	  * @params j: recibimos un JSONObject con el Authentication Token y el estado de la playa
   	  * <pre>
      * {@code     {    
      *                "AuthToken":"dasdasdasdad",
   	  *                "jresult"{
      *                          "state":"open",
      *                          "flag":0,
      *                          "happiness":89,
      *                          "dirtiness":42
      *                          "kids":[{"name":"Joan Torrefarrera","age":12},
      *                                  {"name":"Marc Robira","age":11}]
      *                }
      *            
   	  * }
      * </pre>
      * @throws Exception
   	  */ 
    	
    	
		@Override
		public void onResult(final boolean resultCode, final OpenWeather weather, final JSONObject j) {
			// TODO Auto-generated method stub
			try{
	        	   Log.d("MAIN onResult",j.toString());
	        	   setAuthToken(j.getString("AuthToken"));
	        	   
	        	   //creacion del Inten para el paso de datos a la activity OptionsActivity
	        	   
	        	   Intent intent = new Intent(LaFoscaMain.this, OptionsActivity.class);
				   intent.putExtra("init_cond", true);
				   intent.putExtra("username", getUserName());
				   intent.putExtra("AuthToken", getAuthToken());
				   intent.putExtra("state", j.getJSONObject("jresult").toString());
				   
				   //creacion de la ACtivity
				   startActivity(intent);
	           }catch(Exception e) {
		          Log.d("JSON onResult", e.getLocalizedMessage());
	           }
		}

	    /**
	   	  * Funcion onStateResult. En Esta Activity no tiene uso
	      * 
	   	  */ 

		@Override
		public void onStateResult(boolean resultCode, int i, final JSONObject j) {
		   
		}  
    };
 
    /**
  	  * Creacion de las diferentes opciones de menu en la navigation bar.
  	  * Asociado al menu la_fosca_main
  	  * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
      * 
  	  */ 
    
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.la_fosca_main, menu);
		return true;
	}

    /**
 	  * Definicion de la funcionalidad de los diferentes elementos del menu.
 	  * En esta activity unicamente tenemos un boton, que llama a un AlertDialog.
 	  * Este AlertDialog nos muestra la informacion sobre la compañia (tlf,email y direccion)
 	  * 
 	  * @autor sergio
 	  * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
      * 
 	  */ 	
	
	
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


