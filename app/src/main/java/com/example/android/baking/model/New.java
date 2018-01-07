package com.example.android.baking.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ryanrogers on 1/11/2017.
 */

public class New implements Parcelable {

    private String mName;
    private String mDescription;
    private String mIngredients;
    private String recipeId;
    private String videoUrl;
    private String stepsId;
    private String stepsShortDescription;
    private String stepsThumbnailUrl;


    public New(){

    }


    public String getVideoUrl(){

        return  videoUrl;
    }

    public void setVideoUrl(String videoUrl){
        this.videoUrl = videoUrl;
    }

    public String getStepsId(){

        return stepsId;
    }

    public void setStepsId(String stepsId){

        this.stepsId = stepsId;
    }


    public String getStepsShortDescription(){

        return stepsShortDescription;
    }

    public void setStepsShortDescription(String stepsShortDescription){

        this.stepsShortDescription = stepsShortDescription;
    }

    public String getStepsThumbnailUrl(){
        return stepsThumbnailUrl;
    }

    public void setStepsThumbnailUrl(String stepsThumbnailUrl){

        this.stepsThumbnailUrl = stepsThumbnailUrl;
    }



    public String getmIngredients() {return  mIngredients;}

    public void setmIngredients(String mIngredients){
        this.mIngredients = mIngredients;


    }


    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getDescription(){
        return mDescription;
    }

    public void setDescription(String mDescription){
        this.mDescription = mDescription;
    }


    public String getId(){
        return recipeId;
    }


    public void setId(String id){
        this.recipeId = id;
    }


    public New(Parcel in) {
        mName = in.readString();
        mDescription = in.readString();
        mIngredients = in.readString();
        recipeId = in.readString();
        videoUrl = in.readString();
        stepsId = in.readString();
        stepsShortDescription = in.readString();
        stepsThumbnailUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mDescription);
        dest.writeString(mIngredients);
        dest.writeString(recipeId );
        dest.writeString(videoUrl);
        dest.writeString(stepsId);
        dest.writeString(stepsShortDescription);
        dest.writeString(stepsThumbnailUrl);

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<New> CREATOR = new Parcelable.Creator<New>() {
        @Override
        public New createFromParcel(Parcel in) {
            return new New(in);
        }

        @Override
        public New[] newArray(int size) {
            return new New[size];
        }
    };
}
