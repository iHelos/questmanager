package duality.questmanager.content;

/**
 * Created by olegermakov on 17.04.16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import duality.questmanager.Task;

public class QuestDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QuestManagerDB";
    private static final int DATABASE_VERSION = 6;

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

    public ArrayList<Task> getAllTasks(boolean output) {
        ArrayList<Task> taskList = new ArrayList<Task>();
        String selectQuery;
        String table = QuestDatabase.SQLITE_TABLE_INPUT;
        if (output)
            table = QuestDatabase.SQLITE_TABLE_OUTPUT;

        selectQuery = String.format("SELECT %s, %s, %s, %s, %s FROM %s ORDER BY -%s",
                QuestDatabase.KEY_ROWBACKENDID,
                QuestDatabase.KEY_TITLE,
                QuestDatabase.KEY_TEXT,
                QuestDatabase.KEY_PRICE,
                QuestDatabase.KEY_DATE,
                table,
                QuestDatabase.KEY_ROWID
        );
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Task quest = new Task(cursor.getInt(0), cursor.getString(1), cursor.getInt(3), cursor.getString(4));
                taskList.add(quest);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskList;
    }

    public boolean addTask(int backendID, String title, String text, int price, String author, String date, String backend_hash, boolean output) {
        SQLiteDatabase db = this.getWritableDatabase();

        String table = QuestDatabase.SQLITE_TABLE_INPUT;
        if (output)
            table = QuestDatabase.SQLITE_TABLE_OUTPUT;


        String selectLastQuery = String.format("SELECT %1$s, %2$s FROM %3$s WHERE %4$s = (SELECT MAX(%4$s) FROM %3$s)",
                QuestDatabase.KEY_HASH,
                QuestDatabase.KEY_TITLE,
                table,
                QuestDatabase.KEY_ROWID
        );

        Cursor cursor = db.rawQuery(selectLastQuery, null);
        String hash;
        if (cursor.moveToFirst()) {
            String previous_hash = cursor.getString(0);
            String previous_title = cursor.getString(1);
            String code_text = previous_hash + previous_title;
            hash = code_text.hashCode() + "";
        } else {
            hash = "0";
        }

        int synced = 0;
        if (backend_hash.equals(hash)) {
            synced = 1;
        }

        Log.d("Hash_new", hash);
        Log.d("Synced", synced + "");
        ContentValues values = new ContentValues();

        values.put(QuestDatabase.KEY_ROWBACKENDID, backendID);
        values.put(QuestDatabase.KEY_TITLE, title);
        values.put(QuestDatabase.KEY_TEXT, text);
        values.put(QuestDatabase.KEY_PRICE, price);
        values.put(QuestDatabase.KEY_USER, author);
        values.put(QuestDatabase.KEY_DATE, date);
        values.put(QuestDatabase.KEY_HASH, hash);
        values.put(QuestDatabase.KEY_SYNCED, synced);

        if (output) {
            db.insert(QuestDatabase.SQLITE_TABLE_OUTPUT, null, values);
        } else {
            db.insert(QuestDatabase.SQLITE_TABLE_INPUT, null, values);
        }

        cursor.close();
        db.close();
        return true;
    }

    public boolean syncedAddTask(int backendID, String title, String text, int price, String author, String date, String backend_hash, boolean isCompleted, boolean output) {
        SQLiteDatabase db = this.getWritableDatabase();

        String table = QuestDatabase.SQLITE_TABLE_INPUT;
        if (output)
            table = QuestDatabase.SQLITE_TABLE_OUTPUT;

        ContentValues values = new ContentValues();

        values.put(QuestDatabase.KEY_ROWBACKENDID, backendID);
        values.put(QuestDatabase.KEY_TITLE, title);
        values.put(QuestDatabase.KEY_TEXT, text);
        values.put(QuestDatabase.KEY_PRICE, price);
        values.put(QuestDatabase.KEY_USER, author);
        values.put(QuestDatabase.KEY_DATE, date);
        values.put(QuestDatabase.KEY_HASH, backend_hash);
        values.put(QuestDatabase.KEY_SYNCED, 1);
        values.put(QuestDatabase.KEY_ISCOMPLETED, isCompleted);

        db.insert(table, null, values);
        db.close();
        return true;
    }

    public Task getTask(int backendID, Boolean output) {
        SQLiteDatabase db = this.getWritableDatabase();

        String table = QuestDatabase.SQLITE_TABLE_INPUT;
        if (output) {
            table = QuestDatabase.SQLITE_TABLE_OUTPUT;
        }

        String selectQuery = String.format("SELECT %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s = %s",
                QuestDatabase.KEY_ROWBACKENDID,
                QuestDatabase.KEY_TITLE,
                QuestDatabase.KEY_TEXT,
                QuestDatabase.KEY_USER,
                QuestDatabase.KEY_PRICE,
                QuestDatabase.KEY_DATE,
                QuestDatabase.KEY_HASH,
                table,
                QuestDatabase.KEY_ROWBACKENDID,
                "" + backendID
        );

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        String date = cursor.getString(5);
        String[] dateParts = date.split("-");

        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);

        Task task = new Task(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getInt(4),
                new GregorianCalendar(year, month - 1, day),
                cursor.getString(6)
        );

        cursor.close();
        db.close();
        return task;
    }

    public Task getLastSyncedTask(Boolean output) {
        SQLiteDatabase db = this.getWritableDatabase();

        String table = QuestDatabase.SQLITE_TABLE_INPUT;
        if (output) {
            table = QuestDatabase.SQLITE_TABLE_OUTPUT;
        }

        String selectQuery = String.format("SELECT %1$s, %2$s, %3$s, %4$s, %5$s, %6$s, %7$s FROM %8$s WHERE %9$s = (SELECT MAX(%9$s) FROM %8$s WHERE %10$s = 1)",
                QuestDatabase.KEY_ROWBACKENDID,
                QuestDatabase.KEY_TITLE,
                QuestDatabase.KEY_TEXT,
                QuestDatabase.KEY_USER,
                QuestDatabase.KEY_PRICE,
                QuestDatabase.KEY_DATE,
                QuestDatabase.KEY_HASH,
                table,
                QuestDatabase.KEY_ROWID,
                QuestDatabase.KEY_SYNCED
        );

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (!cursor.moveToFirst())
            return null;

        String date = cursor.getString(5);
        String[] dateParts = date.split("-");

        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);

        Task task = new Task(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getInt(4),
                new GregorianCalendar(year, month, day),
                cursor.getString(6)
        );

        cursor.close();
        db.close();
        return task;
    }

    public void deleteUnsynced(Boolean output) {
        SQLiteDatabase db = this.getWritableDatabase();

        String table = QuestDatabase.SQLITE_TABLE_INPUT;
        if (output) {
            table = QuestDatabase.SQLITE_TABLE_OUTPUT;
        }

        String deleteQuery = String.format("DELETE FROM %s WHERE %s = 0", table, QuestDatabase.KEY_SYNCED);

        db.execSQL(deleteQuery);

        db.close();
    }

    public ArrayList<String> getEmails() {
        ArrayList<String> emails = new ArrayList<String>();
        String selectQuery;

        selectQuery = String.format("SELECT DISTINCT * FROM (SELECT %s FROM %s UNION SELECT %s FROM %s)", QuestDatabase.KEY_USER, QuestDatabase.SQLITE_TABLE_OUTPUT, QuestDatabase.KEY_USER, QuestDatabase.SQLITE_TABLE_INPUT);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //int costint = cursor.getInt(2);
                String email = cursor.getString(0);
                emails.add(email);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return emails;
    }

    public void cleanDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + QuestDatabase.SQLITE_TABLE_INPUT);
        db.execSQL("DROP TABLE IF EXISTS " + QuestDatabase.SQLITE_TABLE_OUTPUT);

        db.execSQL(QuestDatabase.DATABASE_CREATE_INPUT);
        db.execSQL(QuestDatabase.DATABASE_CREATE_OUTPUT);

        db.close();
    }

}