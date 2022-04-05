package com.example.recipeapp.Recipe;

/**
 * class Recipe Entry to store information of recipe
 */
public class RecipeEntry {
    /**
     * recipe id
     */
    public Long id;
    /**
     * recipe title
     */
    public String title;
    /**
     * recipe image url
     */
    public String imageUrl;
    /**
     * recipe detail
     */
    public String details;

    /**
     * object to string
     * @return recipe title
     */
    public String toString() {
        return title;
    }
}
