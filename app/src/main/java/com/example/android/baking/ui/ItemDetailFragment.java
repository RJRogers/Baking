package com.example.android.baking.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.baking.R;
import com.example.android.baking.model.Ingredients;
import com.example.android.baking.model.Message;
import com.example.android.baking.model.New;
import com.example.android.baking.model.Recipe;
import com.example.android.baking.model.Steps;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.baking.ui.ItemListActivity.LOG_TAG;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";


    private RecyclerView recyclerView;
    private MyAdapter mAdapter;

    private ArrayList<New> mNewSteps;


    Recipe mRecipe;


    List<String> stepString = new ArrayList<>();
    List<String> ingredientsString;
    List<Steps> steps;

    Steps step;

    String string;

    SimpleExoPlayerView mPlayerView;

    private SimpleExoPlayer mExoPlayer;

    String stringOne;






    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            mRecipe = Recipe.RECIPE_MAP.get(getArguments().getString(ARG_ITEM_ID));
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mRecipe.getName());
            }
        }




       steps = mRecipe.getSteps();

        stringOne = steps.get(0).getVideoURL();

        for(int j = 0; j < steps.size(); j++){

            Log.d(LOG_TAG, steps.get(j).getVideoURL() + " @@@@@@@@@@@@@@@@@@");
        }






        //INGREDIENTS
        List<Ingredients> ingredients = mRecipe.getIngredients();

        //Create a String array for all ingredients strings
        ingredientsString = new ArrayList<>();
        ingredientsString.add("Ingredients" + "\n");

        for(int x = 0; x < ingredients.size(); x++){

            ingredientsString.add(ingredients.get(x).getIngredient() + " " + ingredients.get(x).getQuantity() + " " + ingredients.get(x).getMeasure());
            Log.d(LOG_TAG, ingredientsString.get(x).toString());
        }



        StringBuilder stringBuilder = new StringBuilder();
        for(String s : ingredientsString){
            stringBuilder.append(s + "\n");
        }

        string = stringBuilder.toString();

        Log.d(LOG_TAG, string + "!!!!!!!!!!@@@@@@@@@@@@@@");








        //List<String> stepString = new ArrayList<>();
        stepString = new ArrayList<>();
//        stepString.add(string);

        int counter = 1;

        for(int j = 0; j < steps.size(); j++){
            Log.d(LOG_TAG, steps.get(j).getShortDescription() + "!!!!!!!!!!!");
            stepString.add("Step " + counter + ": " + steps.get(j).getShortDescription());
            counter++;
        }


        // private ArrayList<New> mNewSteps;
        mNewSteps = new ArrayList<>();

        for( int k = 0; k < stepString.size(); k++){

            New newStep = new New();
            newStep.setStepsShortDescription(stepString.get(k));
            newStep.setName("Step " + k);

            mNewSteps.add(newStep);
            Log.d(LOG_TAG, newStep.getDescription() + "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" + newStep.getName());
        }



    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_lists, container, false);
        recyclerView = (RecyclerView) view
                .findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        mPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.playerView);

        // Load the question mark as the background image until the user answers the question.
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), R.drawable.question_mark));


        if(!steps.get(0).getVideoURL().isEmpty()){
            initializePlayer(Uri.parse(steps.get(0).getVideoURL()));


        }

        updateUI();

        return view;
    }


    private void updateUI(){
        mAdapter = new MyAdapter(mNewSteps);
        recyclerView.setAdapter(mAdapter);


    }

    private class MyHolder extends RecyclerView.ViewHolder{

        private New mNewSteps;

        public TextView mNameTextView;


        public MyHolder(final View itemView){
            super(itemView);

            mNameTextView = (TextView) itemView.findViewById(R.id.textview_name);
            TextView textView = (TextView) getActivity().findViewById(R.id.firsttext);
            textView.setText(string);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int clickPosition = getAdapterPosition();

                    step = steps.get(clickPosition);

                    ArrayList<Steps> newSteps = new ArrayList<Steps>();
                    for(int i = 0; i < steps.size();i++){
                        Steps stepperoo = steps.get(i);
                        newSteps.add(stepperoo);
                        Log.d(LOG_TAG, newSteps.get(i).getDescription() + "WWWWWWWWWWWWWWWWWWWWWWW");
                    }


                    Bundle args = new Bundle();
                    args.putParcelable("steps", step);
                    args.putString("recipeName", mRecipe.getName());
                    args.putParcelableArrayList("stepsList", newSteps);
                    args.putInt("clickPosition", clickPosition);
                    args.putString("ingredients",string);

//                    EventBus.getDefault().postSticky(new Message(2, args));

                    releasePlayer();

                    initializePlayer(Uri.parse(steps.get(clickPosition).getVideoURL()));
                    Log.d(LOG_TAG, steps.get(clickPosition).getVideoURL().toString() + "qqqqqqqqqqqqqqqqqqqq");

                }
            });




        }



        public void bindData(New s){
            mNewSteps = s;

            mNameTextView.setText(s.getStepsShortDescription());

        }




    }

    private class MyAdapter extends RecyclerView.Adapter<MyHolder>{
        private ArrayList<New> mNewSteps;




        public MyAdapter(ArrayList<New> NewSteps){
            mNewSteps = NewSteps;

        }


        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.category_list_item_1,parent,false);
            return new MyHolder(view);

        }



        @Override
        public void onBindViewHolder(MyHolder holder, int position) {


            New s = mNewSteps.get(position);

            holder.bindData(s);



        }



        @Override
        public int getItemCount() {
            return mNewSteps.size();

        }
    }





    @Override
    public void onResume() {
        super.onResume();

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        Recipe mRecipe = Recipe.RECIPE_MAP.get(getArguments().getString(ARG_ITEM_ID));
        actionBar.setTitle(mRecipe.getName());

    }

    private void releasePlayer(){
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }


    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
//            mExoPlayer.addListener(exoPlayerEventListener);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "Baking");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);

        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mExoPlayer != null){
            releasePlayer();
        }



    }





}
