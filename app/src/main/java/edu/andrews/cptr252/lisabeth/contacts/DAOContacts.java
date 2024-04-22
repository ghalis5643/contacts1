package edu.andrews.cptr252.lisabeth.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DAOContacts extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private final String TABLE = "Contacts";
    private static final String DATABASE = "ContactList";

    public DAOContacts(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE +
                "( id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "phone TEXT," +
                "address TEXT," +
                "photo TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public List<InfoContact> getList(String order) {
        List<InfoContact> contacts = new ArrayList<>();

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE +
                " ORDER BY name " + order + ";", null);
        while (cursor.moveToNext()) {
            InfoContact c = new InfoContact();

            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("name");
            int phoneIndex = cursor.getColumnIndex("phone");
            int addressIndex = cursor.getColumnIndex("address");
            int photoIndex = cursor.getColumnIndex("photo");
            c.setId(cursor.getLong(idIndex));
            c.setName(cursor.getString(nameIndex));
            c.setPhone(cursor.getString(phoneIndex));
            c.setAddress(cursor.getString(addressIndex));
            c.setPhoto(cursor.getString(photoIndex));

            contacts.add(c);

        }
        cursor.close();
        return contacts;
    }

    public void insertContact (InfoContact c) {

        ContentValues values = new ContentValues();
        values.put("name", c.getName());
        values.put("phone", c.getPhone());
        values.put("address", c.getAddress());
        values.put("photo", c.getPhoto());
        getWritableDatabase().insert(TABLE, null, values);
    }
    public void editContact (InfoContact c) {
        ContentValues values = new ContentValues();
        values.put("id", c.getId());
        values.put("name", c.getName());
        values.put("phone", c.getPhone());
        values.put("address", c.getAddress());
        values.put("photo", c.getPhoto());

        String[] idToEdit = {c.getId().toString()};
        getWritableDatabase().update(TABLE, values, "id=?", idToEdit);
    }

    public void deleteContact (InfoContact c) {
        SQLiteDatabase db = getWritableDatabase();
        String[] idToDelete = {c.getId().toString()};
        db.delete(TABLE, "id=?", idToDelete);
    }
}
