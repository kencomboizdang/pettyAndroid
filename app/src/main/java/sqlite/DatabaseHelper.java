package sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "login.db";
    public static final String TABLE_NAME1 = "loginsqlite";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "Username";
    public static final String COL_3 = "Password";

    public static final String TABLE_NAME2 = "customersqlite";
    public static final String COL_21 = "id";
    public static final String COL_22 = "AccountId";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME1 + "("+ COL_1 +" TEXT PRIMARY KEY" +
                ", "+ COL_2 +" TEXT, " + COL_3 + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_NAME2 + "(" + COL_21 + " TEXT PRIMARY KEY, "+ COL_22 +" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
    }

    public void insertAccount(String id, String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, password);
        long result = db.insert(TABLE_NAME1, null, contentValues);
    }

    public Cursor checkData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME1, null);
        return res;
    }

    public void insertCustomer(String id, String accountId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_21, id);
        contentValues.put(COL_22, accountId);
        long result = db.insert(TABLE_NAME2, null, contentValues);
    }

    public Cursor getKeyCustomer() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME2, null);
        return res;
    }

    public void dropTable1() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME1);
    }

    public void dropTable2() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME2);
    }
}
