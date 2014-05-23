package codepath.apps.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
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
	private final int REQUEST_CODE = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		lvItems = (ListView) findViewById(R.id.lvItems);
		items = new ArrayList<String>();
		//loading items in onCreate
		readItems();
		itemsAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, items);
		lvItems.setAdapter(itemsAdapter);
		items.add("First Item");
		items.add("Second Item"); 
		setupListViewListener();
	} 		   

	private void readItems() {
			File filesDir= getFilesDir();
			File todoFile = new File(filesDir,"todo.text");
			try{
				items = new ArrayList<String>(FileUtils.readLines(todoFile));
			}catch (IOException e){
				items = new ArrayList<String>();
				e.printStackTrace();
			}
	}			

	private void setupListViewListener(){
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> parent,
					View view, int position, long rowId){
				items.remove(position);
				itemsAdapter.notifyDataSetChanged();
				saveItems();
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
		      Toast.makeText(this, dataItem, Toast.LENGTH_SHORT).show();
		      items.set(position, dataItem);
			  itemsAdapter.notifyDataSetChanged();
			  saveItems();	      
		    }
	}	
}