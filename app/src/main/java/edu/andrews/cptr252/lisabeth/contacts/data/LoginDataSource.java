package edu.andrews.cptr252.lisabeth.contacts.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import edu.andrews.cptr252.lisabeth.contacts.DAOContacts;
import edu.andrews.cptr252.lisabeth.contacts.DAOLogin;
import edu.andrews.cptr252.lisabeth.contacts.ShaHelper;
import edu.andrews.cptr252.lisabeth.contacts.data.model.LoggedInUser;
import edu.andrews.cptr252.lisabeth.contacts.data.model.RegisterUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private final String TABLE_USERS = "Login";
    private final String COLUMN_USERNAME = "username";
    private final String COLUMN_PASSWORD_HASHED = "password";
    private DAOLogin dbHelper;

    public LoginDataSource(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Cannot receive a null context");
        }
        dbHelper = new DAOLogin(context.getApplicationContext());

    }

    public Result<LoggedInUser> login(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            String hashedEnteredPassword = ShaHelper.hash(password);
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS +
                    " WHERE " + COLUMN_USERNAME + " = ? AND " +
                    COLUMN_PASSWORD_HASHED + " = ?", new String[]{username, hashedEnteredPassword});

            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("id");
                int usernameIndex = cursor.getColumnIndex("username");

                LoggedInUser user = new LoggedInUser(
                        cursor.getString(idIndex),
                        cursor.getString(usernameIndex));

                cursor.close();
                db.close();

                return new Result.Success<>(user);
            } else {
                return new Result.Error(new IOException("Invalid username or password"));
            }
        } catch (Exception e) {
            db.close();
            return new Result.Error(new IOException("Error logging in", e));
        }
    }


    public boolean register(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            String hashedEnteredPassword = ShaHelper.hash(password);

            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, username);
            values.put(COLUMN_PASSWORD_HASHED, hashedEnteredPassword);

            long newRowId = db.insert(TABLE_USERS, null, values);

            if (newRowId == -1) {
                // Error inserting the data
                return false;
            } else {
                // Data inserted successfully
                return true;
            }

        } catch (Exception e) {
            db.close();
            return false;
        }
    }

    public void logout() {
        // TODO: Implement logout logic if needed
    }



}
