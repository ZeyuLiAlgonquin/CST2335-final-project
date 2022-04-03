package com.example.recipeapp.Recipe;

import android.content.Intent;
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

import com.example.recipeapp.R;

import java.util.ArrayList;

/**
 * This class shows the listviews and allows for searching.
 * It extends AppCompatActivity
 */
public class RecipeFavActivity extends AppCompatActivity {

    private Menu menu;
    private Toolbar toolbar;
    private Button filterButton;
    private EditText filterText;
    private ListView list;
    private ProgressBar progressBar;
    private ArrayList<RecipeEntry> recipes = new ArrayList<RecipeEntry>();
//    private RecipeDatabaseHelper opener;
//    private SimpleCursorAdapter chatAdapter;

    /**
     * This Overrides the superclass's onCreate method,
     * It sets up the tool bar and button as well as selects the right Table to show in the list view.
     * It sets up the Click Listener for the listview and the search button.
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

            boolean isTablet = findViewById(R.id.recipeFragmentLocation) != null;
            if (isTablet) {
                RecipeDetailFragment fragment = new RecipeDetailFragment();
                fragment.setArguments(bundle);
                fragment.setTablet(true);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.recipeFragmentLocation, fragment)
                        .commit();
            } else { //isPhone
                Intent goToDetail = new Intent(this, RecipeEmptyActivity.class);
                goToDetail.putExtras(bundle); //send data to next activity
                startActivity(goToDetail); //make the transition
            }
        });


        filterText = findViewById(R.id.favFilterText);
        filterButton = findViewById(R.id.favFilterButton);
        filterButton.setOnClickListener(click -> {
//            RecipeDatabaseHelper hp = new RecipeDatabaseHelper(this);
            String keyword = filterText.getText().toString();
            Log.e("keyword", keyword);
            recipes = RecipeDAO.listFavRecipes(helper, keyword);
            Log.e("recipes", Integer.toString(recipes.size()));
            list.setAdapter(new ArrayAdapter(this, R.layout.list_item, recipes));
            list.deferNotifyDataSetChanged();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
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
                Intent nextActivity2 = new Intent(this, RecipeMain.class);
                startActivityForResult(nextActivity2, 346);
                break;
            case R.id.toolbar_help:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.information))
                        .setMessage(getString(R.string.recipeVersion) + "\n" + getString(R.string.recipeSearchHelp))
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            case R.id.toolbar_fav:
                Intent goToFav = new Intent(this, RecipeFavActivity.class);
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
            case R.id.toolbar_about:
                Intent goToAbout = new Intent(this, AboutMeActivity.class);
                startActivity(goToAbout);
                break;
            case R.id.toolbar_search:
                Toast.makeText(this, getString(R.string.searchToast), Toast.LENGTH_LONG).show();

        }
        return super.onOptionsItemSelected(item);
    }

}
