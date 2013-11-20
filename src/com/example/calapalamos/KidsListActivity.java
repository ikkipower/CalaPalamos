package com.example.calapalamos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class KidsListActivity extends Activity {

	ListView kLV;
	List<JSONObject> j;
	ArrayList<Map<String, String>> kidsList;
	HashMap<String, String> item;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kidslist);
		JSONArray jkids;
		
		kidsList = buildData();
		item = new HashMap<String, String>();
		kLV=(ListView)findViewById(R.id.kidsLV);
		//Log.d("KIDS ACTIVITY",j.toString());
		
		/*try {
			jkids = new JSONArray(getIntent().getExtras().getString("Kids"));
			j = sortKidsObjects(jkids);
			Log.d("KIDS ACTIVITY ",j.toString());
			
			for(int i=0; i < j.size() ; i++) {
				/*JSONObject json_data = j.get(i);
			    Log.d("KIDS ACTIVITY",json_data.toString());			    
			    item.put("name", json_data.getString("name"));
			    item.put("age", json_data.getString("age"));
			    Log.d("KIDS ACTIVITY",item.toString());
			    kidsList.add(item);
			}
			 Log.d("KIDS ACTIVITY 3",kidsList.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		SimpleAdapter adapter = new SimpleAdapter(this, kidsList,
		        R.layout.row_listview, new String[] {"name","age"},
		        new int [] { R.id.nLV, R.id.aLV });
		kLV.setAdapter(adapter);
		
	}

	  private ArrayList<Map<String, String>> buildData() {
		    ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {   
		    JSONArray jkids = new JSONArray(getIntent().getExtras().getString("Kids"));
			j = sortKidsObjects(jkids);
		    
		    for(int i=0; i < j.size() ; i++) {
		    	JSONObject json_data = j.get(i);
		    	list.add(putData(json_data.getString("name").toString(), json_data.getString("age").toString()));

		    }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
		
		    return list;
	}

		  private HashMap<String, String> putData(String name, String purpose) {
		    HashMap<String, String> item = new HashMap<String, String>();
		    item.put("name", name);
		    item.put("age", purpose);
		    return item;
		  }
	
	
	
	public static List<JSONObject> sortKidsObjects(JSONArray array) {
	    List<JSONObject> jsons = new ArrayList<JSONObject>();
	    for (int i = 0; i < array.length(); i++) {
	        try {
				jsons.add(array.getJSONObject(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    Collections.sort(jsons, new Comparator<JSONObject>() {
	        @Override
	        public int compare(JSONObject lhs, JSONObject rhs) {
	            Integer lid = 0;
	            Integer rid = 0;
				try {
					lid = lhs.getInt("age");
					rid = rhs.getInt("age");
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
	            return rid.compareTo(lid);
	        }
	    });
	    return jsons;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kids_list, menu);
		return true;
	}
	
	  @Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.action_close:
	    	this.finish();
	      break;
	    default:
	      break;
	    }
	    return true;  
	  }

}
