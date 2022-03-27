package com.example.recipeapp.Recipe;

public class AppState {

    private AppState() {

    }

    private static AppState instance;
    synchronized public static AppState instance() {
        if(instance == null)
            instance = new AppState();
        return instance;
    }

}
