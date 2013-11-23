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
					Log.d("PUTFUNCT",getAuthToken());
				} catch (JSONException e) {
					
					e.printStackTrace();
				}
	        	result = putFunct(j[0]); 
	            Log.d("change state ",result);
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

		 Log.d("resultado postExecute",res);
		 
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
                     // TODO Auto-generated catch block
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
						// TODO Auto-generated catch block
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
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
	 
	 /*
	  * generamos una interface para el paso de datos
	  */
	   
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
	     * @param j
	     * @return
	     */
	    public String logIn(JSONObject j){
	    	String tmp = "";
	    	InputStream inputStream = null;
	    	
	     try {
	        	
	    	HttpClient client = new DefaultHttpClient();
	    	HttpGet httpGet;	 
	    	httpGet = new HttpGet(Constants.url+Constants.SUFIX_LOGIN);
    		String authUser = j.getJSONObject("user").getString("username").toString();
	        String authPass = j.getJSONObject("user").getString("password").toString();    
	        UsernamePasswordCredentials credentials =  new UsernamePasswordCredentials(authUser, authPass);
	        BasicScheme scheme = new BasicScheme();
	        httpGet.addHeader(scheme.authenticate(credentials, httpGet));
    	    
	        httpGet.setHeader("Accept", "application/json");
	        httpGet.setHeader("Content-type", "application/json");
	        // 8. Execute get request to the given URL
	        HttpResponse httpResponse = client.execute(httpGet);
	        StatusLine content = httpResponse.getStatusLine();
	        Log.d("getFunc CONTENT",content.toString());
	        // 9. receive response as inputStream
	        inputStream = httpResponse.getEntity().getContent();
	        if(!content.toString().equals(Constants.LOG_IN_FAILED))
    		{
	        	
	        	String resLogIn = manageInputStream(inputStream,content);
	        	Log.d("resLogIn",resLogIn);
	        	JSONObject jlog = new JSONObject(resLogIn);
	        	setAuthToken(jlog.getString("authentication_token"));
	        	Log.d("temp",getAuthToken());
	        	String temp = getState();
	        	JSONObject jState = new JSONObject(temp);
                tmp = jState.toString();
    	        
    		}
    		else
    		{
    			tmp = content.toString();
    		}
	    } catch (Exception e) {
	    	 Log.d("InputStream", e.getLocalizedMessage());
	    }   
	    	
	     return tmp;
}	    

	    /**
	     * @return
	     */
	    public String getState(){
	    	String tmp = "";
	    	
	    	
    	    try {
    	    	//getState
    	    	HttpClient client = new DefaultHttpClient();
    	    	HttpGet httpGet = new HttpGet(Constants.url+Constants.SUFIX_GET_STATE);
        		httpGet.setHeader("Authorization", "Token token="+ getAuthToken());
    	    	Log.d("AUTH",getAuthToken());
    	    	httpGet.setHeader("Accept", "application/json");
    	        httpGet.setHeader("Content-type", "application/json");
    	        // 8. Execute get request to the given URL
    	        HttpResponse httpResponse = client.execute(httpGet);
    	        StatusLine content = httpResponse.getStatusLine();
    	        Log.d("getFunc CONTENTO",content.toString());
    	        // 9. receive response as inputStream
    	        InputStream inputStream = httpResponse.getEntity().getContent();
    	        
    			if(content.toString().equals(Constants.OK_200))
        		{
        			Log.d("getFunc GET STATE",content.toString());
        			try {
    					tmp = manageInputStream(inputStream,content);
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
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
		 * @param j
		 * @return
		 */
		public String postFunct(JSONObject j){
	 		 String tmp = "";
		     try {
		        
		    	HttpClient client = new DefaultHttpClient();
		    	HttpPost httpPost;
		    	
		    	if(getOption()==Constants.REG_OPT)
		    	{
		    		httpPost = new HttpPost(Constants.url+Constants.SUFIX_REGISTER);
		    		String json = "";
			        
			        json = j.toString();
			        
			        StringEntity se = new StringEntity(json);
			        // 6. set httpPost Entity
			        httpPost.setEntity(se);
			        
		    	}else{
		    		
		    		if(getOption()==Constants.CLEAN_OPT)
		    		{
		    			httpPost = new HttpPost(Constants.url+Constants.SUFIX_POST_CLEAN);
		    		}else{
		    			httpPost = new HttpPost(Constants.url+Constants.SUFIX_POST_BALLS);
		    		}
		    		
		    		httpPost.setHeader("Authorization", "Token token="+ j.getString("Auth"));
		    	}

		        // 7. Set some headers to inform server about the type of the content   
		        httpPost.setHeader("Accept", "application/json");
		        httpPost.setHeader("Content-type", "application/json");

		        // 8. Execute POST request to the given URL
		        HttpResponse httpResponse = client.execute(httpPost);
		        StatusLine content = httpResponse.getStatusLine();
		        Log.d("REG content",content.toString());
		        // 10. convert inputstream to string
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
		        		tmp = "Did not work! 2";
		        	}		        	
		        }

		    } catch (Exception e) {
		            Log.d("InputStream", e.getLocalizedMessage());
		    }        
		        
		    return tmp;
		 }

		 /**
		 * @param j
		 * @return
		 */
		public String putFunct(JSONObject j){
		    	String tmp = "";
		    	
		        try {
		        	Log.d("putFunct",""+getAuthToken());
		        	
		        	
		        	HttpClient client = new DefaultHttpClient();
		        	HttpPut httpPut;
		        	
		        	if(getOption()==Constants.CHANGE_STATE_OPT)
		        	{
		        		if(j.getString("state").equals("open"))
		        	    {
		             		httpPut = new HttpPut(Constants.url+Constants.SUFIX_PUT_CLOSE);
		             	}else{
		        	    	httpPut = new HttpPut(Constants.url+Constants.SUFIX_PUT_OPEN);
		             	}
		        	}else{ /*if(getOption().equals(Constants.CHANGE_STATE_FLAG)){
		        		
		        		// TODO temporalmente*/ 
		        		httpPut = new HttpPut(Constants.url+Constants.SUFIX_PUT_FLAG);
		        		String json = "";
		 		        
		 		        json = j.getString("flag_j");
		 		        Log.d("json",j.getJSONObject("flag_j").getString("flag"));
		 		        //Log.d("json",getAuthToken());
		 		        StringEntity se = new StringEntity(json);
		 		        
		 		        // 6. set httpPost Entity
		 		        httpPut.setEntity(se);
    			        		
		        	}  	
		        	
		        	httpPut.setHeader("Authorization", "Token token="+ getAuthToken());
			        httpPut.setHeader("Accept", "application/json");
			        httpPut.setHeader("Content-type", "application/json");
			        
			        
			        // 8. Execute get request to the given URL
			        HttpResponse httpResponse = client.execute(httpPut);
			        //Log.d("PUT MANAGE",httpResponse.getEntity());
			        // 9. receive response as inputStream
			        //inputStream = httpResponse.getEntity().getContent();

			        StatusLine content = httpResponse.getStatusLine();
		        	Log.d("CONTENT PUT",content.toString());
		        	
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
	     * @return
	     */
	    public String getWeather(){
	    	String tmp = "";
	    	
	    	
    	    try {
    	    	//getState
    	    	HttpClient client = new DefaultHttpClient();
    	    	HttpGet httpGet = new HttpGet(Constants.weather_url);
        		
    	    	Log.d("weather","weather");
    	    	httpGet.setHeader("Accept", "application/json");
    	        httpGet.setHeader("Content-type", "application/json");
    	        // 8. Execute get request to the given URL
    	        HttpResponse httpResponse = client.execute(httpGet);
    	        StatusLine content = httpResponse.getStatusLine();
    	        Log.d("GET Weather",content.toString());
    	        if(content.toString().equals(Constants.OK_200)){
    	        	//  9. receive response as inputStream
    	        	InputStream inputStream = httpResponse.getEntity().getContent();
    	            String temp = manageInputStream(inputStream,content);
    	            
    	        	
    	        	JSONObject jtmp=new JSONObject(temp);
    	        	Log.d("GET Weather jtmp",jtmp.toString());
    	        	String code = jtmp.getJSONArray("weather").getJSONObject(0).getString("icon");
                    
                    
    	        	//
    	        	//get icon
    	        	//
    	        	
    	            InputStream is = null;
    	            try {
    	            	//falta el code
    	            	httpGet = new HttpGet(Constants.weather_img_url+code);
    	            	Log.d("weather img","img");
    	    	    	httpGet.setHeader("Accept", "application/json");
    	    	        httpGet.setHeader("Content-type", "application/json");
    	    	        httpResponse = client.execute(httpGet);
    	    	        content = httpResponse.getStatusLine();
    	    	        
    	    	        if(content.toString().equals(Constants.OK_200)){
    	    	        	Log.d("GET Weather",content.toString());
    	    	        
    	    	        	is = httpResponse.getEntity().getContent();
    	    	        
    	    	        	Bitmap imageBitmap = BitmapFactory.decodeStream(new BufferedInputStream(is));
    	    	        	
    	    	        	op = new OpenWeather(jtmp);
    	    	        	
    	    	        	op.setIcon(imageBitmap);
    	    	        	
    	    	        	tmp = content.toString();
    	    	        }
    	    	        else
    	    	        {
    	    	        	tmp = content.toString();
    	    	        	Log.d("ERROR",tmp);
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
	     * @param inputStream
	     * @param content
	     * @return
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
