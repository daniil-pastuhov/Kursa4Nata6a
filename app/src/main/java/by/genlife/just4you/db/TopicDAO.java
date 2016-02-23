package by.genlife.just4you.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import by.genlife.just4you.model.Topic;

/**
 * Created by NotePad.by on 23.02.2016.
 */
public class TopicDAO extends AbstractDAO<Topic> {

    public static final String TABLE = "Topics";
    public static final String NAME = "TopicNm";
    public static final String THEMES = "THEMES";

    public TopicDAO(Context context) {
        super(context);
    }

    @Override
    public ContentValues buildContentValues(Topic topic) {
        ContentValues cv = new ContentValues();
        cv.put(ID, topic.id);
        cv.put(NAME, topic.name);
        cv.put(THEMES, topic.serializeThemes());
        return cv;
    }

    @Override
    public Topic buildObject(Cursor cursor) {
        Topic topic = new Topic();
        topic.id = cursor.getLong(cursor.getColumnIndex(ID));
        topic.name = cursor.getString(cursor.getColumnIndex(NAME));
        topic.parseThemes(cursor.getString(cursor.getColumnIndex(THEMES)));
        return topic;
    }

    @Override
    protected String getTableName() {
        return TABLE;
    }

    public static String buildCreateTableString() {
        TableBuilder builder = new TableBuilder(TABLE);
        builder.addTextColumn(NAME);
        builder.addTextColumn(THEMES);
        return builder.buildCreateTableString();
    }

    @Override
    public Cursor findAll() {
        return where(null, NAME);
    }
}
