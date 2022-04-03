package com.example.recipeapp.Recipe;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class RecipeDAO {

    /**This method is used to insert values into one of the two tables
     * it uses an insertWithOnConflict() method with a CONFLICT_IGNORE flag to discard
     * failures to insert due to the Unique constraint on our title column
     * @param tableName This is the TableName
     * @param title This is the value for the title column
     * @param publisher This is the value for the publisher column
     * @param contentUrl This is the value for the source_url column
     * @param image This is the value for the image column
     * @return returns boolean true if successful or false if not.
     */
    public static long addFavRecipe(RecipeDatabaseHelper helper, RecipeEntry recipe) {
        ContentValues content = new ContentValues();
        content.put(helper.COL_TITLE, recipe.title);
        content.put(helper.COL_DETAIL, recipe.details);
        content.put(helper.COL_RECIPE_ID, recipe.id);
        content.put(helper.COL_IMAGE, recipe.imageUrl);
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.insertWithOnConflict(helper.FAV_TABLE_NAME, null, content, SQLiteDatabase.CONFLICT_IGNORE);
    }


    /** This is my delete method for remocing specific Items/Rows from one of the Tables
     * @param title We will always use the title column value to delete since it is set to unique.
     * @param tableName This is the table name we should delete from
     * @return returns int which I believe represents the id of the row.
     */
    public static int deleteFavRecipe(RecipeDatabaseHelper helper, Long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(helper.FAV_TABLE_NAME, helper.COL_RECIPE_ID + " = ?", new String[] {id.toString()});
    }

    /**This method gets a Cursor object to a specified Table
     * @param tableName this is a String specifing the name of the table to grab the cursor for
     * @return the return varible is a Cursor object
     */
    public static ArrayList<RecipeEntry> listFavRecipes(RecipeDatabaseHelper helper, String keyword) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor;
        if (keyword.isEmpty()) {
            cursor = db.query(helper.FAV_TABLE_NAME, null, null, null, null, null, null);
        } else {
            cursor = db.query(helper.FAV_TABLE_NAME, null, "title LIKE ?", new String[]{"%"+keyword+"%"}, null, null, null);
        }
        ArrayList<RecipeEntry> recipes = new ArrayList<>();
        while (cursor.moveToNext()) {
            RecipeEntry recipe = new RecipeEntry();
            recipe.id = cursor.getLong(cursor.getColumnIndex(helper.COL_RECIPE_ID));
            recipe.title = cursor.getString(cursor.getColumnIndex(helper.COL_TITLE));
            recipe.imageUrl = cursor.getString(cursor.getColumnIndex(helper.COL_IMAGE));
            recipe.details = cursor.getString(cursor.getColumnIndex(helper.COL_DETAIL));
            recipes.add(recipe);
        }
        return recipes;
    }

    /**This method checks to see if a record already exists in our database table
     * @param tableName This is a String to specify the table to look in for the record
     * @param field this is a String specifying which column to look under. Should use PK_ID or COL_TITLE if looking for unique record.
     * @param fieldValue this is a String to search records for.
     * @return
     */
    public static boolean isExist(RecipeDatabaseHelper helper, Long id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String stmt = String.format("SELECT count(*) FROM %s WHERE %s=%d", helper.FAV_TABLE_NAME, helper.COL_RECIPE_ID, id);
        Cursor cursor = db.rawQuery(stmt, null);
        cursor.moveToFirst();
        boolean found = cursor.getLong(0) > 0;
        cursor.close();
        return found;
    }
}
