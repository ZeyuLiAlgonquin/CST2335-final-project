package com.example.recipeapp.Recipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.recipeapp.R;

/**
 * Activity to hold our fragments on phone, extends AppCompatActivity. It's parent activity is RecipeFavActivity.
 * Tool bar can go to other activities (home, search, fav, about) and up navigation. Help shows AlertDialog.
 */
public class FavEmptyActivity extends AppCompatActivity {

    /**
     * onCreate for RecipeEmptyActivity to hold our fragment of detail, set layout and toolbar
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

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.recipeFragmentLocation, dFragment)
                .commit();


    }
    /**
     * This method Overrides the superclass's onCreateOptionsMenu() method
     * It sets up the toolbar menu
     *
     * @param menu @see AppCompatActivity.onCreateOptionsMenu()
     * @return super result
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the app bar.
        getMenuInflater().inflate(R.menu.accessible_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This sets up the onclickListeners and actions for the menu toolbar
     *
     * @param item takes which item was clicked
     * @return returns the return of the super method call
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.toolbar_help:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.information))
                        .setMessage(getString(R.string.recipeVersion) + "\n" + getString(R.string.favDetailHelp))
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            case R.id.toolbar_fav:
                Intent goToFav = new Intent(FavEmptyActivity.this, RecipeFavActivity.class);
                startActivity(goToFav);
                break;

            case R.id.toolbar_search:
                Intent goToSearch = new Intent(FavEmptyActivity.this, RecipeSearchActivity.class);
                startActivity(goToSearch);
                break;

            case R.id.toolbar_about:
                Intent goToAbout = new Intent(FavEmptyActivity.this, AboutMeActivity.class);
                startActivity(goToAbout);
                break;
            case R.id.toolbar_home:
                Intent goHome = new Intent(FavEmptyActivity.this, RecipeMainActivity.class);
                startActivity(goHome);
        }
        return super.onOptionsItemSelected(item);
    }
}
