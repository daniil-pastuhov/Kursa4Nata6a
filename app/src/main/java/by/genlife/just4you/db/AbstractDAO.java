package by.genlife.just4you.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import by.genlife.just4you.model.AbstractModel;

/**
 * Created by NotePad.by on 21.02.2016.
 */
public abstract class AbstractDAO<T extends AbstractModel> {

    public static final String ID = BaseColumns._ID;

    protected Context mContext;
    protected DBHelper mDbHelper;

    public AbstractDAO(Context context) {
        mContext = context;
        mDbHelper = DBHelper.getInstance(mContext);
    }

    public abstract ContentValues buildContentValues(T object);

    public abstract T buildObject(Cursor cursor);

    protected abstract String getTableName();

    protected SQLiteDatabase getDb() {
        return mDbHelper.getWritableDatabase();
    }

    public long insert(T object) {
        if (object == null) {
            return 0;
        }

        ContentValues cv = buildContentValues(object);
        if (cv.getAsInteger(ID) == null || cv.getAsInteger(ID) == 0) {
            cv.remove(ID);
        }

        long id = getDb().insert(getTableName(), null, cv);
        object.id = (long) id;

        return id;
    }

    public int insert(List<T> list) {
        int count = 0;
        if (list != null) {
            for (T item : list) {
                if (insert(item) > 0) {
                    ++count;
                }
            }
        }
        return count;
    }

    public List<T> buildObjects(Cursor cursor) {
        List<T> list = new ArrayList<T>();
        if (cursor.moveToFirst()) {
            do {
                list.add(buildObject(cursor));
            } while (cursor.moveToNext());
        }
        return list;
    }

    public long update(T object) {
        ContentValues cv = buildContentValues(object);
        getDb().update(getTableName(), cv, ID + " = " + object.id, null);
        return object.id;
    }

    public long insertOrUpdate(T object) {
        if (object.id > 0) {
            return update(object);
        } else {
            return insert(object);
        }
    }

    public Cursor where(String selection, String orderBy) {
        return getDb().query(getTableName(), null, selection, null, null, null, orderBy);
    }

    public Cursor where(String selection) {
        return where(selection, null);
    }

    public Cursor findAll() {
        return where(null);
    }

    public T find(long id) {
        Cursor cursor = where(ID + " = " + id);
        if (cursor.moveToFirst()) {
            return buildObject(cursor);
        } else {
            return null;
        }
    }

    public T findBy(String selection, String orderBy) {
        Cursor cursor = where(selection, orderBy);
        if (cursor != null && cursor.moveToFirst()) {
            return buildObject(cursor);
        } else {
            return null;
        }
    }

    public T findBy(String selection) {
        return findBy(selection, null);
    }

    public Cursor findNone() {
        return getDb().query(getTableName(), null, null, null, null, null, null, "0");
    }

    public int deleteWhere(String where) {
        return getDb().delete(getTableName(), where, null);
    }

    public int delete(long id) {
        return deleteWhere(ID + " = " + id);
    }

    public int delete(T object) {
        return deleteWhere(ID + " = " + object.id);
    }

    public int deleteAll() {
        return deleteWhere(null);
    }

    public T findFirst() {
        return findBy(null, ID);
    }

    public void beginTransaction() {
        getDb().beginTransaction();
    }

    public void commitTransaction() {
        getDb().setTransactionSuccessful();
        getDb().endTransaction();
    }

    public void cancelTransaction() {
        getDb().endTransaction();
    }

    protected static String SQL_FIELDS(String table, String... fields) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < fields.length; ++i) {
            if (i != 0) {
                builder.append(", ");
            }
            builder.append(table).append('.').append(fields[i]);
        }
        return builder.toString();
    }

    protected Cursor findAllWithEmpty(int fieldCount, String orderBy) {
        return findAllWithEmpty(fieldCount, null, orderBy);
    }

    protected Cursor findAllWithEmpty(int fieldCount, String selection, String orderBy) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM ").append(getTableName());
        if (!TextUtils.isEmpty(selection)) {
            queryBuilder.append(" WHERE ").append(selection);
        }
        queryBuilder.append(" UNION SELECT ''");
        for (int i = 1; i < fieldCount; ++i) {
            queryBuilder.append(", ''");
        }
        queryBuilder.append(" ORDER BY ").append(orderBy);
        return getDb().rawQuery(queryBuilder.toString(), null);
    }

    protected static String SQL_FIELDS_ALL(String table) {
        return table + ".*";
    }

    protected static String SQL_COUNT(String table, String field, String as) {
        StringBuilder builder = new StringBuilder("COUNT(");
        if (!TextUtils.isEmpty(table)) {
            builder.append(table).append(".");
        }
        builder.append(field).append(") AS ").append(as);
        return builder.toString();
    }

    protected static String SQL_JOIN(String jointType, String leftTable, String leftField, String rightTable, String rightField) {
        return " " + jointType + " JOIN " + rightTable + " ON " + leftTable + "." + leftField + " = " + rightTable + "." + rightField;
    }

    protected static String SQL_INNER_JOIN(String leftTable, String leftField, String rightTable, String rightField) {
        return SQL_JOIN("INNER", leftTable, leftField, rightTable, rightField);
    }

    protected static String SQL_LEFT_JOIN(String leftTable, String leftField, String rightTable, String rightField) {
        return SQL_JOIN("LEFT", leftTable, leftField, rightTable, rightField);
    }

    public String ORDER_BY(String field) {
        return " ORDER BY " + SQL_FIELDS(getTableName(), field);
    }

    public String ORDER_DESK_BY(String field) {
        return " ORDER BY " + SQL_FIELDS(getTableName(), field) + " DESC";
    }
}
