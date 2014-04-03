package io.viva.httpbase.database;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

public abstract class SQLiteHelper extends SQLiteOpenHelper {
	private Context mContext;
	private String mDataBaseName;
	private String mSdcardPath;
	private int mVersion;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public SQLiteHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory cursorFactory, int version, DatabaseErrorHandler errorHandler) {
		super(context, databaseName, cursorFactory, version, errorHandler);
		this.mContext = context;
		this.mDataBaseName = databaseName;
		this.mVersion = version;
	}

	public SQLiteHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory cursorFactory, int version) {
		super(context, databaseName, cursorFactory, version);
		this.mContext = context;
		this.mDataBaseName = databaseName;
		this.mVersion = version;
	}
}