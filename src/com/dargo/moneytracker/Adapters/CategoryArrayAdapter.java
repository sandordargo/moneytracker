package com.dargo.moneytracker.Adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.dargo.moneytracker.R;
import com.dargo.moneytracker.DataHandling.CategoriesData;
import com.dargo.moneytracker.DataHandling.MoneyDB;

public class CategoryArrayAdapter extends ArrayAdapter<CategoriesData> {
   	private ArrayList<CategoriesData> categories;
    private Context context;
    MoneyDB db;
    String myTS;
    int myCompteur;

    public CategoryArrayAdapter(Context context, int textViewResourceId,
    		ArrayList<CategoriesData>  categories) {
        super(context, textViewResourceId, categories);
        this.context = context;
        this.categories = categories;
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
            view = inflater.inflate(R.layout.manage_categories, null);
        }	
                
        final EditText catNameView = (EditText) view.findViewById(R.id.showCategory);
        catNameView.setOnFocusChangeListener(new OnFocusChangeListener() 
        {          
            public void onFocusChange(View v, boolean hasFocus) 
            {
                if(!hasFocus) 
                {
                    //SAVE THE DATA 
                	db.updateCategoryName(String.valueOf(categories.get(position).id), catNameView.getText().toString());
                	categories.get(position).catName = catNameView.getText().toString();
                	notifyDataSetChanged();
                }  
            }
        });
        
        
		final
		CheckBox incCB = (CheckBox) view.findViewById(R.id.showIncomeCat);
        
		final CheckBox expCB = (CheckBox) view.findViewById(R.id.showExpenseCat);
        expCB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final boolean isChecked = expCB.isChecked();
                if(!isChecked)
                {
                	categories.get(position).expInc -=  2;
                	categories.get(position).isChanged = true;
                }
                else
                {
                	categories.get(position).expInc += 2;
                	categories.get(position).isChanged = true;
                }
            }
        });
        incCB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final boolean isChecked = incCB.isChecked();
                if(!isChecked)
                {
                	categories.get(position).expInc -= 1;
                	categories.get(position).isChanged = true;
                }
                else
                {
                	categories.get(position).expInc += 1;
                	categories.get(position).isChanged = true;
                }
            }
        });
        
        
        
        catNameView.setText(categories.get(position).catName);
        switch(categories.get(position).expInc)
        {
        case 1:
        		incCB.setChecked(true);
        		expCB.setChecked(false);
        		break;
        case 2:
        		incCB.setChecked(false);
        		expCB.setChecked(true);
        		break;
    	case 3:
        		incCB.setChecked(true);
        		expCB.setChecked(true);
        		break;
        default:
        		incCB.setChecked(false);
    			expCB.setChecked(false);
        }
        
        
     final Button aDelButton = (Button) view.findViewById(R.id.deleteCategory);
     aDelButton.setOnClickListener(new OnClickListener() 
     {
		
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			db.deleteCategory(categories.get(position).catName);
			remove(categories.get(position));
			notifyDataSetChanged();
		}
     }
     );
        
     return view;
    }
    

    
    public void onCloseEvent()
    {
    	//for loop to commit changes
    	for (int i = 0; i < categories.size(); i++)
    	{
    		// check for categoryname updates
    		if (categories.get(i).catName != categories.get(i).origCatName)
    		{
    			++myCompteur;
    			db.updateCategoryName(String.valueOf(categories.get(i).id), String.valueOf(categories.get(i).catName));
    		}
    		
    		// check for expinc updates
    		if (categories.get(i).origExpInc != categories.get(i).expInc) 
    		{
    			++myCompteur;
    			db.updateCategory(String.valueOf(categories.get(i).id), String.valueOf(categories.get(i).expInc));
    		}
    	}
    }
    
}