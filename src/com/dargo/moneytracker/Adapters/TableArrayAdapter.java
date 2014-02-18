package com.dargo.moneytracker.Adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;

import com.dargo.moneytracker.R;
import com.dargo.moneytracker.DataHandling.MoneyDB;
import com.dargo.moneytracker.DataHandling.TableMetaData;

public class TableArrayAdapter extends ArrayAdapter<TableMetaData> {
   	private ArrayList<TableMetaData> tables;
    private Context context;
    MoneyDB db;
    String myTS;
    int myCompteur;

    public TableArrayAdapter(Context context, int textViewResourceId,
    		ArrayList<TableMetaData>  tables) {
        super(context, textViewResourceId, tables);
        this.context = context;
        this.tables = tables;
        db = new MoneyDB(super.getContext());
        myCompteur = 0;
      }

    @SuppressLint("UseValueOf")
	public View getView(final int position, View convertView,
						ViewGroup parent) 
    {
       
    	View view = convertView;
        if (view == null) 
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.export_select_tables, null);
        }	
                
        final EditText tableNameView = (EditText) view.findViewById(R.id.showTableName);
        
        
        tableNameView.setText(tables.get(position).tablename);
        
        
        final CheckBox expCB = (CheckBox) view.findViewById(R.id.export_table_CB);
        expCB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final boolean isChecked = expCB.isChecked();
                if(!isChecked)
                {
                	tables.get(position).Selected2Export =  false;
                }
                else
                {
                	tables.get(position).Selected2Export =  true;
                }           
            }
        });
        
        
        
         
     return view;
    }
    
    public ArrayList<String> onNextClicked()
    {
    	//for loop to commit changes
    	ArrayList<String> result = new ArrayList<String>();
    	for (int i = 0; i < tables.size(); i++)
    	{
    		if (tables.get(i).Selected2Export)
    		{
    			result.add(tables.get(i).tablename);
       		}
    	}
    	return result;
    }
    
    
    public void onCloseEvent()
    {
    
    }
    
}