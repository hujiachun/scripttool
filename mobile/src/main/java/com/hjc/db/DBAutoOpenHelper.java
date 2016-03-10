package com.hjc.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAutoOpenHelper extends SQLiteOpenHelper{

	public DBAutoOpenHelper(Context context){
		super(context, "auto.db", null, 4);
	}

	
	public DBAutoOpenHelper(Context context, String name, CursorFactory factory,
							int version) {
		super(context, "auto.db", null, 4);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table auto(id Integer primary key autoincrement, type, testcase)");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL("alter table auto add id primary key autoincrement");
	}

}
