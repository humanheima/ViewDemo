package com.hm.viewdemo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by dumingwei on 2019-11-28.
 * Desc:
 */
public class InfoTwo implements Parcelable {

    private int age;
    private String name;
    private ArrayList<Person> arrayList;


    protected InfoTwo(Parcel in) {
        age = in.readInt();
        name = in.readString();
        arrayList = in.createTypedArrayList(Person.CREATOR);
    }

    public static final Creator<InfoTwo> CREATOR = new Creator<InfoTwo>() {
        @Override
        public InfoTwo createFromParcel(Parcel in) {
            return new InfoTwo(in);
        }

        @Override
        public InfoTwo[] newArray(int size) {
            return new InfoTwo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(age);
        dest.writeString(name);
        dest.writeTypedList(arrayList);
    }
}
