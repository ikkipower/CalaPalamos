package com.example.calapalamos.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

public class HttpAsyncGet  extends AsyncTask<JSONObject, Void, String>{

	private ProgressDialog pDialog;
	private Context context;
    private String option;
	
	public HttpAsyncGet(Context cont, String opt) {
		// TODO Auto-generated constructor stub
	   this.context = cont;
	   this.option = opt;
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
        pDialog.setMessage("Processing...");
        pDialog.show();
        
	}



    @Override
    protected String doInBackground(JSONObject... j) {
        InputStream inputStream = null;
        String result = "";
        
        try {
        	HttpClient client = new DefaultHttpClient();
        	
        	HttpGet httpGet = new HttpGet(Constants.url+Constants.SUFIX_LOGIN);
            
        	String authUser = j[0].getJSONObject("user").getString("username").toString();
            String authPass = j[0].getJSONObject("user").getString("password").toString();
            
            UsernamePasswordCredentials credentials =  new UsernamePasswordCredentials(authUser, authPass);
            BasicScheme scheme = new BasicScheme();
            httpGet.addHeader(scheme.authenticate(credentials, httpGet));
            
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");

            // 8. Execute get request to the given URL
            HttpResponse httpResponse = client.execute(httpGet);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            
            // 10. convert inputstream to string
            if(inputStream != null)
            	{
            		result = convertInputStreamToString(inputStream);
            	    
                }else
            		result = "Did not work!";

        } catch (Exception e) {
             Log.d("InputStream", e.getLocalizedMessage());
        }   
  
        return result;
    }

    @Override
    protected void onPostExecute(String res) {
    	 pDialog.dismiss();
	     if(res.equals(Constants.ERROR_LOGIN)){
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
    }

    
    private String convertInputStreamToString(InputStream inputStream) throws IOException{
       BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
       String line = "";
       String result = "";
       while((line = bufferedReader.readLine()) != null)
           result += line;

       inputStream.close();
       return result;

   } 
}
