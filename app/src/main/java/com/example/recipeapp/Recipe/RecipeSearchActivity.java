package com.example.recipeapp.Recipe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.recipeapp.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * This class is Search activity. It allows for searching by accepting input and showing the results in listview with AsyncTask.
 * It also shows a snack bar about the last search information if exists. A process bar display the process of task
 * Tool bar can go to other activities (home, fav, about) and up navigation. Help shows AlertDialog. Search displays toast.
 * It extends AppCompatActivity
 */
public class RecipeSearchActivity extends AppCompatActivity {

    /**
     * tool bar to go to other activities or show toast and alert dialog
     */
    private Toolbar toolbar;
    /**
     * search button
     */
    private Button searchButton;
    /**
     * edit text to input search keyword
     */
    private EditText searchText;
    /**
     * list view to show search results
     */
    private ListView list;
    /**
     * progress bar based on length of arraylist
     */
    private ProgressBar progressBar;
    /**
     * arraylist to store RecipeEntry objects
     */
    private ArrayList<RecipeEntry> recipes = new ArrayList<RecipeEntry>();
    /**
     * private variable stores JSON string of recipe list
     */
    private String rawJson;


    /**
     * Inner class as a subclass of AsyncTask to finish the async task to download JSON and display in listview with a progress bar
     */
    private class FetchRecipeList extends AsyncTask<String, Integer, String> {
        /**
         * private variable to store the url template in a string
         */
        private final String urlTemplate = "https://api.spoonacular.com/recipes/findByIngredients?apiKey=2311513282b7432684777caf629d344a&number=10&ingredients=%s";

        /**
         * This Overrides AsyncTask.doInBackGround()
         * <p>
         * It pulls the search results based on what the droids want you to think.
         * <p>
         * It starts a http connection with the concat of url template and user input keyword and get back the JSON.
         * It sets the information of each recipe and store it into arraylist recipes. It also shows progress bar according to the length of recipes.
         *
         * @param @See AsyncTask.doInBackground()
         * @return @See AsyncTask.doInBackground()
         */
        @Override
        protected String doInBackground(String... params) {
            try {
                String ingredients = params[0];
                String rawUrl = String.format(urlTemplate, ingredients);
                URL url = new URL(rawUrl);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                rawJson = sb.toString();
                JSONArray json_recipes = new JSONArray(rawJson);

                for (int j = 0; j < json_recipes.length(); j++) {
                    publishProgress((j+1) * (100 / json_recipes.length()));

                    JSONObject r = json_recipes.getJSONObject(j);
                    RecipeEntry recipe = new RecipeEntry();
                    recipe.id = r.getLong("id");
                    recipe.title = r.getString("title");
                    recipe.imageUrl = r.getString("image");
                    recipes.add(recipe);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            return null;
        }

        /**
         * This method takes the param values to update our progress bar.
         *
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);

        }

        /**
         * This method Overrides the super class's onPostExecute.
         * It calls the super method and turns back on the visibility for the search button and text
         * It saves the value of the boolean that keeps track of what was last searched sends us back to our listView of the results
         *
         * @param results @See AsyncTask.onPostExecute()
         */
        @Override
        protected void onPostExecute(String results) {
            super.onPostExecute(results);
            ArrayAdapter adapter = new ArrayAdapter(RecipeSearchActivity.this, R.layout.list_item, recipes);
            list.setAdapter(adapter);
            list.deferNotifyDataSetChanged();
        }

    }

    /**
     * This Overrides the superclass's onCreate method,
     * It sets up the tool bar and progress bar.
     * It sets up the Click Listener for the listview and the search button on both phone and tablet.
     * It shows a snack bar with last search information.
     * @param savedInstanceState @See AppCompatActivity.onCreate()
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        progressBar = findViewById(R.id.recipeSearchProgressBar);

        toolbar = (Toolbar) findViewById(R.id.recipe_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //This is the onClickListener for my List
        list = findViewById(R.id.recipeListView);
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

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.recipeFragmentLocation, fragment)
                        .commit();
            } else { //isPhone
                Intent goToDetail = new Intent(RecipeSearchActivity.this, SearchEmptyActivity.class);
                goToDetail.putExtras(bundle); //send data to next activity
                startActivity(goToDetail); //make the transition
            }
        });


        searchButton = findViewById(R.id.recipeSearchButton);
        searchText = findViewById(R.id.searchEditText);
        searchButton.setOnClickListener(click -> {
            recipes.clear();
            ArrayAdapter adapter = new ArrayAdapter(RecipeSearchActivity.this, R.layout.list_item, recipes);
            list.setAdapter(adapter);
            list.deferNotifyDataSetChanged();
            new FetchRecipeList().execute(searchText.getText().toString());
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String lastSearch = pref.getString("search_keyword", "");
        RelativeLayout slayout = findViewById(R.id.linearLayout);
        if (!lastSearch.isEmpty()) {
            Snackbar.make(slayout, getString(R.string.lastSearchPrompt) + lastSearch, Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(slayout, getString(R.string.firstSearchPrompt), Snackbar.LENGTH_LONG).show();
        }

    }

    /**
     * It override onDestroy from super class.
     * When destroyed, the result of JSON would be removed and the search keyword would be kept.
     * (So next time start, it will remember the keyword in the toast, without the search results list view.)
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("search_result_json");
        editor.putString("search_keyword", searchText.getText().toString());
        editor.commit();
    }

    /**
     * It override onPause from super class.
     * When paused, the result of JSON and search keyword would be stored.
     * (So next time come back, it will remember the keyword in toast and search results in list view.)
     */
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("search_result_json", rawJson);
        editor.putString("search_keyword", searchText.getText().toString());
        editor.commit();
    }

    /**
     * It overrides onResume from super class.
     * When resumed, it will get the stored JSON and stored the recipe entry object in cleared arraylist and display in list view.
     */
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        rawJson = pref.getString("search_result_json", "[]");
        recipes.clear();
        try {
            JSONArray json_recipes = new JSONArray(rawJson);
            for (int j = 0; j < json_recipes.length(); j++) {
                JSONObject r = json_recipes.getJSONObject(j);
                RecipeEntry recipe = new RecipeEntry();
                recipe.id = r.getLong("id");
                recipe.title = r.getString("title");
                recipe.imageUrl = r.getString("image");
                recipes.add(recipe);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayAdapter adapter = new ArrayAdapter(RecipeSearchActivity.this, R.layout.list_item, recipes);
        list.setAdapter(adapter);
        list.deferNotifyDataSetChanged();
    }

    /**
     * This method Overrides the superclass's onCreateOptionsMenu() method
     * It sets up the toolbar menu
     *
     * @param menu @see AppCompatActivity.onCreateOptionsMenu()
     * @return super return
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
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_home:
                Intent goHome = new Intent(RecipeSearchActivity.this, RecipeMainActivity.class);
                startActivity(goHome);
                break;
            case R.id.toolbar_help:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.information))
                        .setMessage(getString(R.string.recipeVersion) + "\n" + getString(R.string.searchHelp))//TODO
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            case R.id.toolbar_fav:
                Intent goToFav = new Intent(RecipeSearchActivity.this, RecipeFavActivity.class);
                startActivity(goToFav);
                break;

            case R.id.toolbar_about:
                Intent goToAbout = new Intent(RecipeSearchActivity.this, AboutMeActivity.class);
                startActivity(goToAbout);
                break;
            case R.id.toolbar_search:
                Toast.makeText(this, getString(R.string.searchToast), Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
