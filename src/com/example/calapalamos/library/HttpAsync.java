package com.example.calapalamos.library;

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
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.calapalamos.Constants;
import com.example.calapalamos.LaFoscaMain;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class HttpAsync extends AsyncTask<JSONObject, Void, String>{

	private ProgressDialog pDialog;
	private Context context;
    private String option;
	private String AuthToken;
    
    /*interface data*/
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
        if(!getOption().equals(Constants.GSTATE_OPT)){
        	pDialog = new ProgressDialog(getContext());
        	pDialog.setMessage("Processing...");
        	pDialog.show();
        }

        
	}
	
	@Override
    protected String doInBackground(JSONObject... j) {
        
		String result = "";
		String temp_res = "";
		
		if(getOption().equals(Constants.REG_OPT)){
			result = postFunct(j[0]);
	    }else if(getOption().equals(Constants.LOG_IN_OPT)){
        	//result = getFunct(j[0]);
	    	temp_res= getFunct(j[0],1); //LOG_IN_OPT

	    	if(!temp_res.equals(Constants.LOG_IN_FAILED)){
	    		
	    		JSONObject jReg = new JSONObject();
	            JSONObject jUser = new JSONObject();
	            
	            try {
	            	JSONObject jresult = new JSONObject(temp_res);
	            	jUser.put("username",jresult.getString("username"));
	            	this.AuthToken=jresult.getString(("authentication_token"));
	            	jUser.put("authenticationToken",this.AuthToken);
			        jReg.put("user", jUser);
			        Log.d("TEMP_RES",jReg.toString());
			        //setOption(Constants.GSTATE_OPT);
			        result = getFunct(jReg,0);
	            } catch (JSONException e) {
					// TODO Auto-generated catch block
	            	   
	            	   Log.e("Error JSON Register",null);
					   e.printStackTrace();
	            }
	    		  //result = temp_res;
	    	}else{
	    		Log.d("LOGIN","FAILED");
	    		result = temp_res;

	    	}
        }else if(getOption().equals(Constants.GSTATE_OPT)){
        	result = getFunct(j[0],1); //GSTATE_OPT
        	Log.d("GSTATE state",getOption().toString());
        }else if(getOption().equals(Constants.CHANGE_STATE_OPT)){
        	result = putFunct(j[0]); //CHANGE_STATE
            Log.d("change state ",j[0].toString());
        }else if(getOption().equals(Constants.CHANGE_STATE_FLAG)){
        	result = putFunct(j[0]); //CHANGE_FLAG
        	Log.d("change flag",j[0].toString());
        }else if(getOption().equals(Constants.THROW_OPT)){
        	result = postFunct(j[0]); //CHANGE_FLAG
        	Log.d("throw balls",getOption().toString());
        }else if(getOption().equals(Constants.CLEAN_OPT)){
        	result = postFunct(j[0]);
        }
		
		
        return result;
	
	}

	
	
	 @Override
     protected void onPostExecute(String res) {
		 Toast.makeText(getContext(), "Data! sended "+res, Toast.LENGTH_LONG).show();
		 Log.d("resultado postExecute",res);
		 
		 if(!getOption().equals(Constants.GSTATE_OPT)){
			 pDialog.dismiss();
	        }
		 
	     
	       
		 
		 
		 if(getOption().equals(Constants.CHANGE_STATE_OPT))
     	 {
	        	
	        	if(res.equals(Constants.OPEN_OK)){

	        		onAsyncResult.onResult(true,"open");
	        	}else{ 
	        		if(res.equals(Constants.CLOSE_OK)){
	        			onAsyncResult.onResult(true,"closed");
	        		}
	        	    else{

			        onAsyncResult.onResult(false,res);

		            }
		        }

     	 }else{ 
     		if(getOption().equals(Constants.CHANGE_STATE_FLAG)){
     			Log.d("postExecute FLAG",res);
        		if(res.equals("0") || res.equals("1") || res.equals("2")){
        			onAsyncResult.onResult(true,res);
        	 	
        		}else{
        			onAsyncResult.onResult(false,"flag_failed");
        		}
     		}else{
     			if(getOption().equals(Constants.GSTATE_OPT)){

					
					JSONObject jresult;
                    try {
                            jresult = new JSONObject(res);
                            onAsyncResult.onStateResult(true, jresult);
                    } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                    }
	    			   
     			}else{
     				if(getOption().equals(Constants.LOG_IN_OPT)){
     		    	   if(!res.equals(Constants.LOG_IN_FAILED)){
     	     				JSONObject jAuth;
     	     				JSONObject jresult;
     						try {
     							jresult = new JSONObject(res);
     							jAuth = new JSONObject();
     							jAuth.put("AuthToken", this.AuthToken);
     							jAuth.put("jresult", jresult);
     							onAsyncResult.onStateResult(true, jAuth);
     						} catch (JSONException e) {
     							// TODO Auto-generated catch block
     							e.printStackTrace();
     						}
     		    	   }else{

     		    		  AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
 				    	  builder.setTitle("Login Failed!").setMessage("Try Again!").setCancelable(false)
 			                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
 			                    public void onClick(DialogInterface dialog, int id) {
 			                         dialog.cancel();
 			                    }
 			             });
 			             AlertDialog alert = builder.create();
 			             alert.show();
     		    		   //onAsyncResult.onResult(false,"Failed"); 
     		    	   }
     		       }
     			}
     			
     		}
     		
     	 }
		 
	 }
	 
	 /*
	  * generamos una interface para el paso de datos
	  */
	   
	    public interface OnAsyncResult {  
	    	public abstract void onResult(boolean resultCode, String message);
	    	public abstract void onStateResult(boolean resultCode, JSONObject j);
	    }  
	        

		
		 public String postFunct(JSONObject j){
			 InputStream inputStream = null;
	 		 String tmp = "";
		     try {
		        
		    	HttpClient client = new DefaultHttpClient();
		    	HttpPost httpPost;
		    	
		    	if(getOption().toString().equals(Constants.REG_OPT))
		    	{
		    		httpPost = new HttpPost(Constants.url+Constants.SUFIX_REGISTER);
		    		String json = "";
			        
			        json = j.toString();
			        
			        StringEntity se = new StringEntity(json);
			        // 6. set httpPost Entity
			        httpPost.setEntity(se);
			        
		    	}else{
		    		
		    		if(getOption().toString().equals(Constants.CLEAN_OPT))
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
		        // 9. receive response as inputStream
		        inputStream = httpResponse.getEntity().getContent();

		        // 10. convert inputstream to string
		        if(getOption().toString().equals(Constants.REG_OPT)){
		        
		        	if(content.toString().equals(Constants.REG_OK))
		        	{
		        		tmp = content.toString();
		        		Log.d("RESULTADO "+getOption(),tmp);
		        	}else{
		        		tmp = "Did not work!";
		        	}
		        	
		        }else{
		        	
		        	if(content.toString().equals(Constants.THROW_CLEAN_OK))
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

		 public String putFunct(JSONObject j){
		    	String tmp = "";
		    	
		        try {
		        	Log.d("putFunct",""+j.getString("Auth"));
		        	
		        	
		        	HttpClient client = new DefaultHttpClient();
		        	HttpPut httpPut;
		        	
		        	if(getOption().equals(Constants.CHANGE_STATE_OPT))
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
		 		        
		 		        StringEntity se = new StringEntity(json);
		 		        
		 		        // 6. set httpPost Entity
		 		        httpPut.setEntity(se);

		        		
		        		
		        	}
		        	
		        	
		        	
		        	httpPut.setHeader("Authorization", "Token token="+ j.getString("Auth"));
			        httpPut.setHeader("Accept", "application/json");
			        httpPut.setHeader("Content-type", "application/json");
			        
			        
			        // 8. Execute get request to the given URL
			        HttpResponse httpResponse = client.execute(httpPut);
			        //Log.d("PUT MANAGE",httpResponse.getEntity());
			        // 9. receive response as inputStream
			        //inputStream = httpResponse.getEntity().getContent();

			        StatusLine content = httpResponse.getStatusLine();
		        	Log.d("CONTENT PUT",content.toString());
		        	
		        	if(getOption().equals(Constants.CHANGE_STATE_OPT))
		        	{
		        		if(j.getString("state").equals("open"))
		        	    {
		        			tmp = content.toString()+" closed";
		             	}else{
		             		tmp = content.toString()+" open";
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
		

	    public String getFunct(JSONObject j, int opt){
	    	
	    	String tmp = "";
	    	InputStream inputStream = null;
	    	
	     try {
	        	
	    	HttpClient client = new DefaultHttpClient();
	    	HttpGet httpGet;
	    	if((getOption().equals(Constants.GSTATE_OPT) && opt == 1)||
	    	  (getOption().equals(Constants.LOG_IN_OPT) && opt == 0))
	    	{
	    		httpGet = new HttpGet(Constants.url+Constants.SUFIX_GET_STATE);
	    		httpGet.setHeader("Authorization", "Token token="+ j.getJSONObject("user").getString("authenticationToken"));
	    	    Log.d("AUTH"+opt,j.getJSONObject("user").getString("authenticationToken"));
	    	}else{
	    		httpGet = new HttpGet(Constants.url+Constants.SUFIX_LOGIN);
	    		String authUser = j.getJSONObject("user").getString("username").toString();
		        String authPass = j.getJSONObject("user").getString("password").toString();    
		        UsernamePasswordCredentials credentials =  new UsernamePasswordCredentials(authUser, authPass);
		        BasicScheme scheme = new BasicScheme();
		        httpGet.addHeader(scheme.authenticate(credentials, httpGet));
	    	}
	    
	        httpGet.setHeader("Accept", "application/json");
	        httpGet.setHeader("Content-type", "application/json");
	        // 8. Execute get request to the given URL
	        HttpResponse httpResponse = client.execute(httpGet);
	        StatusLine content = httpResponse.getStatusLine();
	        Log.d("getFunc CONTENT",content.toString());
	        // 9. receive response as inputStream
	        inputStream = httpResponse.getEntity().getContent();
	        
	        if(getOption().equals(Constants.GSTATE_OPT))
	    	{
	    		if(content.toString().equals(Constants.GSTATE_OK))
	    		{
	    			Log.d("getFunc GET STATE",content.toString());
	    			tmp = manageInputStream(inputStream,content);
	    		}
	    		else
	    		{
	    			tmp = "Failed";
	    		}
	        	
	    	}else{
	    		if(!content.toString().equals(Constants.LOG_IN_FAILED))
	    		{
	    			Log.d("getFunc LOGIN",content.toString());
	    			tmp = manageInputStream(inputStream,content);
	    		}
	    		else
	    		{
	    			tmp = content.toString();
	    		}
	    	} 
	       
	     } catch (Exception e) {
	    	 Log.d("InputStream", e.getLocalizedMessage());
	     }   
	    	
	     return tmp;

	    }
	   
	    
	    private String manageInputStream(InputStream inputStream, StatusLine content) throws IOException{
	       BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
	       String line = "";
	       String result = "";
	       

	       if(getOption().equals(Constants.LOG_IN_OPT)){
	    	   if(!content.equals(Constants.LOG_IN_FAILED)){
	    		   while((line = bufferedReader.readLine()) != null){
	    			   result += line;
	    		   }
	    	   }else{
	    		  result = "Failed";
	    		   
	    	   }
	       }else{
	    	   if(getOption().equals(Constants.GSTATE_OPT)){
	    		
	    		   //StringBuilder responseStrBuilder = new StringBuilder();
	    		   while((line = bufferedReader.readLine()) != null){
	    			   result += line;
	    			   //responseStrBuilder.append(line);
	    		   }
	    		   
	    	   }else{
	    	          
	    		   Log.d("manage others",bufferedReader.readLine());
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
