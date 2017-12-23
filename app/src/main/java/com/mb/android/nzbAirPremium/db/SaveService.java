
package com.mb.android.nzbAirPremium.db;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OpenHelperManager.SqliteOpenHelperFactory;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class SaveService {

	private final String TAG = getClass().getSimpleName();

	protected Context context;

	public SaveService(Context context) {
		this.context = context;

		OpenHelperManager.setOpenHelperFactory(new SqliteOpenHelperFactory() {

			@Override
			public OrmLiteSqliteOpenHelper getHelper(Context context) {
				return new DatabaseHelper(context);
			}
		});
	}

	private Dao<DbEntry, Integer> getDao() {
		try {
			return ((DatabaseHelper) OpenHelperManager.getHelper(context)).getDbEntryDao();
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "Error: " + e.toString());
			return null;
		}
	}

	public List<DbEntry> getEntries(String bucket) {

		try {
			// get our dao
			final Dao<DbEntry, Integer> simpleDao = getDao();

			// get our query builder from the DAO
			final QueryBuilder<DbEntry, Integer> queryBuilder = simpleDao.queryBuilder();
			// the 'password' field must be equal to "qwerty"
			queryBuilder.where().eq(DbEntry.BUCKET_FIELD_NAME, bucket);
			// prepare our sql statement
			final PreparedQuery<DbEntry> preparedQuery = queryBuilder.prepare();
			// query for all accounts that have that password
			final List<DbEntry> entryList = simpleDao.query(preparedQuery);

			return entryList;
		} catch (final java.sql.SQLException e) {
			Log.e(TAG, "Database exception", e);
			return null;
		}
	}

	public List<DbEntry> getEntryByBucketAndValue(String bucket, String key, String value, String extra) {

		try {
			// get our dao
			final Dao<DbEntry, Integer> simpleDao = getDao();

			// get our query builder from the DAO
			final QueryBuilder<DbEntry, Integer> queryBuilder = simpleDao.queryBuilder();
			// the 'password' field must be equal to "qwerty"
			final Where where = queryBuilder.where();

			where.eq(DbEntry.KEY_FIELD_NAME, key);
			where.and();
			where.eq(DbEntry.BUCKET_FIELD_NAME, bucket);
			where.and();
			where.eq(DbEntry.VALUE_FIELD_NAME, value);
			where.and();
			where.eq(DbEntry.EXTRA_FIELD_NAME, extra);
			queryBuilder.orderBy(DbEntry.VALUE_FIELD_NAME, true);
			// prepare our sql statement
			final PreparedQuery<DbEntry> preparedQuery = queryBuilder.prepare();
			// query for all accounts that have that password
			final List<DbEntry> entryList = simpleDao.query(preparedQuery);

			return entryList;
		} catch (final java.sql.SQLException e) {
			Log.e(TAG, "Database exception", e);
			return null;
		}
	}

	public boolean exists(String bucket, String key, String value, String extra) {
		return getEntryByBucketAndValue(bucket, key, value, extra).size() > 0;
	}

	public boolean create(String bucket, String key, String value, String extra) {
		final DbEntry e = new DbEntry();
		e.setKey(key);
		e.setBucket(bucket);
		e.setValue(value);
		e.setExtra(extra);

		if (!exists(bucket, key, value, extra)) {
			return insertEntry(e) == 1;
		} else {
			return true;
		}
	}

	public boolean delete(String bucket, String key, String value, String extra) {
		try {
			final DbEntry d = getEntryByBucketAndValue(bucket, key, value, extra).get(0);
			return deleteEntry(d) == 1;
		} catch (final Exception ex) {
			Log.e(TAG, ex.toString());
			return false;
		}
	}

	public int updateEntry(DbEntry entry) {
		try {
			return getDao().update(entry);
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "Database exception", e);
			e.printStackTrace();
			return 0;
		}
	}

	public int insertEntry(DbEntry entry) {
		try {
			return getDao().create(entry);
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "Database exception", e);
			e.printStackTrace();
			return 0;
		}
	}

	public int deleteEntry(DbEntry entry) {
		try {
			return getDao().delete(entry);
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "Database exception", e);
			e.printStackTrace();
			return 0;
		}
	}

	public int refreshEntry(DbEntry entry) {
		try {
			return getDao().refresh(entry);
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "Database exception", e);
			e.printStackTrace();
			return 0;
		}
	}

}
