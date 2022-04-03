package com.example.recipeapp.Recipe;

//This is for my Recipe Activity

/* this is what we will pull in a query row

{
"publisher": "BBC Good Food",
"f2f_url": "http://food2fork.com/view/495802",
"title": "Chicken cacciatore",
"source_url": "http://www.bbcgoodfood.com/recipes/4251/chicken-cacciatore",
"website": "495802",
"image_url": "http://static.food2fork.com/4251_MEDIUM71f0.jpg",
"social_rank": 99.99999994031722,
"publisher_url": "http://www.bbcgoodfood.com"
}
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**This is my DatabaseHelper class for the Recipe Portion
 *
 */
public class RecipeDatabaseHelper extends SQLiteOpenHelper {


    public static final String DB_NAME = "RecipeDB04";
    public static final int VERSION_NUM = 2;
    public static final String FAV_TABLE_NAME = "favorite_recipe";
    public static final String COL_RECIPE_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_DETAIL = "detail";
    public static final String COL_IMAGE = "image";
    private static final String CREATE_TABLE_STATEMENT = "CREATE TABLE " + FAV_TABLE_NAME + "( "
            + COL_RECIPE_ID + " INTEGER PRIMARY KEY, "
            + COL_TITLE + " TEXT, "
            + COL_IMAGE + " TEXT, "
            + COL_DETAIL + " TEXT)";

    public RecipeDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION_NUM);
    }

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
