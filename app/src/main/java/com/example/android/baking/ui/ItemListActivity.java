package com.example.android.baking.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.baking.R;

import com.example.android.baking.api.RecipeService;
import com.example.android.baking.model.Ingredients;
import com.example.android.baking.model.Message;
import com.example.android.baking.model.Recipe;
import com.example.android.baking.model.Steps;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ItemListActivity extends AppCompatActivity {

    //two pane mode
    private boolean mTwoPane;
    public static final String LOG_TAG = "myLogs";
    List<Recipe> recipeList = new ArrayList<>();
    SimpleItemRecyclerViewAdapter simpleItemRecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_item_list);

        getRecipes();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());



    }


//    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
//    public void onMessage(Message event){
//
//        String text = event.getMessage();
//        Log.d(LOG_TAG, text + "???????????????????????????????!!!!!!!!!!!!!!!");
//
//    }

//    @Subscribe
//    public void abc(String str){
//        Log.d(LOG_TAG, "Main activity called !!!!!!!!" );
//    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        EventBus.getDefault().unregister(this);
//    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(Message event) {

        if(event.getMessage() == 2){

            Fragment newFragment = new StepDetailFragment();
            newFragment.setArguments(event.getBundle());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.item_detail_container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();

            Log.d(LOG_TAG,"Eventbus worked accordingly");

        }

    }





    public void getRecipes(){

        String ROOT_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";


        Retrofit RETROFIT = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeService service = RETROFIT.create(RecipeService.class);

        Call<List<Recipe>> call = service.getMyJson();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                Log.d(LOG_TAG, "Got here");
                if (!response.isSuccessful()) {
                    Log.d(LOG_TAG, "No Success");
                }

                Log.d(LOG_TAG, "Got here");
                String string = response.body().toString();

                Log.d(LOG_TAG, string);


                recipeList = response.body();

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.item_list);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);

                simpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapter(recipeList);
                recyclerView.setAdapter(simpleItemRecyclerViewAdapter);


                if (findViewById(R.id.item_detail_container) != null) {
                    mTwoPane = true;
                }


                Log.e(LOG_TAG, "LOGS " + recipeList.size());




                for (int i = 0; i < recipeList.size(); i++) {
                    String newString = recipeList.get(i).getName();

                    Log.d(LOG_TAG, newString + " ****************");



//
//                    Ingredients[] ingredients = recipeList.get(i).getIngredients();
//                    for(int j = 0; j < ingredients.length; j++){
//                        Log.d(LOG_TAG, ingredients[j].getIngredient() + "!!!!!!!!!!!!!!!!!!");
//                    }
//
//                    Steps[] steps = recipeList.get(i).getSteps();
//                    for(int k = 0; k < steps.length; k++){
//                        Log.d(LOG_TAG, steps[k].getDescription() + "##################");
//                    }



                }


            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e("getRecipes throwable: ", t.getMessage());
                t.printStackTrace();

            }
        });


    }



    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Recipe> mValues;

        public SimpleItemRecyclerViewAdapter(List<Recipe> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {


            holder.mItem = mValues.get(position);


            holder.mContentView.setText(mValues.get(position).getName());


            Recipe recipe = simpleItemRecyclerViewAdapter.mValues.get(position);
            recipe.addItem(recipe);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.getId());
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();

//                        EventBus.getDefault().postSticky(new Message(5, arguments));


                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.getId());
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public  View mView;
            public  TextView mContentView;
            public Recipe mItem;


            public ViewHolder(View view) {
                super(view);
                mView = view;
                mContentView = (TextView) view.findViewById(R.id.content);

            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
