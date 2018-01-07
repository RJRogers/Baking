package com.example.android.baking.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.baking.R;
import com.example.android.baking.model.Message;
import com.example.android.baking.model.Recipe;
import com.example.android.baking.model.Steps;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.android.baking.ui.ItemDetailFragment.ARG_ITEM_ID;
import static com.example.android.baking.ui.ItemListActivity.LOG_TAG;

/**
 * Created by ryanrogers on 9/11/2017.
 */

public class StepDetailFragment extends Fragment {

    Steps steps;

    String string;

    ArrayList<Steps> newSteps;

    TextView textView;

    String mVideoLink;

    Steps buttonOneStep;

    SimpleExoPlayerView mPlayerView;

    private SimpleExoPlayer mExoPlayer;

    private ExoPlayer.EventListener exoPlayerEventListener;

    int clickPostion;

    int index = 1;

    long currentPosition;

    Dialog mFullScreenDialog;

    boolean mExoPlayerFullscreen = false;



    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

       Bundle b = getArguments();
        steps = b.getParcelable("steps");
        string = b.getString("recipeName");
        newSteps = b.getParcelableArrayList("stepsList");
        clickPostion = b.getInt("clickPosition");


//        String stepsId = steps.getId();
        View view = inflater.inflate(R.layout.test_layout, container, false);
        ButterKnife.bind(this, view);

        textView = (TextView)view.findViewById(R.id.test_id);
        textView.setText(steps.getDescription());

        mPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.playerView);

        // Load the question mark as the background image until the user answers the question.
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), R.drawable.question_mark));


        if(!steps.getVideoURL().isEmpty()){
            initializePlayer(Uri.parse(steps.getVideoURL()));
        }




        return view;
    }



    private void releasePlayer(){
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }




    int counter = 0;

    @OnClick(R.id.button_one)
    void clickButtonOne() {

        if (counter < newSteps.size()) {
            Log.d(LOG_TAG, "Button 1 Clicked");
            Bundle args = new Bundle();
            buttonOneStep = newSteps.get(clickPostion + 1);
            args.putParcelable("steps", buttonOneStep);
            args.putString("recipeName", string);
            args.putParcelableArrayList("stepsList", newSteps);
            args.putInt("clickPosition", clickPostion + 1);
            args.putString("ingredients", string);
            EventBus.getDefault().postSticky(new Message(3, args));
            counter = counter + 1;


        }

        else {
            Log.d(LOG_TAG, "ERROR");
        }

    }




    @OnClick(R.id.button_two)
    void clickButtonTwo(){
        Log.d(LOG_TAG, "Button 2 Clicked");
//        Bundle args = new Bundle();
//        args.putString("recipeName", "This is a recipe name");
//        EventBus.getDefault().postSticky(new Message(1,args));

        //Need the textView to live in a FrameLayout (new fragment) and update this on each click of next

//        counter = newSteps.size();

//        if(counter <= newSteps.size() && counter != newSteps.size()) {
//            textView.setText(newSteps.get(clickPostion + counter).getDescription());
//            counter++;
//        }
//
//        else if(counter == newSteps.size()){
//
//            Toast.makeText(getActivity(), "No more steps",
//                    Toast.LENGTH_LONG).show();
//        }
    }

//    Uri.parse("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8")


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
    public void onResume() {
        super.onResume();
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(string);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        releasePlayer();

    }




//    private void initFullscreenDialog() {
//
//        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
//            public void onBackPressed() {
//                if (mExoPlayerFullscreen)
//                    closeFullscreenDialog();
//                super.onBackPressed();
//            }
//        };
//    }


//    private void closeFullscreenDialog() {
//
//        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
//
//
//
//        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(mExoPlayerView);
//        mExoPlayerFullscreen = false;
//        mFullScreenDialog.dismiss();
//
//    }




    }






