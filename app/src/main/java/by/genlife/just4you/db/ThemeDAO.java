package by.genlife.just4you.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import by.genlife.just4you.model.Theme;

/**
 * Created by NotePad.by on 23.02.2016.
 */
public class ThemeDAO extends AbstractDAO<Theme> {

    public static final String TABLE = "THEMES";
    public static final String NAME = "ThemeNm";
    public static final String TOPIC_ID = "TopicId";

    public ThemeDAO(Context context) {
        super(context);
    }

    @Override
    public ContentValues buildContentValues(Theme object) {
        ContentValues cv = new ContentValues();
        cv.put(ID, object.id);
        cv.put(NAME, object.name);
        cv.put(TOPIC_ID, object.topicID);
        return cv;
    }

    @Override
    public Theme buildObject(Cursor cursor) {
        Theme theme = new Theme();
        theme.id = cursor.getLong(cursor.getColumnIndex(ID));
        theme.name = cursor.getString(cursor.getColumnIndex(NAME));
        theme.topicID = cursor.getLong(cursor.getColumnIndex(TOPIC_ID));
        return theme;
    }

    @Override
    protected String getTableName() {
        return TABLE;
    }

    public static String buildCreateTableString() {
        TableBuilder builder = new TableBuilder(TABLE);
        builder.addTextColumn(NAME);
        builder.addIntegerColumn(TOPIC_ID);
        return builder.buildCreateTableString();
    }

    public Cursor findAll(long topicId) {
        return where(TOPIC_ID + "=" + topicId, NAME);
    }
}
