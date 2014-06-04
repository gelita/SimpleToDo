package codepath.apps.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class TodoActivity extends Activity {
	ArrayList<String> items;
	ArrayAdapter<String> itemsAdapter;
	ListView lvItems;
	int longToast = Toast.LENGTH_LONG;
	int shortToast = Toast.LENGTH_SHORT;	
		
	private final int REQUEST_CODE = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		lvItems = (ListView) findViewById(R.id.lvItems);
		items = new ArrayList<String>();
		//loading items
		readItems();
		itemsAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, items);
		lvItems.setAdapter(itemsAdapter);		
		setupListViewListener();		
		showUserGuide();		
	} 
	private void showUserGuide(){
		AlertDialog.Builder builder = new AlertDialog.Builder(TodoActivity.this);
	    builder.setMessage("Click to edit entries.\nLong press to delete entries.")
	    .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;				
			} 	
	    })
	    .setTitle("").setIcon(R.drawable.ic_launcher).create();
	    builder.show();	    
	}	
	private void readItems() {
		//returns abs path where files go for this app
		File filesDir= getFilesDir();
		File todoFile = new File(filesDir,"todo.text");
		try{
			items = new ArrayList<String>(FileUtils.readLines(todoFile));
			//exception occurs if there is no file
		}catch (IOException e){
			items = new ArrayList<String>();
		}
	}	
	private void setupListViewListener(){		
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener(){			
			@Override
			public boolean onItemLongClick(AdapterView<?> parent,View view, final int position, long rowId){
				AlertDialog.Builder builder = new AlertDialog.Builder(TodoActivity.this);
			    builder.setMessage("Are you sure you want to delete this entry?")
			    .setPositiveButton(R.string.confirm_delete, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            // continue with delete
			        	items.remove(position);
						itemsAdapter.notifyDataSetChanged();
						saveItems();
						return;
			        }
			     })
			    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            // do nothing
			        	return;
			        }
			     })
			     .setTitle("Delete entry").setIcon(R.drawable.ic_dialog_alert).show();
				 builder.create();
				 return true;			
			}				  
		});		
		
		lvItems.setOnItemClickListener(new OnItemClickListener(){
			@Override		
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long rowId){
				String item = (String) items.get(position);
				launchComposeView(item,position);
				return;    	   			
			}			
			
			private void launchComposeView(String item, int position) {
				Intent i = new Intent(TodoActivity.this, EditItemActivity.class);				
				i.putExtra("item", item);	
				i.putExtra("position", position);
				startActivityForResult(i, REQUEST_CODE);
			};			
		});	
	}	
	private void saveItems() {
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "todo.txt");
		try{
			FileUtils.writeLines(todoFile, items);	
		}catch (IOException e) {
			e.printStackTrace();
				return;
		}		
	}
	public void addTodoItem(View v){
		EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
		if(etNewItem.getText().toString().length() == 0 ){
		    etNewItem.setError( "Please enter an item." );
		    return;
		}else {
			itemsAdapter.add(etNewItem.getText().toString());
			etNewItem.setText("");
			saveItems();//write to file
			return;
		}
	}	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {			  
			  String dataItem =  data.getStringExtra("item");
			  int position = data.getIntExtra("position", 0);			  
		      makeToast(this, dataItem + " edited", shortToast);
		      items.set(position, dataItem);
			  itemsAdapter.notifyDataSetChanged();
			  saveItems();	      
		    }
	}
	public static void makeToast (Context context, String toastString, int duration) {
		Toast.makeText(context, toastString, duration).show();		
		return;
	}
	
	@Override
	  protected void onPause() {
	    super.onPause();
	  }

	  @Override
	  protected void onResume() {
	    super.onResume();
	  }

	  @Override
	  protected void onStop() {
	    super.onStop();
	  }

	  @Override
	  protected void onDestroy() {
	    super.onDestroy();
	  }

	  @Override
	  protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	  }
}