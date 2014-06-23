package ua.levon.memorize;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final int DIALOG = 1;
	private static final int SELECT_PHOTO = 2;
	private static final int ADD_ITEM = 3;

	HashMap<String, Object> hm;
	ListView memoriesListView;
	int selected = 0;
	View preSelected;
	Dialog editDialog;
	Bitmap editBitmap;
	DBHelper MemoriesDBHelper;

	ArrayList<MemoryItem> memoryItems = new ArrayList<MemoryItem>();
	MemoryItemAdapter adapter;

	SQLiteDatabase MemoriesDB;

	String editPath = null;

	ArrayList<Uri> photoUri = new ArrayList<Uri>();
	ArrayList<String> images = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33B5E5")));
		setTitle("Memories");

		overridePendingTransition(R.animator.s1, R.animator.s2);

		MemoriesDBHelper = new DBHelper(this);
		new ContentValues();
		MemoriesDB = MemoriesDBHelper.getWritableDatabase();
		Cursor c = MemoriesDB.query("memories", null, null, null, null, null,
				null);

		if (c.moveToFirst()) {

			int idColIndex = c.getColumnIndex("id");
			int titleColIndex = c.getColumnIndex("title");
			int descriptionColIndex = c.getColumnIndex("description");
			c.getColumnIndex("photo");

			do {

				ImageView img = new ImageView(this);
				img.setImageURI(Uri.parse(getExternalCacheDir().toString()
						+ "/" + c.getString(idColIndex) + ".jpg"));
				photoUri.add(Uri.parse(getExternalCacheDir().toString() + "/"
						+ c.getString(idColIndex) + ".jpg"));
				images.add(getExternalCacheDir().toString()
						+ "/" + c.getString(idColIndex) + ".jpg");
				memoryItems.add(new MemoryItem(c.getString(titleColIndex), c
						.getString(descriptionColIndex), img));

			} while (c.moveToNext());

		}

		c.close();

		memoriesListView = (ListView) findViewById(R.id.memories_list);
		memoriesListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		adapter = new MemoryItemAdapter(this, memoryItems);
		memoriesListView.setAdapter(adapter);

		memoriesListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				selected = (int) id;

				for (int i = 3; i <= 7; i++) {

					if (((ViewGroup) view).getChildAt(i).getVisibility() == View.GONE) {

						if (preSelected != null)
							((ViewGroup) preSelected).getChildAt(i)
									.setVisibility(View.GONE);

						((ViewGroup) view).getChildAt(i).setVisibility(
								View.VISIBLE);

					} else
						((ViewGroup) view).getChildAt(i).setVisibility(
								View.GONE);

				}

				preSelected = view;

				memoriesListView.invalidateViews();
				memoriesListView.smoothScrollToPosition(position);

			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main_activity_menu, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_add_memory:
			Intent intent = new Intent(this, AddMemoryActivity.class);
			startActivityForResult(intent, ADD_ITEM);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@SuppressWarnings("deprecation")
	public void onClickEdit(View v) {

		showDialog(DIALOG);
		memoriesListView.invalidateViews();

	}

	@Override
	protected Dialog onCreateDialog(int id) {

		AlertDialog.Builder adb = new AlertDialog.Builder(this);

		GridLayout editDialog = (GridLayout) getLayoutInflater().inflate(
				R.layout.edit_dialog, null);
		adb.setView(editDialog);
		GridLayout editDialogTitle = (GridLayout) getLayoutInflater().inflate(
				R.layout.edit_dialog_title, null);
		adb.setCustomTitle(editDialogTitle);

		return adb.create();

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {

		super.onPrepareDialog(id, dialog);

		if (id == DIALOG) {

			editPath = null;

			// TextView
			// editDialogTitle=(TextView)dialog.getWindow().findViewById(R.id.edit_dialog_title_title);
			// editDialogTitle.setText("Edit");

			// Button
			// editDialogOk=(Button)dialog.getWindow().findViewById(R.id.edit_dialog_ok);

			// Button
			// editDialogCancel=(Button)dialog.getWindow().findViewById(R.id.edit_dialog_cancel);

			EditText editTitle = (EditText) dialog.getWindow().findViewById(
					R.id.edit_title);
			editTitle.setText(memoryItems.get(selected).getTitle());
			editTitle.selectAll();

			EditText editDescription = (EditText) dialog.getWindow()
					.findViewById(R.id.edit_description);
			editDescription.setText(memoryItems.get(selected).getDescription());
			editDescription.setSelection(editDescription.length());

			ImageView editImage = (ImageView) dialog.getWindow().findViewById(
					R.id.edit_image);

			Bitmap bmSrc1 = ((BitmapDrawable) memoryItems.get(selected)
					.getPhoto().getDrawable()).getBitmap();
			Bitmap bmSrc2 = bmSrc1.copy(bmSrc1.getConfig(), true);

			editImage.setImageBitmap(bmSrc2);

		}

		editDialog = dialog;

	}

	public void onClickChoose(View v) {

		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, SELECT_PHOTO);

	}

	public void onClickAlarm(View v) {

		Calendar.getInstance();
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("title", memoryItems.get(selected).getTitle());
		intent.putExtra("description", memoryItems.get(selected)
				.getDescription());
		startActivity(intent);

	}

	public void onClickShare(View v) {

		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		Uri screenshotUri = photoUri.get(selected);
		sharingIntent.setType("image/*");
		sharingIntent.setPackage("com.twitter.android");
		sharingIntent
				.putExtra(Intent.EXTRA_TEXT, memoryItems.get(selected)
						.getTitle()
						+ "\n"
						+ memoryItems.get(selected).getDescription());
		sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
		startActivity(Intent.createChooser(sharingIntent, "Share"));

	}

	public void onClickDelete(View v) {
		
		MemoriesDB.delete("memories", "id" + "=" + (selected + 1), null);
		
		File file = new File(images.get(selected));
		file.delete();
		
		if (memoryItems.size()>=selected+1) memoryItems.remove(selected);
		if (photoUri.size()>=selected+1) photoUri.remove(selected);
		if (images.size()>=selected+1) images.remove(selected);
		
		for (int i=selected+1; i<=images.size(); i++) {
			
			File f=new File(getExternalCacheDir().toString()
					+ "/" + (i+1) + ".jpg");
			File to=new File(getExternalCacheDir().toString()
						+ "/" + i + ".jpg");
			f.renameTo(to);
			
			images.set(i-1, getExternalCacheDir().toString()+ "/" + i + ".jpg");
			
		}
		
		adapter = new MemoryItemAdapter(this, memoryItems);
		memoriesListView.setAdapter(adapter);
		memoriesListView.invalidateViews();
		memoriesListView.smoothScrollToPosition(selected-1);
		
		MemoriesDB.delete("memories", null, null);
		
		for (int i=0; i<memoryItems.size(); i++) {
			
			ContentValues cv = new ContentValues();
			cv.put("id", i+1);
			cv.put("title", memoryItems.get(i).getTitle());
			cv.put("description", memoryItems.get(i).getDescription());
			cv.put("photo", images.get(i));
			MemoriesDB.insert("memories", null, cv);
			
		}

	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO fix

		switch (requestCode) {

		case SELECT_PHOTO:

			if (RESULT_OK == resultCode && intent != null
					&& intent.getData() != null) {

				Uri imageUri = intent.getData();

				ImageView editImage = (ImageView) editDialog.getWindow()
						.findViewById(R.id.edit_image);
				editImage.setImageURI(intent.getData());
				editPath = imageUri.toString();

				// path=imageUri.toString();

			}

			break;

		case ADD_ITEM:

			if (RESULT_OK == resultCode) {

				ImageView img;
				if (intent.getStringExtra("photo") == null)
					img = (ImageView) findViewById(R.drawable.bydefault);
				else {
					img = new ImageView(this);
					img.setImageURI(Uri.parse(intent.getStringExtra("photo")));
				}
				memoryItems.add(new MemoryItem(intent.getStringExtra("title"),
						intent.getStringExtra("description"), img));
				images.add(getExternalCacheDir().toString()
						+ "/" + images.size() + ".jpg");
				photoUri.add(Uri.parse(getExternalCacheDir().toString() + "/"
						+ photoUri.size() + ".jpg"));
				ContentValues cv = new ContentValues();
				cv.put("title", intent.getStringExtra("title"));
				cv.put("description", intent.getStringExtra("description"));
				cv.put("photo", Uri.parse(intent.getStringExtra("photo"))
						.toString());
				SavePicture(img, getExternalCacheDir().toString(),
						memoriesListView.getCount());
				MemoriesDB.insert("memories", null, cv);
				adapter = new MemoryItemAdapter(this, memoryItems);
				memoriesListView.setAdapter(adapter);
				memoriesListView.invalidateViews();
				memoriesListView.smoothScrollToPosition(adapter.getCount() - 1);

			}
			break;

		}

	}

	public void onClickEditDialogCancel(View v) {

		editDialog.cancel();

	}

	public void onClickEditDialogOk(View v) {

		EditText editTitle = (EditText) editDialog.getWindow().findViewById(
				R.id.edit_title);

		EditText editDescription = (EditText) editDialog.getWindow()
				.findViewById(R.id.edit_description);

		ImageView editImage = (ImageView) editDialog.getWindow().findViewById(
				R.id.edit_image);

		((MemoryItem) adapter.getItem(selected)).set(editTitle.getText()
				.toString(), editDescription.getText().toString(), editImage);

		if (editPath != null) {

			ImageView img = new ImageView(this);
			img.setImageURI(Uri.parse(editPath));
			ContentValues cv = new ContentValues();
			cv.put("title", editTitle.getText().toString());
			cv.put("description", editDescription.getText().toString());
			cv.put("photo", Uri.parse(editPath).toString());
			SavePicture(img, getExternalCacheDir().toString(), selected);
			MemoriesDB.update("memories", cv, "id " + "=" + (selected + 1),
					null);
			memoryItems.set(selected, new MemoryItem(editTitle.getText()
					.toString(), editDescription.getText().toString(), img));
			photoUri.set(selected, Uri.parse(editPath));

		}

		else {

			ContentValues cv = new ContentValues();
			cv.put("title", editTitle.getText().toString());
			cv.put("description", editDescription.getText().toString());
			SavePicture(((MemoryItem) adapter.getItem(selected)).getPhoto(),
					getExternalCacheDir().toString(), selected);
			MemoriesDB.update("memories", cv, "id " + "=" + (selected + 1),
					null);
			memoryItems.set(selected, new MemoryItem(editTitle.getText()
					.toString(), editDescription.getText().toString(),
					((MemoryItem) adapter.getItem(selected)).getPhoto()));

		}
		
		adapter = new MemoryItemAdapter(this, memoryItems);
		memoriesListView.setAdapter(adapter);
		memoriesListView.invalidateViews();
		memoriesListView.smoothScrollToPosition(selected);

		editDialog.dismiss();
		if (true)
			return;

		memoryItems = null;
		memoryItems = new ArrayList<MemoryItem>();
		photoUri = null;
		photoUri = new ArrayList<Uri>();

		Cursor c = MemoriesDB.query("memories", null, null, null, null, null,
				null);

		if (c.moveToFirst()) {

			int idColIndex = c.getColumnIndex("id");
			int titleColIndex = c.getColumnIndex("title");
			int descriptionColIndex = c.getColumnIndex("description");
			int photoColIndex = c.getColumnIndex("photo");

			do {

				ImageView img = new ImageView(this);
				img.setImageURI(Uri.parse(getExternalCacheDir().toString()
						+ "/" + c.getString(idColIndex) + ".jpg"));
				photoUri.add(Uri.parse(getExternalCacheDir().toString() + "/"
						+ c.getString(idColIndex) + ".jpg"));
				memoryItems.add(new MemoryItem(c.getString(titleColIndex), c
						.getString(descriptionColIndex), img));

			} while (c.moveToNext());

		}

		c.close();

		adapter = new MemoryItemAdapter(this, memoryItems);
		memoriesListView.setAdapter(adapter);
		memoriesListView.invalidateViews();
		memoriesListView.smoothScrollToPosition(selected);

		editDialog.dismiss();

	}

	private String SavePicture(ImageView iv, String folderToSave, int id) {

		OutputStream fOut = null;

		try {

			File file = new File(folderToSave, new Integer(id + 1).toString()
					+ ".jpg");
			fOut = new FileOutputStream(file);

			Bitmap bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();

		} catch (Exception e)

		{
			return e.getMessage();
		}

		return "";

	}
	
}
