package com.example.android.baking.api;

import com.example.android.baking.model.Recipe;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;



public interface RecipeService {
    @GET("baking.json")
    Call<List<Recipe>> getMyJson();
}
