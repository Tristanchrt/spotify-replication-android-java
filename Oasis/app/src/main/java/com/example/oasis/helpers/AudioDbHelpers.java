package com.example.oasis.helpers;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AudioDbHelpers extends SQLiteOpenHelper {

    private static AudioDbHelpers instance = null;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "song";

    // Table name: Note.
    public static final String TABLE_TRACK = "tracks";

    public static final String COLUMN_TRACK_ID ="id";
    public static final String COLUMN_TRACK_TITLE ="title";
    public static final String COLUMN_TRACK_ARTIST ="artist";
    public static final String COLUMN_TRACK_FILE = "file";
    public static final String COLUMN_TRACK_GENRE = "genre";

    private AudioDbHelpers(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized AudioDbHelpers getInstance(Context context){
        if(instance == null){
            instance = new AudioDbHelpers(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "CREATE TABLE " + TABLE_TRACK + "("
                + COLUMN_TRACK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                COLUMN_TRACK_ARTIST + " TEXT," +
                COLUMN_TRACK_TITLE + " TEXT," +
                COLUMN_TRACK_FILE + " TEXT," +
                COLUMN_TRACK_GENRE + " TEXT" + ")";
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int previous, int next) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACK);
        // Create tables again
        onCreate(db);
    }
}
