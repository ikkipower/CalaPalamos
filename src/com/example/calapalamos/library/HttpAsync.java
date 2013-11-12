package com.example.calapalamos.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class HttpAsync extends AsyncTask<JSONObject, Void, String>{

	private ProgressDialog pDialog;
	private Context context;
    private String option;
	private String AuthToken;

    /*interface data LaFoscaMain*/
    OnAsyncResult onAsyncResult;  
    public void setOnResultListener(OnAsyncResult onAsyncResult) {  
       if (onAsyncResult != null) {  
          this.onAsyncResult = onAsyncResult;  
       }  
    }
    
    
 
    
	public HttpAsync(Context cont, String opt) {
		// TODO Auto-generated constructor stub
		   this.context = cont;
		   this.option = opt;
		   this.AuthToken="";
	}

	public String getOption(){
		return this.option;
	}
	
	public void setOption(String opt){
		this.option = opt;
	}
	
	
	public Context getContext(){
		return this.context;
	}
	
	public void setContext(Context cont){
		this.context = cont;
	}
	
	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Sending User...");
        pDialog.show();
        
	}
	
	@Override
    protected String doInBackground(JSONObject... j) {
        
		String result = "";
		
		if(getOption().equals(Constants.REG_OPT)){
			result = regFunct(j[0]);
	    }else if(getOption().equals(Constants.LOG_IN_OPT)){
        	result = logInFunct(j[0]);
        }else if(getOption().equals(Constants.GSTATE_OPT)){
        	Log.d("gStateFunct","before ");
        	result = gStateFunct(j[0]);
        }
		pDialog.dismiss();
        return result;
	
	}

	
	
	 @Override
     protected void onPostExecute(String res) {
		 Toast.makeText(getContext(), "Data! sended"+res, Toast.LENGTH_LONG).show();
		 Log.d("resultado postExecute",res);
	 }
	 
	 /*
	  * generamos una interface para el paso de datos hac’a LaFoscaMain
	  */
	   
	    public interface OnAsyncResult {  
	    	public abstract void onResult(boolean resultCode, String message);
	    	public abstract void onStateResult(boolean resultCode, JSONObject j);
	    }  
	        

		
		 public String regFunct(JSONObject j){
			 InputStream inputStream = null;
	 		 String tmp = "";
		     try {
		        HttpClient client = new DefaultHttpClient();

		        HttpPost httpPost = new HttpPost(Constants.url+Constants.SUFIX_REGISTER);

		        String json = "";
		        
		        json = j.toString();
		        
		        StringEntity se = new StringEntity(json);
		        
		        // 6. set httpPost Entity
		        httpPost.setEntity(se);

		        // 7. Set some headers to inform server about the type of the content   
		        httpPost.setHeader("Accept", "application/json");
		        httpPost.setHeader("Content-type", "application/json");

		        // 8. Execute POST request to the given URL
		        HttpResponse httpResponse = client.execute(httpPost);

		        // 9. receive response as inputStream
		        inputStream = httpResponse.getEntity().getContent();

		        // 10. convert inputstream to string
		        if(inputStream != null)
		        	{
		        		tmp = manageInputStream(inputStream);
		        	    Log.d("RESULTADO "+getOption(),tmp);
		            }else
		        		tmp = "Did not work!";

		        } catch (Exception e) {
		            Log.d("InputStream", e.getLocalizedMessage());
		        }        
		        
		    return tmp;
		 }

	    public String gStateFunct(JSONObject j){
	    	String tmp = "";
	    	InputStream inputStream = null;
	    	
	        try {
	        	HttpClient client = new DefaultHttpClient();
	        	HttpGet httpGet = new HttpGet(Constants.url+Constants.SUFIX_GET_STATE);
	        	Log.d("gstatefunct url",Constants.url+Constants.SUFIX_GET_STATE);
		        httpGet.setHeader("Accept", "application/json");
		        httpGet.setHeader("Content-type", "application/json");
		        Log.d("gstatefunct token",j.toString());
		        httpGet.setHeader("Authorization", "Token token="+ j.getJSONObject("user").getString("authenticationToken"));
		        // 8. Execute get request to the given URL
		        HttpResponse httpResponse = client.execute(httpGet);

		        // 9. receive response as inputStream
		        inputStream = httpResponse.getEntity().getContent();
		        
		         
		        // 11. manage inputstream to string
		        if(inputStream != null)
		        {
		                tmp = manageInputStream(inputStream);

		        }else
		        		tmp = "Did not work!";
	        } catch (Exception e) {
		             Log.d("gStateFunct InputStream", e.getLocalizedMessage());
		    }   
		    	
		    return tmp;

		    
    	
	    }
	    
	    public String logInFunct(JSONObject j){
	    	
	    	String tmp = "";
	    	InputStream inputStream = null;
	    	
	        try {
	        	
	    	HttpClient client = new DefaultHttpClient();
	    	HttpGet httpGet = new HttpGet(Constants.url+Constants.SUFIX_LOGIN);         
	    	String authUser = j.getJSONObject("user").getString("username").toString();
	        String authPass = j.getJSONObject("user").getString("password").toString();    
	        UsernamePasswordCredentials credentials =  new UsernamePasswordCredentials(authUser, authPass);
	        BasicScheme scheme = new BasicScheme();
	        httpGet.addHeader(scheme.authenticate(credentials, httpGet));
	        httpGet.setHeader("Accept", "application/json");
	        httpGet.setHeader("Content-type", "application/json");
	        // 8. Execute get request to the given URL
	        HttpResponse httpResponse = client.execute(httpGet);

	        // 9. receive response as inputStream
	        inputStream = httpResponse.getEntity().getContent();
	        
	         
	        // 11. manage inputstream to string
	        if(inputStream != null)
	        {
	                tmp = manageInputStream(inputStream);

	        }else
	        		tmp = "Did not work!";
	    	
	            
	        } catch (Exception e) {
	             Log.d("InputStream", e.getLocalizedMessage());
	        }   
	    	
	        return tmp;

	    }
	   
	    
	    private String manageInputStream(InputStream inputStream) throws IOException{
	       BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
	       String line = "";
	       String result = "";
	       

	       if(getOption().equals(Constants.LOG_IN_OPT)){
		       StringBuilder responseStrBuilder = new StringBuilder();
	    	   if(!result.equals(Constants.ERROR_LOGIN)){
	    		   while((line = bufferedReader.readLine()) != null){
	    			   result += line;
	    			   responseStrBuilder.append(line);
	    		   }
	    		   try{
	    			   JSONObject jresult = new JSONObject(responseStrBuilder.toString());
	    			   this.AuthToken = jresult.getString("authentication_token");
	    			   onAsyncResult.onResult(true,this.AuthToken);
	    		   }catch(Exception e) {
	    			   Log.d("JSON InputStream", e.getLocalizedMessage());
	    		   }
	    	   }else{
	    		  onAsyncResult.onResult(false,"Failed"); 
	    	   }
	       }else{
	    	   if(getOption().equals(Constants.GSTATE_OPT)){
	    		
	    		   StringBuilder responseStrBuilder = new StringBuilder();
	    		   while((line = bufferedReader.readLine()) != null){
	    			   result += line;
	    			   responseStrBuilder.append(line);
	    		   }
	    		   
	    		   try{
	    			   JSONObject jresult = new JSONObject(responseStrBuilder.toString());
	    			   //Log.d("GGGjresult",""+jresult.getJSONArray("kids").getJSONObject(0).getInt("age"));
	    			   //Log.d("jresult",""+jresult.getJSONArray("kids").getJSONObject(0).getString("name"));
	    			   onAsyncResult.onStateResult(true, jresult);
	    			   //DatabaseHandler db = new DatabaseHandler(getContext());
	    			   //http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
	    			   //http://myandroidsolutions.blogspot.com.es/2013/08/android-listview-with-searchview.html
	    			   
	    		   }catch(Exception e) {
	    			   Log.d("JSON InputStream", e.getLocalizedMessage());
	    		   }
	    		   
	    	   }else{
	    	   
		           while((line = bufferedReader.readLine()) != null)
		           {	   
		             result += line;
		           }
	    	   }
	    	   
	       }
	       inputStream.close();
	 
	       return result;

	   } 
	 
}
