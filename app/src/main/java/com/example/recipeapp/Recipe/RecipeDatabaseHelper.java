package com.example.recipeapp.Recipe;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**This is my DatabaseHelper class for the Recipe Portion as a subclass of SQLiteOpenHelper
 *@author Yuanhui Xu, Xin (Elliot) Peng, Zeyu Li
 */
public class RecipeDatabaseHelper extends SQLiteOpenHelper {

    /**
     * db name
     */
    public static final String DB_NAME = "RecipeDB04";
    /**
     * version
     */
    public static final int VERSION_NUM = 2;
    /**
     * favorite table name
     */
    public static final String FAV_TABLE_NAME = "favorite_recipe";
    /**
     * column id
     */
    public static final String COL_RECIPE_ID = "id";
    /**
     * column title
     */
    public static final String COL_TITLE = "title";
    /**
     * column detail
     */
    public static final String COL_DETAIL = "detail";
    /**
     * column image (url)
     */
    public static final String COL_IMAGE = "image";
    /**
     * statement to create fav table with columns above
     */
    private static final String CREATE_TABLE_STATEMENT = "CREATE TABLE " + FAV_TABLE_NAME + "( "
            + COL_RECIPE_ID + " INTEGER PRIMARY KEY, "
            + COL_TITLE + " TEXT, "
            + COL_IMAGE + " TEXT, "
            + COL_DETAIL + " TEXT)";

    /**
     * constructor with parameters, call from super class with local parameters
     * @param context
     */
    public RecipeDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION_NUM);
    }

    /**
     * create table in db
     * @param db database
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STATEMENT);
    }

    /** This is the onUpgrade, don't really use it. Only ever one version, will update if neeeded in the future
     * @param db SQLite Database
     * @param oldVersion Old version number
     * @param newVersion New version number
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + FAV_TABLE_NAME);
        onCreate(db);
    }


}
