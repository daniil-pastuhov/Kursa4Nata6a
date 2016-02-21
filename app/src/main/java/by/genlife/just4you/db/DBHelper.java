package by.genlife.just4you.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import by.genlife.just4you.word.WordDAO;

/**
 * Created by NotePad.by on 21.02.2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getName();
    public static final String DB_NAME = "db";
    public static final int DB_VERSION = 1;
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

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    protected void createTable(SQLiteDatabase db, String tableSql) {
        execSQL(db, tableSql);
    }

    protected void execSQL(SQLiteDatabase db, String sql) {
        try {
            db.execSQL(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
