package com.example.calapalamos.library;


import java.util.ArrayList;
import java.util.List;

import com.example.calapalamos.R;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

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
            TextView nameTv =(TextView) v.findViewById(R.id.nLV);
            TextView ageTv = (TextView) v.findViewById(R.id.aLV);
            
            holder.name = nameTv;
            holder.age = ageTv;
            v.setTag(holder);
        }
        else
            holder=(KidsHolder)v.getTag();
 
        Kids k = kidsList.get(position);
        holder.name.setText(k.getName());
        holder.age.setText("" + k.getAge());
        
        return v;
    }
    

    
    private class KidsFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
        	FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = origKidsList;
                results.count = kidsList.size();
            }
            else {
                // We perform filtering operation
                List<Kids> nKidsList = new ArrayList<Kids>();
                 
                for (Kids k : kidsList) {
                    if (k.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        nKidsList.add(k);
                }
                 
                results.values = nKidsList;
                results.count = nKidsList.size();
         
            }
            return results;
      
        }
     
   
		@Override
        protected void publishResults(CharSequence constraint,FilterResults results) {
        	// Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                    notifyDataSetInvalidated();
            else {
                    kidsList = (List<Kids>) results.values;
                    notifyDataSetChanged();
            }
        }
         
    }
    

}
