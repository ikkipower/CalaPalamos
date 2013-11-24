package com.example.calapalamos.library;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.calapalamos.Constants;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class HttpAsync extends AsyncTask<JSONObject, Void, String>{

	private ProgressDialog pDialog;
	private Context context;
    private int option = 0;
	private String AuthToken="";
    private OpenWeather op;
    /**
     * Declaracion de la interface entre esta clase y las Activity que la llaman
     * 
     * @author sergio
     */
    OnAsyncResult onAsyncResult;  
    public void setOnResultListener(OnAsyncResult onAsyncResult) {  
       if (onAsyncResult != null) {  
          this.onAsyncResult = onAsyncResult;  
       }  
    }
    
    
	public HttpAsync(Context cont, int opt) {
		   this.context = cont;
		   this.option = opt;
		   this.AuthToken="";
	}

	public int getOption(){
		return this.option;
	}
	
	public void setOption(int opt){
		this.option = opt;
	}

	public OpenWeather getOp(){
		return this.op;
	}
	
	public void setOp(OpenWeather op){
		this.op = op;
	}
	
	public String getAuthToken(){
		return this.AuthToken;
	}
	
	public void setAuthToken(String a){
		this.AuthToken = a;
	}
	
	public Context getContext(){
		return this.context;
	}
	
	public void setContext(Context cont){
		this.context = cont;
	}
	
	/**
	  * Funcion que se ejecuta antes de la Task.
	  * Abrimos un progress dialos que se mostrara mientras se ejecuta la task
	  * @see android.os.AsyncTask#onPreExecute()
	  */
	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(!(getOption()==Constants.GSTATE_OPT)){
        	pDialog = new ProgressDialog(getContext());
        	pDialog.setMessage("Processing...");
        	pDialog.show();
        }

        
	}
	
	/** 
	  * Contiene un switch con las diferentes opciones que se ejecutaran
	  * @author sergio  
	  * @see android.os.AsyncTask#doInBackground(Params[])
	  */
	@Override
    protected String doInBackground(JSONObject... j) {
        
		String result = "";
		
		switch(getOption()) {
		      //opcion registrar
		 case Constants.REG_OPT: 
			 result = postFunct(j[0]);
		     break;
		      //opcion logIn
		 case Constants.LOG_IN_OPT: 
			 result = logIn(j[0]);
		     break;
		     //opcion get State
		 case Constants.GSTATE_OPT: 
			 try {
				    //guardamos el authentication token
					setAuthToken(j[0].getJSONObject("user").getString("authenticationToken"));
				} catch (JSONException e) {

					e.printStackTrace();
				}
	        	result = getState(); 
		     break;
		     //opcion change State
		 case Constants.CHANGE_STATE_OPT: 
			 try {
					setAuthToken(j[0].getString("Auth"));
				} catch (JSONException e) {
					
					e.printStackTrace();
				}
	        	result = putFunct(j[0]); 
		     break;
		     //opcion change flag
		 case Constants.CHANGE_STATE_FLAG:
			 try {
			    	//guardamos el authentication token
	        		setAuthToken(j[0].getString("Auth"));
	        		result = putFunct(j[0]);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			break;
			//opcion lanzar balones	
		 case Constants.THROW_OPT: 
			 result = postFunct(j[0]); 
		     break;
		     //opcion limpiar playa
		 case Constants.CLEAN_OPT:
			 result = postFunct(j[0]);
			 break;
			 //opcion get weather
		 case Constants.WEATHER_OPT:
			 result = getWeather();
			 break;
		 
		 default: 
		     break;
		 }
		
        return result;
	
	}
	
	
    /** 
     * Funcion que se ejecuta una vez ha finalizado la AsynTask. Desde aqui se llama a las 
     * funciones que hacen de interfaz con la activity que la ha llamado
	 * 
	 * @author sergio
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 * @throws JSONException
	 */
	@Override
     protected void onPostExecute(String res) {
		 
		 switch(getOption()){
		 case Constants.REG_OPT:
			 pDialog.dismiss();
			 //sacamos por pantalla que se ha registrado el nuevo usuario
			 Toast.makeText(getContext(), "Nuevo Usuario Registrado", Toast.LENGTH_LONG).show();
			 
			 break;
		 case Constants.GSTATE_OPT:
			 JSONObject jresult;
             try {
            	     //generamos un JSONObject y lo pasamos mediante el onStateResult
                     jresult = new JSONObject(res);
                     onAsyncResult.onStateResult(true, 2,jresult);
             } catch (JSONException e) {
                     
                     e.printStackTrace();
             }
			 break;
		 case Constants.THROW_OPT:
			 pDialog.dismiss();
			 if(res.equals(Constants.CREATED_201))
			 {
				 //mensaje de exito, en la peticion de balones lanzados
				 Toast.makeText(getContext(), "Balones Lanzados!", Toast.LENGTH_LONG).show();
			 }else{
				 //mensaje de que no se han lanzado los balones
				 Toast.makeText(getContext(), res, Toast.LENGTH_LONG).show(); 
			 }
			 break;
		 case Constants.WEATHER_OPT:
			 pDialog.dismiss();
			 if(res.equals(Constants.OK_200)){
				//pasamos los datos del tiempo (getOp())
				onAsyncResult.onResult(true, getOp(), new JSONObject());
			 }else{
				Toast.makeText(getContext(), "Weather Forecast Failed "+res, Toast.LENGTH_LONG).show(); 
			 }
			 
			 break;
		 case Constants.CHANGE_STATE_FLAG:
			 pDialog.dismiss();
	     	 if(getOption()==Constants.CHANGE_STATE_FLAG){
	     			JSONObject jres=new JSONObject();
	     			if(res.equals("0") || res.equals("1") || res.equals("2")){
	     				try {
	     					jres.put("flag", res);
	     					//generamos un objeto JSONObject y lo pasamos los datos
	     					//mediante la interface y la funcion onStateResult
	     					onAsyncResult.onStateResult(true,3,jres);
	     				} catch (JSONException e) {
	     					
	     					e.printStackTrace();
	     				}
	     			}
	     			
	     			
	     	 }
	     	 break;
		 case Constants.CHANGE_STATE_OPT:
			 pDialog.dismiss();
			 if(!res.equals("HTTP/1.1 401 Unauthorized")){

	        		JSONObject jresult1;
					try {
						jresult1 = new JSONObject(res);
						//generamos un objeto JSONObject y lo pasamos los datos
     					//mediante la interface y la funcion onStateResult
						onAsyncResult.onStateResult(true, 4,jresult1);
					} catch (JSONException e) {
						
						e.printStackTrace();
					}
                 
	        }else{
	        	//estado no cambiado
	        	Toast.makeText(getContext(), res, Toast.LENGTH_LONG).show(); 
	        }
			 break;
		 case Constants.LOG_IN_OPT:
			 pDialog.dismiss();
			 
			 if(!res.equals(Constants.LOG_IN_FAILED)){
   				JSONObject jAuth;
   				JSONObject jresult1;
					try {
						jresult1 = new JSONObject(res);
						jAuth = new JSONObject();
						jAuth.put("AuthToken", this.AuthToken);
						jAuth.put("jresult", jresult1);
						//generamos un objeto JSONObject y lo pasamos los datos
     					//mediante la interface y la funcion onStateResult
						onAsyncResult.onResult(true, op, jAuth);
					} catch (JSONException e) {						e.printStackTrace();
					}
	    	   }else{
	    		 //si falla el login se muestra un alertDialog
	    		 AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		    	 builder.setTitle("Login Failed!").setMessage("Try Again!").setCancelable(false)
	                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int id) {
	                         dialog.cancel();
	                    }
	             });
	             AlertDialog alert = builder.create();
	             alert.show();
	    	   }
			 break;
		 default:
			 pDialog.dismiss();
			 break;
		 }
 
		 
	 }
	 
	   
	    /**
	     * Interface con la activity que lo llama
	     * @author i2131344
	     *
	     */
	    public interface OnAsyncResult {  
	    	public abstract void onResult(boolean resultCode, OpenWeather weather, JSONObject j);
	    	public abstract void onStateResult(boolean resultCode, int i, JSONObject j);

	    	
	    }  
	      
	    
	    /**
	     * Funcion logIn que tiene dos partes significativas la primera mandamos una 
	     * peticion de logIn para posteriormente hacer una peticion sobre el estado
	     * de la playa.
	     *  
	     * @author sergio 
	     * @param j
	     * @throws exception
	     */
	    public String logIn(JSONObject j){
	    	String tmp = "";
	    	InputStream inputStream = null;
	    	
	     try {
	       
	    	//creamos un HttpClient y un header de tipo hhtget para poder 
	        //enviar la peticion de un get con los datos de identificacion del user  
	    	HttpClient client = new DefaultHttpClient();
	    	HttpGet httpGet;	 
	    	//pasamos la url
	    	httpGet = new HttpGet(Constants.url+Constants.SUFIX_LOGIN);
    		String authUser = j.getJSONObject("user").getString("username").toString();
	        String authPass = j.getJSONObject("user").getString("password").toString(); 
	        //creamos un header para el hhtpget con el usuario y el pasword
	        UsernamePasswordCredentials credentials =  new UsernamePasswordCredentials(authUser, authPass);
	        BasicScheme scheme = new BasicScheme();
	        
	        //a–adimos al httpget
	        httpGet.addHeader(scheme.authenticate(credentials, httpGet));
    	    //definimos el tipo de datos que vamos a enviar/recibir
	        httpGet.setHeader("Accept", "application/json");
	        httpGet.setHeader("Content-type", "application/json");
	        
	        //ejecutamos la peticion y obtenemos una respuesta
	        HttpResponse httpResponse = client.execute(httpGet);
	        // precesamos la respuesta
	        StatusLine content = httpResponse.getStatusLine();
	        Log.d("getFunc CONTENT",content.toString());
	        
	        inputStream = httpResponse.getEntity().getContent();
	        //comprobamos que el resultado es correcto
	        if(!content.toString().equals(Constants.LOG_IN_FAILED))
    		{
	        	//Transformamos Inpustream (respuesta recibida) a String
	        	String resLogIn = manageInputStream(inputStream,content);
	        	//creamos un JSONObject
	        	JSONObject jlog = new JSONObject(resLogIn);
	        	//guardamos el authentication
	        	setAuthToken(jlog.getString("authentication_token"));
	        	
	        	//llamada a la funcion getState que nos devuelve un string
	        	//con el estado de la playa
	        	String temp = getState();
	        	
	        	JSONObject jState = new JSONObject(temp);
                tmp = jState.toString();
    	        
    		}
    		else
    		{
    			//si el logIn no se realiza correctamente devolvemos el mensaje
    			//que nos da el HTTP: HTTP/1.1 401 Unauthorized
    			tmp = content.toString();
    		}
	    } catch (Exception e) {
	    	 Log.d("InputStream", e.getLocalizedMessage());
	    }   
	    	
	     return tmp;
}	    

	    /**
	     * Clase que define la peticion de get State
	     * 
	     * @author sergio
	     * @throws Exception, IOException
	     */
	    public String getState(){
	    	String tmp = "";
	    	
	    	
    	    try {
    	    	//creamos un HttpClient y un header de tipo hhtget para poder 
    	        //enviar la peticion de un get con los datos de identificacion del user
    	    	HttpClient client = new DefaultHttpClient();
    	    	//pasamos la url
    	    	HttpGet httpGet = new HttpGet(Constants.url+Constants.SUFIX_GET_STATE);
        		//al httpget se le incluye un header con el authentication token
    	    	httpGet.setHeader("Authorization", "Token token="+ getAuthToken());
    	    	//definimos el tipo de datos que vamos a enviar/recibir
    	    	httpGet.setHeader("Accept", "application/json");
    	        httpGet.setHeader("Content-type", "application/json");
    	        //Ejecutamos la peticion y recibimos una respuesta
    	        HttpResponse httpResponse = client.execute(httpGet);
    	        //procesamos la respuesta
    	        StatusLine content = httpResponse.getStatusLine();
    	        InputStream inputStream = httpResponse.getEntity().getContent();
    	        //comprobamos si los datos son correctos
    			if(content.toString().equals(Constants.OK_200))
        		{

        			try {
        				//Transformamos Inpustream (respuesta recibida) a String
    					tmp = manageInputStream(inputStream,content);
    				} catch (IOException e) {
    				
    					e.printStackTrace();
    				}
        		}
        		else
        		{
        			tmp = "Failed";
        		}
    	    } catch (Exception e) {
   	    	 Log.d("InputStream", e.getLocalizedMessage());
   	        }   
	        
	        
	        return tmp;
	    }
	    
		
	   /**
	     * Funcion que implementa las peticiones de registrarse, lanzar balones nivea
	     * y limpiar la playa
	     * @param j
		 * @throws Exception
		 */
		public String postFunct(JSONObject j){
	 		 String tmp = "";
		     try {
		        
		    	
		    	//creamos un HttpClient y un header de tipo hhtpost para poder 
	    	    //enviar la peticion dependiendo de la opcion 
	    	    HttpClient client = new DefaultHttpClient();
	    	    	
		    	HttpPost httpPost;
		    	
		    	//seleccion de la opcin y paso de la url
		    	if(getOption()==Constants.REG_OPT)
		    	{
		    		httpPost = new HttpPost(Constants.url+Constants.SUFIX_REGISTER);
		    		String json = "";
			        
			        json = j.toString();
			        
			        //se transforma el JSONOBject con los datos del registro
			        //al formato necesario para ser enviado
			        StringEntity se = new StringEntity(json);
			        // insertamos los datos en el httpPost
			        httpPost.setEntity(se);
			        
		    	}else{
		    		
		    		if(getOption()==Constants.CLEAN_OPT)
		    		{
		    			httpPost = new HttpPost(Constants.url+Constants.SUFIX_POST_CLEAN);
		    		}else{
		    			httpPost = new HttpPost(Constants.url+Constants.SUFIX_POST_BALLS);
		    		}
		    		
		    		//insertamos el header
		    		httpPost.setHeader("Authorization", "Token token="+ j.getString("Auth"));
		    	}

		        // definimos el formato de datos de los datos   
		        httpPost.setHeader("Accept", "application/json");
		        httpPost.setHeader("Content-type", "application/json");

		        // Ejecutamos y recibimos los datos
		        HttpResponse httpResponse = client.execute(httpPost);
		        StatusLine content = httpResponse.getStatusLine();
		        
		        ///comprobamos si los datos son correctos para cada una de las opciones
		        if(getOption()==Constants.REG_OPT){
		        
		        	if(content.toString().equals(Constants.CREATED_201))
		        	{
		        		tmp = content.toString();
		        		Log.d("RESULTADO "+getOption(),tmp);
		        	}else{
		        		tmp = "Did not work!";
		        	}
		        	
		        }else{
		        	
		        	if(content.toString().equals(Constants.CREATED_201))
		        	{
		        		tmp = content.toString();
		        		Log.d("RESULTADO THROW "+getOption(),tmp);
		        	}else{
		        		tmp = "Did not work!";
		        	}		        	
		        }

		    } catch (Exception e) {
		            Log.d("InputStream", e.getLocalizedMessage());
		    }        
		        
		    return tmp;
		 }

		 /**
		  * Funcion que implementa las opciones de cambiar de estado y cambiar la bandera
		  * @param j
	      * @throws Exception
		  */
		public String putFunct(JSONObject j){
		    	String tmp = "";
		    	
		        try {
		        	
		        	//creamos un HttpClient y un header de tipo hhtpost para poder 
		    	    //enviar la peticion dependiendo de la opcion 
		        	HttpClient client = new DefaultHttpClient();
		        	HttpPut httpPut;
		        	
		        	//seleccion de la opcion (cambiar estado/cambiar flag) y creacion del httpPut
		        	if(getOption()==Constants.CHANGE_STATE_OPT)
		        	{
		        		if(j.getString("state").equals("open"))
		        	    {
		             		httpPut = new HttpPut(Constants.url+Constants.SUFIX_PUT_CLOSE);
		             	}else{
		        	    	httpPut = new HttpPut(Constants.url+Constants.SUFIX_PUT_OPEN);
		             	}
		        	}else{  
		        		httpPut = new HttpPut(Constants.url+Constants.SUFIX_PUT_FLAG);
		        		String json = "";
		 		        
		 		        json = j.getString("flag_j");
		 		        Log.d("json",j.getJSONObject("flag_j").getString("flag"));
		 		        //transformamos los datos (JSONObject) al formato requerido
		 		        StringEntity se = new StringEntity(json);
		 		        
		 		        //Insertamos los datos en el Httput
		 		        httpPut.setEntity(se);
    			        		
		        	}  	
		        	//Definicion del tipo de datos e inclusion del authentication token
		        	httpPut.setHeader("Authorization", "Token token="+ getAuthToken());
			        httpPut.setHeader("Accept", "application/json");
			        httpPut.setHeader("Content-type", "application/json");
			        
			        
			        // Ejecucion de la peticion y respuesta
			        HttpResponse httpResponse = client.execute(httpPut);
			        
			        StatusLine content = httpResponse.getStatusLine();
		        	
			        //comprobacion de que el resultado es el correcto para
			        //las diferentes opciones
		        	if(getOption()==Constants.CHANGE_STATE_OPT)
		        	{
						
		        		if(content.toString().equals(Constants.OK_200)){
		        			if(j.getString("state").equals("open")){
		        				tmp = getState();
		        			}else{
		        				tmp = getState();
		        			}
		        			
		        		}else{
		        			tmp = content.toString();
		        		}
		        		
		        	}else{
		        		if(content.toString().equals(Constants.FLAG_OK)){
                            tmp = j.getJSONObject("flag_j").getString("flag");
                        }else{
                            tmp = content.toString();
                        }
		        		
		        	}	        		    
			        
			       
		        } catch (Exception e) {
			             Log.d("putFunct InputStream", e.getLocalizedMessage());
			    }  
			    	
			    return tmp;	    
	    	
		    }		 
		
	    /**
	     * Funcion que optiene la predicion meteorologica de OpenWeatherMap
	     * Peticion de tipo get para la recepcion de datos, proceso similar a las otras
	     * opciones realizadas. Coge el dato de una estacion meteorolica cercana a las
	     * coordenadas de la cala. Generalmente es de Palamos, pero a veces puede ser 
	     * Calonge u otros sitios cercanos, dependiendo de donde nos env’e los datos.
	     * 
	     * @author sergio
	     * @throws Exception, Throwable
	     */
	    public String getWeather(){
	    	String tmp = "";
	    	
	    	
    	    try {
    	    	//creacion del HttpCient y HttpGet con la url de la Api de openWeather
    	    	HttpClient client = new DefaultHttpClient();
    	    	HttpGet httpGet = new HttpGet(Constants.weather_url);
        		
    	    	//definicion del tipo de datos (no requiere authentication token)
    	    	httpGet.setHeader("Accept", "application/json");
    	        httpGet.setHeader("Content-type", "application/json");
    	        //Ejecucion y obtencion de la respuesta
    	        HttpResponse httpResponse = client.execute(httpGet);
    	        StatusLine content = httpResponse.getStatusLine();
    	        //comprobacion de que la respuesta ha sido correcta
    	        if(content.toString().equals(Constants.OK_200)){
    	        	//Procesamos los datos de InputStream a String
    	        	InputStream inputStream = httpResponse.getEntity().getContent();
    	            String temp = manageInputStream(inputStream,content);
    	            
    	        	//pasamos los datos a JSONObject
    	        	JSONObject jtmp=new JSONObject(temp);
                    //cogemos el codigo del icono de la predicion del tiempo
    	        	String code = jtmp.getJSONArray("weather").getJSONObject(0).getString("icon");
                    
                    //
    	        	//realizamos una peticion para descargar el icono del tiempo
    	        	//
    	        	
    	            InputStream is = null;
    	            try {
    	            	//generamos un httpGet con la url para coger el icono + code
    	            	httpGet = new HttpGet(Constants.weather_img_url+code);
    	            	
    	            	//definicion del tipo de datos
    	    	    	httpGet.setHeader("Accept", "application/json");
    	    	        httpGet.setHeader("Content-type", "application/json");
    	    	        //Ejecucion y obtencion de la respuesta
    	    	        httpResponse = client.execute(httpGet);
    	    	        content = httpResponse.getStatusLine();
    	    	        //comprobamos la respuesta sea correcta
    	    	        if(content.toString().equals(Constants.OK_200)){
    	    	        
    	    	        	//recibimos los datos
    	    	        	is = httpResponse.getEntity().getContent();
    	    	            //procesamos al formato Bitmap
    	    	        	Bitmap imageBitmap = BitmapFactory.decodeStream(new BufferedInputStream(is));
    	    	        	//creamos un objecto del formato OpenWeather y les pasamos los datos
    	    	        	op = new OpenWeather(jtmp);
    	    	        	//insertamos el icono
    	    	        	op.setIcon(imageBitmap);
    	    	        	
    	    	        	tmp = content.toString();
    	    	        }
    	    	        else
    	    	        {
    	    	        	tmp = content.toString();
    	    	        	
    	    	        }	
    	            }
    	            catch(Throwable t) {
    	                t.printStackTrace();
    	            }
    	    
    	        	
    	        }else{
    	        	tmp = content.toString();
    	        }

    	    } catch (Exception e) {
   	    	 Log.d("InputStream", e.getLocalizedMessage());
   	        }   
	        
	        
	        return tmp;
	    	
	    }
		 
		 
	    /**
	     * Funcion que recibe un InputStream y devuelve un String
	     * 
	     * 
	     * @param inputStream
	     * @param content
	     * 
	     * @throws IOException
	     */
	    private String manageInputStream(InputStream inputStream, StatusLine content) throws IOException{
	       BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
	       String line = "";
	       String result = "";
	       

           while((line = bufferedReader.readLine()) != null)
           {	   
        	   result += line;
           }
	    	   

	       inputStream.close();
	 
	       return result;

	   } 
	 
}
