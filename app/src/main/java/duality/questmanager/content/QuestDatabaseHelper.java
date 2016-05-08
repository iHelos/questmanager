package duality.questmanager.content;

/**
 * Created by olegermakov on 17.04.16.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import duality.questmanager.Task;

public class QuestDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QuestManagerDB";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;
    public QuestDatabaseHelper(Context context) {
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

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> taskList = new ArrayList<Task>();
        String selectQuery = String.format("SELECT %s, %s, %s FROM %s", QuestDatabase.KEY_NAME, QuestDatabase.KEY_TEXT, QuestDatabase.KEY_PRICE, QuestDatabase.SQLITE_TABLE);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //int costint = cursor.getInt(2);
                Task quest = new Task(cursor.getString(0), cursor.getInt(2));
                taskList.add(quest);
            } while (cursor.moveToNext());
        }
        db.close();
        return taskList;
    }

    public boolean addTask(String title, String text, int price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(QuestDatabase.KEY_NAME, title);
        values.put(QuestDatabase.KEY_TEXT, text);
        values.put(QuestDatabase.KEY_PRICE, price);
        db.insert(QuestDatabase.SQLITE_TABLE, null, values);
        db.close();
        return true;
    }

}