package com.example.calapalamos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class HttpAsync extends AsyncTask<JSONObject, Void, String>{
	
	
	private String url = "http://lafosca-beach.herokuapp.com/api/v1";
	private ProgressDialog pDialog;
	private Context context;
    private String option;
    
	public HttpAsync(Context cont, String opt) {
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
        pDialog.setMessage("Downloading contacts...");
        pDialog.show();
        
	}
	
	@Override
    protected String doInBackground(JSONObject... j) {
        InputStream inputStream = null;
        String result = "";		
        try {
        HttpClient client = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(url+"/users");

        String json = "";
        
        json = j[0].toString();
        
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
        		result = convertInputStreamToString(inputStream);
        	    Log.d("RESULTADO "+getOption(),result);
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
		 Toast.makeText(getContext(), "Data! sended"+res, Toast.LENGTH_LONG).show();
		 Log.d("resultado postExecute",res);
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
