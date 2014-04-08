package io.viva.httpbase.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public abstract class AbsSqlDao<T> {
	protected String mTableName;
	private static final byte[] _writeLock = new byte[0];

	protected abstract SQLiteHelper getSQLiteHelper();

	public AbsSqlDao(String tableName) {
		this.mTableName = tableName;
	}

	/**
	 * @param column 为该表增加一个TEXT类型数据字段
	 */
	public void addTextColumn(String column) {
		exeSQL("ALTER TABLE '" + this.mTableName + "' ADD '" + column + "' TEXT;");
	}

	/**
	 * @param column 为该表增加一个INTEGER类型数据字段
	 */
	public void addIntegerColumn(String column) {
		exeSQL("ALTER TABLE '" + this.mTableName + "' ADD '" + column + "' INTEGER DEFAULT 0;");
	}

	/**
	 * @param column 为该表增加一个REAL类型数据字段
	 */
	public void addRealColumn(String column) {
		exeSQL("ALTER TABLE '" + this.mTableName + "' ADD '" + column + "' REAL DEFAULT 0;");
	}

	/**
	 * @param column 为该表增加一个BLOB类型数据字段
	 */
	public void addBlobColumn(String column) {
		exeSQL("ALTER TABLE '" + this.mTableName + "' ADD '" + column + "' BLOB  DEFAULT null;");
	}

	/**
	 * @param values 
	 * @return
	 */
	public long insert(ContentValues values) {
		synchronized (_writeLock) {
			long l = -1L;
			SQLiteDatabase database = null;
			try {
				database = getSQLiteHelper().getWritableDatabase();
				l = database.insert(this.mTableName, null, values);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (database != null) {
						database.close();
						database = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return l;
		}
	}

	/**
	 * @param values
	 */
	public void insert(ContentValues[] values) {
		insert(values, 0, values.length);
	}

	/**
	 * @param values
	 * @param startIndex
	 * @param endIndex
	 */
	public void insert(ContentValues[] values, int startIndex, int endIndex) {
		synchronized (_writeLock) {
			SQLiteDatabase database = null;
			try {
				database = getSQLiteHelper().getWritableDatabase();
				database.beginTransaction();
				for (int i = startIndex; i < startIndex + endIndex; i++) {
					database.insert(this.mTableName, null, values[i]);
					database.yieldIfContendedSafely();
				}
				database.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (database != null) {
						database.endTransaction();
						database.close();
						database = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param values
	 * @return
	 */
	public long replace(ContentValues values) {
		synchronized (_writeLock) {
			long l = -1L;
			SQLiteDatabase database = null;
			try {
				database = getSQLiteHelper().getWritableDatabase();
				l = database.replace(this.mTableName, null, values);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (database != null) {
						database.close();
						database = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return l;
		}
	}

	/**
	 * @param values
	 */
	public void replace(ContentValues[] values) {
		replace(values, 0, values.length);
	}

	/**
	 * @param values
	 * @param startIndex
	 * @param endIndex
	 */
	public void replace(ContentValues[] values, int startIndex, int endIndex) {
		synchronized (_writeLock) {
			SQLiteDatabase database = null;
			try {
				database = getSQLiteHelper().getWritableDatabase();
				database.beginTransaction();
				for (int i = startIndex; i < startIndex + endIndex; i++) {
					database.replace(this.mTableName, null, values[i]);
					database.yieldIfContendedSafely();
				}
				database.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (database != null) {
						database.endTransaction();
						database.close();
						database = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param whereClause
	 * @return
	 */
	public long delete(String whereClause) {
		return delete(whereClause, null);
	}

	/**
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public long delete(String whereClause, String[] whereArgs) {
		synchronized (_writeLock) {
			long l = 0L;
			SQLiteDatabase database = null;
			try {
				database = getSQLiteHelper().getWritableDatabase();
				l = database.delete(this.mTableName, whereClause, whereArgs);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (database != null) {
						database.close();
						database = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return l;
		}
	}

	/**
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public long update(ContentValues values, String whereClause, String[] whereArgs) {
		synchronized (_writeLock) {
			long l = -1L;
			SQLiteDatabase database = null;
			try {
				database = getSQLiteHelper().getWritableDatabase();
				l = database.update(this.mTableName, values, whereClause, whereArgs);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (database != null) {
						database.close();
						database = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return l;
		}
	}

	/**
	 * @param values
	 * @param whereClause
	 */
	public void update(ContentValues[] values, String[] whereClause) {
		synchronized (_writeLock) {
			SQLiteDatabase database = null;
			try {
				database = getSQLiteHelper().getWritableDatabase();
				database.beginTransaction();
				for (int i = 0; i < values.length; i++) {
					database.update(this.mTableName, values[i], whereClause[i], null);
					database.yieldIfContendedSafely();
				}
				database.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (database != null) {
						database.endTransaction();
						database.close();
						database = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orgerBy
	 * @return
	 */
	public T queryForObject(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orgerBy) {
		synchronized (_writeLock) {
			SQLiteDatabase database = null;
			Cursor cursor = null;
			try {
				database = getSQLiteHelper().getWritableDatabase();
				cursor = database.query(this.mTableName, columns, selection, selectionArgs, groupBy, having, orgerBy);
				if ((cursor != null) && (cursor.moveToNext())) {
					T localObject1 = cursorRowToObject(cursor);
					try {
						if (cursor != null) {
							cursor.close();
							cursor = null;
						}
						if (database != null) {
							database.close();
							database = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					return localObject1;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
						cursor = null;
					}
					if (database != null) {
						database.close();
						database = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return
	 */
	public List<T> queryForList(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
		ArrayList<T> list = new ArrayList<T>();
		synchronized (_writeLock) {
			SQLiteDatabase database = null;
			Cursor cursor = null;
			try {
				database = getSQLiteHelper().getWritableDatabase();
				cursor = database.query(this.mTableName, columns, selection, selectionArgs, groupBy, having, orderBy);
				if ((cursor != null) && (cursor.getCount() > 0)) {
					while (cursor.moveToNext()) {
						list.add(cursorRowToObject(cursor));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (cursor != null) {
						cursor.close();
						cursor = null;
					}
					if (database != null) {
						database.close();
						database = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	/**
	 * @param sqls sql语句
	 */
	public void exeSQLs(String[] sqls) {
		synchronized (_writeLock) {
			SQLiteDatabase database = null;
			try {
				database = getSQLiteHelper().getWritableDatabase();
				database.beginTransaction();
				for (String str : sqls) {
					database.execSQL(str);
				}
				database.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (database != null) {
						database.endTransaction();
						database.close();
						database = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 删除该表中所有数据
	 */
	public void clear() {
		synchronized (_writeLock) {
			exeSQL("delete from " + this.mTableName + ";");
		}
	}

	public void exeSQL(String sql) {
		synchronized (_writeLock) {
			SQLiteDatabase database = null;
			try {
				database = getSQLiteHelper().getWritableDatabase();
				database.execSQL(sql);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (database != null) {
						database.close();
						database = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param sql
	 * @param bindArgs
	 */
	public void exeSQL(String sql, Object[] bindArgs) {
		synchronized (_writeLock) {
			SQLiteDatabase database = null;
			try {
				database = getSQLiteHelper().getWritableDatabase();
				database.execSQL(sql, bindArgs);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (database != null) {
						database.close();
						database = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param sql
	 * @param objects
	 * @return
	 */
	public long insert(String sql, Object[] objects) {
		synchronized (_writeLock) {
			SQLiteDatabase database = null;
			SQLiteStatement statement = null;
			try {
				database = getSQLiteHelper().getWritableDatabase();
				statement = database.compileStatement(sql);
				if (objects != null) {
					int i = 0;
					int j = objects.length;
					while (i < j) {
						DatabaseUtils.bindObjectToProgram(statement, i + 1, objects[i]);
						i++;
					}
				}
				long l = statement.executeInsert();
				try {
					if (statement != null) {
						statement.close();
						statement = null;
					}
					if (database != null) {
						database.close();
						database = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return l;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (statement != null) {
						statement.close();
						statement = null;
					}
					if (database != null) {
						database.close();
						database = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return -1L;
		}
	}

	/**
	 * @return 该表中的所有数据字段
	 */
	public List<String> getColumnNames() {
		synchronized (_writeLock) {
			ArrayList<String> list = new ArrayList<String>();
			SQLiteDatabase database = getSQLiteHelper().getWritableDatabase();
			Cursor cusor = database.query(this.mTableName, null, null, null, "", "", "");
			if (cusor != null) {
				for (String str : cusor.getColumnNames()) {
					list.add(str);
				}
				cusor.close();
				cusor = null;
				return list;
			}
			database.close();
			database = null;
		}
		return null;
	}

	/**
	 * @return 该表中的数据总量
	 */
	public int getDataCount() {
		synchronized (_writeLock) {
			SQLiteDatabase database = null;
			Cursor cusor = null;
			try {
				database = getSQLiteHelper().getWritableDatabase();
				cusor = database.rawQuery("select count(*) from " + this.mTableName, null);
				if ((cusor != null) && (cusor.moveToNext())) {
					int i = cusor.getInt(0);
					if (cusor != null) {
						cusor.close();
					}
					if (database != null) {
						database.close();
					}
					return i;
				}
				int i = 0;
				if (cusor != null) {
					cusor.close();
				}
				if (database != null) {
					database.close();
				}
				return i;
			} finally {
				if (cusor != null) {
					cusor.close();
				}
				if (database != null) {
					database.close();
				}
			}
		}
	}

	/**
	 * @param cusor
	 * @return
	 */
	public T cursorRowToObject(Cursor cusor) {
		return null;
	}
}