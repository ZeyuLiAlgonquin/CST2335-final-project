package com.example.recipeapp.Recipe;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.recipeapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * This is our Fragment class that is used to display the details of or Records/List items
 * It extends Fragment and implements View.OnClickListener.
 * Sets an onClickListener() to our Star icon for using the Favorites List
 */
public class RecipeDetailFragment extends Fragment {

    private boolean isTablet;
    private boolean isFave;
    private ImageButton faveButton;
    private ProgressBar progressBar;
    private ImageView imageView;
    private TextView titleTextView;
    private TextView detailTextView;
    private RecipeEntry recipe;

    private class FetchRecipeDetail extends AsyncTask<Long, Integer, String> {
        private final String urlTemplate = "https://api.spoonacular.com/recipes/%s/summary?apiKey=2311513282b7432684777caf629d344a";

        /**
         * This Overrides AsyncTask.doInBackGround()
         * <p>
         * It pulls the search results based on what the droids want you to think.
         * <p>
         * Basically it switches between searching Chicken and Lasagna
         *
         * @param @See AsyncTask.doInBackground()
         * @return @See AsyncTask.doInBackground()
         */
        @Override
        protected String doInBackground(Long... params) {
            Long recipeId = params[0];
            String rawUrl = String.format(urlTemplate, recipeId);
            try {
                URL url = new URL(rawUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                publishProgress(10);
                InputStream inStream = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                publishProgress(20);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                JSONObject r = new JSONObject(sb.toString());
                publishProgress(80);
                if (recipe != null) {
                    recipe.title = r.getString("title");
                    recipe.details = r.getString("summary");
                }
                publishProgress(100);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * This method takes the param values to update our progress bar.
         * it keeps the search button and text field out of view while the progress bar is used.
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
        @Override                   //Type 3 of Inner Created Class
        protected void onPostExecute(String results) {
            super.onPostExecute(results);
            Spanned text = Html.fromHtml(recipe.details);
            detailTextView.setMovementMethod(LinkMovementMethod.getInstance());
            detailTextView.setText(text);
        }

    }

    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }


    /**
     * This is the Override of Fragment Class's onCreateView.
     * It sets up all the values in our layout like, text, images, and hyperlinks
     *
     * @param inflater           @see Fragment.onCreateView()
     * @param container          @see Fragment.onCreateView()
     * @param savedInstanceState @see Fragment.onCreateView()
     * @return returns the view back to the calling activity
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        recipe = new RecipeEntry();
        recipe.id = bundle.getLong("id");
        recipe.title = bundle.getString("title");
        recipe.imageUrl = bundle.getString("imageUrl");
        recipe.details = bundle.getString("details");
        new FetchRecipeDetail().execute(recipe.id);

        View detailLayout = inflater.inflate(R.layout.detail, container, false);

        faveButton = detailLayout.findViewById(R.id.faveButton);
        faveButton.setOnClickListener((View v) -> {
            RecipeDatabaseHelper helper = new RecipeDatabaseHelper(getActivity());

            switch (v.getId()) {
                case R.id.faveButton:
                    if (RecipeDAO.isExist(helper, recipe.id)) {
                        Snackbar.make(v, "removed from Favorites", Snackbar.LENGTH_LONG).show();
                        RecipeDAO.deleteFavRecipe(helper, recipe.id);
                    } else {
                        Snackbar.make(v, "added to Favorites", Snackbar.LENGTH_LONG).show();
                        RecipeDAO.addFavRecipe(helper, recipe);
                    }
                    updateIcon();
                    break;
                default:
                    break;
            }

        });
        updateIcon();

        imageView = detailLayout.findViewById(R.id.imageRecipe);
        Picasso.get().load(recipe.imageUrl).into(imageView);

        titleTextView = (TextView) detailLayout.findViewById(R.id.textTitle);
        titleTextView.setText(recipe.title);
        detailTextView = detailLayout.findViewById(R.id.textValue2);
        progressBar = detailLayout.findViewById(R.id.detailProgressBar);

        return detailLayout;
    }

    /**
     * A simple function to update the filled/unfilled star icon based on if the record is in the Favorites Table
     */
    private void updateIcon() {
        RecipeDatabaseHelper helper = new RecipeDatabaseHelper(getActivity());
        if (RecipeDAO.isExist(helper, recipe.id)) {
            faveButton.setImageResource(R.drawable.star_filled);
        } else {
            faveButton.setImageResource(R.drawable.star_unfilled);
        }
    }


}