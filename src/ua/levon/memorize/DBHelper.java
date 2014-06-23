package ua.levon.memorize;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context) {
		
		super(context, "MemoriesDataBase", null, 1);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("create table memories ("
				+ "id integer primary key autoincrement,"
				+ "title text,"
				+ "description text,"
				+ "photo text" + ");");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		

	}

}
