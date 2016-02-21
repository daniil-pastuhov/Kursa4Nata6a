package by.genlife.just4you.db;

/**
 * Created by NotePad.by on 21.02.2016.
 */
public class TableBuilder {

    public static final String TYPE_TEXT = "TEXT";
    public static final String TYPE_INTEGER = "INTEGER";
    public static final String TYPE_REAL = "REAL";
    public static final String TYPE_BLOB = "BLOB";

    private StringBuilder mStringBuilder;

    public TableBuilder(String table) {
        mStringBuilder = new StringBuilder("CREATE TABLE ");
        mStringBuilder.append(table).append("(").append(AbstractDAO.ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT");
    }

    public void addColumn(String columnName, String columnType) {
        mStringBuilder.append(", ").append(columnName).append(" ").append(columnType);
    }

    public void addColumn(String columnName, String columnType, String defaultValue) {
        mStringBuilder.append(", ").append(columnName).append(" ").append(columnType).append(" DEFAULT ")
                .append(defaultValue);
    }

    public void addTextColumn(String column) {
        addColumn(column, TYPE_TEXT);
    }

    public void addBlobColumn(String column) {
        addColumn(column, TYPE_BLOB);
    }

    public void addIntegerColumn(String column) {
        addColumn(column, TYPE_INTEGER);
    }

    public void addIntegerColumn(String column, int defaultValue) {
        addColumn(column, TYPE_INTEGER, String.valueOf(defaultValue));
    }

    public void addRealColumn(String column) {
        addColumn(column, TYPE_REAL);
    }

    public String buildCreateTableString() {
        return mStringBuilder.append(");").toString();
    }

    public static String alterTableAddColumn(String tableName, String columnName, String columnType, int defaultValue) {
        return "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnType +
                " DEFAULT " + defaultValue;
    }

    public static String alterTableAddColumn(String tableName, String columnName, String columnType) {
        return "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnType;
    }

    public static String createIndex(String index, String table, String column) {
        return "CREATE INDEX " + index + " ON " + table + " (" + column + ")";
    }

}