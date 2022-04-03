package com.example.recipeapp.Recipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.recipeapp.R;
import com.google.android.material.navigation.NavigationView;

/**
 * RecipeMain class is the front page for my Search Section of our Application.
 * It extends AppCompatActivity. If has a snack bar to remind you of the SharedPreferences value.
 * It uses the super classic term Gotcha for a button. You are forced to acknowledge it to gain access
 * to the button that will start the search/list activity
 */
public class RecipeMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static Menu menu;

    /**
     * This is an Override of  the onCreate from the super class, it displays a SnackBar and sets a
     * clickListener for the go to Search activity button
     *
     * @param savedInstanceState @See AppCompatActivity.onCreate()
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.recipe_main_activty);
//        setContentView(R.layout.main_with_toolbar_drawer);
        setContentView(R.layout.main);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.recipeToolbar);
//        setSupportActionBar(toolbar);

//        String chickOrLasgne;
//        Boolean bool = false;
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
//
//        pref.getBoolean("chicken", bool);
//
//        if (bool) {
//            chickOrLasgne = getString(R.string.lastchoicechicken);
//        } else {
//            chickOrLasgne = getString(R.string.lastchoicelasagan);
//        }
//        CoordinatorLayout cLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
//
//        Snackbar.make(cLayout, chickOrLasgne, Snackbar.LENGTH_INDEFINITE)
//                .setAction(getString(R.string.gotcha), new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Respond to the click, or not it's really up to you
//                    }
//                }).show();
        Button button = findViewById(R.id.recipeMainButton);

        button.setOnClickListener(click ->
        {
            //first parameter is any view on screen. second parameter is the text. Third parameter is the length (SHORT/LONG)
            Intent goToSearch = new Intent(RecipeMain.this, RecipeSearch.class);
            startActivityForResult(goToSearch, 346); //make the transition
        });


//        Toolbar toolbar1 = findViewById(R.id.toolbar);
//        //setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//
//        FrameLayout frameLayout = findViewById(R.id.main_frameLayout);
//        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
////        navigationView = findViewById(R.id.navigation);
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawerLayout, toolbar1, R.string.navagation_drawer_open,
//                R.string.navagation_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
        Toolbar tBar = (Toolbar)findViewById(R.id.recipe_toolbar);
        setSupportActionBar(tBar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.navagation_drawer_open, R.string.navagation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.drawer_search:
                Intent goToSearch = new Intent(RecipeMain.this, RecipeSearch.class);
                startActivity(goToSearch);
                break;
            case R.id.drawer_fav:
                Intent nextActivity = new Intent(RecipeMain.this, RecipeFavActivity.class);
//                nextActivity.putExtra(RecipeSearch.SHOW_FAVE, true);
                startActivityForResult(nextActivity, 346); //make the transition
                break;
            case R.id.drawer_help:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.information))
                        .setMessage(getString(R.string.recipeVersion) + "\n" + getString(R.string.recipeMainHelp))
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            case R.id.drawer_about:
                Intent goToAbout = new Intent(RecipeMain.this, AboutMeActivity.class);
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
        this.menu = menu;
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
                        .setMessage(getString(R.string.recipeVersion) + "\n" + getString(R.string.recipeMainHelp))
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            case R.id.toolbar_fav:
                // RecipeSearch.setTable = false;
                Intent nextActivity = new Intent(RecipeMain.this, RecipeFavActivity.class);
                startActivity(nextActivity);
//                Intent nextActivity = new Intent(RecipeMain.this, RecipeSearch.class);
//                nextActivity.putExtra(RecipeSearch.SHOW_FAVE, true);
//                startActivityForResult(nextActivity, 346); //make the transition
                break;

            case R.id.toolbar_search:
                // RecipeSearch.setTable = false;
                Intent nextActivity2 = new Intent(RecipeMain.this, RecipeSearch.class);
                startActivityForResult(nextActivity2, 346);
                break;

            case R.id.toolbar_about:
                Intent goToAbout = new Intent(RecipeMain.this, AboutMeActivity.class);
                startActivity(goToAbout);
                break;
            case R.id.toolbar_home:
                Toast.makeText(this, getString(R.string.homeToast), Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
