package com.example.recipeapp.Recipe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.recipeapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * It is favorite activity as a subclass of AppCompatActivity.
 * It shows all favorite results in db in the list view and a snack bar with last search information in fav activity.
 * It provided a filter to display specific results in list view with a toast to show the number of results.
 * Tool bar can go to other activities (home, search, about) and up navigation. Help shows AlertDialog. Favorite displays toast.
 * Long click on the item in list view can delete it from the db and the view
 * @author Yuanhui Xu, Xin (Elliot) Peng, Zeyu Li
 */
public class RecipeFavActivity extends AppCompatActivity {

    /**
     * tool bar to go to other activities
     */
    private Toolbar toolbar;
    /**
     * filter button to show specific results
     */
    private Button filterButton;
    /**
     * input keyword from user
     */
    private EditText filterText;
    /**
     * list view to show results
     */
    private ListView list;
    /**
     * progress bar
     */
    private ProgressBar progressBar;
    /**
     * array list of RecipeEntry
     */
    private ArrayList<RecipeEntry> recipes = new ArrayList<RecipeEntry>();


    /**
     * This Overrides the superclass's onCreate method,
     * It sets up the tool bar and displays the favorite result in db in the list view
     * It sets up the Click Listener for the listview on both phone and tablet.
     * It can delete the record from db and listview by long clicking on the item in listview with alertDialog.
     * It set up the action of a filter button to show results with a count toast.
     * It shows the search keyword in this page last time in a snack bar with shared preference.
     *
     * @param savedInstanceState @See AppCompatActivity.onCreate()
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav);

        progressBar = findViewById(R.id.favProgressBar);

        toolbar = (Toolbar) findViewById(R.id.recipe_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //This is the onClickListener for my List
        list = findViewById(R.id.favRecipeListView);
        RecipeDatabaseHelper helper = new RecipeDatabaseHelper(this);
        recipes = RecipeDAO.listFavRecipes(helper, "");
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, recipes);
        list.setAdapter(adapter);
        list.deferNotifyDataSetChanged();

        list.setOnItemClickListener((mlist, item, position, id) -> {
            Bundle bundle = new Bundle();
            RecipeEntry entry = recipes.get(position);
            bundle.putLong("id", entry.id);
            bundle.putString("title", entry.title);
            bundle.putString("imageUrl", entry.imageUrl);
            bundle.putString("details", entry.details);

            boolean isTablet = findViewById(R.id.recipeFragmentLocation) != null;
            if (isTablet) {
                RecipeDetailFragment fragment = new RecipeDetailFragment();
                fragment.setArguments(bundle);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.recipeFragmentLocation, fragment)
                        .commit();
            } else { //isPhone
                Intent goToDetail = new Intent(this, FavEmptyActivity.class);
                goToDetail.putExtras(bundle); //send data to next activity
                startActivity(goToDetail); //make the transition
            }
        });

        list.setOnItemLongClickListener((mlist, item, position, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            RecipeEntry recipe = recipes.get(position);
            alertDialogBuilder.setTitle("Do you want to delete this?")
                    .setMessage(recipe.title)
                    .setPositiveButton("Yes", (click, arg) -> {
                        recipes.remove(position);
                        RecipeDAO.deleteFavRecipe(helper, recipe.id);
                        list.setAdapter(new ArrayAdapter(this, R.layout.list_item, recipes));
                        list.deferNotifyDataSetChanged();
                    })
                    .setNegativeButton("No", (click, arg) -> {
                    })
                    .create().show();
            return true;
        });

        filterText = findViewById(R.id.favFilterText);
        filterButton = findViewById(R.id.favFilterButton);
        filterButton.setOnClickListener(click -> {
            String keyword = filterText.getText().toString();
            Log.e("keyword", keyword);
            recipes = RecipeDAO.listFavRecipes(helper, keyword);
            Log.e("recipes", Integer.toString(recipes.size()));
            list.setAdapter(new ArrayAdapter(this, R.layout.list_item, recipes));
            list.deferNotifyDataSetChanged();

            Toast.makeText(this, Integer.toString(recipes.size()) + getString(R.string.favSearchCount) , Toast.LENGTH_LONG).show();
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String lastFilter = pref.getString("last_filter", "");
        ConstraintLayout flayout = findViewById(R.id.fav_layout);
        if (!lastFilter.isEmpty()) {
            Snackbar.make(flayout, getString(R.string.favLastSearchPrompt) + lastFilter, Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(flayout, getString(R.string.favFirstSearchPrompt), Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * It override onPause from super class.
     * When paused, it stored last search keyword in this page into shared preference.
     */
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("last_filter", filterText.getText().toString());
        editor.commit();
    }


    /**
     * This method Overrides the superclass's onCreateOptionsMenu() method
     * It sets up the toolbar menu
     *
     * @param menu @see AppCompatActivity.onCreateOptionsMenu()
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the app bar.
        getMenuInflater().inflate(R.menu.accessible_toolbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This sets actions for what will happen when items in the Toolbar are clicked
     *
     * @param item item in toolbar
     * @return super return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_home:
                Intent goHome = new Intent(this, RecipeMainActivity.class);
                startActivity(goHome);
                break;
            case R.id.toolbar_help:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.information))
                        .setMessage(getString(R.string.recipeVersion) + "\n" + getString(R.string.favHelp))
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            case R.id.toolbar_search:
                Intent goToSearch = new Intent(this, RecipeSearchActivity.class);
                startActivity(goToSearch);
                break;
            case R.id.toolbar_about:
                Intent goToAbout = new Intent(this, AboutMeActivity.class);
                startActivity(goToAbout);
                break;
            case R.id.toolbar_fav:
                Toast.makeText(this, getString(R.string.favToast), Toast.LENGTH_LONG).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
