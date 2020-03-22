package com.xu.liferpg.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DB Helper for Quests
 */

public class QuestListDBHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "QuestSQLTable.db";
    public static final String DATABASE_TABLE_NAME ="questlist";

    private static final int DATABASE_VERSION = 2;

    public QuestListDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //0905 added time http://stackoverflow.com/questions/41596841/android-sql-datetime-current-timestamp
    //TODO 2105 solved problem with dateTime, left out a bracket!
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE "+ DATABASE_TABLE_NAME+"(_id INTEGER PRIMARY KEY AUTOINCREMENT, questid STRING NOT NULL, region STRING NOT NULL, questname STRING NOT NULL, details STRING NOT NULL, latitude TEXT NOT NULL, longitude TEXT NOT NULL, objectives STRING NOT NULL, key1 STRING NOT NULL, key2 STRING NOT NULL , key3 STRING NOT NULL, completed STRING NOT NULL, questlevel STRING NOT NULL);");

    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS questlist");
        onCreate(sqLiteDatabase);
    }


}
