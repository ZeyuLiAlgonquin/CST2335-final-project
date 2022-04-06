package com.example.recipeapp.Recipe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.recipeapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

/**
 * RecipeMain class is the front page for my Search Section of our Application with navigation drawer and toolbar to go to other activities (search, favorite, about).
 * It extends AppCompatActivity. It has a snack bar to remind you of the last search keyword.
 * Help will provide a AlertDialog to introduce the function of this page/activity.
 * A toast will show if you click on home in this page.
 *
 */
public class RecipeMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    /**
     * This is an Override of  the onCreate from the super class, it displays a SnackBar and sets a
     * clickListener for the go to Search activity button, sets a toolbar and a navigation drawer
     *
     * @param savedInstanceState @See AppCompatActivity.onCreate()
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String lastSearch = pref.getString("search_keyword", "");
        ConstraintLayout hlayout = findViewById(R.id.home_layout);
        if (!lastSearch.isEmpty()) {
            Snackbar.make(hlayout, getString(R.string.lastSearchPrompt) + lastSearch, Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(hlayout, getString(R.string.firstSearchPrompt), Snackbar.LENGTH_LONG).show();
        }

        Button button = findViewById(R.id.recipeMainButton);

        button.setOnClickListener(click ->
        {
            //first parameter is any view on screen. second parameter is the text. Third parameter is the length (SHORT/LONG)
            Intent goToSearch = new Intent(RecipeMainActivity.this, RecipeSearchActivity.class);
            startActivity(goToSearch);
        });


        Toolbar tBar = (Toolbar)findViewById(R.id.recipe_toolbar);
        setSupportActionBar(tBar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * This sets the action on the items in navigation drawer
     * @param item item in menu
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.drawer_search:
                Intent goToSearch = new Intent(RecipeMainActivity.this, RecipeSearchActivity.class);
                startActivity(goToSearch);
                break;
            case R.id.drawer_fav:
                Intent goToFav = new Intent(RecipeMainActivity.this, RecipeFavActivity.class);
                startActivity(goToFav);
                break;
            case R.id.drawer_help:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.information))
                        .setMessage(getString(R.string.recipeVersion) + "\n" + getString(R.string.mainHelp))
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            case R.id.drawer_about:
                Intent goToAbout = new Intent(RecipeMainActivity.this, AboutMeActivity.class);
                startActivity(goToAbout);
                break;
            case R.id.drawer_home:
                Toast.makeText(this, getString(R.string.homeToast), Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }
    /**
     * This simply creates the toolbar menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                        .setMessage(getString(R.string.recipeVersion) + "\n" + getString(R.string.mainHelp))
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            case R.id.toolbar_fav:
                Intent goToFav = new Intent(RecipeMainActivity.this, RecipeFavActivity.class);
                startActivity(goToFav);
                break;

            case R.id.toolbar_search:
                // RecipeSearch.setTable = false;
                Intent goToSearch = new Intent(RecipeMainActivity.this, RecipeSearchActivity.class);
                startActivity(goToSearch);
                break;

            case R.id.toolbar_about:
                Intent goToAbout = new Intent(RecipeMainActivity.this, AboutMeActivity.class);
                startActivity(goToAbout);
                break;
            case R.id.toolbar_home:
                Toast.makeText(this, getString(R.string.homeToast), Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
