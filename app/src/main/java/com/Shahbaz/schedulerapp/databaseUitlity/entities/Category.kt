package com.Shahbaz.schedulerapp.databaseUitlity.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "category_list")
class Category : Parcelable {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo(name = "color")
    var color = 0

    @ColumnInfo(name = "title")
    var name: String? = null

    @ColumnInfo(name = "icon")
    var icon: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        color = parcel.readInt()
        name = parcel.readString()
        icon = parcel.readString()
    }

    constructor() {}

    @Ignore
    constructor(color: Int, name: String, icon: String) {
        this.color = color
        this.name = name
        this.icon = icon
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(color)
        parcel.writeString(name)
        parcel.writeString(icon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }
}