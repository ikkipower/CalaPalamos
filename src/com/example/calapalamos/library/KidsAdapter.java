package com.example.calapalamos.library;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.calapalamos.R;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

/**
  * Clase que define un Adapter para una Listview adaptando la clase Kids
  * @author sergio
  * 
  */

public class KidsAdapter extends ArrayAdapter<Kids>{
	
	private List<Kids> kidsList;
    private Context context;
    private Filter kidsFilter;
    private List<Kids> origKidsList;
    
    public KidsAdapter(List<Kids> kidsList, Context ctx) {
        super(ctx, R.layout.row_listview, kidsList);
        this.kidsList = kidsList;
        this.context = ctx;
        this.origKidsList = kidsList;
    }
    
    public int getCount() {
        return kidsList.size();
    }


    public Kids getItem(int position) {
        return kidsList.get(position);
    }

    public long getItemId(int position) {
        return kidsList.get(position).hashCode();
    }

    public void resetData() {
        kidsList = origKidsList;
    }    
    

    
    public static class KidsHolder{
        public TextView name;
        public TextView age;
    }
    
    @Override
    public Filter getFilter() {
        if (kidsFilter == null) {
            kidsFilter = new KidsFilter();
        }
        return kidsFilter;
    }    

    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        
        KidsHolder holder;
        if (v == null) {
            LayoutInflater vi =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.row_listview, null);
            holder = new KidsHolder();
            //asociamos los elementos al layout row_listview
            TextView nameTv =(TextView) v.findViewById(R.id.nLV);
            TextView ageTv = (TextView) v.findViewById(R.id.aLV);
            
            holder.name = nameTv;
            holder.age = ageTv;
            //le pasamos al View la clase con los dos textview
            v.setTag(holder);
        }
        else
            holder=(KidsHolder)v.getTag();
 
        //cogemos un kis y asignamos el valor a los textview
        Kids k = kidsList.get(position);
        holder.name.setText(k.getName());
        holder.age.setText("" + k.getAge());
        
        return v;
    }
    

    /**
     * Clase que define el filtrado de los elementos Kids. 
     */
    
    private class KidsFilter extends Filter {
    	@Override
        protected FilterResults performFiltering(CharSequence constraint) {
        	
        	//definimos el filtro
        	FilterResults results = new FilterResults();
            
        	//si no hay ningun filtro devolvemos la lista entera
            if (constraint == null || constraint.length() == 0) {
               
                results.values = origKidsList;
                results.count = kidsList.size();
            }
            else {
                // si hay un filtro creamos una lista de elementos Kids
                List<Kids> nKidsList = new ArrayList<Kids>();
                 
                for (Kids k : kidsList) {
                	//si alguno de los Kids de la lista coincide con lo que hay en el filtro se 
                	//se incluye en la nueva lista
                    if (k.getName().toUpperCase(Locale.getDefault()).startsWith(constraint.toString().toUpperCase(Locale.getDefault())))
                        nKidsList.add(k);
                }
                 
                //se devuelve los resultados
                results.values = nKidsList;
                results.count = nKidsList.size();
                
         
            }
            return results;
      
        }
     
   
        /**
         * Clase que consiste en pasar la nueva lista con los resultados filtrados
         * al adapter 
         */
		
		@SuppressWarnings("unchecked")
		@Override
        protected void publishResults(CharSequence constraint,FilterResults results) {
        	
            if (results.count == 0)
                    notifyDataSetInvalidated();
            else {
            	    //pasamos los resultados del filtrado y notificamos los cambios
                    kidsList = (List<Kids>) results.values;
                    notifyDataSetChanged();
            }
        }
         
    }
    

}
