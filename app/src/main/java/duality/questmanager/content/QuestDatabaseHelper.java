package duality.questmanager.content;

/**
 * Created by olegermakov on 17.04.16.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuestDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QuestManagerDB";
    private static final int DATABASE_VERSION = 1;

    QuestDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        QuestDatabase.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        QuestDatabase.onUpgrade(db, oldVersion, newVersion);
    }

}