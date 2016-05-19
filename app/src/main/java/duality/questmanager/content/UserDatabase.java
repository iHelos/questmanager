package duality.questmanager.content;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by olegermakov on 13.05.16.
 */
public class UserDatabase {
    //primary
    public static final String KEY_ROWID = "_id";
    //others
    public static final String KEY_USER = "email";

    private static final String LOG_TAG = "UserDB";

    public static final String SQLITE_TABLE = "User";

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_USER + "," +
                    ");";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
        onCreate(db);
    }
}
