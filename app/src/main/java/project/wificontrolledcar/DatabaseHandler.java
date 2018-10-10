package project.wificontrolledcar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * DatabaseHandler.java
 *
 * This is a class that represents the local database. The database is under construction. It is
 * inefficient when it comes to looking for an android user in the database.
 *
 * The tutorial at http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/ was
 * used as a reference.
 *
 * @author Jonas Eiselt
 * @since 2016-05-21
 */
public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final String TABLE_NAME = "androidUsers";

    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";
    private static final String USER_PASS = "user_pass";

    private static final String CLIENT_NAME = "client_name";
    private static final String CLIENT_PASS = "client_pass";

    private static final String IP_ADDR = "ip_addr";
    private static final String TCP_PORT = "tcp_port";

    public DatabaseHandler(Context context)
    {
        super(context, "androidusers.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query = "CREATE TABLE " + TABLE_NAME + "(" + USER_ID + " INTEGER PRIMARY KEY," + USER_NAME + " TEXT,"
                + USER_PASS + " TEXT," + CLIENT_NAME + " TEXT," + CLIENT_PASS + " TEXT," + IP_ADDR + " TEXT,"
                + TCP_PORT + " INTEGER" + ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    /**
     * This is a public method that adds an AndroidUser object to the database.
     * @param androidUser to be added
     */
    public void addAndroidUser(AndroidUser androidUser)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        // Constructs row data
        ContentValues values = new ContentValues();
        values.put(USER_NAME, androidUser.getUserName());
        values.put(USER_PASS, androidUser.getPassword());
        values.put(CLIENT_NAME, androidUser.getClientName());
        values.put(CLIENT_PASS, androidUser.getClientPass());
        values.put(IP_ADDR, androidUser.getIpAddress());
        values.put(TCP_PORT, androidUser.getTcpPort());

        // Inserts row data
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    /**
     * This is a public method that returns an AndroidUser object with the given id.
     * @param id to be looked for
     * @return an AndroidUser with id
     */
    public AndroidUser getAndroidUser(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { USER_ID,
                        USER_NAME, USER_PASS , CLIENT_NAME, CLIENT_PASS, IP_ADDR, TCP_PORT}, USER_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
        }

        AndroidUser androidUser = new AndroidUser(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6));
        return androidUser;
    }

    /**
     * This is a public method that updates an AndroidUser object.
     * @param androidUser to be updated
     */
    public void updateAndroidUser(AndroidUser androidUser)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        // Constructs row data
        ContentValues values = new ContentValues();
        values.put(USER_NAME, androidUser.getUserName());
        values.put(USER_PASS, androidUser.getPassword());
        values.put(CLIENT_NAME, androidUser.getClientName());
        values.put(CLIENT_PASS, androidUser.getClientPass());
        values.put(IP_ADDR, androidUser.getIpAddress());
        values.put(TCP_PORT, androidUser.getTcpPort());

        // Inserts row data
        db.insert(TABLE_NAME, null, values);

        // Updates row data
        db.update(TABLE_NAME, values, USER_ID + " = ?", new String[] { String.valueOf(androidUser.getId()) });
    }

    /**
     * This is a public method that deletes an AndroidUser object from the database.
     * @param androidUser to be deleted
     */
    public void deleteAndroidUser(AndroidUser androidUser)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, USER_ID + " = ?", new String[] { String.valueOf(androidUser.getId()) });
        db.close();
    }

    /**
     * This is a public method that looks for an AndroidUser object with the given username
     * and password.
     * @param username to be compared
     * @param password to be compared
     * @return true if user exist and false if not
     */
    public boolean androidUserExists(String username, String password)
    {
        ArrayList<AndroidUser> list = getAllAndroidUsers();
        if(password == null)
        {
            return userNameExists(username, list);
        }
        else
        {
            for(AndroidUser au : list)
            {
                if(username.equals(au.getUserName()) && password.equals(au.getPassword()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This is a private method that looks through all AndroidUser objects and checks if a user
     * with username exists.
     * @param username to be looked for
     * @param list with all users
     * @return true if user name exists and false if not
     */
    private boolean userNameExists(String username, ArrayList<AndroidUser> list)
    {
        for(AndroidUser au : list)
        {
            if(username.equals(au.getUserName()))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * This is a public method that returns a list with all AndroidUser objects in the database.
     * @return list with all AndroidUser objects
     */
    private ArrayList<AndroidUser> getAllAndroidUsers()
    {
        ArrayList<AndroidUser> userList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst())
        {
            do
            {
                AndroidUser androidUser = new AndroidUser();
                androidUser.setId(Integer.parseInt(cursor.getString(0)));
                androidUser.setUserName(cursor.getString(1));
                androidUser.setPassword(cursor.getString(2));
                androidUser.setClientName(cursor.getString(3));
                androidUser.setClientPass(cursor.getString(4));
                androidUser.setIpAddress(cursor.getString(5));
                androidUser.setTcpPort(cursor.getInt(6));

                userList.add(androidUser);
            } while (cursor.moveToNext());
        }
        return userList;
    }

    /**
     * This is a public method that returns the next user id that can be used by a new user.
     * @return id to be used
     */
    public int getNextID()
    {
        int biggestID;
        ArrayList<AndroidUser> list = getAllAndroidUsers();

        if (list.size() == 0)
        {
            return 1;
        }
        else if (list.size() == 1)
        {
            biggestID = list.get(0).getId() + 1;
            return biggestID;
        }
        else
        {
            biggestID = list.get(0).getId();

            for (int i = 0; i < list.size(); i++)
            {
                if (list.get(i).getId() > biggestID)
                {
                    biggestID = list.get(i).getId();
                }
            }
            biggestID = biggestID + 1;
            return biggestID;
        }
    }

    /**
     * This is a public method that deletes all data from the table of all Android users.
     */
    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }

    /**
     * This is a public method that returns the AndroidUser with username. Method is inefficient
     * and will be updated.
     * @param username to be looked for
     * @return android user
     */
    public AndroidUser getAndroidUser(String username)
    {
        ArrayList<AndroidUser> list = getAllAndroidUsers();
        for(AndroidUser au : list)
        {
            if(au.getUserName().equals(username))
            {
                return au;
            }
        }
        return null;
    }
}