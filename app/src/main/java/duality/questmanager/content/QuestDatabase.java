package duality.questmanager.content;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by olegermakov on 17.04.16.
 */
public class QuestDatabase {
    //primary
    public static final String KEY_ROWID = "_id";
    //unique
    public static final String KEY_ROWBACKENDID = "_backendid";
    //others
    public static final String KEY_USER = "user";
    public static final String KEY_DATE= "date";
    public static final String KEY_TITLE = "name";
    public static final String KEY_TEXT = "text";
    public static final String KEY_PRICE = "price";
    public static final String KEY_HASH = "hash";
    public static final String KEY_SYNCED = "sync";
    public static final String KEY_ISCOMPLETED = "is_completed";

    private static final String LOG_TAG = "QuestDB";

    public static final String SQLITE_TABLE_OUTPUT = "QuestOutput";
    public static final String SQLITE_TABLE_INPUT = "QuestInput";

    public static final String DATABASE_CREATE_OUTPUT =
            "CREATE TABLE if not exists " + SQLITE_TABLE_OUTPUT + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_ROWBACKENDID + "," +
                    KEY_USER + "," +
                    KEY_DATE + "," +
                    KEY_TITLE + "," +
                    KEY_TEXT + "," +
                    KEY_PRICE + " integer," +
                    KEY_HASH + "," +
                    KEY_SYNCED + " integer," +
                    KEY_ISCOMPLETED + " integer default 0," +
                    " UNIQUE (" + KEY_ROWBACKENDID +"));";

    public static final String DATABASE_CREATE_INPUT =
            "CREATE TABLE if not exists " + SQLITE_TABLE_INPUT + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_ROWBACKENDID + "," +
                    KEY_USER + "," +
                    KEY_DATE + "," +
                    KEY_TITLE + "," +
                    KEY_TEXT + "," +
                    KEY_PRICE + " integer," +
                    KEY_HASH + "," +
                    KEY_SYNCED + " integer," +
                    KEY_ISCOMPLETED + " integer default 0," +
                    " UNIQUE (" + KEY_ROWBACKENDID +"));";


    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_INPUT);
        db.execSQL(DATABASE_CREATE_OUTPUT);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE_INPUT);
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE_OUTPUT);
        onCreate(db);
    }

}
