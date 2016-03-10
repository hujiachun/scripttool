package com.hjc.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DBAutoservice {

	private DBAutoOpenHelper dbAutoOpenHelper;
	
	public DBAutoservice(Context context){
		this.dbAutoOpenHelper = new DBAutoOpenHelper(context);
		
	}

	public void insert(Auto auto){
		SQLiteDatabase db = dbAutoOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("type", auto.getType());
		values.put("testcase", auto.getTestcase());
		db.insert("auto", null, values);
		
	}

	public ArrayList<Auto> selcet(String type){
		ArrayList<Auto> autos = new ArrayList<Auto>();
		SQLiteDatabase db = dbAutoOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("auto", new String[]{"testcase"} , "type=?", new String[]{type}, null, null, null);

		if (cursor.getCount() != 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				String testcase = cursor.getString(cursor.getColumnIndex("testcase"));
				Auto auto = new Auto(null, testcase, type);
				autos.add(auto);
			}
		}
		cursor.close();
		return autos;
	}


	public void delete(String type){
		SQLiteDatabase db = dbAutoOpenHelper.getWritableDatabase();
		db.delete("auto", "type=?", new String[]{type});
	}

	
	

	
	

}
