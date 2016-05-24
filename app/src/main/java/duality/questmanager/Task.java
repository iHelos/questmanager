package duality.questmanager;

import android.os.Parcel;
import android.os.Parcelable;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Task implements Parcelable{
    int id;
    String title;
    int iconId;
    Integer coinCost;
    String details;
    String worker;
    String hash;
    GregorianCalendar date;
    int isCompleted;

    public Task(int id, String title, String details, String worker, int coinCost, GregorianCalendar date, String hash, int isCompleted) {
        super();
        this.id = id;
        this.title = title;
        this.details = details;
        this.worker = worker;
        this.date = date;
        this.coinCost = coinCost;
        this.hash = hash;
        this.isCompleted = isCompleted;
        if(isCompleted == 0) {
            this.iconId = R.drawable.checkbox_blank_circle_outline__1_;
        }

        if(isCompleted == -1)
        {
            this.iconId = R.drawable.close_circle_outline;
        }
        if(isCompleted == 1)
        {
            this.iconId = R.drawable.check_circle_outline;
        }
    }

    public Task(int id, String title, int coinCost, String date, int isCompleted) {
        super();
        this.id = id;
        this.title = title;
        this.coinCost = coinCost;
//        this.iconId = R.drawable.ic_information_black_18dp;
        this.coinCost = coinCost;
        this.details = "nope";
        this.worker = "nope";
        this.isCompleted = isCompleted;
        if(isCompleted == 0) {
        this.iconId = R.drawable.checkbox_blank_circle_outline__1_;
        }

        if(isCompleted == -1)
        {
            this.iconId = R.drawable.close_circle_outline;
        }
        if(isCompleted == 1)
        {
            this.iconId = R.drawable.check_circle_outline;
        }

        String[] dateParts = date.split("-");

        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);

        this.date = new GregorianCalendar(year, month-1, day);
        };

    public Task() {
        super();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public void setCoinCost(int coinCost) {
        this.coinCost = coinCost;
    }

    public String getTitle() { return this.title; }

    public String getDetails() { return this.details; }

    public String getWorker() { return this.worker; }

    public int getCoinCost() { return this.coinCost; }

    public int getIsCompleted() { return this.isCompleted; }

    public GregorianCalendar getDate() { return this.date; }

    public String getHash() { return this.hash; }

    public int getID() {
        return this.id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}