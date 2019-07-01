package de.mankianer.gruppe5.analyse.classification;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Arrays;

public class Classification {
    String text;
    JsonElement[] categories;
    ArrayList<Category> categoriesAL = new ArrayList<Category>();
    //Label und score

    class Category{
        String label;
        String score;
    }

    private void getCategories(){
        Gson gson = new Gson();
        for(JsonElement ele : categories){
            Category cat = gson.fromJson(ele, Category.class);
            categoriesAL.add(cat);
        }
    }

    public Category[] getCategoriesArr(){
        getCategories();
        return categoriesAL.toArray(new Category[0]);
    }

    public String[] getLabelsArr(){
        return Arrays.stream(getCategoriesArr()).map((cat) -> cat.label).toArray(String[]::new);
    }
}
