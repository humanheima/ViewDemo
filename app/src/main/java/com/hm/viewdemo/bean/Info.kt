package com.hm.viewdemo.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by dumingwei on 2019-11-27.
 * Desc:
 */
class Info(var list: ArrayList<Person>?,
           var id: String?,
           var price: String?) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.createTypedArrayList(Person.CREATOR),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(list)
        parcel.writeString(id)
        parcel.writeString(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Info(list=$list, id=$id, price=$price)"
    }

    companion object CREATOR : Parcelable.Creator<Info> {
        override fun createFromParcel(parcel: Parcel): Info {
            return Info(parcel)
        }

        override fun newArray(size: Int): Array<Info?> {
            return arrayOfNulls(size)
        }
    }




}

data class Person(var height: Double, var name: String?) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readDouble(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(height)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Person> {
        override fun createFromParcel(parcel: Parcel): Person {
            return Person(parcel)
        }

        override fun newArray(size: Int): Array<Person?> {
            return arrayOfNulls(size)
        }
    }
}


