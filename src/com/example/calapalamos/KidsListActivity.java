package com.example.calapalamos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.calapalamos.library.Kids;
import com.example.calapalamos.library.KidsAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

public class KidsListActivity extends Activity {

	ListView kLV;
	List<JSONObject> j;
	List<Kids> kidsList;
	KidsAdapter adapter;
	
	/**
	 * Se asocia la Activity con el layout y cada uno de los componentes individualmente
	 * Este layout contiene una ListView y para ello debemos definir un adaptador
	 * 
	 * @author sergio
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * @see com.example.calapalamos.library.KidsAdapter
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kidslist);
		
		kidsList = buildData();
		kLV=(ListView)findViewById(R.id.kidsLV);
		adapter = new KidsAdapter(kidsList, this);

		kLV.setAdapter(adapter);
		
	}
	
    /**
     * Funcion que devuelve una List<Kids> ordenada por edad de los ni–os.
     * Se coge los datos del Intent, lo pasamos a JSONArray y lo ordenamos
     * llamando la funcion sortKidsObjects.
     * 
     * @author sergio
     * @see com.example.calapalamos.library.Kids
     * 
     * @throws JSONException
     * @return List<Kids>
     */
    private List<Kids> buildData() {
        List<Kids> list = new ArrayList<Kids>();
        try {   
              JSONArray jkids = new JSONArray(getIntent().getExtras().getString("Kids"));
	          
              //ordenamos los diferentes elementos.
              j = sortKidsObjects(jkids);
    
              //A–adimos los elementos ordenados a la lista de Kids.
              for(int i=0; i < j.size() ; i++) {
    	           JSONObject json_data = j.get(i);
    	           list.add(putData(json_data.getString("name").toString(), json_data.getString("age").toString()));

              }
        } catch (JSONException e) {
        	//  TODO Auto-generated catch block
	         e.printStackTrace();
        }    

       return list;
    }
	
    /**
     * @param name
     * @param age
     * @return Kids
     */
    private Kids putData(String name, String age) {
		    Kids item = new Kids();
		    item.setKid(name, age);
		    return item;
    }
	
	
	/**
	 * Funcion que recibe un JSONArray con los elementos desordenados y los ordeona.
	 * 
	 * @param array
	 * @return List<JSONObject>
	 * 
	 * @throws JSONException
	 */ 
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
	        
	    	//Override de la funcion compare, le pasas dos JSONObjects
	    	//y te los devuelves ordenados.
	    	
	    	@Override
	        public int compare(JSONObject lhs, JSONObject rhs) {
	            Integer lid = 0;
	            Integer rid = 0;
				try {
					
					//cogemos los valores "age" que usamos para ordenados
					lid = lhs.getInt("age");
					rid = rhs.getInt("age");
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            //compara y devuelve los datos ordenados
	            return rid.compareTo(lid);
	        }
	    });
	    return jsons;
	}
	
	/**
	  
	  * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	  */
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {        

        getMenuInflater().inflate(R.menu.kids_list, menu);   

        //accedemos a los servicios del sistema (busqueda)
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        
        //Asociamos un id al serachview
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        //coge la informacion de la Activity de la activity y se la pasa al searchView mediante el searchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);   

        //Definimos el OnQueryTextListneer
        
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() 
        {
        	//filtrado cuando el text va cambiando por el usuario
        	
            @Override
            public boolean onQueryTextChange(String newText) 
            {
                //Filtramos los datos del adapter
                adapter.getFilter().filter(newText);
                return true;
            }
            
            //filtrado cuando el text es presentado y definitivo.
            
            @Override
            public boolean onQueryTextSubmit(String query) 
            {
                // this is your adapter that will be filtered
                adapter.getFilter().filter(query);
                return true;
            }
        };
        
        
        //asignamos el OnQueryTextListener al searchView.
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);

    }

	
    /**
	  * Definicion de la funcionalidad de los diferentes elementos del menu.
	  * En esta activity unicamente tenemos un boton, que llama a un AlertDialog.
	  * Este AlertDialog nos muestra la informacion sobre la compa–ia (tlf,email y direccion)
	  * 
	  * @autor sergio
	  * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
      * 
	  */ 	
	

	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.action_close:
	    	
	    	//boton cerrar activity
	    	
	    	this.finish();
	      break;
	    default:
	      break;
	    }
	    return true;  
	  }

}
