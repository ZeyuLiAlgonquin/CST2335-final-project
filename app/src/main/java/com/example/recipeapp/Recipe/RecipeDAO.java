package com.example.recipeapp.Recipe;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * class Recipe DAO as Data Access Object to implement insert, delete, query and query count
 */
public class RecipeDAO {


    /**
     * Insert a record into fav table in db with the information in RecipeEntry object using helper
     * @param helper object of RecipeDatabaseHelper
     * @param recipe object RecipeEntry
     * @return insert result
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



    /**
     * Delete from fav table in db with db using helper
     * @param helper object of RecipeDatabaseHelper
     * @param id recipe id
     * @return
     */
    public static int deleteFavRecipe(RecipeDatabaseHelper helper, Long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(helper.FAV_TABLE_NAME, helper.COL_RECIPE_ID + " = ?", new String[] {id.toString()});
    }


    /**
     * Query from fav table in db with keyword in title using helper
     * @param helper object of RecipeDatabaseHelper
     * @param keyword keyword to search in title in db fav table
     * @return arraylist result of RecipeEntry
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


    /**
     * Check if the recipe with this id exists in fav table in db with helper and cursor.
     * @param helper object of RecipeDatabaseHelper
     * @param id recipe id
     * @return if it exits
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
