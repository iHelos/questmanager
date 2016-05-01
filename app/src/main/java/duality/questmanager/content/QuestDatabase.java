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
    public static final String KEY_CREATOR = "creator";
    public static final String KEY_NAME = "name";
    public static final String KEY_TEXT = "text";
    public static final String KEY_PRICE = "price";
    public static final String KEY_ISCOMPLETED = "is_completed";


    private static final String LOG_TAG = "QuestDB";

    public static final String SQLITE_TABLE = "Quest";

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_ROWBACKENDID + "," +
                    KEY_CREATOR + "," +
                    KEY_NAME + "," +
                    KEY_TEXT + "," +
                    KEY_PRICE + "," +
                    KEY_ISCOMPLETED + "," +
                    " UNIQUE (" + KEY_ROWBACKENDID +"));";

    public static void onCreate(SQLiteDatabase db) {
        Log.w(LOG_TAG, DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
        onCreate(db);
    }

}
