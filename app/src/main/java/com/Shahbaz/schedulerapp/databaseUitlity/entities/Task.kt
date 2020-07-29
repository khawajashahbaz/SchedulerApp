package com.Shahbaz.schedulerapp.databaseUitlity.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*

@Entity(tableName = "task_list")
class Task : Parcelable {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo(name = "title")
    var title: String? = null

    @ColumnInfo(name = "repeat")
    var isRepeat = false

    @ColumnInfo(name = "w_cycle")
    var workingCycle: String? = null

    @ColumnInfo(name = "rest_interval")
    var restInterval: String? = null

    @ColumnInfo(name = "long_rest_interval")
    var longRestInterval: String? = null

    @ColumnInfo(name = "session_for_long_rest")
    var sessionForLongrest: String? = null

    @ColumnInfo(name = "done")
    var done = false

    @ColumnInfo(name = "minutes_worked")
    var minutesWorked: String? = null

    @ColumnInfo(name = "cat_id")
    @ForeignKey(entity = Category::class, parentColumns = ["id"], childColumns = ["cat_id"], onDelete = ForeignKey.CASCADE)
    var catId = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        title = parcel.readString()
        isRepeat = parcel.readByte() != 0.toByte()
        workingCycle = parcel.readString()
        restInterval = parcel.readString()
        longRestInterval = parcel.readString()
        sessionForLongrest = parcel.readString()
        done = parcel.readByte() != 0.toByte()
        minutesWorked = parcel.readString()
        catId = parcel.readInt()
    }

    constructor() {}

    @Ignore
    constructor(title: String?, repeat: Boolean, workingCycle: String?, restInterval: String?, longRestInterval: String?, sessionForLongrest: String?, done: Boolean, catId: Int) {
        this.title = title
        isRepeat = repeat
        this.workingCycle = workingCycle
        this.restInterval = restInterval
        this.longRestInterval = longRestInterval
        this.sessionForLongrest = sessionForLongrest
        this.done = done
        this.catId = catId
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeByte(if (isRepeat) 1 else 0)
        parcel.writeString(workingCycle)
        parcel.writeString(restInterval)
        parcel.writeString(longRestInterval)
        parcel.writeString(sessionForLongrest)
        parcel.writeByte(if (done) 1 else 0)
        parcel.writeString(minutesWorked)
        parcel.writeInt(catId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}