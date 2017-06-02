package com.example.com.jld.facecheck.app;

import com.example.com.jld.facecheck.app.models.RecordInfo;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecordDbUtils extends SQLiteOpenHelper {
	private final static String DATABASE_NAME = "JLDFaceChechk.db";
	private final static int DATABASE_VERSION = 1;
	private final static String TABLE_NAME = "records_table";
	public final static String REC_ID = "rec_id";
	public final static String REC_NAME = "rec_name";
	public final static String REC_SEX = "rec_sex";

	public final static String REC_IDCARD = "rec_idcard";

	public final static String REC_ADDRESS = "rec_address";

	public final static String REC_BRTH = "rec_birthday";

	public final static String REC_MZ = "rec_mz";

	public final static String REC_DATE = "rec_date";
	
	public final static String REC_IDCARDIMG="rec_idcardimg";
	
	public final static String REC_REALTIMEIMG="rec_realtimeimg";

	public RecordDbUtils(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// 创建table
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + REC_ID
				+ " INTEGER primary key autoincrement, " + REC_NAME + " text, "
				+ REC_SEX + " text, " + REC_IDCARD + " text, " + REC_ADDRESS
				+ " text, " + REC_BRTH + " text, " + REC_MZ + " text, "
				+ REC_DATE + " INTEGER, "
				+ REC_IDCARDIMG + " BLOB , "
				+ REC_REALTIMEIMG + " BLOB );";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public Cursor select(long starTtime, long endTime, String idcard,
			String name) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(
				TABLE_NAME,
				null,
				REC_DATE + " >=? and " + REC_DATE + " <=? and " + REC_IDCARD
						+ " like ? and " + REC_NAME + " like ? ",
				new String[] { String.valueOf(starTtime),
						String.valueOf(endTime), "%"+idcard+"%", "%"+name+"%" }, null, null,
				null);
		return cursor;
	}

	// 增加操作
	public long insert(RecordInfo rec) {
		SQLiteDatabase db = this.getWritableDatabase();
		/* ContentValues */
		ContentValues cv = new ContentValues();
		cv.put(REC_ADDRESS,rec.getRec_address());
		cv.put(REC_BRTH, rec.getRec_birthday());
		cv.put(REC_DATE, rec.getRec_date());
		cv.put(REC_IDCARD, rec.getRec_idcard());
		
		cv.put(REC_IDCARDIMG, rec.getRec_idcardimage());
		cv .put(REC_REALTIMEIMG, rec.getRec_realtimeimage());
		cv.put(REC_MZ, rec.getRec_mz());
		cv.put(REC_NAME, rec.getRec_sex());
		cv.put(REC_NAME, rec.getRec_name());
		long row = db.insert(TABLE_NAME, null, cv);
		return row;
	}

	// 删除操作
	public void delete(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = REC_ID + " = ?";
		String[] whereValue = { Integer.toString(id) };
		db.delete(TABLE_NAME, where, whereValue);
	}


}