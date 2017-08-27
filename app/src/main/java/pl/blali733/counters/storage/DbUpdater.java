package pl.blali733.counters.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.UUID;

import static pl.blali733.counters.storage.DbStor.*;

/**
 * Scripts updating local database.
 * @author blali733
 * @version 1.1
 * @since 0.4 app / 1.1 pkg
 */
class DbUpdater {

    /**
     * Debug (logging) switch.
     */
    private static final boolean DEBUG = true;

    /**
     * Logging tag.
     */
    private static final String TAG = "DB_Updater";

    /**
     * Add and generate UUIDs.
     * @param db Database handle
     * @since 1.0
     */
    static void UpdateV2(SQLiteDatabase db){
        //ASSUMING local storage only!
        db.execSQL("ALTER TABLE " + TABLE + " ADD " + KEY_UUID + " TEXT;");
        String countQuery = "SELECT  "+KEY_ID+" FROM " + TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        while(cursor.moveToNext()){
            ContentValues values = new ContentValues();
            values.put(KEY_UUID, UUID.randomUUID().toString());
            db.update(TABLE,values,KEY_ID+" = ?",new String[]{String.valueOf(cursor.getInt(0))});
        }
        cursor.close();
        if(DEBUG){
            countQuery = "SELECT  "+KEY_ID + ", " + KEY_UUID + " FROM " + TABLE;
            cursor = db.rawQuery(countQuery, null);
            while (cursor.moveToNext()){
                Log.d(TAG,cursor.getString(0) + " - " + cursor.getString(1));
            }
            cursor.close();
        }
    }

    /**
     * Add and generate Deleted tags.
     * @param db Database handle
     * @since 1.0
     */
    static void UpdateV3(SQLiteDatabase db){
        db.execSQL("ALTER TABLE " + TABLE + " ADD " + KEY_DELETED + " TEXT;");
        String countQuery = "SELECT  "+KEY_UUID+" FROM " + TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        while(cursor.moveToNext()){
            ContentValues values = new ContentValues();
            values.put(KEY_DELETED,"false");
            db.update(TABLE,values,KEY_UUID+" = ?",new String[]{String.valueOf(cursor.getInt(0))});
        }
        cursor.close();
        if(DEBUG){
            countQuery = "SELECT  "+KEY_ID + ", " + KEY_UUID + ", " + KEY_DELETED + " FROM " + TABLE;
            cursor = db.rawQuery(countQuery, null);
            while (cursor.moveToNext()){
                Log.d(TAG,cursor.getString(0) + " - " + cursor.getString(1) + " - " + cursor.getString(2));
            }
            cursor.close();
        }
    }
}
