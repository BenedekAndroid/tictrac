package trictrack.benedek.com.tictracexercise.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benedek on 2016.08.12..
 */
public class Dao {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "user_db";
    private static final String DATABASE_TABLE = "users";

    private static final String KEY_ID = "id";

    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";
    private static final String KEY_INFOS = "infos";

    private DaoHelper dbHelper;
    private final Context context;
    private SQLiteDatabase database;

    private static class DaoHelper extends SQLiteOpenHelper {

        public DaoHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_TABLE = "CREATE TABLE " + DATABASE_TABLE + "(" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    KEY_EMAIL + " TEXT," +
                    KEY_NAME + " TEXT," +
                    KEY_INFOS + " TEXT" + ")";
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    public Dao(Context context) {
        this.context = context;
    }

    public Dao open() {
        dbHelper = new DaoHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }



    public void save(User user) {

        ContentValues values = new ContentValues();

        values.put(KEY_EMAIL, user.getEmail());

        values.put(KEY_NAME, user.getName());

        values.put(KEY_INFOS, user.getInfos());

        database.insert(DATABASE_TABLE, null, values);
    }



    public boolean isItSaved(User user) {

        String query = "SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_EMAIL + " = '" +user.getEmail()+"'";
        Cursor c = database.rawQuery(query, null);

        boolean result = false;

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result = true;
        }

        return result;

    }

    public List<User> getUsers() {

        String query = "SELECT * FROM "+DATABASE_TABLE;
        Cursor c = database.rawQuery(query, null);

        int iID = c.getColumnIndex(KEY_ID);
        int iEmail = c.getColumnIndex(KEY_EMAIL);
        int iName = c.getColumnIndex(KEY_NAME);
        int iInfos = c.getColumnIndex(KEY_INFOS);

        List<User> users = new ArrayList<>();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

            User user = new User(c.getString(iID), c.getString(iEmail), c.getString(iName), c.getString(iInfos));
            users.add(user);
        }

        c.close();
        return users;
    }

    public void deleteAllUser() {
        String query = "DROP TABLE "+DATABASE_TABLE;
        database.rawQuery(query, null);
    }
}
