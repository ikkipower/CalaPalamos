package com.example.calapalamos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class LaFoscaMain extends Activity implements OnClickListener {
 
	private final static int REGISTER = 0;
	static final String EXTRA_MESSAGE = null;
	EditText nameLogin;
    EditText passwdLogin;
    Button btnLogin;
    TextView regLink;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		nameLogin=(EditText)findViewById(R.id.nameLogin);
		passwdLogin=(EditText)findViewById(R.id.passwdLogin);
		btnLogin= (Button)findViewById(R.id.btnLogin);
		regLink = (TextView)findViewById(R.id.link_to_register);
		
		if(isConnected())
		{
			Toast.makeText(this, "Resultado ok ", Toast.LENGTH_SHORT).show();			
		}
		else
		{
			Toast.makeText(this, "Resultado no oj", Toast.LENGTH_SHORT).show();
		}
		
		
		// add click listener to Button "POST"
        btnLogin.setOnClickListener(this);
	}

    @Override
    public void onClick(View view) {
 
        switch(view.getId()){
            case R.id.btnLogin:
      
              //      Toast.makeText(getBaseContext(), userReg.getText()+" Enter some data! "+passwdReg.getText(), Toast.LENGTH_LONG).show();
            	    
                    break;
                    
            case R.id.link_to_register:
                
                Intent intent = new Intent(this, RegisterActivity.class);
                //inicio Activity Register esperando un resultado
                startActivityForResult(intent,REGISTER);
                
            	
            break;
        }
 
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_CANCELED) {
            
            Toast.makeText(this, "Resultado cancelado", Toast.LENGTH_SHORT)
                    .show();
        } else {
            
        	User nuser = new User();
            nuser.setName(data.getExtras().getString("REGISTER1"));
            nuser.setPasswd(data.getExtras().getString("REGISTER2"));
            Toast.makeText(this, "Resultado "+nuser.getName()+ " "+nuser.getPasswd(), Toast.LENGTH_SHORT).show();
            JSONObject jLogin = new JSONObject();
            JSONObject jUser = new JSONObject();
            try {
            	jUser.put("username",nuser.getName());
            	jUser.put("password",nuser.getPasswd());
		        jLogin.put("user", jUser);
		        new HttpAsync().execute(jLogin);
            } catch (JSONException e) {
				// TODO Auto-generated catch block
            	   
            	   Log.e("Error JSON Login",null);
				   e.printStackTrace();
            }
		    
            

        }
    }    
    

    
    public boolean isConnected(){
    	ConnectivityManager con = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = con.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) 
            return true;
        else
            return false;
    }
    
private class HttpAsync extends AsyncTask<JSONObject, Void, String>{
		
		
		private String url = "http://lafosca-beach.herokuapp.com/api/v1";
		private ProgressDialog pDialog;
		
		
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        pDialog = new ProgressDialog(LaFoscaMain.this);
	        pDialog.setMessage("Downloading contacts...");
	        pDialog.show();
	        
		}
		
		@Override
	    protected String doInBackground(JSONObject... j) {
	        InputStream inputStream = null;
	        String result = "";		
	        try {
	        HttpClient client = new DefaultHttpClient();
	        //HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
            HttpResponse response;
            
            /*try {
                HttpPost post = new HttpPost(url+"/users");
                Log.d("url",""+url+"/users");
                StringEntity se = new StringEntity(j[0].toString());  
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                post.setEntity(se);
                response = client.execute(post);

                
       
                //Checking response 
                if(response!=null){
                    InputStream in = response.getEntity().getContent(); //Get the data in the entity
                    result = in.toString();
                    Log.d("ASYNC",""+response.getEntity().getContentLength());
                }
                

            } catch(Exception e) {
                e.printStackTrace();
                
            }*/ 
	        	// 2. make POST request to the given URL
            
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
	        	    Log.d("RESULTADO",result);
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
			 Toast.makeText(LaFoscaMain.this, "Data! sended"+res, Toast.LENGTH_LONG).show();
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

	
	
}


