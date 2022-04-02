package com.example.recipeapp.Recipe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.recipeapp.R;

public class AboutMeActivity extends AppCompatActivity {
    private Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        Toolbar tBar = (Toolbar)findViewById(R.id.recipe_toolbar);
        setSupportActionBar(tBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * This method Overrides the superclass's onCreateOptionsMenu() method
     * It sets up the toolbar and sets the toggleling icon based on the current list showing
     *
     * @param menu @see AppCompatActivity.onCreateOptionsMenu()
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the app bar.
        getMenuInflater().inflate(R.menu.accessible_toolbar, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This sets actions for what will happen when items in the Toolbar are clicked
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_home:
                // RecipeSearch.setTable = false;
                Intent nextActivity2 = new Intent(AboutMeActivity.this, RecipeMain.class);
                startActivityForResult(nextActivity2, 346);
                break;
            case R.id.toolbar_help:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.information))
                        .setMessage(getString(R.string.recipeVersion) + "\n" + getString(R.string.aboutHelp))
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            case R.id.toolbar_fav:
                Intent goToFav = new Intent(AboutMeActivity.this, RecipeFavActivity.class);
                startActivity(goToFav);
                break;
//                if (showFave) {
//                    showResults();
//                    menu.getItem(0).setIcon(R.drawable.star_unfilled);
//                } else {
//                    showFavorite();
//                    menu.getItem(0).setIcon(R.drawable.search);
//                }
//                showFave = !showFave;
//                break;
            case R.id.toolbar_search:
                Intent goToSearch = new Intent(AboutMeActivity.this, RecipeSearch.class);
                startActivity(goToSearch);
                break;
            case R.id.toolbar_about:
                Toast.makeText(this, getString(R.string.aboutToast) , Toast.LENGTH_LONG).show();

        }
        return super.onOptionsItemSelected(item);
    }
}