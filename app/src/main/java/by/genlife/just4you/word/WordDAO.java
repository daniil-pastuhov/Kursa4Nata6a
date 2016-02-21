package by.genlife.just4you.word;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import by.genlife.just4you.db.AbstractDAO;
import by.genlife.just4you.db.TableBuilder;
import by.genlife.just4you.model.Word;

/**
 * Created by NotePad.by on 21.02.2016.
 */
public class WordDAO extends AbstractDAO<Word> {

    public static final String TABLE = "Word";
    public static final String NAME = "WordNm";
    public static final String TRANSLATIONS = "Translations";
    public static final String IS_FAVORITE = "IsFavorite";

    public WordDAO(Context context) {
        super(context);
    }

    @Override
    public ContentValues buildContentValues(Word word) {
        ContentValues cv = new ContentValues();
        cv.put(ID, word.id);
        cv.put(NAME, word.name);
        cv.put(IS_FAVORITE, word.isFavorite);
        cv.put(TRANSLATIONS, word.serializeTranslations());
        return cv;
    }

    @Override
    public Word buildObject(Cursor cursor) {
        Word word = new Word();
        word.id = cursor.getLong(cursor.getColumnIndex(ID));
        word.name = cursor.getString(cursor.getColumnIndex(NAME));
        word.isFavorite = cursor.getInt(cursor.getColumnIndex(IS_FAVORITE)) == 1;
        word.parseTranslations(cursor.getString(cursor.getColumnIndex(TRANSLATIONS)));
        return word;
    }

    @Override
    protected String getTableName() {
        return TABLE;
    }

    public static String buildCreateTableString() {
        TableBuilder builder = new TableBuilder(TABLE);
        builder.addTextColumn(NAME);
        builder.addIntegerColumn(IS_FAVORITE);
        builder.addTextColumn(TRANSLATIONS);
        return builder.buildCreateTableString();
    }

    public Cursor findAllByName(String filter) {
        String query = NAME + " LIKE '" + filter + "'";
        return where(query, NAME);
    }

    public void changeFavorite(long id) {
        final Word product = find(id);
        product.isFavorite = !product.isFavorite;
        update(product);
    }

    @Override
    public Cursor findAll() {
        return super.where(null, NAME);
    }

    public Cursor findAllRand() {
        return where(null, "RANDOM()");
    }
}