package com.ant.listmaker

import android.os.Parcel
import android.os.Parcelable

class TaskList(val name: String, val tasks: ArrayList<String> = ArrayList()) : Parcelable {

    // Constructor for a Parcel
    // Pass values from the Parcel to the primary constructor
    constructor(source: Parcel) : this(
        source.readString()!!, source.createStringArrayList()!!
    )

    override fun describeContents() = 0

    // Write the name and tasks to a Parcel
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeStringList(tasks)
    }

    companion object CREATOR: Parcelable.Creator<TaskList> {

        // Create a TaskList from the items in the Parcel, using the secondary constructor
        override fun createFromParcel(source: Parcel) :
         TaskList = TaskList(source)

        override fun newArray(size: Int): Array<TaskList?> = arrayOfNulls(size)
        }
    }