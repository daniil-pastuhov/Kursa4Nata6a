package by.genlife.just4you.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by NotePad.by on 21.02.2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getName();
    public static final String DB_NAME = "db";
    public static final int DB_VERSION = 3;
    private static DBHelper mInstance;

    private DBHelper(Context context) {
        this(context, DB_NAME, DB_VERSION);
    }

    public static synchronized DBHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBHelper(context);
        }
        return mInstance;
    }

    public DBHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db, WordDAO.buildCreateTableString());
        createTable(db, ThemeDAO.buildCreateTableString());
        createTable(db, TopicDAO.buildCreateTableString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > 1) {
            createTable(db, ThemeDAO.buildCreateTableString());
            createTable(db, TopicDAO.buildCreateTableString());
        }
        if (newVersion > 2) {
            alterTableAddColumn(db, ThemeDAO.TABLE, ThemeDAO.TOPIC_ID, TableBuilder.TYPE_INTEGER, 0);
        }
    }

    protected void createTable(SQLiteDatabase db, String tableSql) {
        execSQL(db, tableSql);
    }

    protected void alterTableAddColumn(SQLiteDatabase db, String tableName, String columnName, String columnType,
                                       int defaultValue) {
        execSQL(db, TableBuilder.alterTableAddColumn(tableName, columnName, columnType, defaultValue));
    }

    protected void execSQL(SQLiteDatabase db, String sql) {
        try {
            db.execSQL(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
