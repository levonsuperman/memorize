package ua.levon.memorize;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddMemoryActivity extends Activity {

	EditText addTitle;
	EditText addDescription;
	ImageView addPreview;
	Button addChoose;
	Button addCancel;
	Button addOk;
	String path=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_memory);

		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33B5E5")));

		setTitle("Add Memory");

		addTitle = (EditText) findViewById(R.id.add_title);
		addDescription = (EditText) findViewById(R.id.add_description);
		addPreview = (ImageView) findViewById(R.id.add_preview);
		addChoose = (Button) findViewById(R.id.add_choose);
		addCancel = (Button) findViewById(R.id.add_cancel);
		addOk = (Button) findViewById(R.id.add_ok);
		
		overridePendingTransition(R.animator.s1, R.animator.s2);

	}

	public void onClickAddChoose(View v) {

		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, 1);

	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO fix

		switch (requestCode) {

		case 1:

			if (RESULT_OK == resultCode && intent != null
					&& intent.getData() != null) {

				Uri imageUri = intent.getData();					
				addPreview.setImageURI(intent.getData());					
				path=imageUri.toString();

			}

			break;

		}

	}

	public void onClickAddCancel(View v) {

		setResult(RESULT_CANCELED);
		finish();

	}

	public void onClickAddOk(View v) {
		
		if (path==null) {
			
			Toast.makeText(getApplicationContext(), "Choose image!", Toast.LENGTH_SHORT).show();
			return;
			
		}
		
		if (addTitle.getText().toString().equals("")) addTitle.setText("No title");
		if (addDescription.getText().toString().equals("")) addDescription.setText("No Description");

		Intent intent = new Intent();
		intent.putExtra("title", addTitle.getText().toString());
		intent.putExtra("description", addDescription.getText().toString());
		intent.putExtra("photo", path);
		setResult(RESULT_OK, intent);
		finish();

	}
	
	@Override
	public void onBackPressed() {
		
	    Toast.makeText(getApplicationContext(), "Click Cancel to return back", Toast.LENGTH_SHORT).show();
	    
	}

}
