package gt.edu.umg.gallery_and_memories.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import gt.edu.umg.gallery_and_memories.models.PhotoItem;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private Context context;
    private static final String DATABASE_NAME = "PhotoGallery.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_PHOTOS = "photos";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_URI = "uri";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_PHOTOS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_URI + " TEXT NOT NULL, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_DATE + " TEXT NOT NULL, " +
                COLUMN_LATITUDE + " REAL, " +
                COLUMN_LONGITUDE + " REAL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);
        onCreate(db);
    }

    public long insertPhoto(String uri, String description, String date, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_URI, uri);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);

        long id = db.insert(TABLE_PHOTOS, null, values);
        db.close();
        return id;
    }

    public List<PhotoItem> getAllPhotos() {
        List<PhotoItem> photoList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PHOTOS + " ORDER BY " + COLUMN_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                PhotoItem photo = new PhotoItem(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URI)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE))
                );
                photoList.add(photo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return photoList;
    }

    public PhotoItem getPhotoById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        PhotoItem photo = null;

        Cursor cursor = db.query(TABLE_PHOTOS, null,
                COLUMN_ID + " = ?", new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            photo = new PhotoItem(
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URI)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE))
            );
            cursor.close();
        }

        db.close();
        return photo;
    }

    public boolean deletePhoto(long id) {
        boolean success = false;
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // Primero obtener la URI de la foto
            PhotoItem photo = getPhotoById(id);
            if (photo != null) {
                String uriString = photo.getUri();

                // Intentar eliminar el archivo físico
                try {
                    Uri uri = Uri.parse(uriString);
                    // Eliminar el archivo usando ContentResolver
                    context.getContentResolver().delete(uri, null, null);
                } catch (Exception e) {
                    Log.e(TAG, "Error al eliminar archivo físico: " + e.getMessage());
                }

                // Eliminar el registro de la base de datos
                int result = db.delete(TABLE_PHOTOS, COLUMN_ID + " = ?",
                        new String[]{String.valueOf(id)});
                success = result > 0;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al eliminar foto: " + e.getMessage());
        } finally {
            db.close();
        }

        return success;
    }

    public boolean updatePhoto(PhotoItem photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_DESCRIPTION, photo.getDescription());
        values.put(COLUMN_DATE, photo.getDate());
        values.put(COLUMN_LATITUDE, photo.getLatitude());
        values.put(COLUMN_LONGITUDE, photo.getLongitude());

        int result = db.update(TABLE_PHOTOS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(photo.getId())});
        db.close();
        return result > 0;
    }
}