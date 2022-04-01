package com.example.recipeapp.Recipe;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.recipeapp.R;

/**
 * Activity to hold our fragments, extends AppCompatActivity
 */
public class RecipeEmptyActivity extends AppCompatActivity {

    /**onCreate for RecipeEmptyActivity to hold our fragment
     * @param savedInstanceState  @see AppCompatActivity.onCreate()
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_empty);

        Toolbar tBar = (Toolbar)findViewById(R.id.recipe_toolbar);
        setSupportActionBar(tBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle dataToPass = getIntent().getExtras();

        RecipeDetailFragment dFragment = new RecipeDetailFragment();
        dFragment.setArguments(dataToPass); //pass data to the the fragment
        dFragment.setTablet(false); //tell the Fragment that it's on a phone.
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.recipeFragmentLocation, dFragment)
                .commit();


    }
}
