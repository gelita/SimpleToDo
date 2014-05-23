package codepath.apps.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends Activity {
		EditText etEditItem;
		String item;
		int pos;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);		
		String item= getIntent().getStringExtra("item");		
		EditText etEditItem = (EditText) findViewById(R.id.etEditItem);		
		etEditItem.setText(item);
		etEditItem.setSelection(etEditItem.getText().length());
	}
		
	public void onSubmit(View v) {		
		  int pos = getIntent().getIntExtra("position", 0);
		  EditText etEditItem = (EditText) findViewById(R.id.etEditItem);
		  if(etEditItem.getText().toString().length() == 0 ){
			    etEditItem.setError( "Please make a valid entry." );
			    return;
		  }else {
			  // Prepare data intent 
			  Intent data = new Intent();
			  // Pass relevant data back as a result
			  data.putExtra("item", etEditItem.getText().toString() );
			  data.putExtra("position", pos);
			  // Activity finished ok, return the data
			  setResult(RESULT_OK, data); // set result code and bundle data for response
			  finish(); // closes the activity, pass data to parent
		  }
	}	
}