package duality.questmanager;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable{
    String title;
    int photoId;
    int iconId;
    int coinCost;

    public Task(String title, int photoId, int iconId, int coinCost) {
        super();
        this.title = title;
        this.photoId = photoId;
        this.iconId = iconId;
        this.coinCost = coinCost;
    }

    public Task(String title, int coinCost) {
        super();
        this.title = title;
        this.coinCost = coinCost;
        this.photoId = R.drawable.coin;
        this.iconId = R.drawable.info;
        this.coinCost = coinCost;
    }
    public Task() {
        super();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public void setCoinCost(int coinCost) {
        this.coinCost = coinCost;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}