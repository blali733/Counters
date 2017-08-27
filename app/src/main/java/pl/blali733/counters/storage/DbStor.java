package pl.blali733.counters.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import pl.blali733.counters.exceptions.TransactionException;
import pl.blali733.counters.storage.data.CounterListElement;
import pl.blali733.counters.storage.data.LocalElement;

/**
 * Class containing local database logic.
 * @author blali733
 * @version 1.2
 * @since 0.2 app / 1.0 pkg
 */
public class DbStor extends SQLiteOpenHelper {
    //DOCME fields
    private static final int DB_VERSION = 5;
    private static final String DB_NAME = "Counters";
    static final String TABLE_MAIN = "counters";
    static final String TABLE_HISTORY = "history";

    // Contacts Table Columns names
    static final String KEY_ID = "id";
    static final String KEY_AUTH = "account";
    static final String KEY_LABEL = "label";
    static final String KEY_V1 = "v1";
    static final String KEY_V2 = "v2";
    static final String KEY_MIXED = "mixed";
    static final String KEY_DIRTY = "dirty";
    static final String KEY_UUID = "uuid";
    static final String KEY_DELETED = "del";
    static final String KEY_OPERATION1 = "operation1";
    static final String KEY_OPERATION2 = "operation2";
    static final String KEY_TIME = "timestamp";

    private static final String TAG = "Local DB handler";

    /**
     * Constructor establishing internal database connection.
     * @param context Application context.
     * @since 1.0
     */
    public DbStor(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Method responsible for creation of database.
     * @param db Database handle.
     * @since 1.0
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_MAIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_AUTH + " TEXT,"
                + KEY_LABEL + " TEXT,"
                + KEY_V1 + " INTEGER,"
                + KEY_V2 + " INTEGER,"
                + KEY_MIXED + " TEXT,"
                + KEY_DIRTY + " TEXT,"
                + KEY_UUID + " TEXT,"
                + KEY_DELETED + " TEXT );";
        db.execSQL(CREATE_TABLE);
        CREATE_TABLE = "CREATE TABLE " + TABLE_HISTORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_AUTH + " TEXT,"
                + KEY_UUID + " TEXT,"
                + KEY_OPERATION1 + " TEXT,"
                + KEY_OPERATION2 + " TEXT,"
                + KEY_TIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP );";
        db.execSQL(CREATE_TABLE);
    }

    /**
     * Method responsible for database upgrades.
     * @param db Database handle.
     * @param oldVersion Old version number.
     * @param newVersion New version number.
     * @since 1.0
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch(oldVersion) {
            case 1: {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAIN);
                onCreate(db);
            }break;
            case 2: {
                DbUpdater.UpdateV2(db);
            }//Lack of break is intentional.
            case 3:{
                DbUpdater.UpdateV3(db);
            }//Lack of break is intentional.
            case 4:{
                DbUpdater.UpdateV4(db);
            }//Lack of break is intentional.

        }
    }

    /**
     * Method responsible for adding new element to database
     * @param localElement Element to be created.
     * @since 1.0
     */
    public void addLocalElement(LocalElement localElement) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            values.put(KEY_AUTH, mAuth.getCurrentUser().getEmail());
        }else{
            values.put(KEY_AUTH, "localhost");
        }
        values.put(KEY_LABEL, localElement.getLabel());
        values.put(KEY_V1, localElement.getV1());
        values.put(KEY_V2, localElement.getV2());
        values.put(KEY_MIXED, localElement.getMixed());
        values.put(KEY_DIRTY, localElement.getDirty());
        values.put(KEY_UUID, localElement.getUuid().toString());
        values.put(KEY_DELETED, "false");

        db.insert(TABLE_MAIN,null,values);
        db.close();
    }

    /**
     * Method responsible for getting element from database depending on its id.
     * @param id Id of desired element.
     * @return Found element or null.
     * @since 1.0
     * @deprecated Use UUID based version. {@link pl.blali733.counters.storage.DbStor#getLocalElement(UUID)}
     */
    //TESTME
    @Deprecated
    public LocalElement getLocalElement(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        LocalElement cElem = null;
        Cursor cursor;
        try {
            cursor = db.query(TABLE_MAIN, new String[]{
                            KEY_ID, KEY_AUTH, KEY_LABEL, KEY_V1, KEY_V2, KEY_MIXED, KEY_DIRTY, KEY_UUID, KEY_DELETED
                    }, KEY_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);
        }catch(NullPointerException ex){
            cursor = null;
        }
        if (cursor != null) {
            cursor.moveToFirst();
            cElem = new LocalElement(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    Integer.parseInt(cursor.getString(3)),
                    Integer.parseInt(cursor.getString(4)),
                    cursor.getString(5),
                    cursor.getString(6),
                    UUID.fromString(cursor.getString(7)),
                    cursor.getString(8));
            cursor.close();
        }
        db.close();
        // return contact
        return cElem;
    }

    /**
     * Method responsible for getting element from database depending on its uuid.
     * @param uuid Uuid of desired element.
     * @return Found element or null.
     * @since 1.2
     */
    //TESTME
    public LocalElement getLocalElement(UUID uuid){
        SQLiteDatabase db = this.getReadableDatabase();
        LocalElement cElem = null;
        Cursor cursor;
        try {
            cursor = db.query(TABLE_MAIN, new String[]{
                            KEY_ID, KEY_AUTH, KEY_LABEL, KEY_V1, KEY_V2, KEY_MIXED, KEY_DIRTY, KEY_UUID, KEY_DELETED
                    }, KEY_UUID + "=?",
                    new String[]{uuid.toString()}, null, null, null, null);
        }catch(NullPointerException ex){
            cursor = null;
        }
        if (cursor != null) {
            cursor.moveToFirst();
            cElem = new LocalElement(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    Integer.parseInt(cursor.getString(3)),
                    Integer.parseInt(cursor.getString(4)),
                    cursor.getString(5),
                    cursor.getString(6),
                    UUID.fromString(cursor.getString(7)),
                    cursor.getString(8));
            cursor.close();
        }
        db.close();
        // return contact
        return cElem;
    }

    /**
     * Method getting number of elements in database.
     * @return Number of elements.
     * @since 1.0
     */
    //TESTME
    public int getLocalElementCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MAIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count;
        count = cursor.getCount();
        cursor.close();
        db.close();
        // return count
        return count;
    }

    /**
     * Method responsible for updating element of database.
     * @param localElement Element with updated values.
     * @return Result of update procedure.
     * @throws TransactionException Exception when transaction failed and data cannot be persisted.
     * @since 1.0
     */
    //TESTME check if update is properly performed
    public int updateLocalElement(LocalElement localElement, int dv1, int dv2) throws TransactionException {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_V1, localElement.getV1());
        values.put(KEY_V2, localElement.getV2());
        values.put(KEY_DIRTY, "true");

        ContentValues mvalues = new ContentValues();
        mvalues.put(KEY_AUTH, localElement.getAuth());
        mvalues.put(KEY_UUID, localElement.getUuid().toString());
        mvalues.put(KEY_OPERATION1, Integer.toString(dv1));
        mvalues.put(KEY_OPERATION2, Integer.toString(dv2));

        int result;
        db.beginTransaction();
        {
            long res = db.insert(TABLE_HISTORY,null,mvalues);
            result = db.update(TABLE_MAIN, values, KEY_UUID + " = ?", new String[]{localElement.getUuid().toString()});
            if(res != -1 && result > 0){
                db.setTransactionSuccessful();
            }else{
                db.endTransaction();
                db.close();
                throw new TransactionException();
            }
        }
        db.endTransaction();
        db.close();
        return result;
    }

    /**
     * Method responsible for transferring ownership of counter.
     * @param localElement Element which owner should be updated
     * @return Result of update procedure.
     * @since 1.1
     */
    //TESTME check if update is properly performed
    public int updateOwnerOfLocalElement(LocalElement localElement) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            values.put(KEY_AUTH, mAuth.getCurrentUser().getEmail());
        }else{
            values.put(KEY_AUTH, "localhost");
        }
        int result = db.update(TABLE_MAIN,values,KEY_UUID+" = ?",new String[]{localElement.getUuid().toString()});
        db.close();
        return result;
    }

    /**
     * Method responsible for deleting elements from database.
     * @param localElement Element to be deleted.
     * @since 1.0
     * @deprecated Use tagging instead.
     */
    //TESTME
    @Deprecated
    public void deleteLocalElement(LocalElement localElement) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MAIN, KEY_ID + " = ?",
                new String[] { String.valueOf(localElement.getId()) });
        db.close();
    }

    /**
     * Method responsible for deleting elements from database.
     * @param localElement Element to be deleted.
     * @since 1.2
     */
    //TESTME
    public void tagDeletedLocalElement(LocalElement localElement) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DELETED,"true");
        db.update(TABLE_MAIN,values,KEY_UUID+" = ?",new String[]{localElement.getUuid().toString()});
        db.close();
    }

    /**
     * Method getting list of counters belonging to current user.
     * @param auth Email of user which is active.
     * @return List of elements from database.
     * @since 1.0
     */
    public List<CounterListElement> displayList(String auth){
        String countQuery = "SELECT  * FROM " + TABLE_MAIN +" WHERE "+KEY_AUTH+" LIKE '"+auth+"' AND " + KEY_DELETED + "LIKE 'false'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        List<CounterListElement> list = new ArrayList<>();
        CounterListElement elem;
        while(cursor.moveToNext()){
            elem = new CounterListElement();
            elem.setLabel(cursor.getString(2));
            elem.setV1(Integer.parseInt(cursor.getString(3)));
            elem.setV2(Integer.parseInt(cursor.getString(4)));
            elem.setMixed(Boolean.parseBoolean(cursor.getString(5)));
            elem.setUuid(cursor.getString(7));
            list.add(elem);
        }
        cursor.close();
        db.close();
        return list;
    }
}
